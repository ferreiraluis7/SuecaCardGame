package org.academiadecodigo.bootcamp.server;

import org.academiadecodigo.bootcamp.server.game.*;
import org.academiadecodigo.bootcamp.server.player.Player;

import java.util.ArrayList;
import java.util.List;

public class GameHandler implements Runnable {
    private List<Player> playersInLobby;
    private Game game;
    private GameServer server;
    private int lobbyNumber;
    private GameType gameType;

    GameHandler(List<Player> playersForLobby, GameServer server, int lobbyNumber, GameType gameType) {
        this.gameType = gameType;
        this.playersInLobby = new ArrayList<>();
        this.playersInLobby.addAll(playersForLobby);
        this.server = server;
        this.lobbyNumber = lobbyNumber;

        if (gameType.equals(GameType.SUECA)){
            game = new Sueca();
        }
        if (gameType.equals(GameType.SALEMA)){
            game = new Salema();
        }
    }

    /**
     * Runs the task(s) to be executed by a thread
     */
    @Override
    public void run() {
        Thread.currentThread().setName("Lobby - " + lobbyNumber);

        while (true) { // delete if game doesn't ends after a number of wins

            if (game.isPlayerLeft() && server.getSuecaPlayerList().isEmpty()) { //IF NO PLAYERS WAITING STOP
                sendAll("No more players available... To play again, type </newGame>");
                return;

            } else if (game.isPlayerLeft() && (!server.getSuecaPlayerList().isEmpty())) { //IF THERE ARE PLAYERS WAITING

                try {
                    sendAll("Getting a new player...");
                    Thread.sleep(3000); //PARA ENGANAR E FINGIR QUE ESTA A FAZER MILHOES DE PROCESSOS COMPLICADOS
                    sendAll("\033[H\033[2J"); // CLEAR SCREEN

                    manageGameLists();

                    if (playersInLobby.size() != 4) { //IF LIST IS NOT FULL YET, CONTINUE TO PICK MORE PLAYERS
                        continue;
                    }

                    //NEW GAME WHEN LIST IS FULL
                    sendAll("Starting a new game with a new player.");
                    Thread.sleep(3000); //PARA ENGANAR E FINGIR QUE ESTA A FAZER MILHOES DE PROCESSOS COMPLICADOS
                    sendAll("\033[H\033[2J"); // CLEAR SCREEN
                    newGame();

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            System.out.println(Thread.currentThread().getName() + " is running.\r\n");
            game.setDealer(new CardDealer());
            game.playGame(playersInLobby);
        }
    }

    private void manageGameLists() {
        if (gameType.equals(GameType.SUECA)) {
            playersInLobby.add(server.getSuecaPlayerList().get(0)); //ADD THE WAITING PLAYER TO THIS LIST
            server.getSuecaPlayerList().get(0).send("Joined " + Thread.currentThread().getName());
            server.getSuecaPlayerList().remove(server.getSuecaPlayerList().get(0)); //REMOVE FROM WAITING LIST
        }
        if (gameType.equals(GameType.SALEMA)) {
            playersInLobby.add(server.getSalemaPlayerList().get(0)); //ADD THE WAITING PLAYER TO THIS LIST
            server.getSalemaPlayerList().get(0).send("Joined " + Thread.currentThread().getName());
            server.getSalemaPlayerList().remove(server.getSalemaPlayerList().get(0)); //REMOVE FROM WAITING LIST
        }
    }

    private void newGame() {
        if (gameType.equals(GameType.SUECA)){
            game = new Sueca();
        }
        if (gameType.equals(GameType.SALEMA)){
            game = new Salema();
        }
    }

    /**
     * Sends a message to all player in a lobby
     *
     * @param message message to be sent
     */
    private void sendAll(String message) {
        for (Player p : playersInLobby) {
            p.send(message);
        }
    }


}
