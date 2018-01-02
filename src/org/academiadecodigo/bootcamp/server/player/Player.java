package org.academiadecodigo.bootcamp.server.player;

import org.academiadecodigo.bootcamp.server.game.Cards;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class Player {

    private PrintWriter output;
    private BufferedReader input;
    private String name;
    private Socket clientSocket;
    private int team;
    private List<Cards> hand = new ArrayList<>();

    public Player(Socket playerConnection, int nameSuffix) {
        this.clientSocket = playerConnection;
        try {
            this.output = new PrintWriter(new OutputStreamWriter(
                    clientSocket.getOutputStream(), StandardCharsets.UTF_8), true);
            this.input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.name = "" + nameSuffix;

    }

    /**
     * Removes card from player's hand
     *
     * @param card card to be removed
     * @return removed card
     */
    public void removeCard(Cards card) {
        System.out.println(name + " removed " + card.getCompleteName() + " from hand. \n");
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
     * Sets the player's hand
     *
     * @param hand player's hand
     */
    public void setHand(List<Cards> hand) {
        this.hand = hand;
    }

    /**
     * Sends a message to the corresponding client
     *
     * @param string message to be sent
     */
    public void send(String string) {
        System.out.println( "Sent a message to " + name + " at " + clientSocket.getInetAddress() + ":" + clientSocket.getPort() + ".\r\n");
        output.println(string);
        output.flush();
    }

    /**
     * Receives message from the corresponding client
     *
     * @return message received
     *
     * @throws IOException
     */
    public String readFromClient() throws IOException {
        System.out.println("Received a message from " + name + " at " + clientSocket.getInetAddress() + ":" + clientSocket.getPort() + ".\r\n");
        return input.readLine();
    }

    /**
     * Sets the player's team
     *
     * @param team player's hand
     */
    public void setTeam(int team) {
        this.team = team;
    }

    public void setName(String name) {
        this.name = name + "#" + this.name;
    }
}


