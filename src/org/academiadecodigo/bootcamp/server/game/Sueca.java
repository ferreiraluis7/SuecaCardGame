package org.academiadecodigo.bootcamp.server.game;

import org.academiadecodigo.bootcamp.Randomizer;
import org.academiadecodigo.bootcamp.server.player.Player;

import java.io.IOException;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

public class Sueca implements Game {

    public static final int TOTAL_POINTS = 120;
    public static final int NUMBER_OF_PLAYERS = 4;
    public static final int CARDS_PER_PLAYER = 10;
    public static final CardDealer.DeckType DECK_TYPE = CardDealer.DeckType.REGIONAL;
    private int teamOneVictories;
    private int teamTwoVictories;
    private CardDealer dealer;
    private int startingPlayer = 0;
    private boolean playerLeft;

    /**
     * Starts the game loop
     *
     * @param players the game players
     */
    public void playGame(List<Player> players) {
        Cards.Suit trumpSuit = null;
        int currentPlayer = startingPlayer;
        List<Cards> cardsInPlay = new ArrayList<>();
        Cards playedCard;
        Cards.Suit currentSuit = null;
        int totalCardsPlayed = 0;
        Cards higherCard = null;
        Cards tempCard;
        Player winningPlayer = null;
        int scoreTeamOne = 0;
        int scoreTeamTwo = 0;
        String currentGameHand = "";
        boolean isGameStarted = false;
        //the player that starts the first game of a lobby is always the first to

        // game init
        System.out.println("GAME IS ABOUT TO START");

        if (!isGameStarted) {
            //Set card hand for each player
            updateScoreInfo(players, scoreTeamOne, scoreTeamTwo);
            prepareGame(players);
            //choose the trumpSuit
            trumpSuit = randomizeTrumpSuit();
            currentGameHand = generateTrumpSuitMessage(trumpSuit) + "\r\nGAME HAND:\r\n";
            informPlayerPartner(players);
            dealer.broadcastMessage(players, generateTrumpSuitMessage(trumpSuit));
            isGameStarted = true;

        }

        while (isGameStarted) {

            if (currentPlayer >= players.size()) {
                currentPlayer = 0;
            }

            try {
                isGameStarted = checkGameEnd(players, totalCardsPlayed, scoreTeamOne);

                playedCard = getMove(players.get(currentPlayer), players);

                if (checkIfPlayerLeft(players)) {
                    playerLeft = true;
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    return;
                }

                if (cardsInPlay.isEmpty()) {

                    confirmPlay(players, currentPlayer, cardsInPlay, playedCard);

                    higherCard = playedCard;
                    winningPlayer = players.get(currentPlayer);

                    currentGameHand += players.get(currentPlayer).getName() + " card: " + playedCard.getUnicode() + "\r\n";

                    currentSuit = playedCard.getSuit();
                    totalCardsPlayed++;
                    currentPlayer++;

                    updateGameInfo(players, scoreTeamOne, scoreTeamTwo, currentGameHand);
                    continue;
                }


                if (checkMove(players.get(currentPlayer), playedCard, currentSuit)) {
                    System.out.println(players.get(currentPlayer).getName() + " tried to cheat");
                    players.get(currentPlayer).send("You are not allowed to play that card, please play another");  //INNER CLASS W/ MESSAGES BUILDER METHODS
                    continue;
                }

                confirmPlay(players, currentPlayer, cardsInPlay, playedCard);
                tempCard = checkHigherCard(playedCard, higherCard, trumpSuit);

                if (!tempCard.equals(higherCard)) {
                    winningPlayer = players.get(currentPlayer);
                    higherCard = tempCard;
                }
                currentGameHand += players.get(currentPlayer).getName() + " card: " + playedCard.getUnicode() + "\n"; //add players and card to the string
                totalCardsPlayed++;

                updateGameInfo(players, scoreTeamOne, scoreTeamTwo, currentGameHand);

                if (cardsInPlay.size() == NUMBER_OF_PLAYERS) {
                    dealer.broadcastMessage(players, winningPlayer.getName() + " WINS THIS ROUND AND MAKES " + getPoints(cardsInPlay, winningPlayer, players) + " POINTS \n");

                    if (winningPlayer.getTeam() == 1) {
                        scoreTeamOne += getPoints(cardsInPlay, winningPlayer, players);
                    }
                    if (winningPlayer.getTeam() == 2) {
                        scoreTeamTwo += getPoints(cardsInPlay, winningPlayer, players);
                    }

                    currentPlayer = players.indexOf(winningPlayer);
                    currentGameHand = generateTrumpSuitMessage(trumpSuit) + "\r\nGAME HAND:\r\n"; //reset String to default
                    cardsInPlay.clear();
                    continue;
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

            currentPlayer++;
        }


    }

    /**
     * Updates the game information
     *
     * @param players in game players
     *
     * @param scoreTeamOne team one score
     *
     * @param scoreTeamTwo team two score
     *
     * @param currentGameHand current game hand
     */
    private void updateGameInfo(List<Player> players, int scoreTeamOne, int scoreTeamTwo, String currentGameHand) {
        updateScoreInfo(players, scoreTeamOne, scoreTeamTwo);
        dealer.sendAll(players, currentGameHand);
    }

    /**
     * Updates the score information
     *
     * @param players in game players
     *
     * @param scoreTeamOne team one score
     *
     * @param scoreTeamTwo team two score
     */
    private void updateScoreInfo(List<Player> players, int scoreTeamOne, int scoreTeamTwo) {
        dealer.broadcastMessage(players, "VICTORIES - TEAM 1: " + teamOneVictories + " TEAM 2: " + teamTwoVictories);
        dealer.broadcastMessage(players, "SCORE - TEAM 1: " + scoreTeamOne + " TEAM 2: " + scoreTeamTwo + "\n");
    }

    /**
     * Generates the trump suit message
     *
     * @param trumpSuit trump suit
     *
     * @return trump suit message
     */
    private String generateTrumpSuitMessage(Cards.Suit trumpSuit) {
        return "\r\nTRUMP: " + trumpSuit + "\r\n";
    }

    /**
     * Adds the played card to table and removes it from player hand
     *
     * @param players       players in game
     * @param currentPlayer current player
     * @param cardsInPlay   cards in table
     * @param playedCard    played card
     */
    private void confirmPlay(List<Player> players, int currentPlayer, List<Cards> cardsInPlay, Cards playedCard) {
        cardsInPlay.add(playedCard);
        players.get(currentPlayer).removeCard(playedCard);
    }

    /**
     * Informs who is each player teammate
     *
     * @param players players in game
     */

    private void informPlayerPartner(List<Player> players) {
        players.get(0).setTeam(1);
        players.get(1).setTeam(2);
        players.get(2).setTeam(1);
        players.get(3).setTeam(2);
        players.get(0).send("\nYou're in Team " + players.get(0).getTeam() + ". Your team mate is " + players.get(2).getName());
        players.get(1).send("\nYou're in Team " + players.get(1).getTeam() + ". Your team mate is " + players.get(3).getName());
        players.get(2).send("\nYou're in Team " + players.get(2).getTeam() + ". Your team mate is " + players.get(0).getName());
        players.get(3).send("\nYou're in Team " + players.get(3).getTeam() + ". Your team mate is " + players.get(1).getName());
    }

    /**
     * Randomly generates the trump suit
     *
     * @return the trump suit
     */
    private Cards.Suit randomizeTrumpSuit() {
        return Cards.values()[Randomizer.getRandom(Cards.values().length)].getSuit();
    }

    /**
     * Allows each player to have an assigned hand
     *
     * @param players players in game
     */
    private void prepareGame(List<Player> players) {
        dealer.dealCards(players, CARDS_PER_PLAYER, DECK_TYPE);
    }

    /**
     * Checks if the game has ended
     *
     * @param players players in game
     *
     * @param totalCardsPlayed total cards played during game
     *
     * @param score game score for team One
     *
     * @return true or false if the game has or has not end
     */
    private boolean checkGameEnd(List<Player> players, int totalCardsPlayed, int score) {
        if (totalCardsPlayed == NUMBER_OF_PLAYERS * CARDS_PER_PLAYER) {
            System.out.println("Game ended\n");
            dealer.broadcastMessage(players, "\n\n GAME HAS ENDED \n");

            setGameScore(players, score);

            dealer.broadcastMessage(players, "\n GLOBAL SCORE:");
            dealer.broadcastMessage(players, "TEAM ONE: " + teamOneVictories + " TEAM TWO: " + teamTwoVictories + "\n");

            startingPlayer++;
            if (startingPlayer >= NUMBER_OF_PLAYERS) {
                startingPlayer = 0;
            }

            System.out.println("Starting a new game.\n");
            playGame(players);
            return false;
        }
        return true;
    }

    /**
     * Sets the game score according to Sueca game rules
     *
     * @param players in game players
     *
     * @param score team One score
     */

    private void setGameScore(List<Player> players, int score) {
        if (score < TOTAL_POINTS / 2) {
            teamTwoVictories++;
            dealer.broadcastMessage(players, "Team Two has won this game With " + (TOTAL_POINTS - score) + " points");
            if (score == 0){
                teamTwoVictories+=3;
                dealer.broadcastMessage(players, "Team Two has scored 120 points, quadruple victory for team Two");
            }
            if (score < 30) {
                teamTwoVictories++;
                dealer.broadcastMessage(players, "Team Two has scored more than 90 points, double victory for team Two");
            }
        } else if (score > TOTAL_POINTS / 2) {
            teamOneVictories++;
            dealer.broadcastMessage(players, "Team One has won this game With " + score + " points");
            if (score == TOTAL_POINTS) {
                teamOneVictories += 3;
                dealer.broadcastMessage(players, "Team One has scored 120 points, quadruple victory for team One");
            }
            if (score > 90) {
                teamOneVictories++;
                dealer.broadcastMessage(players, "Tem One has scored more than 90 points, double victory for team One");
            }
        } else {
            dealer.broadcastMessage(players, "Game tie");
        }
    }

    /**
     * Checks for the higher card
     *
     * @param playedCard played card
     *
     * @param higherCard higher card in table
     *
     * @param trumpSuit  trump suit
     *
     * @return higher card
     */
    private Cards checkHigherCard(Cards playedCard, Cards higherCard, Cards.Suit trumpSuit) {
        System.out.println("Entered checkHigherCard");

        if (!playedCard.getSuit().equals(higherCard.getSuit())) {
            if (!playedCard.getSuit().equals(trumpSuit)) {
                return higherCard;
            }

            return playedCard;
        }

        if (playedCard.getRank().getSuecaRank() > higherCard.getRank().getSuecaRank()) {
            return playedCard;
        }

        return higherCard;
    }

    /**
     * @throws IOException
     *
     * @see Game#getMove(Player, List)
     */
    @Override
    public Cards getMove(Player currentPlayer, List<Player> players) throws IOException {

        for (Player p : players) {
            if (p.equals(currentPlayer)) {
                p.send("It is your turn, choose a card to play [0 - " + (p.getHand().size() - 1) + "]");
            } else {
                p.send(currentPlayer.getName() + " is playing...");
            }
        }

        while (true) {

            String moveString = null;
            try {
                moveString = currentPlayer.readFromClient();

            } catch (SocketException e) {
                System.err.println("Player has left\n");
            }

            if (moveString == null) {
                for (Player p : players) {
                    System.out.println(currentPlayer.getName() + " has left\n");
                    p.send("PLAYERQUIT@@" + currentPlayer.getName() + " has left the game");//"PLAYERQUIT" is a reference so client can read and print
                    playerLeft = true;
                }
                return null;
            }

            try {
                int cardIndex = Integer.parseInt(moveString);

                if (cardIndex < 0 || cardIndex >= currentPlayer.getHand().size()) {

                    currentPlayer.send("It is your turn, please give us a card you have");
                    continue;
                }

                Cards card = currentPlayer.getHand().get(cardIndex);

                System.out.println(currentPlayer.getName() + " played " + card.getCompleteName() + ".\n");

                return card;


            } catch (NumberFormatException e) {
                currentPlayer.send("invalid choice, please select a card to play");
            }
        }
    }


    /**
     * @see Game#checkMove(Player, Cards, Cards.Suit)
     */
    @Override
    public boolean checkMove(Player player, Cards card, Cards.Suit currentSuit) {

        return (playerHandHasSuit(player, currentSuit) && !card.getSuit().equals(currentSuit));  //check Renuncia

    }

    /**
     * Checks if the player hand has a specific suit
     *
     * @param player player which hand will be checked
     *
     * @param currentSuit the specific suit to check
     *
     * @return true or false if the player hand has or has not the suit
     */
    private boolean playerHandHasSuit(Player player, Cards.Suit currentSuit) {

        for (Cards c : player.getHand()) {
            if (c.getSuit().equals(currentSuit)) {
                return true;
            }
        }
        return false;
    }


    /**
     * @see Game#getPoints(List, Player, List)
     */
    @Override
    public int getPoints(List<Cards> cardsPlayed, Player winningPlayer, List<Player> players) {

        int points = 0;
        for (Cards c : cardsPlayed) {
            points += c.getRank().getSuecaPoints();
        }
        return points;

    }

    /**
     * @see Game#getScore()
     */
    @Override
    public int getScore() {
        throw new UnsupportedOperationException();
    }

    /**
     * @see Game#setDealer(CardDealer)
     */
    @Override
    public void setDealer(CardDealer dealer) {
        this.dealer = dealer;
    }

    /**
     * @see Game#isPlayerLeft()
     */
    @Override
    public boolean isPlayerLeft() {
        return playerLeft;
    }

    /**
     * Checks for player disconnections
     *
     * @param playersInLobby in game players
     *
     * @return true or false if there was or there wasn't a disconnection
     */
    private boolean checkIfPlayerLeft(List<Player> playersInLobby) {
        boolean playerLeft = false;
        List<Player> checkList = new ArrayList<>();
        checkList.addAll(playersInLobby);

        for (Player p : checkList) {
            p.send("CHECKCONNECT");
            try {
                String read = p.readFromClient();
            } catch (IOException e) {

                try {
                    String pName = p.getName();
                    p.getClientSocket().close();
                    playersInLobby.remove(p);
                    for (Player players : playersInLobby) {
                        players.send(pName + " has left.");
                    }
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
        if (playersInLobby.size() < 4) {
            playerLeft = true;
        }
        return playerLeft;
    }
}



