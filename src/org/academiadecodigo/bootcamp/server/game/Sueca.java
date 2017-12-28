package org.academiadecodigo.bootcamp.server.game;

import org.academiadecodigo.bootcamp.Randomizer;
import org.academiadecodigo.bootcamp.server.player.Player;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Sueca implements Game {

    public static final int TOTAL_POINTS = 120;
    public static final int NUMBER_OF_PLAYERS = 4;
    public static final int CARDS_PER_PLAYER = 10;
    public static final CardDealer.DeckType DECK_TYPE = CardDealer.DeckType.REGIONAL;
    private int trueVictories;
    private int falseVictories;
    private int score = 0;
    private CardDealer dealer;

    private boolean isGameStarted;

    /**
     * Starts the game loop
     *
     * @param players the game players
     */
    public void playGame(List<Player> players, int startingPlayer) {
        Cards.Suit trumpSuit;
        int currentPlayer = startingPlayer;
        List<Cards> cardsInPlay = new ArrayList<>();
        Cards playedCard;
        Cards.Suit currentSuit = null;
        int totalCardsPlayed = 0;
        //the player that starts the first game of a lobby is always the first to

        // game init
        if (!isGameStarted) {
            //Set card hand for each player
            dealer.dealCards(players,CARDS_PER_PLAYER, DECK_TYPE);
            isGameStarted = true;
            //choose the trumpSuit
            trumpSuit = Cards.values()[Randomizer.getRandom(Cards.values().length)].getSuit();

        }

        //game loop


        //don't forget o change the while condition
            while (isGameStarted) {

                try {

                    if(totalCardsPlayed == NUMBER_OF_PLAYERS * CARDS_PER_PLAYER){
                        isGameStarted = false; //set, show and send game score ++ update team score ++ call for a new game(new method) ++ GAME SETS?? create playsets method??
                        continue;
                    }

                    if(currentPlayer >= players.size()){
                        currentPlayer = 0;
                    }

                    playedCard = getMove(players.get(currentPlayer));

                    if(cardsInPlay.isEmpty()){
                        cardsInPlay.add(playedCard);
                        players.get(currentPlayer).removeCard(playedCard); // convert to method
                        totalCardsPlayed++;//need to send info to client remove card
                        currentSuit = playedCard.getSuit();
                        currentPlayer++;
                        continue;
                    }


                    if(!checkMove(players.get(currentPlayer), playedCard, currentSuit)){
                        players.get(currentPlayer).send("You are not allowed to play that card, please play another");  //INNER CLASS W/ MESSAGES BUILDER METHODS
                        //Need to send info to client so he knows is an invalid move
                        continue;
                    }

                    cardsInPlay.add(playedCard);
                    players.get(currentPlayer).removeCard(playedCard);
                    totalCardsPlayed++;                                 // convert to method
                                                                        //need to send info to client remove card
                    if(cardsInPlay.size() == NUMBER_OF_PLAYERS){
                        currentPlayer = checkPlay(cardsInPlay);
                        cardsInPlay.clear();
                        continue;
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
                currentPlayer++;
            }


    }

    @Override
    public Cards getMove(Player currentPlayer) throws IOException {

        currentPlayer.send("It is your turn, choose a card to play");

        while (true){

            String moveString = currentPlayer.readFromClient();
            System.out.println("player said " + moveString);
            try {
                int cardIndex = Integer.parseInt(moveString);
                System.out.println("pasedInt " + cardIndex);
                if (cardIndex < 0 || cardIndex >= currentPlayer.getHand().size()){
                    System.out.println("player tried to cheat");
                    currentPlayer.send("please give us a card you have");
                    continue;
                }

                Cards card = currentPlayer.getHand().get(cardIndex);

                System.out.println(currentPlayer + " played " + card);

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

        if (playerHandHasSuit(player, currentSuit) && card.getSuit() != currentSuit){  //check Renuncia
            return false;
        }

        return true;
    }

    private boolean playerHandHasSuit(Player player, Cards.Suit currentSuit) {

        for (Cards c : player.getHand()) {
            if (c.getSuit() == currentSuit){
                return true;
            }
        }
        return false;
    }

    /**
     * @see Game#checkPlay(List, Cards.Suit)
     */
    @Override
    public int checkPlay(List<Cards> cardsPlayed, Cards.Suit trumpSuit) {
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

    /**
     * Changes the game score
     *
     * @param score the score value to be changed
     */
    private void setScore(int score) {
        this.score += score;
    }
}
