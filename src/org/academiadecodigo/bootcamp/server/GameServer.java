package org.academiadecodigo.bootcamp.server;

import org.academiadecodigo.bootcamp.server.game.Game;
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

            //cesar : faz-me mais sentido que o array seja dimensionado conforme o valor que de uma variavel
            // estática defenida no Game

            // retiramos o array para uma lista


            int playersConnected = 0;
            while (playersConnected < Sueca.NUMBER_OF_PLAYERS) {
                try {
                    System.out.println("Waiting...");
                    Socket playerConnection = serverSocket.accept();
                    System.out.println("Player Connected.");
                    playerList.add(new Player(playerConnection));
                    playersConnected++;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            System.out.println("new Lobby");
            lobby.submit(new GameHandler(playerList, this, lobbyNumber));
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
