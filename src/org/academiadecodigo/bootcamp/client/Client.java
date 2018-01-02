package org.academiadecodigo.bootcamp.client;

import org.academiadecodigo.bootcamp.kuusisto.tinysound.Music;
import org.academiadecodigo.bootcamp.kuusisto.tinysound.Sound;
import org.academiadecodigo.bootcamp.kuusisto.tinysound.TinySound;
import org.academiadecodigo.bootcamp.server.game.Cards;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Client {
    private final static int PORT = 8080;
    private static String HOST = "localhost";

    private boolean playerTurn = false;
    private Socket clientSocket = null;
    private BufferedReader input = null;
    private PrintWriter output = null;
    private boolean playingGame;



    public static void main(String[] args) {
        if (args.length == 1) {
            HOST = args[0];
        }
        if (args.length > 1) {
            System.out.println("Usage: java -jar SuecaGameClient.jar <server IP ADDRESS> - connects to a specific server\n" +
                    "java -jar SuecaGameClient.jar - connects to a local host (your machine)");
            System.exit(1);
        }
        Client client = new Client();
        client.start();
    }

    /**
     * Starts the client
     *
     * Listens for server communications
     */
    private void start() {
        TinySound.init();
        Sound sound = TinySound.loadSound("SE/turn.wav");
        Music music = TinySound.loadMusic("BGM/waiting.wav");
        //If can't connect to server, leave.
        if (!connectServer()) {
            return;
        }
        music.play(true);
        ExecutorService outThread = Executors.newSingleThreadExecutor();
        outThread.execute(new ClientHelper(clientSocket, this));
        try {

            input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            String whenToPlay = "It is your turn,";

            while (true) {
                String readLine = input.readLine();
                decodeReceivedString(readLine, music);
                if (readLine.contains(whenToPlay)) {
                    music.stop();
                    playerTurn = true;
                    sound.play();
                }
            }
        } catch (IOException e) {
            System.err.println("Server went down");
            System.exit(1);
        }
    }

    /**
     * Clears the terminal screen
     */
    private void clearScreen() {
        System.out.print("\033[H\033[2J");
    }

    /**
     * Decodes incoming message from server
     *
     * @param readLine incoming message from server
     *
     * @return decoded message
     */
    private void decodeReceivedString(String readLine, Music music) throws IOException {
        if (readLine == null) {
            System.exit(1);
        }

        if (readLine.contains("VICTORIES") || readLine.contains("GLOBAL SCORE")) {
            playingGame = true;
            music.stop();
            clearScreen();
            renderToScreen(readLine);
            return;
        }

        if(readLine.contains("type </newGame>")){
            playingGame = false;
        }

        if (readLine.equals("CHECKCONNECT")) {
            output = new PrintWriter(clientSocket.getOutputStream(), true);
            output.println("YES");
            output.flush();
        } else if (readLine.contains("//")) {
            String[] readLineSplit = readLine.split(",,");
            String[] handSplit = readLineSplit[0].split("//");
            for (int counter = 0; counter < handSplit.length; counter++) {
                String cardCode = Cards.valueOf(handSplit[counter]).getUnicode();
                renderToScreen(counter + " - " + cardCode); //SOUT CARDS HAND
            }
            if (readLineSplit.length > 1) { //BECAUSE 1ST STRING DOESN'T HAVE ,, AND WITHOUT THIS CONDITION IT WOULD CAUSE INDEX OUT OF BOUNDS
                renderToScreen(readLineSplit[1]); //SOUT GAME HAND
            }
        } else if (readLine.contains("PLAYERQUIT")) {
            playingGame = false;
            String[] readLineSplit = readLine.split("@@");
            renderToScreen(readLineSplit[1]);
            renderToScreen(input.readLine());
            //input.close();
        } else {
            renderToScreen(readLine);
        }
    }

    /**
     * Renders the decoded message to the terminal
     */
    void renderToScreen(String decodedMessage) {
        System.out.println(decodedMessage);
    }

    /**
     * Connects to the server if possible
     *
     * @return true or false if the connection was or wasn't not successful
     */
    private boolean connectServer() {
        boolean serverConnected = false;
        try {
            renderToScreen("Connecting to server...");
            clientSocket = new Socket(HOST, PORT);
            renderToScreen("Connected.");
            serverConnected = true;
        } catch (IOException e) {
            renderToScreen("Couldn't connect.");
            serverConnected = false;
        }
        return serverConnected;
    }

    /**
     * Restarts the client
     */
    void newGame() {
        start();
    }

    /**
     * Gets true or false if it is or isn't the player turn
     */
    boolean isPlayerTurn() {
        return playerTurn;
    }

    /**
     * Sets the player turn true or false
     */
    void setPlayerTurn(boolean playerTurn) {
        this.playerTurn = playerTurn;
    }


    void setPlayingGame(boolean playingGame) {
        this.playingGame = playingGame;
    }

    boolean isPlayingGame() {
        return playingGame;
    }
}

