package org.academiadecodigo.bootcamp.server.game;

import org.academiadecodigo.bootcamp.Randomizer;
import org.academiadecodigo.bootcamp.server.player.Player;

import java.io.IOException;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

public class Salema implements Game {

    public static final int TOTAL_POINTS = 20;
    private static final int END_GAME_POINTS = 100;
    public static final int NUMBER_OF_PLAYERS = 4;
    public static final int CARDS_PER_PLAYER = 10;
    public static final CardDealer.DeckType DECK_TYPE = CardDealer.DeckType.REGIONAL;
    private CardDealer dealer;
    private int startingPlayer = 0;
    private boolean playerLeft;

    private int p1GlobalScore = 0;
    private int p2GlobalScore = 0;
    private int p3GlobalScore = 0;
     private int p4GlobalScore = 0;

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
        int p1Points = 0;
        int p2Points = 0;
        int p3Points = 0;
        int p4Points = 0;

        String currentGameHand = "";
        boolean isGameStarted = false;
        //the player that starts the first game of a lobby is always the first to

        // game init

        if (!isGameStarted) {
            //Set card hand for each player
            updateScoreInfo( players , p1Points , p2Points , p3Points , p4Points );
            prepareGame(players);
            currentGameHand = ",,GAME HAND:,,";
            informPlayerPartner(players);
            isGameStarted = true;

        }

