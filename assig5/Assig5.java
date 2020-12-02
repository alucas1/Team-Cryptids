import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;


public class Assig5
{
    static int NUM_CARDS_PER_HAND = 7;
    static int  NUM_PLAYERS = 2;
    static JLabel[] computerLabels = new JLabel[NUM_CARDS_PER_HAND];
    static JButton[] humanButtons = new JButton[NUM_CARDS_PER_HAND];
    static JLabel[] playedCardLabels  = new JLabel[NUM_PLAYERS];
    static JLabel[] playLabelText  = new JLabel[NUM_PLAYERS];
    static int score[] = new int[NUM_PLAYERS];

    public static void main(String[] args)
    {
        int numPacksPerDeck = 1;
        int numJokersPerPack = 2;
        int numUnusedCardsPerPack = 0;
        Card[] unusedCardsPerPack = null;

        CardGameFramework LowCardGame = new CardGameFramework(
                numPacksPerDeck, numJokersPerPack,
                numUnusedCardsPerPack, unusedCardsPerPack,
                NUM_PLAYERS, NUM_CARDS_PER_HAND);

        // establish main frame in which program will run
        CardTable myCardTable
                = new CardTable("CardTable", NUM_CARDS_PER_HAND, NUM_PLAYERS);
        myCardTable.setSize(900, 600);
        myCardTable.setLocationRelativeTo(null);
        myCardTable.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        LowCardGame.deal();

        // CREATE LABELS ----------------------------------------------------
        for (int k = 0; k < NUM_CARDS_PER_HAND; k++)
        {
            // Human card
            Card newCard = LowCardGame.getHand(1).inspectCard(k);
            humanButtons[k] = new JButton(GUICard.getIcon(newCard));
            humanButtons[k].setActionCommand("" + k);
            humanButtons[k].addActionListener(new gameLogicInitiated(myCardTable, LowCardGame, score));
            computerLabels[k] = new JLabel(GUICard.getBackCardIcon());
        }

        // Create labels in the play area.
        for (int k = 0; k < NUM_PLAYERS; k++)
        {
            if (k == 0)
                playLabelText[k] = new JLabel("Computer", JLabel.CENTER);
            else
                playLabelText[k] = new JLabel("Player " + k, JLabel.CENTER);
        }

        // ADD LABELS TO PANELS -----------------------------------------
        for (int k = 0; k < NUM_CARDS_PER_HAND; k++)
        {
            // Add human and computer card labels to panels
            myCardTable.pnlHumanHand.add(humanButtons[k]);
            myCardTable.pnlComputerHand.add(computerLabels[k]);
        }

        // Add human / computer text labels to played area.
        for (int k = 0; k < NUM_PLAYERS; k++)
            myCardTable.pnlPlayArea.add(playLabelText[k]);

        playedCardLabels[0] = new JLabel("", JLabel.CENTER);
        playedCardLabels[1] = new JLabel("", JLabel.CENTER);

        myCardTable.pnlPlayArea.add(playedCardLabels[0]);
        myCardTable.pnlPlayArea.add(playedCardLabels[1]);

        // show everything to the user
        myCardTable.setVisible(true);
    }

    private static class gameLogicInitiated implements ActionListener
    {
        CardTable myCardTable;
        CardGameFramework LowCardGame;
        int score[];

        public gameLogicInitiated(CardTable myCardTable,
                                  CardGameFramework LowCardGame, int score[])
        {
            this.myCardTable = myCardTable;
            this.LowCardGame = LowCardGame;
            this.score = score;
        }

