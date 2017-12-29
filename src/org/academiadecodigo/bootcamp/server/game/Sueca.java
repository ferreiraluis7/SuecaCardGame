package org.academiadecodigo.bootcamp.server.game;

import org.academiadecodigo.bootcamp.Randomizer;
import org.academiadecodigo.bootcamp.server.player.Player;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Sueca implements Game {

    public static final int TOTAL_POINTS = 120;
    public static final int NUMBER_OF_PLAYERS = 4;
    public static final int CARDS_PER_PLAYER = 10;
    public static final CardDealer.DeckType DECK_TYPE = CardDealer.DeckType.REGIONAL;
    private int trueVictories;
    private int falseVictories;
    private CardDealer dealer;
    private  int startingPlayer = 0;

    private boolean isGameStarted;

    /**
     * Starts the game loop
     *
     * @param players the game players
     */
    public void playGame (List<Player> players) {
        Cards.Suit trumpSuit= null;
        int currentPlayer = startingPlayer;
        List<Cards> cardsInPlay = new ArrayList<>();
        Cards playedCard;
        Cards.Suit currentSuit = null;
        int totalCardsPlayed = 0;
        Cards higherCard = null;
        Cards tempCard;
        Player winningPlayer = null;
        int score = 0;
        String currentGameHand = "";
        //the player that starts the first game of a lobby is always the first to

        // game init
        System.out.println("GAME IS ABOUT TO START");

        if (!isGameStarted) {
            //Set card hand for each player
            dealer.dealCards(players,CARDS_PER_PLAYER, DECK_TYPE);
            isGameStarted = true;
            //choose the trumpSuit
            trumpSuit = Cards.values()[Randomizer.getRandom(Cards.values().length)].getSuit();
            System.out.println("TRUMP IS " + trumpSuit);
            currentGameHand = "\n\nTRUMP: " + trumpSuit + "\n\nGAME HAND:\n";

        }

        //game loop


        //don't forget o change the while condition
            while (isGameStarted) {

                if(currentPlayer >= players.size()){
                    currentPlayer = 0;
                }
                try {

                    if(totalCardsPlayed == NUMBER_OF_PLAYERS * CARDS_PER_PLAYER){
                        System.out.println("Entered end game condition");

                        if (score < TOTAL_POINTS/2){
                            falseVictories++;
                        }else if (score > TOTAL_POINTS/2){
                            trueVictories ++;
                        }


                        startingPlayer ++;
                        isGameStarted = false; //set, show and send game score ++ update team score ++ call for a new game(new method) ++ GAME SETS?? create playsets method??
                        playGame(players);
                    }


                    playedCard = getMove(players.get(currentPlayer),players);


                    if(cardsInPlay.isEmpty()){
                        cardsInPlay.add(playedCard);
                        higherCard = playedCard;
                        winningPlayer = players.get(currentPlayer);

                        currentGameHand += players.get(currentPlayer).getName() + " card: " + playedCard + "\n"; //add players and card to the string

                        players.get(currentPlayer).removeCard(playedCard); // convert to method
                        totalCardsPlayed++;//need to send info to client remove card
                        currentSuit = playedCard.getSuit();
                        currentPlayer++;
                        dealer.sendAll(players, currentGameHand);
                        continue;
                    }


                    if(checkMove(players.get(currentPlayer), playedCard, currentSuit)){
                        System.out.println("Entered renuncia condition");
                        players.get(currentPlayer).send("You are not allowed to play that card, please play another");  //INNER CLASS W/ MESSAGES BUILDER METHODS
                        //Need to send info to client so he knows is an invalid move
                        continue;
                    }

                    cardsInPlay.add(playedCard);
                    System.out.println("Entered the not first play segment");
                    tempCard = checkHigherCard(playedCard,higherCard, trumpSuit);
                    if(!tempCard.equals(higherCard)){
                        winningPlayer = players.get(currentPlayer);
                        higherCard = tempCard;
                    }

                    players.get(currentPlayer).removeCard(playedCard);
                    currentGameHand += players.get(currentPlayer).getName() + " card: " + playedCard + "\n"; //add players and card to the string
                    totalCardsPlayed++;                                 // convert to method
                    //dealer.sendAll(players, cardsInPlay);
                    dealer.sendAll(players, currentGameHand);
                    if(cardsInPlay.size() == NUMBER_OF_PLAYERS){
                        //added Method to tell all players the winner of the round
                        sendAllWinner(players,"PLAYER" + players.indexOf(winningPlayer) + " WINS THIS ROUND" + "AND MAKES " + getPoints(cardsInPlay, winningPlayer, players) + " POINTS");
                        score += getPoints(cardsInPlay, winningPlayer, players);
                        currentPlayer = players.indexOf(winningPlayer);
                        currentGameHand = "\n\nTRUMP: " + trumpSuit + "\n\nGAME HAND:\n"; //reset String to default
                        cardsInPlay.clear();
                        continue;
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
                currentPlayer++;
            }


    }

    private Cards checkHigherCard(Cards playedCard, Cards higherCard, Cards.Suit trumpSuit) {
        System.out.println("Entered checkHigherCard");
       if (!playedCard.getSuit().equals(higherCard.getSuit())){
           if (!playedCard.getSuit().equals(trumpSuit)){
               System.out.println("Exited checkHigherCard w/ same card because different suit");
               return higherCard;
           }
           System.out.println("Exited checkHigherCard w/ trump card");
           return playedCard;
        }

        if (playedCard.getRank().getSuecaRank() > higherCard.getRank().getSuecaRank()){
            System.out.println("Exited checkHigherCard w/ higher card");
           return playedCard;
        }

        System.out.println("Exited checkHigherCard w/ same card equal suit");
        return higherCard;

    }

    @Override
    public Cards getMove(Player currentPlayer, List<Player> players) throws IOException {

        for (Player p : players) {
            if (p == currentPlayer) {
                p.send("It is your turn, choose a card to play [0 - " + (p.getHand().size() - 1) + "]");
            } else {
                p.send(currentPlayer.getName() + " is playing...");
            }
        }

        while (true){
            System.out.println("enterd getmove loop");

            String moveString = currentPlayer.readFromClient();
            System.out.println("player said " + moveString);
            try {
                int cardIndex = Integer.parseInt(moveString);
                if (cardIndex < 0 || cardIndex >= currentPlayer.getHand().size()){
                    System.out.println("card index higher/lower than expected");
                    currentPlayer.send("please give us a card you have");
                    continue;
                }

                Cards card = currentPlayer.getHand().get(cardIndex);

                System.out.println("Player" + " played " + card);

                System.out.println("exited get move");
                return card;


            }catch (NumberFormatException e){
                currentPlayer.send("invalid choice, please select a card to play");
                continue;
            }

            //Cards cards  = currentPlayer.getHand().get()

        }


    }

    private void sendAllWinner(List<Player> players, String winnerMessage) {
        for (Player p : players) {
            p.send(winnerMessage);
        }

    }

    /**
     * @see Game#checkMove(Player, Cards, Cards.Suit)
     */
    @Override
    public boolean checkMove(Player player, Cards card, Cards.Suit currentSuit) {
        System.out.println("renuncia: " + (playerHandHasSuit(player, currentSuit) && !card.getSuit().equals(currentSuit)));

        return  (playerHandHasSuit(player, currentSuit) && !card.getSuit().equals(currentSuit));  //check Renuncia

    }

    private boolean playerHandHasSuit(Player player, Cards.Suit currentSuit) {
        System.out.println("entered player has suit");
        for (Cards c : player.getHand()) {
            if (c.getSuit().equals(currentSuit)){
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
        if (players.indexOf(winningPlayer) == 1 || players.indexOf(winningPlayer) == 3){
            return 0;
        }
        int points = 0;
        for (Cards c : cardsPlayed){
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

    @Override
    public int getTotalPlayers() {
        return NUMBER_OF_PLAYERS;
    }

    @Override
    public void setDealer(CardDealer dealer) {
        this.dealer = dealer;
    }

}
