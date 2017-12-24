package org.academiadecodigo.bootcamp.server;

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
    ServerSocket serverSocket;
    private List<Player> playerList;

    public GameServer() {
        try {
            serverSocket = new ServerSocket(PORT);
            playerList = new ArrayList<>();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        GameServer game = new GameServer();
        game.start();
    }


    /**
     * Starts the game server
     * Creates Players
     */
    public void start() {
        //server methods here

        ExecutorService lobby = Executors.newCachedThreadPool();
        int lobbyNumber = 1;
        while (true) {
            Player[] playersForLobby = new Player[4];
            int playersConnected = 0;
            while (playersConnected < 4) {
                try {
                    System.out.println("Waiting...");
                    Socket playerConnection = serverSocket.accept();
                    System.out.println("Player Connected.");
                    playersForLobby[playersConnected] = new Player(playerConnection);
                    playerList.add(playersForLobby[playersConnected]);
                    playersConnected++;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            System.out.println("new Lobby");
            lobby.submit(new GameHandler(playersForLobby, this, lobbyNumber));
            lobbyNumber++;
        }

    }

    /**
     * Gets the player list
     *
     * @return list of players
     */
    public List<Player> getPlayerList() {
        return playerList;
    }
}