        @Override
        public void actionPerformed(ActionEvent e)
        {
            int cardIndex = Integer.valueOf(e.getActionCommand());
            //for debugging
            System.out.println(e);

            //Play card from hand and remove it from hand
            Card playerCard = LowCardGame.getHand(1).playCard(cardIndex);
            Card computerCard = LowCardGame.getHand(0).playCard(0);

            //remove card button and shifts buttons down
            myCardTable.pnlHumanHand.remove(cardIndex);
            myCardTable.pnlComputerHand.remove(0);
            myCardTable.pnlHumanHand.repaint();
            myCardTable.pnlComputerHand.repaint();
            for(int i = cardIndex; i < LowCardGame.getHand(1).getNumberOfCards(); i++)
            {
                humanButtons[i] = humanButtons[i+1];
                humanButtons[i].setActionCommand(i + "");
            }


            //Put card in play area & computer logic
            //Computer
            playedCardLabels[0].setIcon(GUICard.getIcon(computerCard));
            //Human
            playedCardLabels[1].setIcon((GUICard.getIcon(playerCard)));

            //check cards
            gameLogic(playerCard, computerCard);

            //winner gets points

            //if cards == 0, end game, declare winner
            if( LowCardGame.getHand(1).getNumberOfCards() == 0 ){
                if(score[0] < score[1]) {
                    playLabelText[1].setText("!!!WINNER!!! Player: " + score[1] + " points");
                    playLabelText[0].setText("!!!LOSER!!! Computer: " + score[0] + " points");
                }
                else if (score[0] > score[1])
                {
                    playLabelText[0].setText("!!!WINNER!!! Computer: " + score[0] + " points");
                    playLabelText[1].setText("!!!LOSER!!! Player: " + score[1] + " points");
                }
                else
                {
                    playLabelText[0].setText("!!!DRAW!!! Computer: " + score[0] + " points");
                    playLabelText[1].setText("!!!DRAW!!! Player: " + score[1] + " points");
                }
                System.out.println("YOU WIN");
            }

            myCardTable.repaint();
        }

        public void gameLogic(Card playerCard, Card opponentCard)
        {
            int playerValue = 0;
            int computerValue = 0;

            for(int i = 0; i < Card.valuRanks.length; i++){
                if(playerCard.getValue() == Card.valuRanks[i])
                    playerValue = i;

                if(opponentCard.getValue() == Card.valuRanks[i])
                    computerValue = i;
            }

            if (playerValue < computerValue) {
                score[1] += 2;
                System.out.println("myscore = " + score[1]);
                playLabelText[1].setText("Player: " + score[1]);

            }
            else if ( computerValue < playerValue){
                score[0] += 2;
                System.out.println("computerscore = " + score[0]);
                playLabelText[0].setText("Computer: " + score[0]);
            }
        }
    }
}

class CardTable extends JFrame
{
    static int MAX_CARDS_PER_HAND = 56;
    static int MAX_PLAYERS = 2;

    private int numCardsPerHand;
    private int numPlayers;

    public JPanel pnlComputerHand, pnlHumanHand, pnlPlayArea;

    CardTable(String title, int numCardsPerHand, int numPlayers)
    {
        super(title);

        if ( numCardsPerHand >= 0 && numCardsPerHand <= MAX_CARDS_PER_HAND)
            this.numCardsPerHand = numCardsPerHand;
        else
            this.numCardsPerHand = 7;

        if (numPlayers > 0 && numPlayers <= MAX_PLAYERS)
            this.numPlayers = numPlayers;
        else
            this.numPlayers = 2;

        pnlComputerHand = new JPanel();
        pnlPlayArea = new JPanel(new GridLayout(2,2));
        pnlHumanHand = new JPanel();

        pnlComputerHand.setBorder(new TitledBorder("Computer Hand"));
        pnlPlayArea.setBorder(new TitledBorder("Playing Area"));
        pnlHumanHand.setBorder(new TitledBorder("Your Hand"));

        setLayout(new BorderLayout(20, 20));
        add(pnlComputerHand, BorderLayout.NORTH);
        add(pnlPlayArea, BorderLayout.CENTER);
        add(pnlHumanHand, BorderLayout.SOUTH);
    }

    public int getNumCardsPerHand()
    {
        return numCardsPerHand;
    }

    public int getNumPlayers()
    {
        return numPlayers;
    }
}