        while (isGameStarted) {

            if (currentPlayer >= players.size()) {
                currentPlayer = 0;
            }

            try {
                isGameStarted = checkGameEnd(players, p1Points, p2Points, p3Points, p4Points, totalCardsPlayed);

                playedCard = getMove(players.get(currentPlayer), players);

                if(playedCard == null){
                    players.get(currentPlayer).getClientSocket().close();
                    players.remove(players.get(currentPlayer));
                    return;
                }

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

                    currentGameHand += players.get(currentPlayer).getName() + " card: " + playedCard + "@@";

                    currentSuit = playedCard.getSuit();
                    totalCardsPlayed++;
                    currentPlayer++;

                    updateGameInfo(players , p1Points , p2Points , p3Points , p4Points , currentGameHand);
                    continue;
                }



                if (checkMove(players.get(currentPlayer), playedCard, currentSuit)) {
                    System.out.println(players.get(currentPlayer).getName() + "<" + players.get(currentPlayer).getClientSocket().getInetAddress() + ":" + players.get(currentPlayer).getClientSocket().getPort() + "> tried to cheat");
                    players.get(currentPlayer).send("You are not allowed to play that card, please play another");  //INNER CLASS W/ MESSAGES BUILDER METHODS
                    continue;
                }

                confirmPlay(players, currentPlayer, cardsInPlay, playedCard);
                tempCard = checkHigherCard(playedCard, higherCard, trumpSuit);

                if (!tempCard.equals(higherCard)) {
                    winningPlayer = players.get(currentPlayer);
                    higherCard = tempCard;
                }
                currentGameHand += players.get(currentPlayer).getName() + " card: " + playedCard + "@@"; //add players and card to the string
                totalCardsPlayed++;

                updateGameInfo(players, p1Points , p2Points , p3Points , p4Points , currentGameHand);

                if (cardsInPlay.size() == NUMBER_OF_PLAYERS) {
                    dealer.broadcastMessage(players, winningPlayer.getName() + " WINS THIS ROUND AND MAKES " + getPoints(cardsInPlay, winningPlayer, players) + " POINTS \n");

                    if (winningPlayer.getTeam() == 1) {
                        p1Points += getPoints(cardsInPlay, winningPlayer, players);
                    }
                    if (winningPlayer.getTeam() == 2) {
                        p2Points += getPoints(cardsInPlay, winningPlayer, players);
                    }
                    if (winningPlayer.getTeam() == 3) {
                        p3Points += getPoints(cardsInPlay, winningPlayer, players);
                    }
                    if (winningPlayer.getTeam() == 4) {
                        p4Points += getPoints(cardsInPlay, winningPlayer, players);
                    }

                    currentPlayer = players.indexOf(winningPlayer);
                    currentGameHand = ",,GAME HAND:,,"; //reset String to default
                    cardsInPlay.clear();
                    continue;
                }

            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            currentPlayer++;
        }


    }





    private void updateGameInfo(List<Player> players, int p1Points, int p2points, int p3Points, int p4Points, String currentGameHand) {
        updateScoreInfo(players, p1Points, p2points, p3Points,p4Points);
        dealer.sendAll(players, currentGameHand);
    }


    private void updateScoreInfo(List<Player> players,int p1Points,int p2Points,int p3Points ,int p4Points) {
        dealer.broadcastMessage(players, "Global Score - " + players.get(0).getName() + ": " + p1GlobalScore + " || " + players.get(1).getName() + ": " + p2GlobalScore +
                " || " + players.get(2).getName() + ": " + p3GlobalScore + " || " + players.get(3).getName() + ": " + p4GlobalScore);
        dealer.broadcastMessage(players, "Points made this game - " + players.get(0).getName() + ": " + p1Points + " || " + players.get(1).getName() + ": " + p2Points +
                " || " + players.get(2).getName() + ": " + p3Points + " || " + players.get(3).getName() + ": " + p4Points);
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

    private void informPlayerPartner(List<Player> players) {
        players.get(0).setTeam(1);
        players.get(1).setTeam(2);
        players.get(2).setTeam(3);
        players.get(3).setTeam(4);
        players.get(0).send("\nYou're Player number: " + players.get(0).getTeam() );
        players.get(1).send("\nYou're Player number: " + players.get(1).getTeam() );
        players.get(2).send("\nYou're Player number: " + players.get(2).getTeam() );
        players.get(3).send("\nYou're Player number: " + players.get(3).getTeam() );
    }



    /**
     * Allows each player to have an assigned hand
     *
     * @param players players in game
     */
    private void prepareGame(List<Player> players) {
        dealer.dealCards(players, CARDS_PER_PLAYER, DECK_TYPE);
    }


    private boolean checkGameEnd(List<Player> players,int p1Points, int p2Points, int p3Points, int p4Points, int totalCardsPlayed) throws InterruptedException {
        if (totalCardsPlayed == NUMBER_OF_PLAYERS * CARDS_PER_PLAYER) {
            System.out.println("Game ended\n");
            dealer.broadcastMessage(players, "\n\n GAME HAS ENDED \n");

            setGameScore(players, p1Points, p2Points, p3Points, p4Points);

            dealer.broadcastMessage(players, "\n GLOBAL SCORE:");
            dealer.broadcastMessage(players, "Global Score - " + players.get(0).getName() + ": " + p1GlobalScore + " || " + players.get(1).getName() + ": " + p2GlobalScore +
                    " || " + players.get(2).getName() + ": " + p3GlobalScore + " || " + players.get(3).getName() + ": " + p4GlobalScore);

            startingPlayer++;

            if (p1GlobalScore >= END_GAME_POINTS){
                dealer.broadcastMessage(players,"\n\n SET ENDED \n");
                dealer.broadcastMessage(players, (players.get(0).getName() + "has lost the game \n\n"));
                resetSet();
                Thread.sleep(3000);
            }else if (p2GlobalScore >= END_GAME_POINTS){
                dealer.broadcastMessage(players,"\n\n SET ENDED \n");
                dealer.broadcastMessage(players, (players.get(1).getName() + "has lost the game\n\n"));
                  resetSet();
                Thread.sleep(3000);
            }else if( p3GlobalScore >= END_GAME_POINTS){
                dealer.broadcastMessage(players,"\n\n SET ENDED \n");
                dealer.broadcastMessage(players, (players.get(2).getName() + "has lost the game\n\n"));
                resetSet();
                Thread.sleep(3000);
            } else if (p4GlobalScore >= END_GAME_POINTS ){
                    dealer.broadcastMessage(players,"\n\n SET ENDED \n");
                    dealer.broadcastMessage(players, (players.get(3).getName() + "has lost the game\n\n"));
                    resetSet();
                Thread.sleep(3000);
            }

            if (startingPlayer >= NUMBER_OF_PLAYERS) {
                startingPlayer = 0;
            }

            System.out.println("Starting a new game.\n");
            playGame(players);
            return false;
        }
        return true;
    }

    private void resetSet() {
        p1GlobalScore = 0;
        p2GlobalScore = 0;
        p3GlobalScore = 0;
        p4GlobalScore = 0;
    }


    private void setGameScore(List<Player> players,int p1Points, int p2Points, int p3Points, int p4Points) {

        p1GlobalScore += p1Points;
        p2GlobalScore += p2Points;
        p3GlobalScore += p3Points;
        p4GlobalScore += p4Points;

        dealer.broadcastMessage(players, players.get(0).getName() + " made" + p1Points  + " points");
        dealer.broadcastMessage(players, players.get(1).getName() + " made" + p2Points  + " points");
        dealer.broadcastMessage(players, players.get(2).getName() + " made" + p3Points  + " points");
        dealer.broadcastMessage(players, players.get(3).getName() + " made" + p4Points  + " points");

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

        if (!playedCard.getSuit().equals(higherCard.getSuit())) {
                return higherCard;
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
                System.err.println(currentPlayer.getName() + "<" + currentPlayer.getClientSocket().getInetAddress() + ":" + currentPlayer.getClientSocket().getPort() + "> left");
            }

            if (moveString == null) {
                for (Player p : players) {
                    System.out.println(currentPlayer.getName() + "<" + currentPlayer.getClientSocket().getInetAddress() + ":" + currentPlayer.getClientSocket().getPort() + "> left\n");
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
            if (c.getSuit().equals(Cards.Suit.HEARTS)){
                points += 1;
            }
            if (c.equals(Cards.QUEEN_OF_SPADES)){
                points+= 10;
            }
        }
        return points;

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



