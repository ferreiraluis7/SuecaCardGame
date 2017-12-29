package org.academiadecodigo.bootcamp.client;

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
    private final static String HOST = "localhost";
    public static boolean playerTurn = false;
    private Socket clientSocket = null;
    private BufferedReader input = null;

    public static void main(String[] args) {
        Client client = new Client();
        client.start();
    }

    /**
     * Starts the client
     */
    private void start(){
        //If can't connect to server, leave.
        if(!connectServer()) {
            return;
        }

        ExecutorService outThread = Executors.newSingleThreadExecutor();
        outThread.execute(new ClientPlays(clientSocket, this));
        try {
            input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            String whenToPlay = "It is your turn,";

            while (true) {
                String readLine = input.readLine();

                decodeReceivedString(readLine);

                if (readLine.contains(whenToPlay)) {
                    playerTurn = true;
                }
            }
        } catch (IOException e) {
            System.err.println("Client disconnected");
        }

    }

    /**
     * Decodes incoming message from server
     *
     * @param readLine incoming message from server
     *
     * @return decoded message
     */
    private void decodeReceivedString(String readLine) throws IOException {
        if (readLine.contains("//")) {
            String[] readLineSplit = readLine.split(",,");
            String[] handSplit = readLineSplit[0].split("//");
            for(int counter = 0; counter < handSplit.length; counter++) {
                String cardCode = Cards.valueOf(handSplit[counter]).getUnicode();
                renderToScreen(counter + " - " + cardCode); //SOUT CARDS HAND
            }
            if (readLineSplit.length > 1) { //BECAUSE 1ST STRING DOESN'T HAVE ,, AND WITHOUT THIS CONDITION IT WOULD CAUSE INDEX OUT OF BOUNDS
                renderToScreen(readLineSplit[1]); //SOUT GAME HAND
            }
        }else if (readLine.contains("PLAYERQUIT")){
            String[] readLineSplit = readLine.split("@@");
            renderToScreen(readLineSplit[1]);
            renderToScreen(input.readLine());
            input.close();
        } else {
            renderToScreen(readLine);
        }
    }

    /**
     * Renders the decoded message to the terminal
     */
    public void renderToScreen(String decodedMessage){
        System.out.println(decodedMessage);
    }

    /**
     * @see Playable#play(String)
     */

    /**
     * @see Playable#checkKeyboardInput(String)
     *
     **/


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

    public void newGame(){
        start();
    }

}

