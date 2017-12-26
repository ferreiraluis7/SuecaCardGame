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

        if (!isGameStarted) {
            //Set card hand for each player
            List<Cards> deck = shuffleDeck();
            for (Player p : players) {
                p.setHand(drawCards(deck));
                p.send(Cards.encode(deck));
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
        List<Cards> hand = deck.subList(0, CARDS_PER_PLAYER);
        System.out.println("Server Side: hand: " + hand);
        deck.removeAll(hand);
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
