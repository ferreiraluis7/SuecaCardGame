package org.academiadecodigo.bootcamp.server;

import org.academiadecodigo.bootcamp.server.game.GameType;
import org.academiadecodigo.bootcamp.server.game.Salema;
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
    private List<Player> suecaPlayerList;
    private List<Player> salemaPlayerList;
    private int playerNumber = 1; //To assign dynamic names to the players
    private int lobbyNumber = 0;


    private GameServer() {
        try {
            serverSocket = new ServerSocket(PORT);
            suecaPlayerList = new ArrayList<>();
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
        ExecutorService selectGame = Executors.newCachedThreadPool();
        salemaPlayerList = new ArrayList<>();
        suecaPlayerList = new ArrayList<>();


        while (true) {


            try {
                System.out.println("Waiting...\n");
                Socket playerConnection = serverSocket.accept();
                System.out.println("Player Connected from " + playerConnection.getInetAddress() + ":" + playerConnection.getPort() + "\r\n");
                Player playerConnected = new Player(playerConnection, playerNumber);

                playerConnected.send("LEGITCHECK");
                if (!playerConnected.readFromClient().equals("LEGIT")){
                    playerConnection.close();
                    continue;
                }

                totalPlayersList.add(playerConnected);

                selectGame.submit(new ServerHelper(playerConnected, lobby, this));


                playerNumber++;

            } catch (IOException e) {
                e.printStackTrace();
            }


        }
    }

    /**
     * Gets the player list
     *
     * @return list of players
     */
    List<Player> getSuecaPlayerList() {
        return suecaPlayerList;
    }

    public List<Player> getSalemaPlayerList() {
        return salemaPlayerList;
    }

    /**
     * Generates and shows the welcome message
     *
     * @param player player to see the welcome message
     */

    private void welcomeMessageSalema(Player player){
        String salemaGame = "  _________      .__                            ________                       \n" +
                " /   _____/____  |  |   ____   _____ _____     /  _____/_____    _____   ____  \n" +
                " \\_____  \\\\__  \\ |  | _/ __ \\ /     \\\\__  \\   /   \\  ___\\__  \\  /     \\_/ __ \\ \n" +
                " /        \\/ __ \\|  |_\\  ___/|  Y Y  \\/ __ \\_ \\    \\_\\  \\/ __ \\|  Y Y  \\  ___/ \n" +
                "/_______  (____  /____/\\___  >__|_|  (____  /  \\______  (____  /__|_|  /\\___  >\n" +
                "        \\/     \\/          \\/      \\/     \\/          \\/     \\/      \\/     \\/ ";


        player.send(salemaGame);
        player.send("Welcome " + player.getName() + "! Waiting for players...");
    }

    private void welcomeSuecaMessage(Player player) {
        String suecaGame = "  _________                               ________                       \n" +
                " /   _____/__ __   ____   ____ _____     /  _____/_____    _____   ____  \n" +
                " \\_____  \\|  |  \\_/ __ \\_/ ___\\\\__  \\   /   \\  ___\\__  \\  /     \\_/ __ \\ \n" +
                " /        \\  |  /\\  ___/\\  \\___ / __ \\_ \\    \\_\\  \\/ __ \\|  Y Y  \\  ___/ \n" +
                "/_______  /____/  \\___  >\\___  >____  /  \\______  (____  /__|_|  /\\___  >\n" +
                "        \\/            \\/     \\/     \\/          \\/     \\/      \\/     \\/ ";

        player.send(suecaGame);
        player.send("Welcome " + player.getName() + "! Waiting for players...");

    }

    private class ServerHelper implements Runnable {
        private final ExecutorService lobby;
        private final GameServer server;
        private Player connectedPlayer;

        public ServerHelper(Player connectedPlayer, ExecutorService lobby, GameServer gameServer) {
            this.connectedPlayer = connectedPlayer;
            this.lobby = lobby;
            this.server = gameServer;
        }

        @Override
        public void run() {
            connectedPlayer.send("Type your username:");
            try {
                connectedPlayer.setName(connectedPlayer.readFromClient());
            } catch (IOException e) {
                e.printStackTrace();
            }


            connectedPlayer.send("Which game you want to play? ");
            connectedPlayer.send("type </sueca> for sueca or </salema> for salema");

            String chosenGame = null;
            try {
                chosenGame = connectedPlayer.readFromClient();
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (chosenGame.toUpperCase().equals("/SUECA")) {
                welcomeSuecaMessage(connectedPlayer);

                synchronized (suecaPlayerList) {
                    suecaPlayerList.add(connectedPlayer);
                }

            }
            if (chosenGame.toUpperCase().equals("/SALEMA")) {
                welcomeMessageSalema(connectedPlayer);
                synchronized (salemaPlayerList) {
                    salemaPlayerList.add(connectedPlayer);
                }
            }

            if (suecaPlayerList.size() == Sueca.NUMBER_OF_PLAYERS) {

                synchronized ((Integer) lobbyNumber) {
                    lobbyNumber++;
                }

                System.out.println("Lobby " + lobbyNumber + " started.");
                synchronized (lobby) {
                    lobby.submit(new GameHandler(suecaPlayerList, server, lobbyNumber, GameType.SUECA));
                }


                suecaPlayerList = new ArrayList<>();
            }


            if (salemaPlayerList.size() == Salema.NUMBER_OF_PLAYERS) {
                synchronized ((Integer) lobbyNumber) {
                    lobbyNumber++;
                }
                System.out.println("Lobby " + lobbyNumber + " started.");

                synchronized (lobby) {
                    lobby.submit(new GameHandler(salemaPlayerList, server, lobbyNumber, GameType.SALEMA));
                }

                salemaPlayerList = new ArrayList<>();
            }
        }
    }
}
