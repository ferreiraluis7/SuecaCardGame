package org.academiadecodigo.bootcamp.server.game;

import org.academiadecodigo.bootcamp.Randomizer;
import org.academiadecodigo.bootcamp.server.player.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;

public class CardDealer {
    /**
     * Assigns a hand of cards to each player
     *
     * @param players player in game
     *
     * @param cardsPerPlayer the number of cards each player receive
     *
     * @param type type of deck to play
     */
    public void dealCards(List<Player> players, int cardsPerPlayer, DeckType type){

        List<Cards> deck = getDeck(type);
        deck = shuffleDeck(deck);
        for (Player p: players) {
            List<Cards> playerHand = drawCards(deck, cardsPerPlayer);
            p.setHand(playerHand);
            p.send(Cards.encode(playerHand));
        }

    }

    /**
     * Draws cards from a specific deck
     *
     * @param deck deck to be used
     *
     * @return a hand of cards
     */
    public List<Cards> drawCards(List<Cards> deck, int cardsPerPlayer) {
        if (deck.size()<= cardsPerPlayer){
            deck = sortCards(deck);
            return deck;
        }
        List<Cards> hand = new ArrayList<>();
        for (int i = 0 ; i< cardsPerPlayer ; i++){
            hand.add(deck.remove(i));
        }
        hand = sortCards(hand);

        return hand;
    }

    /**
     * Sorts cards by rank
     *
     * @param hand cards to be sorted
     *
     * @return sorted cards
     */
    private List<Cards> sortCardsByRank(List<Cards> hand){
        Queue<Cards> q = new PriorityQueue<>();

        while(!hand.isEmpty()){
            q.offer(hand.remove(0));
        }

        while (!q.isEmpty()){
            hand.add(q.remove());
        }

        return hand;
    }

    private List<Cards> sortCards(List<Cards> hand) {
        List<Cards> sortedHand = new ArrayList<>();
        List<Cards> clubsHand = new ArrayList<>();
        List<Cards> spadesHand = new ArrayList<>();
        List<Cards> heartsHand = new ArrayList<>();
        List<Cards> diamondsHand = new ArrayList<>();


        while (hand.size() > 0){
            if(hand.get(0).getSuit().equals(Cards.Suit.CLUBS)){
                clubsHand.add(hand.remove(0));
                continue;
            }
            if(hand.get(0).getSuit().equals(Cards.Suit.HEARTS)){
                heartsHand.add(hand.remove(0));
                continue;
            }
            if(hand.get(0).getSuit().equals(Cards.Suit.SPADES)){
                spadesHand.add(hand.remove(0));
                continue;
            }
            if(hand.get(0).getSuit().equals(Cards.Suit.DIAMONDS)){
                diamondsHand.add(hand.remove(0));
            }
        }

        sortedHand.addAll(sortCardsByRank(clubsHand));
        sortedHand.addAll(sortCardsByRank(spadesHand));
        sortedHand.addAll(sortCardsByRank(heartsHand));
        sortedHand.addAll(sortCardsByRank(diamondsHand));
        return sortedHand;
    }

    /**
     * Shuffles the deck
     *
     * @param deck deck to be shuffled
     *
     * @return shuffled deck
     */
    private List<Cards> shuffleDeck(List<Cards> deck){
        ArrayList<Cards> tempDeck = new ArrayList<>();
        int numberOfShuffles = Randomizer.getRandom(10,20);

        List<Cards> shuffledDeck = deck;
        for (int i = 0; i < numberOfShuffles; i++) {
            while (shuffledDeck.size()> 0){
                int  index = Randomizer.getRandom(shuffledDeck.size() - 1);
                tempDeck.add(shuffledDeck.get(index));
                shuffledDeck.remove(index);
            }
            shuffledDeck.addAll(tempDeck);
            tempDeck.clear();
        }
        return shuffledDeck;
    }

    /**
     * Gets a deck to play
     *
     * @param type type of deck do play
     *
     * @return choosen deck
     */
    private ArrayList<Cards> getDeck(DeckType type) {
        ArrayList<Cards> deck = new ArrayList<>();

        switch (type){
            case REGIONAL:
                for (Cards card : Cards.values()){
                    if (card.getRank().equals(Cards.Rank.SEVEN) ||
                            card.getRank().equals(Cards.Rank.EIGHT) ||
                            card.getRank().equals(Cards.Rank.NINE) ){

                        continue;
                    }
                    deck.add(card);
                }
                break;
            case TRADITIONAL:
                for (Cards card : Cards.values()){
                    deck.add(card);
                }
                break;
        }

        return deck;
    }

    /**
     * Sends to all player the information of current hand and cards in table
     *
     * @param players player in game
     *
     * @param cardsInPlay cards in table
     */
    public void sendAll(List<Player> players, String cardsInPlay) {
        for (Player p: players) {
            p.send(Cards.encode(p.getHand()) + ",," + cardsInPlay);
        }
    }

    /**
     * Broadcasts a message to all players
     *
     * @param players players to broadcast message
     *
     * @param message message to be broadcast
     */
    public void broadcastMessage(List<Player> players, String message){
        for (Player p: players) {
            p.send(message);
        }

    }

    public enum DeckType {
        REGIONAL,
        TRADITIONAL
    }
}
