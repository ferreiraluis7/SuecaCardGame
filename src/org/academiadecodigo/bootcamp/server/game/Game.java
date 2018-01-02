package org.academiadecodigo.bootcamp.server.game;

import org.academiadecodigo.bootcamp.server.player.Player;

import java.io.IOException;
import java.util.List;

public interface Game {


    /**
     * Starts and/or runs the game
     *
     * @param players game players
     */
    void playGame(List<Player> players);

    /**
     * wait for a move from the specified player
     *
     * @param currentPlayer the current player
     *
     * @return the card played
     */
    Cards getMove(Player currentPlayer,List<Player> players) throws IOException;

    /**
     * Checks if the player move is legal according to game rules
     *
     * @param card intended to play
     */
    boolean checkMove(Player player, Cards card, Cards.Suit currentSuit);

    /**
     * Checks the winning team for each play
     *
     * @return the winning team number
     */
    int getPoints(List<Cards> cardsPlayed, Player winningPlayer, List<Player> players);

    /**
     * Sets the game dealer
     *
     * @param dealer the game dealer
     */
    void setDealer(CardDealer dealer);

    /**
     * Checks if a player has left the game
     *
     * @return true or false if the player has or hasn't left the game
     */
    boolean isPlayerLeft();
}