class GUICard
{
    private static Icon[][] iconCards = new ImageIcon[14][4];
    private static Icon iconBack;
    static boolean iconsLoaded = false;

    static void loadCardIcons()
    {
        // Change IMGFOLDER to where the image folder is located
        final String IMGFOLDER = ".\\images\\";
        final String IMGSUFFIX = ".gif";

        // if Icons are already loaded then return
        if (iconsLoaded)
            return;

        for (int suitIndex = 0; suitIndex < 4; suitIndex++)
            for (int valueIndex = 0; valueIndex < 14; valueIndex++)
            {
                String fileName = IMGFOLDER + intToCardValue(valueIndex)
                        + intToCardSuit(suitIndex) + IMGSUFFIX;
                iconCards[valueIndex][suitIndex] = new ImageIcon(fileName);
            }

        // Finally, add card back.
        iconBack = new ImageIcon(IMGFOLDER + "BK" + IMGSUFFIX);

        // set icons loaded flag.
        iconsLoaded = true;
    }

    static public Icon getIcon(Card card)
    {
        if(card.getErrorFlag())
            return null;

        loadCardIcons();

        return iconCards[valueAsInt(card)][suitAsInt(card)];
    }

    static public Icon getBackCardIcon()
    {
        return iconBack;
    }

    static int valueAsInt(Card card)
    {
        if (card.getErrorFlag())
            return -1;

        char[] values = Card.valuRanks;
        for (int index = 0; index < values.length; index++)
            if (values[index] == card.getValue())
                return index;

        return -1;
    }

    static int suitAsInt(Card card)
    {
        if(card.getErrorFlag())
            return -1;

        Card.Suit[] suits = { Card.Suit.clubs, Card.Suit.diamonds,
                Card.Suit.hearts, Card.Suit.spades,};
        for(int index = 0; index < suits.length; index++)
            if (suits[index] == card.getSuit())
                return index;

        return -1;
    }
    private static char intToCardSuit(int suitIndex)
    {
        final char[] CARDSUITS = {'C', 'D', 'H', 'S'};
        if (suitIndex >= CARDSUITS.length || suitIndex < 0)
            return 'E';
        else
            return CARDSUITS[suitIndex];
    }

    private static char intToCardValue(int valueIndex)
    {
        char[] cardValues = Card.valuRanks;
        if (valueIndex >= cardValues.length || valueIndex < 0)
            return 'E';
        else
            return cardValues[valueIndex];
    }
}

/**
 The Card class represents a playing card with a suit and a value.
 If card has an invalid suit or value, card is placed in an error
 state with errorFlag.
 */
class Card
{
    // Declares the possible suits of a card.
    public enum Suit
    {
        clubs, diamonds, hearts, spades
    }

    // Instance Variables.
    private Suit suit;
    private char value;
    private boolean errorFlag;
    public static final char[] valuRanks = {'A', '2', '3', '4', '5', '6', '7', '8' ,
            '9', 'T', 'J', 'Q', 'K', 'X'};

    /**
     Constructor that sets suit and value instance variables.
     @param value The value of the card
     @param suit  The suit of the card
     */
    public Card(char value, Suit suit)
    {
        set(value, suit);
    }

    // Parameterless constructor - sets default values.
    public Card()
    {
        this('A', Suit.spades);
    }

    /**
     Constructor if the user instantiates with suit but not value.
     @param suit The suit of the card
     */
    public Card(Suit suit)
    {
        this('A', suit);
    }

    /**
     Constructor if the user instantiates with value but not suit.
     @param value The value of a card
     */
    public Card(char value)
    {
        this(value, Suit.spades);
    }

    /**
     Accessor method for suit.
     @return The suit of the card.
     */
    public Suit getSuit()
    {
        return suit;
    }

    /**
     Accessor method for value.
     @return The value of the card.
     */
    public char getValue()
    {
        return value;
    }

