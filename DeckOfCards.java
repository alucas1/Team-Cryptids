//Team Cryptids

//Emerald Kunkle
//Bryan Aguiar
//Gabriel De Leon
//Alberto Lucas  

//Deck Of Cards Assignment
//11-11-2020 to 11-17-2020
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

      keyboard.close();
   }
}

/////////////////////////////////////////////////////////////////////////
/////////////////////     class Card below          /////////////////////
/////////////////////////////////////////////////////////////////////////   

class Card
{
   public enum Suit
   {
      clubs, diamonds, hearts, spades
   };

   private char value;
   private Suit suit;
   private boolean errorFlag;

   // default constructor
   public Card()
   {
      value = 'A';
      suit = Suit.spades;
   }

   // constructor if the user instantiates with both value and suit
   public Card(char value, Suit suit)
   {
      set(value, suit);
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

   @Override
   public String toString()
   {
      if (errorFlag)
         return "Invalid card! :c\n";
      else
         return getValue() + " of " + getSuit();
   }

   // mutator
   public boolean set(char value, Suit suit)
   {
      this.suit = suit;
      this.value = value;
      this.errorFlag = !(isValid(value, suit));

      if (errorFlag)
         return false;
      else
         return true;
   }

   // accessor for suit (getter)
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

   // checks if card is same as a reference card
   public boolean equals(Card card)
   {
      return (this.suit == card.suit && this.value == card.value);
   }

   // check if suit and value are valid values
   private boolean isValid(char value, Suit suit)
   {
      //define the valid values and suits
      char validValues[] = {'K', 'Q', 'J', 'T', 
            '9', '8', '7', '6', '5', '4', '3', '2', 'A'};
      Card.Suit validSuit[] = Card.Suit.values();
      
      //checks suit. If suit is valid, checks value. if value is valid, returns
      //true (valid suit and value). Else, returns false.
      for(int i = 0; i < validSuit.length; i++) {
         if (suit == validSuit[i]) {
            for(int j = 0; j < validValues.length; j++) {
               if(value == validValues[j]) {
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

   // "removes" all cards by setting numCards to 0
   public void resetHand()
   {
      numCards = 0;
   }

   // this will return true if a card was able to be added, and false otherwise
   public boolean takeCard(Card card)
   {
      // if the added card doesn't make the deck surpass the max, add it
      // and return true. Else, return false
      if (numCards < MAX_CARDS)
      {
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
      // returns top card and reduces numCards by 1
      if (numCards > 0)
      {
         numCards--; // remove card first to provide correct index
         return myCards[numCards];
      }
      return new Card('d', Card.Suit.hearts);
   }

   // Turns everything into a single string and returns it
   public String toString()
   {
      String returnString = "Hand = " + "( ";
      // prints the cards out in a nice and pretty card manner :3
      for (int i = 0; i < numCards; i++)
      {
         if (i % 5 == 4)
            returnString = returnString + "\n";
         if (i != numCards - 1)
            returnString = returnString + (myCards[i].getValue() + " of " +
                  myCards[i].getSuit() + ", ");
         else
            returnString = returnString + (myCards[i].getValue() + " of " +
                  myCards[i].getSuit());
      }
      // this sends the cards back as one string
      return returnString + " )\n";
   }

   // getter for numCards (accessor)
   public int getNumberOfCards()
   {
      return numCards;
   }

   // inspectCard gets the card at a specific position in the myCards array
   Card inspectCard(int k)
   {
      // returns card if spot is valid. Else, return a random card with a true
      // errorFlag
      if (k < myCards.length)
         return myCards[k];
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
   public final int MAX_CARDS = 52 * 6;
   // holds a static deck of 52 cards
   private static Card[] masterPack;
   // cards will store all the decks by the caller up to 6
   private Card[] cards;
   // the current max card of the deck (e.g if 4 then topCard == 208)
   int topCard;

   // Default constructor that calls Deck(int numpacks) with a default value
   // of 1
   public Deck()
   {
      this(1);
   }

   // constructor that accepts the amount of packs the caller requests
   public Deck(int numPacks)
   {
      // checks to see if numPacks is within MAX_CARDS space
      if ((numPacks * 52) <= MAX_CARDS)
      {
         // creates the master pack
         allocateMasterPack();
         // calls the function to copy cards from masterPacks to cards
         init(numPacks);
      }
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
         // return the current top Card
         return inspectCard(topCard);
      }
      // returns a card that is not valid if topCard is 0
      else
         return new Card('E', Card.Suit.spades);
   }

   // accessor function for TopCard
   public int getTopCard()
   {
      return topCard;
   }

   // checks to see if card at location k is valid.
   public Card inspectCard(int k)
   {
      if (k < cards.length)
      {
         return cards[k];
      }
      else
      {
         return new Card('E', Card.Suit.spades);
      }
   }

   // function to create our masterPack
   private static void allocateMasterPack()
   {
      if(masterPack == null) {
         //Initialize masterpack
         masterPack = new Card[52];
         
         //Initialize arrays for valid values and suites.
         char validValues[] = {'K', 'Q', 'J', 'T', 
               '9', '8', '7', '6', '5', '4', '3', '2', 'A'};
         Card.Suit validSuit[] = Card.Suit.values();
         
         // Assigns each suite
         int index = 0;
         for(int suit = 0; suit < validSuit.length; suit++) {
            // Maps values to a suite and creates cards with those values
            for(int value = 0; value < validValues.length; value++) {
               masterPack[index] = 
                     new Card(validValues[value], validSuit[suit]);
               index++;
            }
         }
      } 
   }
}

/*********************PHASE 3 OUTPUT********************************** 
 K of spades / Q of spades / J of spades / T of spades /
 9 of spades / 8 of spades / 7 of spades / 6 of spades /
 5 of spades / 4 of spades / 3 of spades / 2 of spades /
 A of spades / K of diamonds / Q of diamonds / J of diamonds /
 T of diamonds / 9 of diamonds / 8 of diamonds / 7 of diamonds /
 6 of diamonds / 5 of diamonds / 4 of diamonds / 3 of diamonds /
 2 of diamonds / A of diamonds / K of clubs / Q of clubs /
 J of clubs / T of clubs / 9 of clubs / 8 of clubs /
 7 of clubs / 6 of clubs / 5 of clubs / 4 of clubs /
 3 of clubs / 2 of clubs / A of clubs / K of hearts /
 Q of hearts / J of hearts / T of hearts / 9 of hearts /
 8 of hearts / 7 of hearts / 6 of hearts / 5 of hearts /
 4 of hearts / 3 of hearts / 2 of hearts / A of hearts /
 K of spades / Q of spades / J of spades / T of spades /
 9 of spades / 8 of spades / 7 of spades / 6 of spades /
 5 of spades / 4 of spades / 3 of spades / 2 of spades /
 A of spades / K of diamonds / Q of diamonds / J of diamonds /
 T of diamonds / 9 of diamonds / 8 of diamonds / 7 of diamonds /
 6 of diamonds / 5 of diamonds / 4 of diamonds / 3 of diamonds /
 2 of diamonds / A of diamonds / K of clubs / Q of clubs /
 J of clubs / T of clubs / 9 of clubs / 8 of clubs /
 7 of clubs / 6 of clubs / 5 of clubs / 4 of clubs /
 3 of clubs / 2 of clubs / A of clubs / K of hearts /
 Q of hearts / J of hearts / T of hearts / 9 of hearts /
 8 of hearts / 7 of hearts / 6 of hearts / 5 of hearts /
 4 of hearts / 3 of hearts / 2 of hearts / A of hearts /
 K of diamonds / T of hearts / 7 of hearts / 4 of diamonds /
 4 of hearts / K of clubs / Q of hearts / 6 of diamonds /
 8 of diamonds / J of spades / A of diamonds / 8 of spades /
 6 of clubs / 8 of spades / 7 of diamonds / 4 of clubs /
 K of diamonds / J of hearts / K of spades / J of spades /
 8 of diamonds / K of clubs / 8 of hearts / 2 of clubs /
 5 of spades / A of clubs / 7 of diamonds / K of spades /
 9 of hearts / 3 of diamonds / Q of diamonds / T of diamonds /
 J of diamonds / 9 of hearts / 9 of spades / 2 of diamonds /
 5 of diamonds / 6 of hearts / 5 of diamonds / 3 of hearts /
 2 of hearts / 4 of spades / 3 of spades / T of spades /
 Q of diamonds / 5 of hearts / J of clubs / 7 of spades /
 K of hearts / 5 of clubs / 2 of spades / 8 of hearts /
 J of clubs / A of clubs / 8 of clubs / 7 of clubs /
 7 of clubs / 6 of hearts / A of hearts / 9 of diamonds /
 2 of diamonds / 7 of spades / J of diamonds / 6 of spades /
 T of hearts / 4 of hearts / K of hearts / 2 of spades /
 T of clubs / Q of clubs / 7 of hearts / 5 of spades /
 4 of spades / 2 of hearts / Q of spades / 2 of clubs /
 J of hearts / Q of clubs / 3 of diamonds / 6 of spades /
 3 of clubs / 9 of clubs / Q of hearts / 4 of diamonds /
 6 of clubs / T of spades / 5 of hearts / 8 of clubs /
 T of clubs / A of spades / A of diamonds / T of diamonds /
 A of spades / 5 of clubs / 3 of spades / 9 of diamonds /
 9 of clubs / 6 of diamonds / Q of spades / 4 of clubs /
 3 of clubs / 9 of spades / 3 of hearts / A of hearts /
 K of spades / Q of spades / J of spades / T of spades /
 9 of spades / 8 of spades / 7 of spades / 6 of spades /
 5 of spades / 4 of spades / 3 of spades / 2 of spades /
 A of spades / K of diamonds / Q of diamonds / J of diamonds /
 T of diamonds / 9 of diamonds / 8 of diamonds / 7 of diamonds /
 6 of diamonds / 5 of diamonds / 4 of diamonds / 3 of diamonds /
 2 of diamonds / A of diamonds / K of clubs / Q of clubs /
 J of clubs / T of clubs / 9 of clubs / 8 of clubs /
 7 of clubs / 6 of clubs / 5 of clubs / 4 of clubs /
 3 of clubs / 2 of clubs / A of clubs / K of hearts /
 Q of hearts / J of hearts / T of hearts / 9 of hearts /
 8 of hearts / 7 of hearts / 6 of hearts / 5 of hearts /
 4 of hearts / 3 of hearts / 2 of hearts / A of hearts /
 Q of spades / 3 of clubs / 6 of spades / T of hearts /
 T of diamonds / 4 of clubs / 6 of clubs / 4 of diamonds /
 K of diamonds / 9 of diamonds / 7 of spades / A of clubs /
 K of hearts / 5 of diamonds / 8 of spades / A of spades /
 8 of diamonds / 5 of spades / 8 of hearts / 8 of clubs /
 A of diamonds / Q of hearts / 2 of spades / Q of diamonds /
 J of spades / T of spades / T of clubs / 4 of hearts /
 J of hearts / 9 of spades / 3 of spades / 5 of clubs /
 Q of clubs / 6 of diamonds / 2 of diamonds / 7 of clubs /
 5 of hearts / J of clubs / 4 of spades / 9 of clubs /
 6 of hearts / 7 of diamonds / 3 of hearts / 2 of clubs /
 2 of hearts / 7 of hearts / 3 of diamonds / K of clubs /
 K of spades / 9 of hearts / J of diamonds / A of hearts /
**********************END OF PHASE 3 OUTPUT ******************/

/*********************PHASE 4 OUTPUT***************************
How many hands? (1-10, please): -1
How many hands? (1-10, please): 0
How many hands? (1-10, please): 11
How many hands? (1-10, please): 100
How many hands? (1-10, please): abc123
How many hands? (1-10, please): 6

Hands from the UNSHUFFLED deck:
Hand = ( K of spades, 7 of spades, A of spades, 8 of diamonds, 
2 of diamonds, 9 of clubs, 3 of clubs, T of hearts, 4 of hearts )

Hand = ( Q of spades, 6 of spades, K of diamonds, 7 of diamonds, 
A of diamonds, 8 of clubs, 2 of clubs, 9 of hearts, 3 of hearts )

Hand = ( J of spades, 5 of spades, Q of diamonds, 6 of diamonds, 
K of clubs, 7 of clubs, A of clubs, 8 of hearts, 2 of hearts )

Hand = ( T of spades, 4 of spades, J of diamonds, 5 of diamonds, 
Q of clubs, 6 of clubs, K of hearts, 7 of hearts, A of hearts )

Hand = ( 9 of spades, 3 of spades, T of diamonds, 4 of diamonds, 
J of clubs, 5 of clubs, Q of hearts, 6 of hearts )

Hand = ( 8 of spades, 2 of spades, 9 of diamonds, 3 of diamonds, 
T of clubs, 4 of clubs, J of hearts, 5 of hearts )


Hands from the SHUFFLED deck:
Hand = ( K of spades, K of diamonds, 4 of hearts, 8 of hearts, 
4 of clubs, K of hearts, 2 of hearts, K of clubs, A of diamonds )

Hand = ( J of hearts, J of clubs, 6 of clubs, 2 of diamonds, 
Q of diamonds, 3 of hearts, 3 of clubs, 4 of spades, A of clubs )

Hand = ( Q of clubs, 6 of hearts, T of diamonds, 7 of hearts, 
T of clubs, Q of hearts, T of hearts, 4 of diamonds, J of diamonds )

Hand = ( 7 of spades, 9 of clubs, 2 of clubs, A of spades, 
5 of hearts, 3 of spades, T of spades, Q of spades, 9 of spades )

Hand = ( 9 of hearts, 9 of diamonds, 6 of diamonds, J of spades, 
8 of clubs, 8 of spades, 6 of spades, 2 of spades )

Hand = ( 7 of diamonds, A of hearts, 3 of diamonds, 7 of clubs, 
5 of clubs, 8 of diamonds, 5 of spades, 5 of diamonds )


**********************END OF PHASE 4 OUTPUT*******************/