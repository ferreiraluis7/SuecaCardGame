package org.academiadecodigo.bootcamp.server;

import org.academiadecodigo.bootcamp.server.game.Sueca;
import org.academiadecodigo.bootcamp.server.player.Player;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class GameServer {
    private final int PORT = 8080;
    private ServerSocket serverSocket;
    private List<Player> totalPlayersList;
    private List<Player> playerList;
    private int playerNumber = 1; //To assign dynamic names to the players


    private GameServer() {
        try {
            serverSocket = new ServerSocket(PORT);
            playerList = new ArrayList<>();
            totalPlayersList = new ArrayList<>();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        GameServer gameServer = new GameServer();
        gameServer.start();
    }


    /**
     * Starts the game server
     * Creates Players
     */
    private void start() {
        ExecutorService lobby = Executors.newCachedThreadPool();
        int lobbyNumber = 1;

        while (true) {
            playerList = new ArrayList<>();

            while (playerList.size() < Sueca.NUMBER_OF_PLAYERS) {
                try {
                    System.out.println("Waiting...\n");
                    Socket playerConnection = serverSocket.accept();
                    System.out.println("Player Connected from " + playerConnection.getInetAddress() + " at port " + serverSocket.getLocalPort() + ".\r\n");

                    Player playerConnected = new Player(playerConnection, playerNumber);
                    welcomeMessage(playerConnected);

                    totalPlayersList.add(playerConnected);
                    playerList.add(playerConnected);
                    playerNumber++;

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            System.out.println("Lobby " + lobbyNumber + " started.");

            lobby.submit(new GameHandler(playerList, this, lobbyNumber));
            lobbyNumber++;

            playerList.clear();
        }
    }

    /**
     * Gets the player list
     *
     * @return list of players
     */
    List<Player> getPlayerList() {
        return playerList;
    }

    /**
     * Generates and shows the welcome message
     *
     * @param player player to see the welcome message
     */

    private void welcomeMessage(Player player) {
        String suecaGame = "  _________                               ________                       \n" +
                " /   _____/__ __   ____   ____ _____     /  _____/_____    _____   ____  \n" +
                " \\_____  \\|  |  \\_/ __ \\_/ ___\\\\__  \\   /   \\  ___\\__  \\  /     \\_/ __ \\ \n" +
                " /        \\  |  /\\  ___/\\  \\___ / __ \\_ \\    \\_\\  \\/ __ \\|  Y Y  \\  ___/ \n" +
                "/_______  /____/  \\___  >\\___  >____  /  \\______  (____  /__|_|  /\\___  >\n" +
                "        \\/            \\/     \\/     \\/          \\/     \\/      \\/     \\/ ";

        player.send(suecaGame);
        player.send("Welcome " + player.getName() + "! Waiting for players...");

    }
}
