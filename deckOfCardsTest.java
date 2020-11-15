//Team Cryptids

//Emerald Kunkle
//Bryan Aguiar
//Gabriel De Leon
//Alberto Lucas  

//Deck Of Cards Assignment
//11-11-2020 to 11-17-2020


public class DeckOfCards
{
   public static void main(String[] args) 
   {
      
   }
}

/////////////////////////////////////////////////////////////////////////
/////////////////////     class Card below          /////////////////////
/////////////////////////////////////////////////////////////////////////   
  
class Card
{
   public enum Suit {clubs, diamonds, hearts, spades};
   
   private char value;
   private Suit suit;
   private boolean errorFlag;
   
   //default constructor
   public Card()
   {
      value = 'A';
      suit = Suit.spades;
   }
   
   //constructor if the user instantiates with both value and suit 
   public Card(char value, Suit suit)
   {
      set(value, suit);
   }
   
   //constructor if the user instantiates with suit but not value
   public Card(Suit suit)
   {
      this('A', suit);
   }
   
   //constructor if the user instantiates with value but not suit
   public Card(char value)
   {
      this(value, Suit.spades);
   }
   
   @Override
   public String toString()
   {
      if(errorFlag)
         return "Invalid card! :c\n";
      else
         return getValue() + " of " + getSuit();
   }
   
   //mutator
   public boolean set(char value, Suit suit)
   {
      this.suit = suit;
      this.value = value;
      this.errorFlag = !(isValid(value, suit));
       
      if(errorFlag)
         return false;
      else
         return true;
   }
   //accessor for suit (getter)
   public Suit getSuit()
   {
      return suit;
   }
   
   //accessor for value
   public char getValue()
   {
      return value;
   }
   
   //accessor for errorFlag
   public boolean getErrorFlag()
   {
      return errorFlag;
   }
   
   //checks if card is same as a reference card
   public boolean equals(Card card)
   {
      return (this.suit == card.suit && this.value == card.value);
   }
   
   //check if suit and value are valid values
   private boolean isValid(char value, Suit suit)
   {
      if(suit.equals(suit.clubs) || suit.equals(suit.diamonds) || 
            suit.equals(suit.hearts) || suit.equals(suit.spades))
      {
         if (value == 'A' || value == '2' || value == '3' || value == '4' || 
               value == '5' || value == '6' || value == '7' || value == '8' ||
               value == '9' || value == 'T' || value == 'J' || value == 'Q' ||
               value == 'K')
            {
               return true;
            }
      }
      
      return false;
   }   
/* Test of Card Class   
   public static void main(String[] args)
   {
      System.out.println("I am going to try and make a card, A of spades:");
      Card card1 = new Card('A', Card.Suit.spades);
      System.out.println("My result: " + card1.toString());
        
      System.out.println("I am going to try and make a card, V of spades:");
      Card badCard1 = new Card('V', Card.Suit.spades);
      System.out.println("My result: " + badCard1.toString());
        
      System.out.println("I am going to try and make a card, 4 of hearts:");
      Card card2 = new Card('4', Card.Suit.hearts);
      System.out.println("My result: " + card2.toString());
        
      System.out.println("I am going to try and make a card, F of diamonds:");
      Card card3 = new Card('F', Card.Suit.diamonds);
      System.out.println("My result: " + card3.toString());
        
      System.out.println("I am going to try and make a card, 9 of clubs:");
      Card card4 = new Card('9', Card.Suit.clubs);
      System.out.println("My result: " + card4.toString());
        
      System.out.println("I am going to try and change the first card, A of ");
      System.out.print("spades. I will change the A to a D:\\");
      card1.set('D', card1.getSuit());
      System.out.println("My result: " + card1.toString());
        
      System.out.println("I am going to try and change the second card, V of ");
      System.out.print("spades. I will change the V to a 5:\\");
      badCard1.set('5', badCard1.getSuit());
      System.out.println("My result: " + badCard1.toString());     
   }
*/
}

/////////////////////////////////////////////////////////////////////////
/////////////////////     class Hand below          /////////////////////
/////////////////////////////////////////////////////////////////////////       

class Hand
{   
   public final int MAX_CARDS;
   
   private Card[] myCards;
   private int numCards;
   
   //default constructor
   public Hand()
   {
       MAX_CARDS = 75;
       numCards = 0;
   }
   
   //removes all cards by setting array to null
   public void resetHand()
   {
       System.out.println("I am reseting the hand. Array myCards is equal to " +
             numCards);
       myCards = null;
       numCards = 0;
       System.out.println("Reset complete. Array myCards is now equal to " + 
             numCards);
   }
   
