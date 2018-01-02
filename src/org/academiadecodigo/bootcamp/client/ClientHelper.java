package org.academiadecodigo.bootcamp.client;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class ClientHelper implements Runnable, Playable {
    private static final int CARDSINHAND = 9;
    private Socket socket;
    private PrintWriter output;
    private int cardsPlayed = 0;
    private Client client;

    ClientHelper(Socket socket, Client client) {
        this.client = client;
        this.socket = socket;
        try {
            output = new PrintWriter(socket.getOutputStream(), true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * @see Runnable#run()
     * <p>
     * Sends information to server
     */
    @Override
    public void run() {

        Scanner sc = new Scanner(System.in);


        //change while later
        while (true) {

            String message = "-1";


            // THE PARSEINT METHOD TROWS AN EXCEPTION IF THE STRING IS NOT A NUMBER
            int parsedInt = 0;
            String wronglyEnteredString;
            try {
                parsedInt = Integer.parseInt(message);
                wronglyEnteredString = "";
            } catch (NumberFormatException e) {

                wronglyEnteredString = "error";
            }

            while (parsedInt < 0 || parsedInt >= CARDSINHAND - cardsPlayed || !wronglyEnteredString.isEmpty()) {

                message = sc.nextLine();
                if (message.toUpperCase().equals("/NEWGAME") && !client.isPlayingGame()) {
                    client.setPlayingGame(false);
                    client.newGame();
                }

                if (!client.isSetName()) {
                    play(message);
                    client.setSetName(true);
                    continue;
                }

                if (!client.isPlayerTurn() && (message.toUpperCase().equals("/SUECA") || message.toUpperCase().equals("/SALEMA")) && !client.isPlayingGame()) {
                    play(message.toUpperCase());
                   continue;
                }

                if (!client.isPlayerTurn() && client.isPlayingGame()) {
                    client.renderToScreen("It's not your turn...");

                    continue;
                }

                if (!client.isPlayerTurn()){
                    client.renderToScreen("Wrong command. ");
                    continue;
                }
                try {
                    parsedInt = Integer.parseInt(message);
                } catch (NumberFormatException e) {
                    client.renderToScreen("Use the card numbers at left to play.");
                    continue;
                }

                play(message);
                client.setPlayerTurn(false);
            }
            if (!client.isPlayerTurn()) {
                continue;
            }
            sc.reset();
            cardsPlayed++;
            client.setPlayerTurn(false);
        }
    }

    /**
     * @see Playable#checkKeyboardInput(String)
     */
    @Override
    public boolean checkKeyboardInput(String input) {
        return false;
    }

    /**
     * @see Playable#play(String)
     */
    @Override
    public void play(String move) {
        try {
            output = new PrintWriter(socket.getOutputStream(), true);
            output.println(move);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
