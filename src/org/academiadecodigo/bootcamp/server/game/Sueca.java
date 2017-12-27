package org.academiadecodigo.bootcamp.server.game;

import org.academiadecodigo.bootcamp.Randomizer;
import org.academiadecodigo.bootcamp.server.player.Player;

import java.io.IOException;
import java.util.List;

public class Sueca implements Game {

    public static final int TOTAL_POINTS = 120;
    public static final int NUMBER_OF_PLAYERS = 4;
    public static final int CARDS_PER_PLAYER = 10;
    public static final CardDealer.DeckType DECK_TYPE = CardDealer.DeckType.REGIONAL;
    private int trueVictories;
    private int falseVictories;
    private CardDealer dealer;

    private int cardsPlayed = 0;
    private boolean isGameStarted;

    /**
     * Starts the game loop
     *
     * @param players the game players
     */
    public void playGame(List<Player> players, int startingPlayer) {
        Cards.Rank trumpRank;
        int currentPlayer = startingPlayer;
        //the player that starts the first game of a lobby is always the first to

        // game init
        if (!isGameStarted) {
            //Set card hand for each player
            dealer.dealCards(players,CARDS_PER_PLAYER, DECK_TYPE);
            isGameStarted = true;
            //choose the trumpRank
            trumpRank = Cards.values()[Randomizer.getRandom(Cards.values().length)].getRank();

        }

        //game loop


        //don't forget o change the while condition
            while (true) {
                try {
                    if(currentPlayer >= players.size()){
                        currentPlayer = 0;
                    }
                    // don't forget to store the cards played in each hand somewhere
                    getMove(players.get(currentPlayer));




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
     * @see Game#checkMove(Cards)
     */
    @Override
    public boolean checkMove(Cards card) {
        throw new UnsupportedOperationException();
    }

    /**
     * @see Game#checkPlay()
     */
    @Override
    public int checkPlay() {
        throw new UnsupportedOperationException();
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
        throw new UnsupportedOperationException();
    }
}
