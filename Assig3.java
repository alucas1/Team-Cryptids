/**
 * Group: Team Cryptids
 * Authors: Emerald Kunkle, Bryan Aguiar, Gabriel De Leon, Alberto Lucas 
 * Class: CST338
 * Date: 11/17/2020
 * 
 * Program Name: Assig3
 * Description: This program sets up some classes that can be used in future 
 * programs that involve playing card games with a human, or simulating card 
 * games entirely by a computer. Three classes are used: Card, Hand, and Deck.
 * Card represents a playing card, Hand is a collection of cards for a player,
 * and Deck is a deck of playing cards. Hand and Deck objects have cards, and
 * Hand objects obtain cards from Deck objects.
 */ 

import java.util.Scanner;

/** 
   Contains main method used to run and test Card, Hand, and Deck classes.
   You can run all test phases at once, or comment out the ones you don't
   want to run.
*/
public class Assig3
{
   public static void main(String[] args)
   {
      // ******************* PHASE 1 TESTING **********************
      /*
      System.out.println("PHASE 1 TEST BEGIN:");
      // Initialize variables.
      Card[] cards = new Card[3];
      cards[0] = new Card('A', Card.Suit.spades); // legal
      cards[1] = new Card('Z', Card.Suit.spades); // illegal
      cards[2] = new Card('J', Card.Suit.clubs); // legal

      // Output cards.
      for (Card card : cards)
         System.out.println(card);

      // Change status of cards.
      cards[0].set('Z', Card.Suit.spades);
      cards[1].set('Q', Card.Suit.spades);

      // Re-output cards.
      System.out.println();
      for (Card card : cards)
         System.out.println(card);
      System.out.println("PHASE 1 TEST END\n");
      */
      // **********************************************************
      // ******************* PHASE 2 TESTING **********************
      /*
      System.out.println("PHASE 2 TEST BEGIN:");
      // Initialize variables.
      Card explicitCard1 = new Card('A', Card.Suit.clubs);
      Card explicitCard2 = new Card('3', Card.Suit.hearts);
      Card explicitCard3 = new Card('7', Card.Suit.diamonds);
      Card explicitCard4 = new Card('Q', Card.Suit.spades);
      Hand myHand = new Hand();
      int maxNumberOfCards = myHand.MAX_CARDS;

      // Fill hand with the 4 defined cards in sequential order.
      System.out.println("Adding cards to hand until full...");
      for (int i = 0; i < maxNumberOfCards; i++)
      {
         switch (i % 4)
         {
         case 0:
            myHand.takeCard(explicitCard1);
            break;
         case 1:
            myHand.takeCard(explicitCard2);
            break;
         case 2:
            myHand.takeCard(explicitCard3);
            break;
         case 3:
            myHand.takeCard(explicitCard4);
            break;
         }
      }

      // Returns the state of the hand (full or not).
      if (maxNumberOfCards == myHand.getNumberOfCards())
         System.out.println("Hand full");
      else
         System.out.println("Error - Hand is not full!");

      // Output cards in hand object.
      System.out.println("\nAfter deal:");
      System.out.println(myHand);

      // Tests inspectCard() method.
      System.out.println("\nTesting inspectCard():");
      System.out.println("Valid card: " + myHand.inspectCard(10));
      System.out.println("Invalid card: " + myHand.inspectCard(100));

      // Plays all cards in the deck.
      System.out.println("\nPlaying all the cards in deck!:");
      maxNumberOfCards = myHand.getNumberOfCards();
      for (int i = 0; i < maxNumberOfCards; i++)
         System.out.println("Playing " + myHand.playCard());
      
      // Output cards in hand object.
      System.out.println("\nAfter playing all cards");
      System.out.println(myHand.toString());
      System.out.println("PHASE 2 TEST END\n");
      */
      // **********************************************************
      // ******************* PHASE 3 TESTING **********************
      
      System.out.println("PHASE 3 TEST BEGIN:");
      // Unshuffled 2-deck test.
      // Creates an unshuffled deck with 2 packs of cards and outputs them.
      Deck myDeck = new Deck(2);
      int myMaxCard = myDeck.getTopCard();
      for (int i = 0; i < myMaxCard; i++)
      {
         System.out.printf(" %s /", myDeck.dealCard().toString());
         if ((i % 4) == 3)
            System.out.println();
      }

      // Shuffled 2-deck test.
      // Creates a deck with 2 packs of cards, shuffles them, and outputs them.
      System.out.println();
      myDeck.init(2);
      myDeck.shuffle();
      for (int i = 0; i < myMaxCard; i++)
      {
         System.out.printf(" %s /", myDeck.dealCard().toString());
         if ((i % 4) == 3)
            System.out.println();
      }

      // Unshuffled 1-deck test.
      // Creates an unshuffled deck with 1 pack of cards and outputs them.
      System.out.println();
      myDeck.init(1);
      myMaxCard = myDeck.getTopCard();
      for (int i = 0; i < myMaxCard; i++)
      {
         System.out.printf(" %s /", myDeck.dealCard().toString());
         if ((i % 4) == 3)
            System.out.println();
      }

      // Shuffled 1-deck test.
      // Creates a deck with 1 packs of cards, shuffles them, and outputs them.
      System.out.println();
      myDeck.init(1);
      myDeck.shuffle();
      for (int i = 0; i < myMaxCard; i++)
      {
         System.out.printf(" %s /", myDeck.dealCard().toString());
         if ((i % 4) == 3)
            System.out.println();
      }
      System.out.println("PHASE 3 TEST END\n");
      
      // **********************************************************
      // ******************* PHASE 4 TESTING **********************
      
      System.out.println("PHASE 4 TEST BEGIN:");
      // Initialize variables.
      int sizeOfDeck = 0;
      int players = 0;
      Scanner keyboard = new Scanner(System.in);
      Hand hands[] = null;
      Deck deckOfCards = null;

      // Ask for user input on how many hands to create.
      // invalid values repeat loop
      do
      {
         System.out.print("How many hands? (1-10, please): ");
         players = keyboard.nextInt();
      }
      while (players < 1 || players > 10); 

      // Create X amount of hands.
      hands = new Hand[players];
      for (int i = 0; i < players; i++)
         hands[i] = new Hand();

      // Deal the cards to the hands, 1 card per hand until out of cards.
      deckOfCards = new Deck();
      sizeOfDeck = deckOfCards.getTopCard();
      for (int i = 0; i < sizeOfDeck; i++)
         hands[i % players].takeCard(deckOfCards.dealCard());

      // Display unshuffled hands.
      System.out.println("\nHands from the UNSHUFFLED deck:");
      for (Hand hand : hands)
         System.out.println(hand + "\n");

      // Reset the hands, reset the deck, shuffle the deck.
      for (Hand hand : hands)
         hand.resetHand();   
      deckOfCards.init(1);
      deckOfCards.shuffle();

      // Deal the cards to the hands, 1 card per hand until out of cards.
      for (int i = 0; i < sizeOfDeck; i++)
         hands[i % players].takeCard(deckOfCards.dealCard());

      // Display shuffled hands.
      System.out.println("\nHands from the SHUFFLED deck:");
      for (Hand hand : hands)
         System.out.println(hand + "\n");

      // Close Scanner.
      keyboard.close();
      System.out.println("PHASE 4 TEST END");
      
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
      this.suit = suit;
      this.value = value;

      boolean isValidCard = isValid(value, suit);
      errorFlag = !isValidCard; // If card is valid (true), errorFlag is false.
      
      return isValidCard;
   }

