package org.academiadecodigo.bootcamp.client;

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
    private int cardsPlayed = 0;
    private String  message;
    private Socket clientSocket = null;
    private PrintWriter output = null;
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

        ExecutorService sendThread = Executors.newSingleThreadExecutor();
        sendThread.execute(new ClientPlays(clientSocket));
        try {
            input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            String whenToPlay = "It is your turn,";
            while (true) {
                String readLine = input.readLine();
                if (readLine.contains("//")) {
                    String[] readLineSplit = readLine.split(",,");
                    String[] handSplit = readLineSplit[0].split("//");
                    for(int counter = 0; counter < handSplit.length; counter++) {
                        System.out.println(counter + " - " + handSplit[counter]); //SOUT CARDS HAND
                    }
                    if (readLineSplit.length > 1) { //BECAUSE 1ST STRING DOESN'T HAVE ,, AND WITHOUT THIS CONDITION IT WOULD CAUSE INDEX OUT OF BOUNDS
                        System.out.println(readLineSplit[1]); //SOUT GAME HAND
                    }
                } else {
                    System.out.println(readLine);
                }
                if (readLine.contains(whenToPlay)) {
                    playerTurn = true;
                    cardsPlayed++;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * Decodes incoming message from server
     *
     * @param incoming incoming message from server
     *
     * @return decoded message
     */
    private String decodeReceivedString(String incoming) {
        throw new UnsupportedOperationException();
    }

    /**
     * Renders the decoded message to the terminal
     */
    private void renderToScreen(String decodedMessage){
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
            System.out.println("Connecting to server...");
            clientSocket = new Socket(HOST, PORT);
            System.out.println("Connected.");
            serverConnected = true;
        } catch (IOException e) {
            System.out.println("Couldn't connect.");
            serverConnected = false;
        }
        return serverConnected;
    }

    private void clearScreen(){
        final String clearString = "\r\n\r\n\r\n\r\n\r\n\r\n\r\n\r\n\r\n\r\n\r\n\r\n\r\n\r\n\r\n\r\n\r\n\r\n\r\n\r\n\r\n\r\n\r\n\r\n\r\n\r\n\r\n\r\n\r\n\r\n\r\n\r\n\r\n\r\n";
        System.out.println(clearString);
    }
}

