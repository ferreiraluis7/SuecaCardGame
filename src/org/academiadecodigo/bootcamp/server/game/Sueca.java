package org.academiadecodigo.bootcamp.server.game;

import org.academiadecodigo.bootcamp.Randomizer;
import org.academiadecodigo.bootcamp.server.player.Player;

import java.io.IOException;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

public class Sueca implements Game {

    public static final int TOTAL_POINTS = 120;
    public static final int NUMBER_OF_PLAYERS = 4;
    public static final int CARDS_PER_PLAYER = 10;
    public static final CardDealer.DeckType DECK_TYPE = CardDealer.DeckType.REGIONAL;
    private int teamOneVictories;
    private int teamTwoVictories;
    private CardDealer dealer;
    private  int startingPlayer = 0;
    private boolean playerLeft;



    /**
     * Starts the game loop
     *
     * @param players the game players
     */
    public void playGame (List<Player> players) {
        Cards.Suit trumpSuit= null;
        int currentPlayer = startingPlayer;
        List<Cards> cardsInPlay = new ArrayList<>();
        Cards playedCard;
        Cards.Suit currentSuit = null;
        int totalCardsPlayed = 0;
        Cards higherCard = null;
        Cards tempCard;
        Player winningPlayer = null;
        int score = 0;
        String currentGameHand = "";
        boolean isGameStarted = false;
        //the player that starts the first game of a lobby is always the first to

        // game init
        System.out.println("GAME IS ABOUT TO START");

        if (!isGameStarted) {
            //Set card hand for each player
            prepareGame(players);
            //choose the trumpSuit
            trumpSuit = randomizeTrumpSuit();
            System.out.println("TRUMP IS " + trumpSuit);
            currentGameHand = generateTrumpSuitMessage(trumpSuit) + "\r\nGAME HAND:\r\n";
            informPlayerPartner(players);
            dealer.broadcastMessage(players, generateTrumpSuitMessage(trumpSuit));
            isGameStarted = true;

        }

        //game loop


        //don't forget o change the while condition
            while (isGameStarted) {




                if(currentPlayer >= players.size()){
                    currentPlayer = 0;
                }
                try {

                    isGameStarted = checkGameEnd(players, totalCardsPlayed, score);


                    playedCard = getMove(players.get(currentPlayer),players);
                    if (checkIfPlayerLeft(players)) {
                        System.out.println("EXIT PLAYER LOOP");
                        playerLeft = true;
                        try {
                            Thread.sleep(2000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        return;
                    }

                    //if (playedCard == null){
                    //    return;
                    //}

                    if(cardsInPlay.isEmpty()){

                        confirmPlay(players, currentPlayer, cardsInPlay, playedCard);

                        higherCard = playedCard;
                        winningPlayer = players.get(currentPlayer);

                        currentGameHand += players.get(currentPlayer).getName() + " card: " + playedCard.getUnicode() + "\r\n"; //add players and card to the string
                        // convert to method
                        totalCardsPlayed++;//need to send info to client remove card
                        currentSuit = playedCard.getSuit();
                        currentPlayer++;
                        dealer.sendAll(players, currentGameHand);
                        continue;
                    }


                    if(checkMove(players.get(currentPlayer), playedCard, currentSuit)){
                        System.out.println("Entered renuncia condition");
                        players.get(currentPlayer).send("You are not allowed to play that card, please play another");  //INNER CLASS W/ MESSAGES BUILDER METHODS
                        //Need to send info to client so he knows is an invalid move
                        continue;
                    }

                    confirmPlay(players, currentPlayer, cardsInPlay, playedCard);
                    System.out.println("Entered the not first play segment");
                    tempCard = checkHigherCard(playedCard,higherCard, trumpSuit);
                    if(!tempCard.equals(higherCard)){
                        winningPlayer = players.get(currentPlayer);
                        higherCard = tempCard;
                    }
                    currentGameHand += players.get(currentPlayer).getName() + " card: " + playedCard.getUnicode() + "\n"; //add players and card to the string
                    totalCardsPlayed++;                                 // convert to method
                    //dealer.sendAll(players, cardsInPlay);
                    dealer.sendAll(players, currentGameHand);
                    if(cardsInPlay.size() == NUMBER_OF_PLAYERS){
                        //added Method to tell all players the winner of the round
                        dealer.broadcastMessage(players, winningPlayer.getName() + " WINS THIS ROUND AND MAKES " + getPoints(cardsInPlay, winningPlayer, players) + " POINTS \n");
                        if (players.indexOf(winningPlayer) == 0 || players.indexOf(winningPlayer) == 2){
                            score += getPoints(cardsInPlay, winningPlayer, players);
                        }
                        currentPlayer = players.indexOf(winningPlayer);
                        currentGameHand = generateTrumpSuitMessage(trumpSuit) + "\r\nGAME HAND:\r\n"; //reset String to default
                        cardsInPlay.clear();
                        continue;
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }

                currentPlayer++;
            }


    }

    private String generateTrumpSuitMessage(Cards.Suit trumpSuit) {
        return "\r\nTRUMP: " + trumpSuit + "\r\n";
    }

    private void confirmPlay(List<Player> players, int currentPlayer, List<Cards> cardsInPlay, Cards playedCard) {
        cardsInPlay.add(playedCard);
        players.get(currentPlayer).removeCard(playedCard);
    }

    private void informPlayerPartner(List<Player> players) {
        players.get(0).setTeam(1);
        players.get(1).setTeam(2);
        players.get(2).setTeam(1);
        players.get(3).setTeam(2);
        players.get(0).send("\nYou're in Team " + players.get(0).getTeam() + ". Your team mate is " + players.get(2).getName());
        players.get(1).send("\nYou're in Team " + players.get(1).getTeam() + ". Your team mate is " + players.get(3).getName());
        players.get(2).send("\nYou're in Team " + players.get(2).getTeam() + ". Your team mate is " + players.get(0).getName());
        players.get(3).send("\nYou're in Team " + players.get(3).getTeam() + ". Your team mate is " + players.get(1).getName());
    }

    private Cards.Suit randomizeTrumpSuit() {
        return Cards.values()[Randomizer.getRandom(Cards.values().length)].getSuit();
    }

    private void prepareGame(List<Player> players) {
        dealer.dealCards(players,CARDS_PER_PLAYER, DECK_TYPE);
    }

    private boolean checkGameEnd(List<Player> players, int totalCardsPlayed, int score) {
        if(totalCardsPlayed == NUMBER_OF_PLAYERS * CARDS_PER_PLAYER){
            System.out.println("Entered end game condition");
            dealer.broadcastMessage(players,"\n\n GAME HAS ENDED \n");
            if (score < TOTAL_POINTS/2){
                teamTwoVictories++;
                dealer.broadcastMessage(players, "Team Two has won this game With " + (TOTAL_POINTS - score) + " points");
                if (score < 30){
                    teamTwoVictories ++;
                    dealer.broadcastMessage(players, "Team two has scored more than 90 points, double victory for team Two");
                }
            }else if (score > TOTAL_POINTS/2) {
                teamOneVictories++;
                dealer.broadcastMessage(players, "Team One has won this game With " + score + " points");
                if (score > 90){
                    teamTwoVictories ++;
                    dealer.broadcastMessage(players, "Tem One has scored more than 90 points, double victory for team One");
                }
            }else  {
                dealer.broadcastMessage(players, "Game tie");
            }
            dealer.broadcastMessage(players, "\n GLOBAL SCORE:");
            dealer.broadcastMessage(players, "TEAM ONE: " + teamOneVictories + " TEAM TWO: " + teamTwoVictories +"\n");
            startingPlayer ++;
             //set, show and send game score ++ update team score ++ call for a new game(new method) ++ GAME SETS?? create playsets method??
            playGame(players);
            return false;
        }
        return true;
    }

    private Cards checkHigherCard(Cards playedCard, Cards higherCard, Cards.Suit trumpSuit) {
        System.out.println("Entered checkHigherCard");
       if (!playedCard.getSuit().equals(higherCard.getSuit())){
           if (!playedCard.getSuit().equals(trumpSuit)){
               System.out.println("Exited checkHigherCard w/ same card because different suit");
               return higherCard;
           }
           System.out.println("Exited checkHigherCard w/ trump card");
           return playedCard;
        }

        if (playedCard.getRank().getSuecaRank() > higherCard.getRank().getSuecaRank()){
            System.out.println("Exited checkHigherCard w/ higher card");
           return playedCard;
        }

        System.out.println("Exited checkHigherCard w/ same card equal suit");
        return higherCard;

    }

    @Override
    public Cards getMove(Player currentPlayer, List<Player> players) throws IOException {

        for (Player p : players) {
            if (p.equals(currentPlayer)) {
                p.send("It is your turn, choose a card to play [0 - " + (p.getHand().size() - 1) + "]");
            } else {
                p.send(currentPlayer.getName() + " is playing...");
            }
        }

        while (true){
            System.out.println("enterd getmove loop");
            String moveString  = null;
            try {
                moveString = currentPlayer.readFromClient();

            }catch (SocketException e){
                System.err.println("Player has left");
            }

            System.out.println("moveString = " + moveString);
            System.out.println("before null condition");

            if (moveString ==null){
                System.out.println("inside null condition");
                //players.remove(currentPlayer);
                System.out.println("LIST SIZE " + players.size());
                for (Player p :players) {
                    System.out.println("player " +currentPlayer + " has left");
                    p.send("PLAYERQUIT@@" + currentPlayer.getName() + " has left the game");//"PLAYERQUIT" is a reference so client can read and print
                    playerLeft = true;
                }
              return null;
            }
            System.out.println("player said " + moveString);
            try {
                int cardIndex = Integer.parseInt(moveString);
                if (cardIndex < 0 || cardIndex >= currentPlayer.getHand().size()){
                    System.out.println("card index higher/lower than expected");
                    currentPlayer.send("please give us a card you have");
                    continue;
                }

                Cards card = currentPlayer.getHand().get(cardIndex);

                System.out.println("Player" + " played " + card);

                System.out.println("exited get move");
                return card;


            }catch (NumberFormatException e){
                currentPlayer.send("invalid choice, please select a card to play");
                continue;
            }

            //Cards cards  = currentPlayer.getHand().get()

        }


    }



    /**
     * @see Game#checkMove(Player, Cards, Cards.Suit)
     */
    @Override
    public boolean checkMove(Player player, Cards card, Cards.Suit currentSuit) {
        System.out.println("renuncia: " + (playerHandHasSuit(player, currentSuit) && !card.getSuit().equals(currentSuit)));

        return  (playerHandHasSuit(player, currentSuit) && !card.getSuit().equals(currentSuit));  //check Renuncia

    }

    private boolean playerHandHasSuit(Player player, Cards.Suit currentSuit) {
        System.out.println("entered player has suit");
        for (Cards c : player.getHand()) {
            if (c.getSuit().equals(currentSuit)){
                return true;
            }
        }
        return false;
    }


    /**
     * @see Game#getPoints(List, Player, List)
     */
    @Override
    public int getPoints(List<Cards> cardsPlayed, Player winningPlayer, List<Player> players) {

        int points = 0;
        for (Cards c : cardsPlayed){
            points += c.getRank().getSuecaPoints();
        }
        return points;

    }
    @Override
    public void setPlayerLeft(boolean playerLeft) {
        this.playerLeft = playerLeft;
    }

    /**
     * @see Game#getScore()
     */
    @Override
    public int getScore() {
        throw new UnsupportedOperationException();
    }

    @Override
    public int getTotalPlayers() {
        return NUMBER_OF_PLAYERS;
    }

    @Override
    public void setDealer(CardDealer dealer) {
        this.dealer = dealer;
    }

    @Override
    public boolean isPlayerLeft() {
        return playerLeft;
    }

    private boolean checkIfPlayerLeft(List<Player> playersInLobby) {
        boolean playerLeft = false;
        List<Player> checkList = new ArrayList<>();
        checkList.addAll(playersInLobby);
        for(Player p : checkList) {
            p.send("CHECKCONNECT");
            try {
                String read = p.readFromClient();
            } catch (IOException e) {

                try {
                    String pName = p.getName();
                    p.getClientSocket().close();
                    playersInLobby.remove(p);
                    for (Player players : playersInLobby) {
                        players.send(pName + " has left.");
                    }
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
        if (playersInLobby.size() < 4) {
            playerLeft = true;
        }
        return playerLeft;
    }
}