   //this will return true if a card was able to be added, and false otherwise
   public boolean takeCard(Card card)
   {
      //if the added card doesn't make the deck surpass the max, add it. Else, 
      //return false
      if(numCards < 75)
      {
         //makes a temp array of increased size to add the card to
         Card[] moreCards = new Card[numCards + 1];
         //copy over the entire array, and add the card at the end
         for(int i = 0; i <= numCards; i++)
            {
               if(i != numCards)
                  moreCards[i] = myCards[i];
               else
                  moreCards[i] = card;
            }
            //set the deck array to the temp array and return true
            myCards = moreCards;
            numCards++;
           
            return true;
      }
      else
         return false;
   }
   
   //playCard will take the first card element in the array myCards, remove it, 
   //and return it 
   public Card playCard()
   {
       //makes a temp array of decrease size to subtract the top card from
       if(numCards > 0)
       {
           Card[] lessCards = new Card[numCards - 1];
           //take the top card to later return
           Card topCard = myCards[numCards-1];
           //assign each variable in the temp array with the variable one spot 
           //ahead in myCards
           for(int i = 0; i < (numCards-1); i++)
           {
              lessCards[i] = myCards[i];
           }
           //set the deck array to the temp array and return the topcard
           myCards = lessCards;
           numCards--;

           return topCard;
       }
       return new Card('d',Card.Suit.hearts);
   }
   
   //toString does... yeah you guessed it... turns everything into a single 
   //string and returns it
   public String toString()
   {
      String returnString = "Hands contains: ";
      //prints the cards out in a nice and pretty card manner :3
      for(int i = 0; i < numCards; i++)
      {
         if(i%5 == 0)
            returnString = returnString + "\n";
         if(i != numCards-1)
            returnString = returnString + (myCards[i].getValue() + " of " + 
                  myCards[i].getSuit() + ", ");
         else
            returnString = returnString + (myCards[i].getValue() + " of " + 
                  myCards[i].getSuit());
      }
      //lmao what this total break on return... anywho this sends the cards 
      //back as one big ol' string
      return returnString;
   }
   
   //getter for numCards (accessor)
   public int getNumberOfCards()
   {
      return numCards;
   }
   
