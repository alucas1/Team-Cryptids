  
//Team Cryptids

//Emerald Kunkle
//Bryan Aguiar
//Gabriel De Leon
//Alberto Lucas 
import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;


public class Assig5
{
   // static variables
   static int NUM_CARDS_PER_HAND = 7;
   static int  NUM_PLAYERS = 2;
   static JLabel[] computerLabels = new JLabel[NUM_CARDS_PER_HAND];
   static JLabel[] humanLabels = new JLabel[NUM_CARDS_PER_HAND];  
   static JLabel[] playedCardLabels  = new JLabel[NUM_PLAYERS]; 
   static JLabel[] playLabelText  = new JLabel[NUM_PLAYERS];
   
   public static void main(String[] args)
   {
      //instance variable
      int k;
      
      // establish main frame in which program will run
      CardTable myCardTable 
         = new CardTable("CardTable", NUM_CARDS_PER_HAND, NUM_PLAYERS);
      myCardTable.setSize(800, 600);
      myCardTable.setLocationRelativeTo(null);
      myCardTable.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

      // show everything to the user
      myCardTable.setVisible(true);

      // CREATE LABELS ----------------------------------------------------
      // loop creates labels for both cpu and player. randomCardGenerator is called 
      // and distributes a card icon to the human player label.
      for (k = 0; k < NUM_CARDS_PER_HAND; k++)
      {
         // Human card
         Card newCard = randomCardGenerator();
         humanLabels[k] = new JLabel(GUICard.getIcon(newCard));
         
         //Computer card
         computerLabels[k] = new JLabel(GUICard.getBackCardIcon()); 
      }
      
      // Create cards in the play area for both cpu and player. randomCardGenerator
      // is called to distribute a random card for both players
      for (k = 0; k < NUM_PLAYERS; k++)
      {
         Card newCard = randomCardGenerator();
         playedCardLabels[k] = new JLabel(GUICard.getIcon(newCard));
         
         // Differentiates CPU at 0 versus players up to NUM_Players
         if (k == 0)
            playLabelText[k] = new JLabel("Computer", JLabel.CENTER);
         else
            playLabelText[k] = new JLabel("Player " + k, JLabel.CENTER);
      }
      
  
      // ADD LABELS TO PANELS ----------------------------------------
      for (k = 0; k < NUM_CARDS_PER_HAND; k++)
      {
         // Add human and computer card labels to panels
         myCardTable.pnlHumanHand.add(humanLabels[k]);
         myCardTable.pnlComputerHand.add(computerLabels[k]);
      }
      
      // and two random cards in the play region 
      // (simulating a computer/hum ply). 
      for (k = 0; k < NUM_PLAYERS; k++)
         myCardTable.pnlPlayArea.add(playedCardLabels[k]);
      
   // Add human/cpu text labels to played area with cpu being 0
      for (k = 0; k < NUM_PLAYERS; k++)
         myCardTable.pnlPlayArea.add(playLabelText[k]);

      // show everything to the user
      myCardTable.setVisible(true);
   }
   
   // function that returns a random card when called
   static Card randomCardGenerator()
   {
      // uses the valuRanks in the card class to obtain each
      // value for cards
      char[] values = Card.valuRanks;
      // uses math random to generate a random array location using
      // the array length and pass the character to a char called value
      char value = values[(int)(Math.random() * (values.length))];
      // The valid suits are put into an array and the algorithm is
      // similar to value
      Card.Suit[] suits = { Card.Suit.clubs, Card.Suit.diamonds, 
            Card.Suit.hearts, Card.Suit.spades,};
      Card.Suit suit = suits[(int)(Math.random() * (suits.length))];
      
      // returns the new card
      return new Card(value,suit);
      
   }
}

/*
The CardTable class represents a playing card table using JPanels.
The class also contains two private integers accessed via accessor
functions
*/
class CardTable extends JFrame
{
   static int MAX_CARDS_PER_HAND = 56;
   static int MAX_PLAYERS = 2; 
   
   private int numCardsPerHand;
   private int numPlayers;
   
   public JPanel pnlComputerHand, pnlHumanHand, pnlPlayArea;
   
   // Default constructor calls the constructor with parameters
   CardTable()
   {
      this("CardTable", 7, 2);
   }
   
   // Constructor with parameters
   // @param title - a string with the title of the GUI
   // @param numCardsPerHand - stores how many cards should be dealt
   // per hand
   // @param numPlayers - int that stores how many players are playing
   CardTable(String title, int numCardsPerHand, int numPlayers)
   {
      // Names the program whatever is loaded into title
      super(title);
      
      // checks if numCardsPerHand is valid to MAX_CARDS_PER_HAND
      // otherwise set it to default values
      if ( numCardsPerHand >= 0 && numCardsPerHand <= MAX_CARDS_PER_HAND)
         this.numCardsPerHand = numCardsPerHand;
      else
         this.numCardsPerHand = 7;
      
      // checks if numPlayers is valid to MAX_PLAYERS otherwise
      // set it to default values
      if (numPlayers > 0 && numPlayers <= MAX_PLAYERS)
         this.numPlayers = numPlayers;
      else
         this.numPlayers = 2;
      
      // initialize JPanels for three seperate panels
      pnlComputerHand = new JPanel();
      pnlPlayArea = new JPanel(new GridLayout(2,2));
      pnlHumanHand = new JPanel();
      
      // title panel areas
      pnlComputerHand.setBorder(new TitledBorder("Computer Hand"));
      pnlPlayArea.setBorder(new TitledBorder("Playing Area"));
      pnlHumanHand.setBorder(new TitledBorder("Your Hand"));
      
      // set the border and layout panels according to spec
      setLayout(new BorderLayout());
      add(pnlComputerHand, BorderLayout.NORTH);
      add(pnlPlayArea, BorderLayout.CENTER);
      add(pnlHumanHand, BorderLayout.SOUTH);
   }
   
