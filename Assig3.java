/* Group: Team Cryptids
 * Authors: Emerald Kunkle, Bryan Aguiar, Gabriel De Leon, Alberto Lucas 
 * Class: CST338
 * Date: 11/17/2020
 * 
 * Program Name: DeckOfCards
 * Description: 
 */

import java.util.Scanner;

public class DeckOfCards
{
   public static void main(String[] args)
   {
      // Ask for user input on how many hands to create
      String input = "";
      Scanner keyboard = new Scanner(System.in);
      do
      {
         System.out.print("How many hands? (1-10, please): ");
         input = keyboard.nextLine();
      }
      while (!input.matches("^[1-9]|10$")); //RegEx checks valid values (1-10)
      
      // Create X amount of hands
      int players = Integer.valueOf(input);
      Hand hands[] = new Hand[players];
      for (int i = 0; i < players; i++)
      {
         hands[i] = new Hand();
      }
      
      // Create a 52 card deck
      Deck deckOfCards = new Deck();
      
      // Deal the cards to the hands, 1 card per hand over and over 
      // until we are out of cards
      int sizeOfDeck = deckOfCards.getTopCard();
      for(int i = 0; i < sizeOfDeck; i++) {
         hands[i % players].takeCard(deckOfCards.dealCard());
      }
      
      // Display
      System.out.println("\nHands from the UNSHUFFLED deck:");
      for (Hand hand : hands)
      {
         System.out.println(hand);
      }
      
      // Reset the hands, reset the deck, shuffle the deck
      for (int i = 0; i < players; i++)
      {
         hands[i].resetHand();
      }
      deckOfCards.init(1);
      deckOfCards.shuffle();
      
      // Deal the cards to the hands (same amount of hands), 1 card per hand
      // over and over until we are out of cards
      for(int i = 0; i < sizeOfDeck; i++) {
         hands[i % players].takeCard(deckOfCards.dealCard());
      }
      
      // Display
      System.out.println("\nHands from the SHUFFLED deck:");
      for (Hand hand : hands)
      {
         System.out.println(hand);
      }
      
      // Close Scanner
      keyboard.close();
   }
}

/////////////////////////////////////////////////////////////////////////
/////////////////////     class Card below          /////////////////////
/////////////////////////////////////////////////////////////////////////   

class Card
{
   // Declares the possible suits of a card
   public enum Suit
   {
      clubs, diamonds, hearts, spades
   }

   // Instance Variables
   private Suit suit;
   private char value;
   private boolean errorFlag;

   // constructor if the user instantiates with both value and suit
   public Card(char value, Suit suit)
   {
      set(value, suit);
   }

   // parameterless constructor
   public Card()
   {
      this('A', Suit.spades);
   }

   // constructor if the user instantiates with suit but not value
   public Card(Suit suit)
   {
      this('A', suit);
   }

   // constructor if the user instantiates with value but not suit
   public Card(char value)
   {
      this(value, Suit.spades);
   }

   // accessor for suit
   public Suit getSuit()
   {
      return suit;
   }

   // accessor for value
   public char getValue()
   {
      return value;
   }

   // accessor for errorFlag
   public boolean getErrorFlag()
   {
      return errorFlag;
   }

   // mutator
   public boolean set(char value, Suit suit)
   {
      // sets card suit and value
      this.suit = suit;
      this.value = value;

      // checks if arguments create a valid card
      boolean isValidCard = isValid(value, suit);

      // errorFlag is true if the card is not valid
      errorFlag = !isValidCard;

      return isValidCard;
   }

   // checks if card is same as a reference card
   public boolean equals(Card card)
   {
      return (this.suit == card.suit && this.value == card.value);
   }

   @Override
   public String toString()
   {
      if (errorFlag)
         return "Invalid card! :c\n";
      else
         return value + " of " + suit;
   }

   // check if suit and value are valid values
   private boolean isValid(char value, Suit suit)
   {
      // define the valid values and suits
      char[] validValues =
      { 'K', 'Q', 'J', 'T',
            '9', '8', '7', '6', '5', '4', '3', '2', 'A' };
      Card.Suit[] validSuits = Card.Suit.values();

      // first loop checks suit. If suit is valid, second loop checks value. if
      // value is valid, returns true (valid suit & value). Else, returns false
      for (Suit validSuit : validSuits)
      {
         if (suit == validSuit)
         {
            for (char validValue : validValues)
            {
               if (value == validValue)
               {
                  return true;
               }
            }
         }
      }

      return false;
   }
}

