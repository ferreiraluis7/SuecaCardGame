package org.academiadecodigo.bootcamp.server;

import org.academiadecodigo.bootcamp.server.game.Game;
import org.academiadecodigo.bootcamp.server.game.Sueca;
import org.academiadecodigo.bootcamp.server.player.Player;

import java.util.List;

public class GameHandler implements Runnable{
    private List<Player> playersInLobby;
    private Game game;
    private GameServer server;
    private int lobbyNumber;

    public GameHandler(List<Player> playersForLobby, GameServer server, int lobbyNumber) {
        playersInLobby = playersForLobby;
        this.game = new Sueca();
        this.server = server;
        this.lobbyNumber = lobbyNumber;
    }

    /**
     * Runs the task(s) to be executed by a thread
     */
    @Override
    public void run() {
            Thread.currentThread().setName("Lobby - " + lobbyNumber);



        while (true) {
            System.out.println(Thread.currentThread().getName() + " is running.");
            game.start(playersInLobby);





            //no futuro apagar o sleep
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
