package org.academiadecodigo.bootcamp.server.game;

import org.academiadecodigo.bootcamp.server.player.Player;

import java.io.IOException;
import java.util.List;

public class Sueca implements Game {

    public static final int TOTAL_POINTS = 120;
    public static final int NUMBER_OF_PLAYERS = 4;
    public static final int CARDS_PER_PLAYER = 10;
    public static final CardDealer.DeckType DECK_TYPE = CardDealer.DeckType.REGIONAL;
    private int trueVictories;
    private int falseVictories;
    private CardDealer dealer;

    private int cardsPlayed = 0;
    private boolean isGameStarted;

    /**
     * Starts the game loop
     *
     * @param players the game players
     */
    public void start(List<Player> players, int startingPlayer) {
        //the player that starts the first game of a lobby is always the first to

        // game init
        if (!isGameStarted) {
            //Set card hand for each player
            dealer.dealCards(players,CARDS_PER_PLAYER, DECK_TYPE);
            isGameStarted = true;
        }
        //choose the trumpRank
        Cards.Rank trumpRank = Cards.values()[(int)Math.random()*Cards.values().length].getRank();

        try {
            players.get(startingPlayer).send("It is your turn, choose a card to play");

            System.out.println(  players.get(startingPlayer).readFromClient());


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

    /**
     * Changes the game score
     *
     * @param score the score value to be changed
     */
    private void setScore(int score) {
        throw new UnsupportedOperationException();
    }
}