   // accessor for numCardsPerHand
   // @return - int representing numCardsPerHand
   public int getNumCardsPerHand()
   {
      return numCardsPerHand;
   }
   
   // accessor for numPlayers
   // @return - int representing numPlayers
   public int getNumPlayers()
   {
      return numPlayers;
   }
}

/*
The GUICard class represents the icons to be placed on the GUI
*/
class GUICard
{
   // a 2d array that stores the images of the various cards
   private static Icon[][] iconCards = new ImageIcon[14][4];
   private static Icon iconBack;
   static boolean iconsLoaded = false;
   
   // function that loads the Card Icons into the static iconCards
   // array
   static void loadCardIcons()
   {
      // Change IMGFOLDER to where the image folder is located
      final String IMGFOLDER = ".\\images\\";
      final String IMGSUFFIX = ".gif";
      
      // if Icons are already loaded then return
      if (iconsLoaded)
         return;
      
      // uses two helper functions to help store the images of the cards
      // into the iconCards array
      for (int suitIndex = 0; suitIndex < 4; suitIndex++)
         for (int valueIndex = 0; valueIndex < 14; valueIndex++)
         {
            String fileName = IMGFOLDER + intToCardValue(valueIndex)
            + intToCardSuit(suitIndex) + IMGSUFFIX;
            iconCards[valueIndex][suitIndex] = new ImageIcon(fileName);
         }
      
      // Adds the iconBack card
      iconBack = new ImageIcon(IMGFOLDER + "BK" + IMGSUFFIX);
      
      // Flip flag for iconsLoaded so this function does not run again
      iconsLoaded = true;
   }
   
   // accessor function for iconCards that returns a specific icon based on
   // the parameter
   // @param Card - an object of the card class that has the current card
   // being searched
   // @return Icon - returns an object of the Icon class representing an image
   // of the current card requested.
   static public Icon getIcon(Card card)
   {
      // checks the cards error function 
      if(card.getErrorFlag())
         return null;
      
      // makes sure images are loaded before returning an image
      loadCardIcons();
      
      // returns the specific image using the card value and suit
      return iconCards[valueAsInt(card)][suitAsInt(card)];
   }
   
   // accessor function for iconBack
   // @return Icon - returns an Icon object representing the iconBack image
   static public Icon getBackCardIcon()
   {
      return iconBack;
   }
   
   // helper function that turns the value of the passed card into an integer
   // @param card - a Card object that represents a playing card
   // @return int - returns the integer value of value of the card object, otherwise
   // returns -1 if invalid
   static int valueAsInt(Card card)
   {
      // checks to see if the card is valid
      if (card.getErrorFlag())
         return -1;
      
      // array that stores the values of each card into an array of chars
      char[] values = Card.valuRanks;
      // iterates through the array and uses the current index as the value
      // to return to the user to state the current card being look at
      for (int index = 0; index < values.length; index++)
         if (values[index] == card.getValue())
            return index;
      
      // if card makes it to this point and doesn't find a matching card return
      // -1 
      return -1;      
   }
   
   // helper function similar to valueAsInt but for suit
   // @param card - a Card object that represents a playing card
   // @return int - returns the integer value of the suit of the card object, otherwise
   // returns -1 if invalid
   static int suitAsInt(Card card)
   {
      // checks to see if the card is valid
      if(card.getErrorFlag())
         return -1;
      
      // array that stores the suit of each card into an array of suits
      Card.Suit[] suits = { Card.Suit.clubs, Card.Suit.diamonds, 
            Card.Suit.hearts, Card.Suit.spades,};
      // iterates through the array and uses the current index as the value
      // to return to the user to state the current card suit being looked at
      for(int index = 0; index < suits.length; index++)
         if (suits[index] == card.getSuit())
            return index;
      
      // if card makes it to this point and doesn't find a matching card return
      // -1 
      return -1;     
   }
   
   // helper function that turns an int back into a card suit
   // @param suitIndex - an integer that represents the index of the suit requested
   // @return char - returns the char representing a suit of a card
   private static char intToCardSuit(int suitIndex)
   {
      // an array of CARDSUITS represented as chars
      final char[] CARDSUITS = {'C', 'D', 'H', 'S'};
      
      // checks if index is valid otherwise return an invalid char
      if (suitIndex >= CARDSUITS.length || suitIndex < 0)
         return 'E';
      else
         return CARDSUITS[suitIndex];
   }
   
