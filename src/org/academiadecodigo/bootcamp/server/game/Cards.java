package org.academiadecodigo.bootcamp.server.game;

public enum Cards {
    // @formatter:off
    ACE_OF_CLUBS      ("Ace of Clubs",      Rank.ACE,   Suit.CLUBS,    "A\u2663",  "\uD83C\uDCD1"),
    ACE_OF_DIAMONDS   ("Ace of Diamonds",   Rank.ACE,   Suit.DIAMONDS, "A\u2666",  "\uD83C\uDCC1"),
    ACE_OF_HEARTS     ("Ace of Hearts",     Rank.ACE,   Suit.HEARTS,   "A\u2665",  "\uD83C\uDCB1"),
    ACE_OF_SPADES     ("Ace of Spades",     Rank.ACE,   Suit.SPADES,   "A\u2660",  "\uD83C\uDCA1"),
    KING_OF_DIAMONDS  ("King of Diamonds",  Rank.KING,  Suit.DIAMONDS, "K\u2666",  "\uD83C\uDCCE"),
    KING_OF_HEARTS    ("King of Hearts",    Rank.KING,  Suit.HEARTS,   "K\u2665",  "\uD83C\uDCBE"),
    KING_OF_SPADES    ("King of Spades",    Rank.KING,  Suit.SPADES,   "K\u2660",  "\uD83C\uDCAE"),
    JACK_OF_CLUBS     ("Jack of Clubs",     Rank.JACK,  Suit.CLUBS,    "J\u2663",  "\uD83C\uDCDB"),
    JACK_OF_DIAMONDS  ("Jack of Diamonds",  Rank.JACK,  Suit.DIAMONDS, "J\u2666",  "\uD83C\uDCCB"),
    JACK_OF_HEARTS    ("Jack of Hearts",    Rank.JACK,  Suit.HEARTS,   "J\u2665",  "\uD83C\uDCBB"),
    JACK_OF_SPADES    ("Jack of Spades",    Rank.JACK,  Suit.SPADES,   "J\u2660",  "\uD83C\uDCAB"),
    QUEEN_OF_CLUBS    ("Queen of Clubs",    Rank.QUEEN, Suit.CLUBS,    "Q\u2663",  "\uD83C\uDCDD"),
    QUEEN_OF_DIAMONDS ("Queen of Diamonds", Rank.QUEEN, Suit.DIAMONDS, "Q\u2666",  "\uD83C\uDCCD"),
    QUEEN_OF_HEARTS   ("Queen of Hearts",   Rank.QUEEN, Suit.HEARTS,   "Q\u2665",  "\uD83C\uDCBD"),
    QUEEN_OF_SPADES   ("Queen of Spades",   Rank.QUEEN, Suit.SPADES,   "Q\u2660",  "\uD83C\uDCAD"),
    TEN_OF_CLUBS      ("Ten of Clubs",      Rank.TEN,   Suit.CLUBS,    "10\u2663", "\uD83C\uDCDA"),
    TEN_OF_DIAMONDS   ("Ten of Diamonds",   Rank.TEN,   Suit.DIAMONDS, "10\u2666", "\uD83C\uDCCA"),
    TEN_OF_HEARTS     ("Ten of Hearts",     Rank.TEN,   Suit.HEARTS,   "10\u2665", "\uD83C\uDCBA"),
    TEN_OF_SPADES     ("Ten of Spades",     Rank.TEN,   Suit.SPADES,   "10\u2660", "\uD83C\uDCAA"),
    EIGHT_OF_CLUBS    ("Eight of Clubs",    Rank.EIGHT, Suit.CLUBS,    "8\u2663",  "\uD83C\uDCD8"),
    EIGHT_OF_DIAMONDS ("Eight of Diamonds", Rank.EIGHT, Suit.DIAMONDS, "8\u2666",  "\uD83C\uDCC8"),
    EIGHT_OF_HEARTS   ("Eight of Hearts",   Rank.EIGHT, Suit.HEARTS,   "8\u2665",  "\uD83C\uDCB8"),
    EIGHT_OF_SPADES   ("Eight of Spades",   Rank.EIGHT, Suit.SPADES,   "8\u2660",  "\uD83C\uDCA8"),
    KING_OF_CLUBS     ("King of Clubs",     Rank.KING,  Suit.CLUBS,    "K\u2663",  "\uD83C\uDCDE"),
    NINE_OF_CLUBS     ("Nine of Clubs",     Rank.NINE,  Suit.CLUBS,    "9\u2663",  "\uD83C\uDCD9"),
    NINE_OF_DIAMONDS  ("Nine of Diamonds",  Rank.NINE,  Suit.DIAMONDS, "9\u2666",  "\uD83C\uDCC9"),
    NINE_OF_HEARTS    ("Nine of Hearts",    Rank.NINE,  Suit.HEARTS,   "9\u2665",  "\uD83C\uDCB9"),
    NINE_OF_SPADES    ("Nine of Spades",    Rank.NINE,  Suit.SPADES,   "9\u2660",  "\uD83C\uDCA9"),
    SEVEN_OF_CLUBS    ("Seven of Clubs",    Rank.SEVEN, Suit.CLUBS,    "7\u2663",  "\uD83C\uDCD7"),
    SEVEN_OF_DIAMONDS ("Seven of Diamonds", Rank.SEVEN, Suit.DIAMONDS, "7\u2666",  "\uD83C\uDCC7"),
    SEVEN_OF_HEARTS   ("Seven of Hearts",   Rank.SEVEN, Suit.HEARTS,   "7\u2665",  "\uD83C\uDCB7"),
    SEVEN_OF_SPADES   ("Seven of Spades",   Rank.SEVEN, Suit.SPADES,   "7\u2660",  "\uD83C\uDCA7"),
    SIX_OF_CLUBS      ("Six of Clubs",      Rank.SIX,   Suit.CLUBS,    "6\u2663",  "\uD83C\uDCD6"),
    SIX_OF_DIAMONDS   ("Six of Diamonds",   Rank.SIX,   Suit.DIAMONDS, "6\u2666",  "\uD83C\uDCC6"),
    SIX_OF_HEARTS     ("Six of Hearts",     Rank.SIX,   Suit.HEARTS,   "6\u2665",  "\uD83C\uDCB6"),
    SIX_OF_SPADES     ("Six of Spades",     Rank.SIX,   Suit.SPADES,   "6\u2660",  "\uD83C\uDCA6"),
    FIVE_OF_CLUBS     ("Five of Clubs",     Rank.FIVE,  Suit.CLUBS,    "5\u2663",  "\uD83C\uDCD5"),
    FIVE_OF_DIAMONDS  ("Five of Diamonds",  Rank.FIVE,  Suit.DIAMONDS, "5\u2666",  "\uD83C\uDCC5"),
    FIVE_OF_HEARTS    ("Five of Hearts",    Rank.FIVE,  Suit.HEARTS,   "5\u2665",  "\uD83C\uDCB5"),
    FIVE_OF_SPADES    ("Five of Spades",    Rank.FIVE,  Suit.SPADES,   "5\u2660",  "\uD83C\uDCA5"),
    FOUR_OF_CLUBS     ("Four of Clubs",     Rank.FOUR,  Suit.CLUBS,    "4\u2663",  "\uD83C\uDCD4"),
    FOUR_OF_DIAMONDS  ("Four of Diamonds",  Rank.FOUR,  Suit.DIAMONDS, "4\u2666",  "\uD83C\uDCC4"),
    FOUR_OF_HEARTS    ("Four of Hearts",    Rank.FOUR,  Suit.HEARTS,   "4\u2665",  "\uD83C\uDCB4"),
    FOUR_OF_SPADES    ("Four of Spades",    Rank.FOUR,  Suit.SPADES,   "4\u2660",  "\uD83C\uDCA4"),
    THREE_OF_CLUBS    ("Three of Clubs",    Rank.THREE, Suit.CLUBS,    "3\u2663",  "\uD83C\uDCD3"),
    THREE_OF_DIAMONDS ("Three of Diamonds", Rank.THREE, Suit.DIAMONDS, "3\u2666",  "\uD83C\uDCC3"),
    THREE_OF_HEARTS   ("Three of Hearts",   Rank.THREE, Suit.HEARTS,   "3\u2665",  "\uD83C\uDCB3"),
    THREE_OF_SPADES   ("Three of Spades",   Rank.THREE, Suit.SPADES,   "3\u2660",  "\uD83C\uDCA3"),
    TWO_OF_CLUBS      ("Two of Clubs",      Rank.TWO,   Suit.CLUBS,    "2\u2663",  "\uD83C\uDCD2"),
    TWO_OF_DIAMONDS   ("Two of Diamonds",   Rank.TWO,   Suit.DIAMONDS, "2\u2666",  "\uD83C\uDCC2"),
    TWO_OF_HEARTS     ("Two of Hearts",     Rank.TWO,   Suit.HEARTS,   "2\u2665",  "\uD83C\uDCB2"),
    TWO_OF_SPADES     ("Two of Spades",     Rank.TWO,   Suit.SPADES,   "2\u2660",  "\uD83C\uDCA2");
    // @formatter:on

    private final String name;
    private final Rank rank;
    private final Suit suit;
    private final String unicode;
    private final String unicode2;

    private Cards(String name, Rank rank, Suit suit, String unicode, String unicode2) {
        this.name = name;
        this.rank = rank;
        this.suit = suit;
        this.unicode = unicode;
        this.unicode2 = unicode2;
    }

    public String getName() {
        return name;
    }

    public Rank getRank() {
        return rank;
    }

    public Suit getSuit() {
        return suit;
    }

    public String getUnicode() {
        return unicode;
    }

    public String getUnicode2() {
        return unicode2;
    }

    public enum Rank {
        ACE, TWO, THREE, FOUR, FIVE, SIX, SEVEN, EIGHT, NINE, TEN, KING, QUEEN, JACK;
    }

    public enum Suit {
        CLUBS, DIAMONDS, HEARTS, SPADES;
    }
}
