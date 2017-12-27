package org.academiadecodigo.bootcamp.server.game;

import org.academiadecodigo.bootcamp.server.player.Player;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Sueca implements Game {

    public static final int TOTAL_POINTS = 120;
    public static final int NUMBER_OF_PLAYERS = 4;
    public static final int CARDS_PER_PLAYER =10;


    private int cardsPlayed = 0;
    private boolean isGameStarted;


    /* public void playGame(List<Player> players){
        private int team1Wins;
        private int team2Wins;
        int playingPlayer = 0;
        int gameScore = 0;

        while ()
        init(players);
        gameloop(players, playingPlayer );



    } */

    private void init(List<Player> players) {
        if (isGameStarted){
            return;
        }

        List<Cards> deck = shuffleDeck();
        for (Player p : players){
            List<Cards> hand = drawCards(deck);
            p.setHand(hand);
            p.send(Cards.encode(hand));
        }
        isGameStarted = true;
    }



    /**
     * Starts the game loop
     *
     * @param players the game players
     */
    public void start(List<Player> players) {
        //the player that starts the first game of a lobby is always the first to
        int playingPlayer = 0;

        // game init
        if (!isGameStarted) {
            //Set card hand for each player
            List<Cards> deck = shuffleDeck();
            for (Player p : players){
                    List<Cards> hand = drawCards(deck);
                    p.setHand(hand);
                    p.send(Cards.encode(hand));
                }
            isGameStarted = true;
        }
        //choose the trumpRank
        Cards.Rank trumpRank = Cards.values()[(int)Math.random()*Cards.values().length].getRank();

        try {
            players.get(playingPlayer).send("It is your turn, choose a card to play");

            System.out.println(  players.get(playingPlayer).readFromClient());


        } catch (IOException e) {
            e.printStackTrace();
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