    /**
     Accessor method for errorFlag.
     @return The errorFlag of the card.
     */
    public boolean getErrorFlag()
    {
        return errorFlag;
    }

    /**
     General mutator for value and suit. Triggers errorFlag if suit or value
     create and invalid card. Value and suit are set regardless of validity.
     @param value The value of the card.
     @param suit  The suit of the card.
     @return      True if the card is valid, else returns false.
     */
    public boolean set(char value, Suit suit)
    {
        boolean isValidCard = isValid(value, suit);
        errorFlag = !isValidCard; // If card is valid (true), errorFlag is false.

        if(isValidCard){
            this.suit = suit;
            this.value = value;
        }

        return isValidCard;
    }

    /**
     Checks for card equality by comparing suit and value.
     @param card A card object that will be compared.
     @return     True if suit and value are the same, else false.
     */
    public boolean equals(Card card)
    {
        return (this.suit == card.suit && this.value == card.value && this.errorFlag == card.errorFlag);
    }

    /**
     Returns a string representation of a card.
     @return The value and suit of the card if error flag is not raised, else
     return a string stating that the card is invalid.
     */
    @Override
    public String toString()
    {
        if (errorFlag)
            return "Invalid card! :c";
        else
            return value + " of " + suit;
    }

    /**
     Helper method - checks if suit and value are valid card arguments.
     @param value The value of the card.
     @param suit  The suit of the card.
     @return      True if both suit and value are valid, else return false.
     */
    private boolean isValid(char value, Suit suit)
    {
        // Define the valid suits
        Card.Suit[] validSuits = Card.Suit.values();

        // First loop checks suit.
        for (Suit validSuit : validSuits)
        {
            if (suit == validSuit)
            {  // If a valid suit is found, check for a valid value.
                for (char validValue : valuRanks)
                {  // If a valid value is found, return true (valid suit and value)
                    if (value == validValue)
                        return true;
                }
            }
        }

        return false;
    }

    static void arraySort(Card[] card, int arraySize)
    {
        if (card == null)
            return;

        for(int i = 0; i < arraySize - 1; i++)
            for (int j = i + 1; j < arraySize; j++)
                if (GUICard.valueAsInt(card[i]) > GUICard.valueAsInt(card[j]))
                {
                    Card tempCard;
                    tempCard = card[i];
                    card[i] = card[j];
                    card[j] = tempCard;

                }
    }
}

/**
 The Hand class represents a hand of playing cards. Array myCards is able to
 hold MAX_CARDS. numCards describes amount of cards currently in hand.
 */
class Hand
{
    // Initialize class variables.
    public final int MAX_CARDS = 56;

    // Declare instance variables.
    private Card[] myCards;
    private int numCards;

    // Parameterless constructor - assigns default values.
    public Hand()
    {
        numCards = 0;
        myCards = new Card[MAX_CARDS];
    }

    /**
     Accessor method for numCards.
     @return Current number of cards in hand.
     */
    public int getNumberOfCards()
    {
        return numCards;
    }

    // Resets number of cards to 0 and removes all cards from hand.
    public void resetHand()
    {
        myCards = new Card[MAX_CARDS];
        numCards = 0;
    }

    /**
     Takes a card as an argument and adds it to the hand (myCards).
     @param card A card object that will be added to this hand
     @return     True if card was successfully added, else false.
     */
    public boolean takeCard(Card card)
    {
        if (numCards < MAX_CARDS) // Cards in hand cannot exceed MAX_CARDS.
        {
            // Creates card copy to prevent privacy leak.
            Card cardClone = new Card(card.getValue(), card.getSuit());
            myCards[numCards] = cardClone;
            numCards++;
            return true;
        }
        else
            return false;
    }

