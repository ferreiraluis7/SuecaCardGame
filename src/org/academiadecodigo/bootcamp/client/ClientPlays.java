package org.academiadecodigo.bootcamp.client;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class ClientPlays implements Runnable, Playable {
    private static final int CARDSINHAND = 9;
    private Socket socket;
    private PrintWriter output;
    private int cardsPlayed = 0;
    private Client client;

    public ClientPlays(Socket socket, Client client) {
        this.client = client;
        this.socket = socket;
        try {
            output = new PrintWriter(socket.getOutputStream(), true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    @Override
    public void run() {

            Scanner sc = new Scanner(System.in);


            //change while later
           while(true) {

               String message = "-1";


               // THE PARSEINT METHOD TROWS AN EXCEPTION IF THE STRING IS NOT A NUMBER
               int parsedInt = 0;
               String wronglyEnteredString;
               try {
                  parsedInt = Integer.parseInt(message);
                  wronglyEnteredString = "";
               } catch (NumberFormatException e){

                    wronglyEnteredString = "error";
               }

                   while (parsedInt < 0 || parsedInt >= CARDSINHAND - cardsPlayed || !wronglyEnteredString.isEmpty()) {

                       message = sc.nextLine();
                       if (message.toUpperCase().equals("/NEWGAME")){
                           client.newGame();
                       }
                       try {
                           parsedInt = Integer.parseInt(message);
                       }catch (NumberFormatException e) {
                           client.renderToScreen("No Strings allowed.");
                           continue;
                       }
                       if (!Client.playerTurn) {
                           client.renderToScreen("It's not your turn...");
                           continue;
                       }
                       play(message);
                       Client.playerTurn = false;
                   }
                   if (!Client.playerTurn) {
                       continue;
                   }
                   sc.reset();
                   cardsPlayed++;
                   Client.playerTurn = false;
               clearScreen();


           }
    }

    @Override
    public boolean checkKeyboardInput(String input) {
        return false;
    }

    @Override
    public void play(String move) {
        try {
            output = new PrintWriter(socket.getOutputStream(), true);
            output.println(move);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void clearScreen(){
        client.renderToScreen("\033[H\033[2J");
    }

    public static void send(String confirm) {
    }
}