   // helper function similar to intToCardSuit but for the card value
   // @param valueIndex - an integer that represents the index of the value requested
   // @return char - returns the char representing a value of a card
   private static char intToCardValue(int valueIndex)
   {      
      // array of card values
      char[] cardValues = Card.valuRanks;
      // checks if index is valid otherwise returns an invalid char
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
   public static char[] valuRanks = {'2', '3', '4', '5', '6', '7', '8' ,
         '9', 'T', 'J', 'Q', 'K', 'A', 'X'};

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
      
      if(isValidCard)
      {
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
      return (this.suit == card.suit && this.value == card.value && this.errorFlag
              == card.errorFlag);
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
      // Define the valid values and suits
      char[] validValues = { 'X', 'K', 'Q', 'J', 'T',
            '9', '8', '7', '6', '5', '4', '3', '2', 'A' };
      Card.Suit[] validSuits = Card.Suit.values();

      // First loop checks suit.
      for (Suit validSuit : validSuits)
      {  
         if (suit == validSuit)
         {  // If a valid suit is found, check for a valid value.
            for (char validValue : validValues)
            {  // If a valid value is found, return true (valid suit and value)
               if (value == validValue)
                  return true; 
            }
         }
      }

      return false; 
   }
   
   // Function that sorts the passed card array using a bubble sort
   // @param card - an array of Card objects needing to be sorted
   // @arraySize - an int that stores the size of the card array
   static void arraySort(Card[] card, int arraySize)
   {
      // if array is empty return without sorting
      if (card == null)
         return;
      
      // bubble sort for the card array using card value as the values to be
      // sorted
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
   Takes the card element at the cardIndex and return the Card object
   otherwise return an invalid card
   @param cardIndex - integer that has the current location of the card in
   regards to hand position.
   @return A copy of the top card if there are cards in the hand, else null.
    */
   public Card playCard(int cardIndex)
   {
      if (numCards == 0) //error
      {
         //Creates a card that does not work
         return new Card('M', Card.Suit.spades);
      }
      // store the card in the index into a temporary
      // card.
      Card card = myCards[cardIndex];
      
      // decrement the size of the array by 1
      numCards--;
      
      // loops through the array shifting the cards over based
      // on the card that was removed
      for(int i = cardIndex; i < numCards; i++)
      {
         myCards[i] = myCards[i+1];
      }
      
      // set the last card in the array which should be a duplicate
      // to null
      myCards[numCards] = null;
      
      // return the stored card
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
   
   // function that calls the arraySort in the Card class to sort hand
   void sort()
   {
      Card.arraySort(myCards, numCards);
   }
}

/**
The Deck class represents a deck of playing cards. The deck can hold a max
of MAX_CARDS in array 'cards'. topCard is the number of cards in the deck.
Array 'masterPack' is used as a blueprint of all the valid cards 
(suit and value) in a standard 56 card pack.
*/
class Deck
{
   // Class variables.
   public final int MAX_CARDS = 6 * 56;   // 6 packs of 56 cards.
   private static Card[] masterPack;

   // Instance variables.
   private Card[] cards;  
   private int topCard;
   private int numPacks;

   /** 
   Constructor that initializes all instance and class variables.
   @param numPacks The amount of 56-card packs to initialize the deck with.
    */
   public Deck(int numPacks)
   {
      allocateMasterPack(); // Initialize array 'masterPack'.
      this.numPacks = numPacks;
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
         char[] validValues = { 'X', 'K', 'Q', 'J', 'T',
            '9', '8', '7', '6', '5', '4', '3', '2', 'A' };
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
   
   // a function that adds a card to the deck
   // @param card - a card object to be added to the deck
   // @return - returns a boolean if card was succesfully added
   boolean addCard(Card card)
   {
      // checks to see if card exists
      if(card == null)
         return false;
      
      // checks to see if topCard exceeds MAX_CARDS before adding
      if (topCard >= MAX_CARDS)
         return false;
      
      // integer that stores how many duplicates of the card if found
      int numFound = 0;
      // sort through the deck to count how many copies of card exist
      for (int i = 0; i < topCard; i++)
         if (cards[i].equals(card))
            numFound++;
      
      // return false if too many of the card is found
      if (numFound >= numPacks)
         return false;
      
      // places the card at the top of the deck
      cards[topCard] = new Card(card.getValue(), card.getSuit());
      topCard++;
      
      return true;
   }
   
   // similar to addCard but removes a specific card from the deck
   // and deals it out
   // @param card - a Card object that removes a specific card from the
   // deck
   // @return - returns true if successful otherwise returns false
   boolean removeCard(Card card)
   {
      if (card == null)
         return false;
      
      // function that sorts through the deck and returns the specific
      // true if found card is found after dealing it out
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
   
   // sorts through array using the function from the card class.
   void sort()
   {
      Card.arraySort(cards, topCard);
   }
   
   // accessor function for the total number of cards
   // @return - an int that returns the topCard which is
   // also the total number of cards in the array
   int getNumCards()
   {
      return topCard;
   }
}