    /**
     Takes the top card element in the array myCards, removes it,
     and returns a copy of it.
     @return A copy of the top card if there are cards in the hand, else null.
     */
    public Card playCard(int cardIndex)
    {
        if (numCards == 0) //error
        {
            //Creates a card that does not work
            return new Card('M', Card.Suit.spades);
        }
        //Decreases numCards.
        Card card = myCards[cardIndex];

        numCards--;

        //Shifts hand card array down
        for(int i = cardIndex; i < numCards; i++)
        {
            myCards[i] = myCards[i+1];
        }

        myCards[numCards] = null;

        return card;
    }

    /**
     A string representation of the cards in the hand.
     @return A formatted string of the cards in myCards array.
     */
    public String toString()
    {
        String returnString = "Hand = " + "( ";
        // Prints the cards out in a nice and pretty card manner :3
        for (int i = 0; i < numCards; i++)
        {
            // Insert a newline every 5th card
            if (i % 5 == 4)
                returnString += "\n";

            // Insert a comma after every card, except for the last one
            if (i != numCards - 1)
                returnString += (myCards[i].getValue() + " of " +
                        myCards[i].getSuit() + ", ");
            else
                returnString += (myCards[i].getValue() + " of " +
                        myCards[i].getSuit());
        }

        return returnString + " )";
    }

    /**
     Checks to see if card at index k in array 'myCards' is valid.
     @param k The index location to inspect in the myCards array
     @return  A copy of the card at index k in the myCards array,
     or an invalid card with a true errorFlag for an invalid index.
     */
    public Card inspectCard(int k)
    {
        if (k < numCards)
            return new Card(myCards[k].getValue(), myCards[k].getSuit());
        else
            return new Card('D', Card.Suit.diamonds);
    }

    void sort()
    {
        Card.arraySort(myCards, numCards);
    }
}

/**
 The Deck class represents a deck of playing cards. The deck can hold a max
 of MAX_CARDS in array 'cards'. topCard is the number of cards in the deck.
 Array 'masterPack' is used as a blueprint of all the valid cards
 (suit and value) in a standard 52 card pack.
 */
class Deck
{
    // Class variables.
    public final int MAX_CARDS = 6 * 56;   // 6 packs of 56 cards.
    private static Card[] masterPack;

    // Instance variables.
    private Card[] cards;
    int topCard;

    /**
     Constructor that initializes all instance and class variables.
     @param numPacks The amount of 56-card packs to initialize the deck with.
     */
    public Deck(int numPacks)
    {
        allocateMasterPack(); // Initialize array 'masterPack'.
        init(numPacks);
    }

    //Parameterless constructor - initiates with 1 pack of cards.
    public Deck()
    {
        this(1);
    }

    /**
     Accessor function for TopCard.
     @return The amount of cards in the deck.
     */
    public int getTopCard()
    {
        return topCard;
    }

    /**
     Fills the 'cards' array with cards from the 'masterPack' array. If
     numPacks creates an invalid number of cards, then initialize the 'cards'
     array with the default numPacks = 1.
     @param numPacks The amount of 56-card packs to initialize the deck with.
     */
    public void init(int numPacks)
    {
        // Ensures that amount of cards does not exceed MAX_CARDS or is negative.
        if ((numPacks * 56) < 0 || (numPacks * 56) > MAX_CARDS )
            numPacks = 1;

        // Initializes cards array with numPacks amount of 52-card packs.
        topCard = numPacks * 56;
        cards = new Card[topCard];

        // Initializes cards in 'cards' array by copying valid cards .
        // from 'masterPack' array.
        for (int i = 0; i < topCard; i++)
            cards[i] = new Card(masterPack[(i % 56)].getValue(),
                    masterPack[(i % 56)].getSuit());
    }

    // Shuffles cards in array 'cards'.
    public void shuffle()
    {
        // Randomly swaps cards in the 'cards' array starting with the topCard.
        for (int i = topCard - 1; i > 0; i--)
        {
            // Generate a random int between 0 and topCard, exclusive.
            int randomIndex = (int) (Math.random() * topCard);

            // Swap card in index i with one in a randomly generated index.
            Card placeHolder = cards[i];
            cards[i] = cards[randomIndex];
            cards[randomIndex] = placeHolder;
        }
    }

