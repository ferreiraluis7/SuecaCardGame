package org.academiadecodigo.bootcamp.server.game;

import org.academiadecodigo.bootcamp.server.player.Player;

import java.util.List;

public class Sueca implements Game {

    public static final int TOTALPOINTS = 120;
    public static final int NUMBEROFPLAYERS = 4;

    private int score;

    /**
     * Starts the game loop
     *
     * @param players the game players
     */
    public void start(List<Player> players) {
        throw new UnsupportedOperationException();
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
     *@see Game#drawCards()
     */
    @Override
    public List<Cards> drawCards() {
        throw new UnsupportedOperationException();
    }

    /**
     * @see Game#getScore()
     */
    @Override
    public int getScore() {
        throw new UnsupportedOperationException();
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
