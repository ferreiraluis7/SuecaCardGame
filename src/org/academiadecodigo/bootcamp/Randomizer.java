package org.academiadecodigo.bootcamp;

public class Randomizer {

    public static int getRandom(int min, int max) {
        return (int) Math.round(Math.floor(Math.random() * max) + min);
    }

    public static int getRandom(int max) {
        return (int) Math.round(Math.random() * max);
    }
}
