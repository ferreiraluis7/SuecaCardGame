package org.academiadecodigo.bootcamp.server.game;

public class Test {
    public static void main(String[] args) {
        for (Cards cards : Cards.values()) {
            System.out.println(cards.getUnicode());
            System.out.println(cards.getUnicode2());
        }

        System.out.println("the back of the card : \uD83C\uDCA0 \uD83C\uDCA0 " );



        System.out.println("\uD83C\uDCA0");


    }
}
