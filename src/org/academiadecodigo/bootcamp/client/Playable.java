package org.academiadecodigo.bootcamp.client;

public interface Playable {
    /**
     * Checks for a proper keyboard input
     *
     * @param input the input given by the client
     *
     * @return if the keyboard input can be used
     */
    boolean checkKeyboardInput(String input);

    /**
     * Allows the client to communicate his move
     *
     * @param move the client move
     */
    void play(String move);
}