    /**
     Deals the topCard in array 'cards' and removes it from the deck.
     @return A copy of the topmost card, or null if the deck has no cards.
     */
    public Card dealCard()
    {
        // Checks to see if the topCard is not 0. i.e is the deck empty.
        if (topCard > 0)
        {
            // Decrement topCard first to provide correct index.
            topCard--;

            // Creates a copy of the card and removes it from the deck
            Card cardClone = new Card(cards[topCard].getValue(),
                    cards[topCard].getSuit());
            cards[topCard] = null;

            return cardClone;
        }
        else
            return null;
    }

    /**
     Checks to see if card at index k in array 'cards' is valid.
     @param k The index location to inspect in the cards array
     @return  A copy of the card at index k in the cards array,
     or an invalid card with a true errorFlag for an invalid index.
     */
    public Card inspectCard(int k)
    {
        if (k < topCard)
            return new Card(cards[k].getValue(), cards[k].getSuit());
        else
            return new Card('D', Card.Suit.diamonds);
    }

    /**
     Helper method - Initializes masterPack array, a blueprint of a
     standard 56-card pack.
     */
    private static void allocateMasterPack()
    {
        // only runs once when masterPack has not been initialized
        if (masterPack == null)
        {
            // Initialize masterPack
            masterPack = new Card[56];

            // Initialize arrays for valid values and suites.
            char[] validValues = Card.valuRanks;
            Card.Suit[] validSuits = Card.Suit.values();

            // Outer loop goes through all valid suits
            int index = 0;
            for (Card.Suit validSuit : validSuits)
            {
                // Inner loop maps all valid values to suit from outer loop
                for (char validValue : validValues)
                {
                    // Creates a valid card and adds it to masterPack
                    masterPack[index] = new Card(validValue, validSuit);
                    index++;
                }
            }
        }
    }

    boolean addCard(Card card)
    {
        if(card == null)
            return false;

        if (topCard >= MAX_CARDS)
            return false;

        int numFound = 0;
        for (int i = 0; i < topCard; i++)
            if (cards[i].equals(card))
                numFound++;

        // numFound must not appear more than the amount of decks added
        if (numFound == cards.length / 56)
            return false;

        cards[topCard] = new Card(card.getValue(), card.getSuit());
        topCard++;

        return true;
    }

    boolean removeCard(Card card)
    {
        if (card == null)
            return false;

        for(int i = 0; i < topCard; i++)
            if(cards[i].equals(card))
            {
                if (cards[i] == card)
                    dealCard();
                else
                {
                    Card cardTop = dealCard();
                    cards[i] = cardTop;
                }

                return true;
            }

        return false;
    }

    void sort()
    {
        Card.arraySort(cards, topCard);
    }

    int getNumCards()
    {
        return topCard;
    }
}

//class CardGameFramework  ----------------------------------------------------
class CardGameFramework
{
    private static final int MAX_PLAYERS = 50;

    private int numPlayers;
    private int numPacks;            // # standard 52-card packs per deck
    // ignoring jokers or unused cards
    private int numJokersPerPack;    // if 2 per pack & 3 packs per deck, get 6
    private int numUnusedCardsPerPack;  // # cards removed from each pack
    private int numCardsPerHand;        // # cards to deal each player
    private Deck deck;               // holds the initial full deck and gets
    // smaller (usually) during play
    private Hand[] hand;             // one Hand for each player
    private Card[] unusedCardsPerPack;   // an array holding the cards not used
    // in the game.  e.g. pinochle does not
    // use cards 2-8 of any suit