   /** 
      Checks for card equality by comparing suit and value.
      @param card A card object that will be compared.
      @return     True if suit and value are the same, else false.
   */
   public boolean equals(Card card)
   {
      return (this.suit == card.suit && this.value == card.value);
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
      char[] validValues = { 'K', 'Q', 'J', 'T',
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
}

/**
   The Hand class represents a hand of playing cards. Array myCards is able to 
   hold MAX_CARDS. numCards describes amount of cards currently in hand.
*/
class Hand
{
   // Initialize class variables.
   public final int MAX_CARDS = 52;

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
   public Card playCard()
   {
      if (numCards > 0)
      {
         // Removes card from count first in order to use correct index.
         numCards--;

         // Creates card copy to prevent privacy leak.
         Card cardClone = new Card(myCards[numCards].getValue(),
               myCards[numCards].getSuit());

         // Removes card from hand.
         myCards[numCards] = null;

         return cardClone;
      }
      else
         return null;
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
   public final int MAX_CARDS = 6 * 52;   // 6 packs of 52 cards.
   private static Card[] masterPack;
   
   // Instance variables.
   private Card[] cards;  
   int topCard;

   /** 
      Constructor that initializes all instance and class variables.
      @param numPacks The amount of 52-card packs to initialize the deck with.
   */
   public Deck(int numPacks)
   {
      allocateMasterPack(); // Initialize array 'masterPack'.
      init(numPacks);
   }

   //Paramaterless constructor - initiates with 1 pack of cards.
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
      @param numPacks The amount of 52-card packs to initialize the deck with.
   */
   public void init(int numPacks)
   {
      // Ensures that amount of cards does not exceed MAX_CARDS or is negative.
      if ((numPacks * 52) < 0 || (numPacks * 52) > MAX_CARDS )
        numPacks = 1;
      
      // Initializes cards array with numPacks amount of 52-card packs.
      topCard = numPacks * 52; 
      cards = new Card[topCard];

      // Initializes cards in 'cards' array by copying valid cards .
      // from 'masterPack' array. 
      for (int i = 0; i < topCard; i++)
         cards[i] = new Card(masterPack[(i % 52)].getValue(),
               masterPack[(i % 52)].getSuit());
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
      standard 52-card pack.
   */
   private static void allocateMasterPack()
   {
      // only runs once when masterPack has not been initialized
      if (masterPack == null)
      {
         // Initialize masterPack
         masterPack = new Card[52];

         // Initialize arrays for valid values and suites.
         char[] validValues = { 'K', 'Q', 'J', 'T',
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
}

/*********************PHASE 3 OUTPUT********************************** 
PHASE 3 TEST BEGIN:
 A of spades / 2 of spades / 3 of spades / 4 of spades /
 5 of spades / 6 of spades / 7 of spades / 8 of spades /
 9 of spades / T of spades / J of spades / Q of spades /
 K of spades / A of hearts / 2 of hearts / 3 of hearts /
 4 of hearts / 5 of hearts / 6 of hearts / 7 of hearts /
 8 of hearts / 9 of hearts / T of hearts / J of hearts /
 Q of hearts / K of hearts / A of diamonds / 2 of diamonds /
 3 of diamonds / 4 of diamonds / 5 of diamonds / 6 of diamonds /
 7 of diamonds / 8 of diamonds / 9 of diamonds / T of diamonds /
 J of diamonds / Q of diamonds / K of diamonds / A of clubs /
 2 of clubs / 3 of clubs / 4 of clubs / 5 of clubs /
 6 of clubs / 7 of clubs / 8 of clubs / 9 of clubs /
 T of clubs / J of clubs / Q of clubs / K of clubs /
 A of spades / 2 of spades / 3 of spades / 4 of spades /
 5 of spades / 6 of spades / 7 of spades / 8 of spades /
 9 of spades / T of spades / J of spades / Q of spades /
 K of spades / A of hearts / 2 of hearts / 3 of hearts /
 4 of hearts / 5 of hearts / 6 of hearts / 7 of hearts /
 8 of hearts / 9 of hearts / T of hearts / J of hearts /
 Q of hearts / K of hearts / A of diamonds / 2 of diamonds /
 3 of diamonds / 4 of diamonds / 5 of diamonds / 6 of diamonds /
 7 of diamonds / 8 of diamonds / 9 of diamonds / T of diamonds /
 J of diamonds / Q of diamonds / K of diamonds / A of clubs /
 2 of clubs / 3 of clubs / 4 of clubs / 5 of clubs /
 6 of clubs / 7 of clubs / 8 of clubs / 9 of clubs /
 T of clubs / J of clubs / Q of clubs / K of clubs /

 3 of clubs / J of spades / 4 of spades / 5 of clubs /
 K of diamonds / 6 of hearts / 8 of spades / A of spades /
 2 of hearts / 7 of spades / 8 of hearts / 3 of diamonds /
 K of hearts / A of diamonds / T of hearts / Q of clubs /
 9 of spades / J of spades / 7 of diamonds / 9 of clubs /
 K of clubs / 2 of spades / 8 of diamonds / 3 of spades /
 2 of spades / 7 of diamonds / 5 of diamonds / 2 of clubs /
 J of hearts / 9 of hearts / 2 of hearts / T of diamonds /
 9 of diamonds / Q of spades / 2 of clubs / A of clubs /
 T of clubs / 7 of clubs / 4 of hearts / T of diamonds /
 3 of hearts / K of spades / K of hearts / 8 of hearts /
 5 of diamonds / 4 of diamonds / 5 of spades / 6 of spades /
 3 of hearts / J of diamonds / A of hearts / 8 of spades /
 T of spades / 5 of hearts / 4 of hearts / 9 of diamonds /
 K of diamonds / 6 of spades / T of clubs / J of diamonds /
 Q of diamonds / 6 of clubs / A of diamonds / 7 of spades /
 Q of hearts / 4 of diamonds / 5 of clubs / Q of hearts /
 A of hearts / 3 of diamonds / 9 of hearts / 3 of clubs /
 J of hearts / Q of clubs / 7 of clubs / 6 of diamonds /
 4 of spades / 5 of hearts / 6 of diamonds / 4 of clubs /
 3 of spades / T of spades / 6 of hearts / A of spades /
 7 of hearts / T of hearts / 6 of clubs / J of clubs /
 K of clubs / Q of diamonds / J of clubs / 9 of spades /
 8 of clubs / 2 of diamonds / 5 of spades / Q of spades /
 K of spades / 7 of hearts / 9 of clubs / 8 of clubs /
 4 of clubs / 2 of diamonds / 8 of diamonds / A of clubs /

 A of spades / 2 of spades / 3 of spades / 4 of spades /
 5 of spades / 6 of spades / 7 of spades / 8 of spades /
 9 of spades / T of spades / J of spades / Q of spades /
 K of spades / A of hearts / 2 of hearts / 3 of hearts /
 4 of hearts / 5 of hearts / 6 of hearts / 7 of hearts /
 8 of hearts / 9 of hearts / T of hearts / J of hearts /
 Q of hearts / K of hearts / A of diamonds / 2 of diamonds /
 3 of diamonds / 4 of diamonds / 5 of diamonds / 6 of diamonds /
 7 of diamonds / 8 of diamonds / 9 of diamonds / T of diamonds /
 J of diamonds / Q of diamonds / K of diamonds / A of clubs /
 2 of clubs / 3 of clubs / 4 of clubs / 5 of clubs /
 6 of clubs / 7 of clubs / 8 of clubs / 9 of clubs /
 T of clubs / J of clubs / Q of clubs / K of clubs /

 7 of spades / J of clubs / A of spades / 4 of spades /
 K of diamonds / A of hearts / 3 of spades / 6 of spades /
 9 of diamonds / J of hearts / J of diamonds / Q of spades /
 Q of hearts / 7 of diamonds / 3 of diamonds / K of spades /
 8 of diamonds / 9 of clubs / 4 of diamonds / 7 of hearts /
 3 of clubs / 5 of spades / 5 of hearts / J of spades /
 2 of hearts / 6 of clubs / T of hearts / 2 of clubs /
 Q of diamonds / 8 of spades / Q of clubs / T of spades /
 T of diamonds / K of clubs / 9 of spades / 4 of hearts /
 8 of clubs / 4 of clubs / T of clubs / 2 of spades /
 K of hearts / 5 of diamonds / 8 of hearts / 5 of clubs /
 6 of diamonds / 7 of clubs / 2 of diamonds / A of clubs /
 9 of hearts / A of diamonds / 3 of hearts / 6 of hearts /
PHASE 3 TEST END


**********************END OF PHASE 3 OUTPUT ******************/

/*********************PHASE 4 OUTPUT***************************
PHASE 4 TEST BEGIN:
How many hands? (1-10, please): -1
How many hands? (1-10, please): 0
How many hands? (1-10, please): 11
How many hands? (1-10, please): 6

Hands from the UNSHUFFLED deck:
Hand = ( A of spades, 7 of spades, K of spades, 6 of hearts, 
Q of hearts, 5 of diamonds, J of diamonds, 4 of clubs, T of clubs )

Hand = ( 2 of spades, 8 of spades, A of hearts, 7 of hearts, 
K of hearts, 6 of diamonds, Q of diamonds, 5 of clubs, J of clubs )

Hand = ( 3 of spades, 9 of spades, 2 of hearts, 8 of hearts, 
A of diamonds, 7 of diamonds, K of diamonds, 6 of clubs, Q of clubs )

Hand = ( 4 of spades, T of spades, 3 of hearts, 9 of hearts, 
2 of diamonds, 8 of diamonds, A of clubs, 7 of clubs, K of clubs )

Hand = ( 5 of spades, J of spades, 4 of hearts, T of hearts, 
3 of diamonds, 9 of diamonds, 2 of clubs, 8 of clubs )

Hand = ( 6 of spades, Q of spades, 5 of hearts, J of hearts, 
4 of diamonds, T of diamonds, 3 of clubs, 9 of clubs )


Hands from the SHUFFLED deck:
Hand = ( 8 of hearts, Q of spades, A of clubs, 6 of clubs, 
7 of spades, 3 of spades, 8 of clubs, 7 of hearts, J of spades )

Hand = ( A of hearts, 6 of hearts, 2 of hearts, J of diamonds, 
2 of diamonds, 4 of spades, 3 of diamonds, T of spades, 4 of clubs )

Hand = ( 7 of diamonds, 6 of spades, 5 of hearts, 2 of clubs, 
2 of spades, Q of clubs, 9 of spades, T of hearts, 9 of hearts )

Hand = ( Q of hearts, 7 of clubs, 5 of clubs, K of diamonds, 
K of spades, A of spades, 5 of diamonds, J of clubs, K of clubs )

Hand = ( T of clubs, K of hearts, 8 of diamonds, 9 of clubs, 
4 of diamonds, 5 of spades, 3 of hearts, 6 of diamonds )

Hand = ( J of hearts, 8 of spades, A of diamonds, T of diamonds, 
9 of diamonds, Q of diamonds, 4 of hearts, 3 of clubs )

PHASE 4 TEST END

**********************END OF PHASE 4 OUTPUT*******************/

/******************COMBINED PHASE 3 & 4 OUTPUT*****************
PHASE 3 TEST BEGIN:
 A of spades / 2 of spades / 3 of spades / 4 of spades /
 5 of spades / 6 of spades / 7 of spades / 8 of spades /
 9 of spades / T of spades / J of spades / Q of spades /
 K of spades / A of hearts / 2 of hearts / 3 of hearts /
 4 of hearts / 5 of hearts / 6 of hearts / 7 of hearts /
 8 of hearts / 9 of hearts / T of hearts / J of hearts /
 Q of hearts / K of hearts / A of diamonds / 2 of diamonds /
 3 of diamonds / 4 of diamonds / 5 of diamonds / 6 of diamonds /
 7 of diamonds / 8 of diamonds / 9 of diamonds / T of diamonds /
 J of diamonds / Q of diamonds / K of diamonds / A of clubs /
 2 of clubs / 3 of clubs / 4 of clubs / 5 of clubs /
 6 of clubs / 7 of clubs / 8 of clubs / 9 of clubs /
 T of clubs / J of clubs / Q of clubs / K of clubs /
 A of spades / 2 of spades / 3 of spades / 4 of spades /
 5 of spades / 6 of spades / 7 of spades / 8 of spades /
 9 of spades / T of spades / J of spades / Q of spades /
 K of spades / A of hearts / 2 of hearts / 3 of hearts /
 4 of hearts / 5 of hearts / 6 of hearts / 7 of hearts /
 8 of hearts / 9 of hearts / T of hearts / J of hearts /
 Q of hearts / K of hearts / A of diamonds / 2 of diamonds /
 3 of diamonds / 4 of diamonds / 5 of diamonds / 6 of diamonds /
 7 of diamonds / 8 of diamonds / 9 of diamonds / T of diamonds /
 J of diamonds / Q of diamonds / K of diamonds / A of clubs /
 2 of clubs / 3 of clubs / 4 of clubs / 5 of clubs /
 6 of clubs / 7 of clubs / 8 of clubs / 9 of clubs /
 T of clubs / J of clubs / Q of clubs / K of clubs /

 4 of hearts / J of hearts / 7 of hearts / 4 of spades /
 T of clubs / 9 of clubs / K of spades / K of hearts /
 6 of clubs / 8 of diamonds / 8 of clubs / 4 of clubs /
 7 of hearts / 9 of diamonds / 9 of hearts / A of hearts /
 2 of hearts / J of spades / T of diamonds / 5 of spades /
 9 of spades / 7 of clubs / A of spades / K of hearts /
 7 of diamonds / Q of clubs / J of diamonds / K of clubs /
 4 of hearts / 5 of diamonds / 7 of clubs / 7 of spades /
 6 of spades / 5 of clubs / 5 of spades / A of hearts /
 7 of diamonds / J of hearts / K of spades / Q of spades /
 6 of diamonds / 3 of spades / A of spades / 6 of hearts /
 J of clubs / 6 of diamonds / T of spades / A of diamonds /
 T of diamonds / Q of diamonds / 4 of clubs / 4 of spades /
 3 of hearts / 4 of diamonds / 5 of hearts / 6 of spades /
 8 of spades / 5 of diamonds / 4 of diamonds / 6 of hearts /
 6 of clubs / 2 of hearts / 9 of diamonds / 8 of hearts /
 A of clubs / 2 of spades / T of hearts / Q of hearts /
 8 of spades / 2 of clubs / 5 of clubs / 3 of clubs /
 5 of hearts / 8 of diamonds / A of clubs / Q of clubs /
 K of diamonds / 2 of clubs / T of clubs / Q of diamonds /
 J of spades / 3 of diamonds / J of diamonds / Q of hearts /
 3 of clubs / 9 of clubs / J of clubs / 9 of hearts /
 T of hearts / 8 of clubs / 7 of spades / 9 of spades /
 2 of diamonds / 3 of spades / 2 of diamonds / 8 of hearts /
 T of spades / 3 of hearts / Q of spades / A of diamonds /
 K of diamonds / 3 of diamonds / 2 of spades / K of clubs /

 A of spades / 2 of spades / 3 of spades / 4 of spades /
 5 of spades / 6 of spades / 7 of spades / 8 of spades /
 9 of spades / T of spades / J of spades / Q of spades /
 K of spades / A of hearts / 2 of hearts / 3 of hearts /
 4 of hearts / 5 of hearts / 6 of hearts / 7 of hearts /
 8 of hearts / 9 of hearts / T of hearts / J of hearts /
 Q of hearts / K of hearts / A of diamonds / 2 of diamonds /
 3 of diamonds / 4 of diamonds / 5 of diamonds / 6 of diamonds /
 7 of diamonds / 8 of diamonds / 9 of diamonds / T of diamonds /
 J of diamonds / Q of diamonds / K of diamonds / A of clubs /
 2 of clubs / 3 of clubs / 4 of clubs / 5 of clubs /
 6 of clubs / 7 of clubs / 8 of clubs / 9 of clubs /
 T of clubs / J of clubs / Q of clubs / K of clubs /

 4 of hearts / J of spades / Q of diamonds / 3 of hearts /
 6 of clubs / 7 of spades / 5 of hearts / Q of clubs /
 6 of hearts / 4 of diamonds / 7 of diamonds / 9 of hearts /
 5 of clubs / 3 of spades / J of clubs / Q of spades /
 A of spades / 9 of clubs / 7 of clubs / 2 of spades /
 6 of diamonds / K of hearts / 2 of clubs / K of spades /
 T of spades / 3 of diamonds / 2 of diamonds / T of diamonds /
 J of diamonds / T of hearts / 9 of spades / 4 of clubs /
 8 of diamonds / 5 of spades / A of hearts / 5 of diamonds /
 7 of hearts / 4 of spades / 9 of diamonds / 6 of spades /
 A of diamonds / K of diamonds / 8 of spades / T of clubs /
 3 of clubs / A of clubs / Q of hearts / 2 of hearts /
 J of hearts / 8 of hearts / 8 of clubs / K of clubs /
PHASE 3 TEST END

PHASE 4 TEST BEGIN:
How many hands? (1-10, please): 2

Hands from the UNSHUFFLED deck:
Hand = ( A of spades, 3 of spades, 5 of spades, 7 of spades, 
9 of spades, J of spades, K of spades, 2 of hearts, 4 of hearts, 
6 of hearts, 8 of hearts, T of hearts, Q of hearts, A of diamonds, 
3 of diamonds, 5 of diamonds, 7 of diamonds, 9 of diamonds, J of diamonds, 
K of diamonds, 2 of clubs, 4 of clubs, 6 of clubs, 8 of clubs, 
T of clubs, Q of clubs )

Hand = ( 2 of spades, 4 of spades, 6 of spades, 8 of spades, 
T of spades, Q of spades, A of hearts, 3 of hearts, 5 of hearts, 
7 of hearts, 9 of hearts, J of hearts, K of hearts, 2 of diamonds, 
4 of diamonds, 6 of diamonds, 8 of diamonds, T of diamonds, Q of diamonds, 
A of clubs, 3 of clubs, 5 of clubs, 7 of clubs, 9 of clubs, 
J of clubs, K of clubs )


Hands from the SHUFFLED deck:
Hand = ( 3 of spades, Q of diamonds, 8 of clubs, 3 of clubs, 
J of diamonds, 9 of spades, J of hearts, T of spades, 6 of clubs, 
5 of hearts, 9 of clubs, 7 of diamonds, Q of clubs, 4 of spades, 
T of clubs, T of diamonds, 9 of hearts, J of clubs, J of spades, 
2 of hearts, 5 of diamonds, 4 of clubs, K of hearts, T of hearts, 
A of diamonds, 8 of spades )

Hand = ( 3 of hearts, 9 of diamonds, K of diamonds, K of spades, 
7 of hearts, 7 of clubs, 2 of spades, A of spades, 8 of diamonds, 
4 of hearts, 2 of diamonds, Q of spades, 5 of clubs, 4 of diamonds, 
3 of diamonds, 2 of clubs, 5 of spades, 6 of diamonds, 6 of hearts, 
7 of spades, Q of hearts, 8 of hearts, A of hearts, A of clubs, 
6 of spades, K of clubs )

PHASE 4 TEST END

***************END OFCOMBINED PHASE 3 & 4 OUTPUT**************/