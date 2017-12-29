package org.academiadecodigo.bootcamp.server.game;

import org.academiadecodigo.bootcamp.Randomizer;
import org.academiadecodigo.bootcamp.server.player.Player;

import java.util.ArrayList;
import java.util.List;

public class CardDealer {

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
     *
     * @param deck
     * @return
     */
    public List<Cards> drawCards(List<Cards> deck, int cardsPerPlayer) {
        if (deck.size()<= cardsPerPlayer){
            return deck;
        }
        List<Cards> hand = new ArrayList<>();
        for (int i = 0 ; i< cardsPerPlayer ; i++){
            hand.add(deck.remove(i));
        }
        return hand;
    }


    private List<Cards> shuffleDeck(List<Cards> deck){
        ArrayList<Cards> tempDeck = new ArrayList<>();
        int numberOfShuffles = Randomizer.getRandom(1,10);

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

    public void sendAll(List<Player> players, String cardsInPlay) {
        for (Player p: players) {
            p.send(Cards.encode(p.getHand()) + ",," + cardsInPlay);
        }
    }


    public enum DeckType {
        REGIONAL,
        TRADITIONAL;
    }
}
