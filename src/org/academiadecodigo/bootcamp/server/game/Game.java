package org.academiadecodigo.bootcamp.server.game;

import org.academiadecodigo.bootcamp.server.player.Player;

import java.util.List;

public interface Game {


    /**
     * Starts and/or runs the game
     * @param players
     */
    void start(List<Player> players);

    /**
     * Checks if the player move is legal according to game rules
     *
     * @param card intended to play
     */
    boolean checkMove(Cards card);

    /**
     * Checks the winning team for each play
     *
     * @return the winning team number
     */
    int checkPlay();

    /**
     * Draw cards for a specific player
     *
     * @return the hand of cards
     */
    List<Cards> drawCards(List<Cards> deck);

    /**
     * Gets the game score
     *
     * @return the game score
     */
    int getScore();

    int getTotalPlayers();
}