/////////////////////////////////////////////////////////////////////////
/////////////////////     class Hand below          /////////////////////
/////////////////////////////////////////////////////////////////////////       

class Hand
{
   // initialize class variables
   public final int MAX_CARDS = 52;

   // declare instance variables
   private Card[] myCards;
   private int numCards;

   // default constructor
   public Hand()
   {
      numCards = 0;
      myCards = new Card[MAX_CARDS];
   }

   // accessor for numCards
   public int getNumberOfCards()
   {
      return numCards;
   }

   // "removes" all cards by setting numCards to 0
   public void resetHand()
   {
      myCards = new Card[MAX_CARDS];
      numCards = 0;
   }

   // this will return true if a card was able to be added, and false otherwise
   public boolean takeCard(Card card)
   {
      // if the added card doesn't make the deck surpass the max, add it
      // and return true. Else, return false
      if (numCards < MAX_CARDS)
      {
         // creates card copy to prevent privacy leak
         Card cardClone = new Card(card.getValue(), card.getSuit());
         myCards[numCards] = cardClone;
         numCards++;
         return true;
      }
      else
         return false;
   }

   // playCard will take the top card element in the array myCards, remove it,
   // and return it
   public Card playCard()
   {
      if (numCards > 0)
      {
         // removes card from count first in order to use correct index
         numCards--;

         // creates card copy to prevent privacy leak & nullPointerException
         Card cardClone = new Card(myCards[numCards].getValue(),
               myCards[numCards].getSuit());

         // removes card from hand
         myCards[numCards] = null;

         return cardClone;
      }
      else
      {
         return null;
      }
   }

   // Turns everything into a single string and returns it
   public String toString()
   {
      String returnString = "Hand = " + "( ";
      // prints the cards out in a nice and pretty card manner :3
      for (int i = 0; i < numCards; i++)
      {
         if (i % 5 == 4) // insert a newline every 5th card
            returnString += "\n";
         if (i != numCards - 1) // insert a comma after every card
            returnString += (myCards[i].getValue() + " of " +
                  myCards[i].getSuit() + ", ");
         else // if last card, do not insert a comma
            returnString += (myCards[i].getValue() + " of " +
                  myCards[i].getSuit());
      }

      return returnString + " )\n";
   }

   // inspectCard gets the card at a specific position in the myCards array
   public Card inspectCard(int k)
   {
      // returns card clone (to avoid privacy leak) if spot is valid.
      // Else, return a random card with a true errorFlag
      if (k < numCards)
         return new Card(myCards[k].getValue(), myCards[k].getSuit());
      else
         return new Card('D', Card.Suit.diamonds);
   }
}

/*******************************************************************************
 * 
 * Deck Class
 *
 ******************************************************************************/
class Deck
{
   // dictates the max amount of packs that can be alloted
   public final int MAX_CARDS = 6 * 52;
   // holds a static deck of 52 cards
   private static Card[] masterPack;
   // cards will store all the decks by the caller up to 6
   private Card[] cards;
   // the current max card of the deck (e.g if 4 then topCard == 208)
   int topCard;

   // constructor that accepts the amount of packs the caller requests
   public Deck(int numPacks)
   {
      // creates the master pack
      allocateMasterPack();

      // checks to see if numPacks is within MAX_CARDS space
      if ((numPacks * 52) <= MAX_CARDS)
      {
         // calls the function to copy cards from masterPacks to cards
         init(numPacks);
      }
      else
      {
         // if invalid numPacks, initialize w/default decks (1)
         init(1);
      }
   }

   // paramaterless constructor - initiates with 1 deck
   public Deck()
   {
      this(1);
   }

   // accessor function for TopCard
   public int getTopCard()
   {
      return topCard;
   }

   // fills the cards array with cards from the masterPack array according to
   // numPacks
   public void init(int numPacks)
   {
      // checks to see if user entered a valid value in numPacks
      if ((numPacks * 52) <= MAX_CARDS)
      {
         // set the topCard variable to the product of 52 and numPacks.
         // this will be how big our array of cards will be and we allocate
         // space for a topCard amount of cards.
         topCard = numPacks * 52;
         cards = new Card[topCard];

         // iterates through the cards array and initialize them to a card in
         // masterPack.
         for (int i = 0; i < topCard; i++)
         {
            // assigns cards in the current i to a new card using the
            // values and suit from masterPack. masterPack's i is
            // modulo 52 because masterPack is an array of size 52 but cards
            // can be multiples of that.
            cards[i] = new Card(masterPack[(i % 52)].getValue(),
                  masterPack[(i % 52)].getSuit());
         }
      }
   }