   //inspectCard gets the card at a specific position in the myCards array
   Card inspectCard(int k)
   {
      //returns card if spot is valid. Else, return a random card with a true
      //errorFlag
         if(k < myCards.length)
            return myCards[k];
         else
            return new Card('D', Card.Suit.diamonds);
   }
   /* Test for Hand Class
   public static void main(String[] args)
   {
      Card explicitCard1 = new Card('A', Card.Suit.clubs);
      Card explicitCard2 = new Card('3', Card.Suit.hearts);
      Card explicitCard3 = new Card('7', Card.Suit.diamonds);
      Card explicitCard4 = new Card('Q', Card.Suit.spades);

      Hand myHand = new Hand();

      int counter = 1;
      boolean lock = true;

      System.out.println("Adding cards to hand until full...");

      while(lock && counter != 75)
      {
         if(counter == 1)
            lock = myHand.takeCard(explicitCard1);
         else if(counter == 2)
            lock = myHand.takeCard(explicitCard2);
         else if(counter == 3)
            lock = myHand.takeCard(explicitCard3);
         else if(counter == 4)
            lock = myHand.takeCard(explicitCard4);
         else if(counter == 5)
            counter = 0;
         counter++;
      }
      
      System.out.println("Hand is now full!\n\nDisplaying hand now.\n" + 
            myHand.toString());

      System.out.println("\nTesting inspectCard():\nGrabbing a card at an " + 
      "invalid spot = " + myHand.inspectCard(100) + "Grabbing a card at a valid"
            + " spot = " + myHand.inspectCard(10));

      System.out.println("\nPlaying all the cards in deck!:");
      counter = 74;
      lock = false;
      Card temp;
      while(!lock && counter >= 0)
      {
         temp = myHand.playCard();
         System.out.print("Playing " + temp.toString() + ", ");
         if(counter%5 == 0)
            System.out.println();
         lock = temp.errorFlag;
         counter--;
      }
      
      System.out.println("\nHand should now me empty. Let me print the hand to "
            + "check: \n" + myHand.toString());

   }
   */
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
      if((numPacks * 52) <= MAX_CARDS )
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
      if((numPacks * 52) <= MAX_CARDS)
      {
         // set the topCard variable to the product of 52 and numPacks.
         // this will be how big our array of cards will be and we allocate
         // space for a topCard amount of cards.
         topCard = numPacks * 52;
         cards = new Card[getTopCard()];
         
         // iterates through the cards array and initialize them to a card in
         // masterPack.
         for(int iterator = 0; iterator < getTopCard(); iterator++)
         {
            // assigns cards in the current iterator to a new card using the
            // values and suit from masterPack. masterPack's iterator is
            // modulo 52 because masterPack is an array of size 52 but cards
            // can be multiples of that.
            cards[iterator] = new Card(masterPack[(iterator % 52)].getValue(),
                  masterPack[(iterator % 52)].getSuit());
         }
      }
   }
   
   // function for shuffling cards
   public void shuffle()
   {
      // iterator starts at the last card which is topCard - 1 and decrements
      // till we've gone through every card
      
      for (int iterator = getTopCard() - 1; iterator > 0; iterator--)
      {
         // uses math.rand but type cast to int since Math.random returns a
         // double. We use topCards value because we are randomizing and want 
         // the number used to be between the amount of cards in the deck.
         int randomizer = (int)(Math.random() * getTopCard());
         // we hold the value in placeHolder and then place the value in
         // the randomizer location into the iterator location. We then put the
         // value in placeholder into where the randomizer is currently located
         // All of this is to swap spaces between cards.
         Card placeHolder = cards[iterator];
         cards[iterator] = cards[randomizer];
         cards[randomizer] = placeHolder;
      }
   }
   
   // function to deal the topCard
   public Card dealCard()
   {
      // checks to see if the topCard is not 0. i.e is the deck empty
      if(getTopCard() > 0)
      {
         // decrement topCard because we are "removing" the card from the deck
         // we decrement first because if the max array is 104 then topCard is
         // 104 and we will run into an error
         topCard--;
         // return the current top Card
         return inspectCard(getTopCard());
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
      if(k < cards.length)
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
      // checks to see if the masterPack is pointing to a null location. If not
      // the function does nothing. Otherwise, it creates a new masterPack of 52
      // cards.
      if(masterPack == null)
      {
         // creates space for 52 new Card objects in masterPack
         masterPack = new Card[52];
         
         // iterates through each card object and assign them a card
         for (int i = 0; i < 52; i++)
         {
            switch (i % 13)
            {
            case 1:
               if ((i / 13) == 0)
                  masterPack[i] = new Card('2', Card.Suit.hearts);
               else if ((i / 13) == 1)
                  masterPack[i] = new Card('2', Card.Suit.clubs);
               else if ((i / 13) == 2)
                  masterPack[i] = new Card('2', Card.Suit.diamonds);
               else
                  masterPack[i] = new Card('2', Card.Suit.spades);
               break;
            case 2:
               if ((i / 13) == 0)
                  masterPack[i] = new Card('3', Card.Suit.hearts);
               else if ((i / 13) == 1)
                  masterPack[i] = new Card('3', Card.Suit.clubs);
               else if ((i / 13) == 2)
                  masterPack[i] = new Card('3', Card.Suit.diamonds);
               else
                  masterPack[i] = new Card('3', Card.Suit.spades);
               break;
            case 3:
               if ((i / 13) == 0)
                  masterPack[i] = new Card('4', Card.Suit.hearts);
               else if ((i / 13) == 1)
                  masterPack[i] = new Card('4', Card.Suit.clubs);
               else if ((i / 13) == 2)
                  masterPack[i] = new Card('4', Card.Suit.diamonds);
               else
                  masterPack[i] = new Card('4', Card.Suit.spades);
               break;
            case 4:
               if ((i / 13) == 0)
                  masterPack[i] = new Card('5', Card.Suit.hearts);
               else if ((i / 13) == 1)
                  masterPack[i] = new Card('5', Card.Suit.clubs);
               else if ((i / 13) == 2)
                  masterPack[i] = new Card('5', Card.Suit.diamonds);
               else
                  masterPack[i] = new Card('5', Card.Suit.spades);
               break;
            case 5:
               if ((i / 13) == 0)
                  masterPack[i] = new Card('6', Card.Suit.hearts);
               else if ((i / 13) == 1)
                  masterPack[i] = new Card('6', Card.Suit.clubs);
               else if ((i / 13) == 2)
                  masterPack[i] = new Card('6', Card.Suit.diamonds);
               else
                  masterPack[i] = new Card('6', Card.Suit.spades);
               break;
            case 6:
               if ((i / 13) == 0)
                  masterPack[i] = new Card('7', Card.Suit.hearts);
               else if ((i / 13) == 1)
                  masterPack[i] = new Card('7', Card.Suit.clubs);
               else if ((i / 13) == 2)
                  masterPack[i] = new Card('7', Card.Suit.diamonds);
               else
                  masterPack[i] = new Card('7', Card.Suit.spades);
               break;
            case 7:
               if ((i / 13) == 0)
                  masterPack[i] = new Card('8', Card.Suit.hearts);
               else if ((i / 13) == 1)
                  masterPack[i] = new Card('8', Card.Suit.clubs);
               else if ((i / 13) == 2)
                  masterPack[i] = new Card('8', Card.Suit.diamonds);
               else
                  masterPack[i] = new Card('8', Card.Suit.spades);
               break;
            case 8:
               if ((i / 13) == 0)
                  masterPack[i] = new Card('9', Card.Suit.hearts);
               else if ((i / 13) == 1)
                  masterPack[i] = new Card('9', Card.Suit.clubs);
               else if ((i / 13) == 2)
                  masterPack[i] = new Card('9', Card.Suit.diamonds);
               else
                  masterPack[i] = new Card('9', Card.Suit.spades);
               break;
            case 9:
               if ((i / 13) == 0)
                  masterPack[i] = new Card('T', Card.Suit.hearts);
               else if ((i / 13) == 1)
                  masterPack[i] = new Card('T', Card.Suit.clubs);
               else if ((i / 13) == 2)
                  masterPack[i] = new Card('T', Card.Suit.diamonds);
               else
                  masterPack[i] = new Card('T', Card.Suit.spades);
               break;
            case 10:
               if ((i / 13) == 0)
                  masterPack[i] = new Card('J', Card.Suit.hearts);
               else if ((i / 13) == 1)
                  masterPack[i] = new Card('J', Card.Suit.clubs);
               else if ((i / 13) == 2)
                  masterPack[i] = new Card('J', Card.Suit.diamonds);
               else
                  masterPack[i] = new Card('J', Card.Suit.spades);
               break;
            case 11:
               if ((i / 13) == 0)
                  masterPack[i] = new Card('Q', Card.Suit.hearts);
               else if ((i / 13) == 1)
                  masterPack[i] = new Card('Q', Card.Suit.clubs);
               else if ((i / 13) == 2)
                  masterPack[i] = new Card('Q', Card.Suit.diamonds);
               else
                  masterPack[i] = new Card('Q', Card.Suit.spades);
               break;
            case 12:
               if ((i / 13) == 0)
                  masterPack[i] = new Card('K', Card.Suit.hearts);
               else if ((i / 13) == 1)
                  masterPack[i] = new Card('K', Card.Suit.clubs);
               else if ((i / 13) == 2)
                  masterPack[i] = new Card('K', Card.Suit.diamonds);
               else
                  masterPack[i] = new Card('K', Card.Suit.spades);
               break;
            default:
               if ((i / 13) == 0)
                  masterPack[i] = new Card('A', Card.Suit.hearts);
               else if ((i / 13) == 1)
                  masterPack[i] = new Card('A', Card.Suit.clubs);
               else if ((i / 13) == 2)
                  masterPack[i] = new Card('A', Card.Suit.diamonds);
               else
                  masterPack[i] = new Card('A', Card.Suit.spades);
            }
         }
      }
   }
/*  
   public static void main(String[] args) 
   {
      Deck myDeck = new Deck(2);
      int myMaxCard = myDeck.getTopCard();
      for (int iterator = 0; iterator < myMaxCard; iterator++)
      {
         System.out.printf(" %s /", myDeck.dealCard().toString());
         
         if((iterator % 4) == 3)
            System.out.println();
      }
      
      System.out.println();
      myDeck.init(2);
      myDeck.shuffle();
      for (int iterator = 0; iterator < myMaxCard; iterator++)
      {
         System.out.printf(" %s /", myDeck.dealCard().toString());
         
         if((iterator % 4) == 3)
            System.out.println();
      }
      
      System.out.println();
      myDeck.init(1);
      myMaxCard = myDeck.getTopCard();
      for (int iterator = 0; iterator < myMaxCard; iterator++)
      {
         System.out.printf(" %s /", myDeck.dealCard().toString());
         
         if((iterator % 4) == 3)
            System.out.println();
      }
      
      System.out.println();
      myDeck.init(1);
      myDeck.shuffle();
      for (int iterator = 0; iterator < myMaxCard; iterator++)
      {
         System.out.printf(" %s /", myDeck.dealCard().toString());
         
         if((iterator % 4) == 3)
            System.out.println();
      }
   }
*/
}

/********************************Output*****************************************

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

 ******************************************************************************/
