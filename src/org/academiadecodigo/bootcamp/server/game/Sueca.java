package org.academiadecodigo.bootcamp.server.game;

import org.academiadecodigo.bootcamp.server.player.Player;

import java.util.ArrayList;
import java.util.List;

public class Sueca implements Game {

    public static final int TOTAL_POINTS = 120;
    public static final int NUMBER_OF_PLAYERS = 4;
    public static final int CARDS_PER_PLAYER =10;
    private int score;

    private int cardsPlayed = 0;
    private boolean isGameStarted;

    /**
     * Starts the game loop
     *
     * @param players the game players
     */
    public void start(List<Player> players) {
        System.out.println("inside start game");


        if (!isGameStarted) {
            //Set card hand for each player
            List<Cards> deck = shuffleDeck();

            System.out.println(deck.size());
            System.out.println(players.size());

                for (Player p : players){
                    System.out.println("here");

                List<Cards> hand = drawCards(deck);

                    System.out.println("second");
                p.setHand(hand);
                    System.out.println("third");
                p.send(Cards.encode(hand));
            System.out.println("--------------------------" + players.size());
            }
            isGameStarted = true;
            return;
        }
        if (cardsPlayed == NUMBER_OF_PLAYERS * CARDS_PER_PLAYER){
            //the game ends
            //maybe needs a method for this?
            return;
        }








    }

    /**
     * @see Game#checkMove(Cards)
     */
    @Override
    public boolean checkMove(Cards card) {
        throw new UnsupportedOperationException();
    }

    /**
     * @see Game#checkPlay()
     */
    @Override
    public int checkPlay() {
        throw new UnsupportedOperationException();
    }


    /**
     *
     * @param deck
     * @return
     */
    @Override
    public List<Cards> drawCards(List<Cards> deck) {

        System.out.println("inside draw cards "  + deck.size());
        if (deck.size()<= CARDS_PER_PLAYER){
            return deck;
        }
        List<Cards> hand = new ArrayList<>();
        for (int i = 0 ; i< CARDS_PER_PLAYER ; i++){
            hand.add(deck.remove(i));
        }
        System.out.println(hand.size());
        System.out.println("second stage in draw");
        System.out.println( deck.removeAll(hand));


        return hand;
    }


    private List<Cards> shuffleDeck(){

        ArrayList<Cards> deck = new ArrayList<>();
        for (Cards card : Cards.values()){
            if (card.getRank().equals(Cards.Rank.SEVEN) ||
                    card.getRank().equals(Cards.Rank.EIGHT) ||
                    card.getRank().equals(Cards.Rank.NINE) ){

                continue;
            }
            deck.add(card);
        }
        ArrayList<Cards> shuffledDeck = new ArrayList<>();
       while (deck.size()> 0){
           int  index = (int) Math.random()*deck.size();
            shuffledDeck.add(deck.get(index));
            deck.remove(index);
       }
       return shuffledDeck;
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

    /**
     * Changes the game score
     *
     * @param score the score value to be changed
     */
    private void setScore(int score) {
        throw new UnsupportedOperationException();
    }
}