   // function for shuffling cards
   public void shuffle()
   {
      // i starts at the last card which is topCard - 1 and decrements
      // till we've gone through every card

      for (int i = topCard - 1; i > 0; i--)
      {
         // uses math.rand but type cast to int since Math.random returns a
         // double. We use topCards value because we are randomizing and want
         // the number used to be between the amount of cards in the deck.
         int randomizer = (int) (Math.random() * topCard);
         // we hold the value in placeHolder and then place the value in
         // the randomizer location into the i location. We then put the
         // value in placeholder into where the randomizer is currently located
         // All of this is to swap spaces between cards.
         Card placeHolder = cards[i];
         cards[i] = cards[randomizer];
         cards[randomizer] = placeHolder;
      }
   }

   // function to deal the topCard
   public Card dealCard()
   {
      // checks to see if the topCard is not 0. i.e is the deck empty
      if (topCard > 0)
      {
         // decrement topCard because we are "removing" the card from the deck
         // we decrement first because if the max array is 104 then topCard is
         // 104 and we will run into an error
         topCard--;
         // creates a copy of the card and removes it from the deck (prevents
         // privacy leak)
         Card cardClone = new Card(cards[topCard].getValue(),
               cards[topCard].getSuit());
         cards[topCard] = null;
         // return a copy of the current top Card
         return cardClone;
      }
      else
         return null;
   }

   // checks to see if card at location k is valid.
   public Card inspectCard(int k)
   {
      // returns card clone (to avoid privacy leak) if spot is valid.
      // Else, return a random card with a true errorFlag
      if (k < topCard)
      {
         return new Card(cards[k].getValue(), cards[k].getSuit());
      }
      else
      {
         return new Card('D', Card.Suit.diamonds);
      }
   }

   // function to create our masterPack
   private static void allocateMasterPack()
   {
      // only runs once when masterPack has not been initialized
      if (masterPack == null)
      {
         // Initialize masterPack
         masterPack = new Card[52];

         // Initialize arrays for valid values and suites.
         char[] validValues =
         { 'K', 'Q', 'J', 'T',
               '9', '8', '7', '6', '5', '4', '3', '2', 'A' };
         Card.Suit[] validSuits = Card.Suit.values();

         // Outer loop goes through each suit once
         int index = 0;
         for (Card.Suit validSuit : validSuits)
         {
            // Maps a valid value to each suit and builds a card
            for (char validValue : validValues)
            {
               masterPack[index] = new Card(validValue, validSuit);
               index++;
            }
         }
      }
   }
}

/*********************PHASE 3 OUTPUT********************************** 
NEED TO REDO
**********************END OF PHASE 3 OUTPUT ******************/

/*********************PHASE 4 OUTPUT***************************
How many hands? (1-10, please): dfg
How many hands? (1-10, please): 56316
How many hands? (1-10, please): -1651
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
Hand = ( 2 of spades, 6 of clubs, K of diamonds, Q of hearts, 
6 of hearts, 7 of spades, 6 of diamonds, 6 of spades, 2 of diamonds )

Hand = ( 3 of clubs, T of clubs, 3 of hearts, 5 of diamonds, 
K of spades, 5 of hearts, 2 of hearts, J of clubs, 4 of spades )

Hand = ( A of diamonds, Q of diamonds, 8 of clubs, A of clubs, 
5 of clubs, 8 of hearts, 3 of diamonds, J of diamonds, T of spades )

Hand = ( 2 of clubs, 9 of spades, T of diamonds, 5 of spades, 
9 of hearts, 7 of clubs, 8 of diamonds, 7 of hearts, 9 of clubs )

Hand = ( A of hearts, J of spades, Q of spades, A of spades, 
J of hearts, 9 of diamonds, 4 of clubs, Q of clubs )

Hand = ( K of clubs, 8 of spades, T of hearts, 7 of diamonds, 
4 of hearts, K of hearts, 4 of diamonds, 3 of spades )


**********************END OF PHASE 4 OUTPUT*******************/
