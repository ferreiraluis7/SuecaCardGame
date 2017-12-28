package org.academiadecodigo.bootcamp.server.player;

import org.academiadecodigo.bootcamp.server.game.Cards;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Player {

    private PrintWriter output;
    private BufferedReader input;
    private String name;
    private Socket clientSocket;
    private int team;
    private List<Cards> hand = new ArrayList<>();

    public Player(Socket playerConnection) {
        this.clientSocket = playerConnection;
        try {
            this.output = new PrintWriter(clientSocket.getOutputStream(), true);
            this.input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Allows player to play a card
     *
     * @param card card to be played
     */
    public void play(Cards card){
        throw new UnsupportedOperationException();
    }

    /**
     * Removes card from player's hand
     *
     * @param card card to be removed
     *
     * @return removed card
     */
    public void removeCard(Cards card){
        hand.remove(card);
    }

    /**
     * Gets the player name
     *
     * @return the player name
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the client socket
     *
     * @return the client socket
     */
    public Socket getClientSocket() {
        return clientSocket;
    }

    /**
     * Gets the player team number
     *
     * @return the player team number
     */
    public int getTeam() {
        return team;
    }

    /**
     * Gets the player hand
     *
     * @return the player hand
     */
    public List<Cards> getHand() {
        return hand;
    }

    /**
     * Sets the player's name
     *
     * @param name player's name
     */
    public void setName(String name) {
        this.name = name;
    }

    public void setHand(List<Cards> hand) {
        this.hand = hand;
    }

    public void send(String string) {
        output.println("string to send: " + string);
        output.flush();




    }

    public String readFromClient() throws IOException {
        return input.readLine();
    }

}
