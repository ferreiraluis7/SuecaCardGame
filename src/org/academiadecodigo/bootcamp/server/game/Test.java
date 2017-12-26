package org.academiadecodigo.bootcamp.server.game;

import org.academiadecodigo.bootcamp.server.player.Player;

import java.util.ArrayList;
import java.util.List;

public class Test {
    public static void main(String[] args) {

        List<Cards> cards   = new ArrayList<>();

        cards.add(Cards.ACE_OF_DIAMONDS);
        cards.add(Cards.ACE_OF_SPADES);
        cards.add(Cards.EIGHT_OF_CLUBS);

        System.out.println(Cards.encode(cards));

        List<Cards> cards1;

        cards1 = Cards.decode(Cards.encode(cards));
        System.out.println(cards1.get(0).getCompleteName() + " ");








    }
}