    public CardGameFramework( int numPacks, int numJokersPerPack,
                              int numUnusedCardsPerPack,  Card[] unusedCardsPerPack,
                              int numPlayers, int numCardsPerHand)
    {
        int k;

        // filter bad values
        if (numPacks < 1 || numPacks > 6)
            numPacks = 1;
        if (numJokersPerPack < 0 || numJokersPerPack > 4)
            numJokersPerPack = 0;
        if (numUnusedCardsPerPack < 0 || numUnusedCardsPerPack > 50) //  > 1 card
            numUnusedCardsPerPack = 0;
        if (numPlayers < 1 || numPlayers > MAX_PLAYERS)
            numPlayers = 4;
        // one of many ways to assure at least one full deal to all players
        if  (numCardsPerHand < 1 ||
                numCardsPerHand >  numPacks * (52 - numUnusedCardsPerPack)
                        / numPlayers )
            numCardsPerHand = numPacks * (52 - numUnusedCardsPerPack) / numPlayers;

        // allocate
        this.unusedCardsPerPack = new Card[numUnusedCardsPerPack];
        this.hand = new Hand[numPlayers];
        for (k = 0; k < numPlayers; k++)
            this.hand[k] = new Hand();
        deck = new Deck(numPacks);

        // assign to members
        this.numPacks = numPacks;
        this.numJokersPerPack = numJokersPerPack;
        this.numUnusedCardsPerPack = numUnusedCardsPerPack;
        this.numPlayers = numPlayers;
        this.numCardsPerHand = numCardsPerHand;
        for (k = 0; k < numUnusedCardsPerPack; k++)
            this.unusedCardsPerPack[k] = unusedCardsPerPack[k];

        // prepare deck and shuffle
        newGame();
    }

    // constructor overload/default for game like bridge
    public CardGameFramework()
    {
        this(1, 0, 0, null, 4, 13);
    }

    public Hand getHand(int k)
    {
        // hands start from 0 like arrays

        // on error return automatic empty hand
        if (k < 0 || k >= numPlayers)
            return new Hand();

        return hand[k];
    }

    public Card getCardFromDeck() { return deck.dealCard(); }

    public int getNumCardsRemainingInDeck() { return deck.getNumCards(); }

    public void newGame()
    {
        int k, j;

        // clear the hands
        for (k = 0; k < numPlayers; k++)
            hand[k].resetHand();

        // restock the deck
        deck.init(numPacks);

        // remove unused cards
        for (k = 0; k < numUnusedCardsPerPack; k++)
            deck.removeCard( unusedCardsPerPack[k] );

        // add jokers
        for (k = 0; k < numPacks; k++)
            for ( j = 0; j < numJokersPerPack; j++)
                deck.addCard( new Card('X', Card.Suit.values()[j]) );

        // shuffle the cards
        deck.shuffle();
    }

    public boolean deal()
    {
        // returns false if not enough cards, but deals what it can
        int k, j;
        boolean enoughCards;

        // clear all hands
        for (j = 0; j < numPlayers; j++)
            hand[j].resetHand();

        enoughCards = true;
        for (k = 0; k < numCardsPerHand && enoughCards ; k++)
        {
            for (j = 0; j < numPlayers; j++)
                if (deck.getNumCards() > 0)
                    hand[j].takeCard( deck.dealCard() );
                else
                {
                    enoughCards = false;
                    break;
                }
        }

        return enoughCards;
    }

    void sortHands()
    {
        int k;

        for (k = 0; k < numPlayers; k++)
            hand[k].sort();
    }

    Card playCard(int playerIndex, int cardIndex)
    {
        // returns bad card if either argument is bad
        if (playerIndex < 0 ||  playerIndex > numPlayers - 1 ||
                cardIndex < 0 || cardIndex > numCardsPerHand - 1)
        {
            //Creates a card that does not work
            return new Card('M', Card.Suit.spades);
        }

        // return the card played
        return hand[playerIndex].playCard(cardIndex);

    }


    boolean takeCard(int playerIndex)
    {
        // returns false if either argument is bad
        if (playerIndex < 0 || playerIndex > numPlayers - 1)
            return false;

        // Are there enough Cards?
        if (deck.getNumCards() <= 0)
            return false;

        return hand[playerIndex].takeCard(deck.dealCard());
    }

}