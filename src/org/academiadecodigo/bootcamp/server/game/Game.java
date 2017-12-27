package org.academiadecodigo.bootcamp.server.game;

import org.academiadecodigo.bootcamp.server.player.Player;

import java.io.IOException;
import java.util.List;

public interface Game {


    /**
     * Starts and/or runs the game
     * @param players
     */
    void playGame(List<Player> players, int startingPlayer);

    /**
     * wait for a move from the specified player
     * @param currentPlayer
     * @return
     */
    Cards getMove(Player currentPlayer) throws IOException;

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
     * Gets the game score
     *
     * @return the game score
     */
    int getScore();

    int getTotalPlayers();

    void setDealer(CardDealer dealer);
}


