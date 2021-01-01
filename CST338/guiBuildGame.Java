/**
 * Group: Team Cryptids
 * Authors: Emerald Kunkle, Bryan Aguiar, Gabriel De Leon, Alberto Lucas
 * Class: CST338
 * Date: 12/08/2020
 *
 * Program Name: assig6
 * Description: Uses the model-controller-view design pattern to implement a
 * game called "BUILD". The model class represents the data of the game, the
 * view is the display, and the controller mediates information between the
 * two.
 */

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Assig6
{
   public static void main(String[] args)
   {
      // Initialize game variables
      int numCardsPerHand = 7;
      int numPlayers = 2;
      int numPacksPerDeck = 1;
      int numJokersPerPack = 2;
      int numUnusedCardsPerPack = 0;
      Card[] unusedCardsPerPack = null;
      boolean setClock = true;

      // Create a cardGameController
      CardGameController cardGameController = new CardGameController(
            "CardTable", unusedCardsPerPack, numPacksPerDeck,
            numJokersPerPack, numPlayers, numCardsPerHand,
            numUnusedCardsPerPack,
            setClock);

      // Deals cards and starts game
      cardGameController.initializeGame();
   }
}

/**
 * Contains methods that control the flow of the game. Contains action listeners
 * that take an action when an event in CardGameView is triggered. Uses
 * CardGameModel to obtain the state of the game.
 */

class CardGameController
{
   // Initialize instance variables
   CardGameView view;
   CardGameModel model;

   /**
    * Constructor that initializes all instance variables
    * 
    * @param view  A CardGameView class that controls the view
    * @param model A CardGameModel class that controls the game data
    */
   public CardGameController(CardGameView view, CardGameModel model)
   {
      this.view = view;
      this.model = model;
   }

   /**
    * Constructor creates CardGameView and CardGameModel with supplied
    * arguments.
    *
    * @param windowName            The name of the window title
    * @param unusedCardsPerPack    Card objects representing unused cards
    * @param packs                 Amount of card packs to use
    * @param jokersPerPack         Amount of jokers per pack of cards
    * @param players               Amount of players
    * @param CardsPerHand          Amount of cards per hand
    * @param numUnusedCardsPerPack number of unused cards per pack
    * @param setClock              enable or disable a timer in the game
    */
   public CardGameController(String windowName, Card[] unusedCardsPerPack,
         int packs, int jokersPerPack, int players,
         int CardsPerHand, int numUnusedCardsPerPack,
         boolean setClock)
   {
      // Create a card game model using provided arguments
      model = new CardGameModel(packs, jokersPerPack, numUnusedCardsPerPack,
            unusedCardsPerPack, players, CardsPerHand);

      // Model filters bad data. Build view using data validated from model
      int validatedNumCardsPerHand = model.getNumCardsPerHand();
      int validatedNumPlayers = model.getNumPlayers();
      view = new CardGameView(windowName, validatedNumCardsPerHand,
            validatedNumPlayers, setClock);

      // enable listeners for clock and cannotPlay buttons
      this.view.toggleClockListener(new ToggleClockListener());
      this.view.cannotPlayPressed(new CannotPlayPressed());
   }

   // Initializes game by assigning cards and creating buttons for user
   public void initializeGame()
   {
      // Deals cards
      model.deal();

      // Set action listeners to buttons in player area
      view.setPlayerButtons(this);
      view.setPlayAreaListeners(this);

      // The computer always goes first. It places a card and gets a new one.
      Card computerFirstCard = model.getHand(0).playCard(0);
      view.playedCardButtons[0].setIcon(view.getIcon(computerFirstCard));
      view.playedCardButtons[0]
            .setActionCommand(0 + "" + computerFirstCard.getValue());
      model.getHand(0).takeCard(model.getCardFromDeck());

      // Displays number of cards remaining to the view
      view.displayCurrentDeckSize(model.getNumCardsRemainingInDeck());
      view.displayText("GAME STARTED!!! YOUR MOVE");

      view.refreshScreen();
   }

   // Disables all buttons when the game is over and displays the score
   public void gameOver()
   {
      view.disableAllButtons();

      // Display end game text
      if (model.score[0] > model.score[1])
      {
         view.displayEndGameText(0, model.getNumCardsRemainingInDeck(),
               model.score[0],
               model.score[1]);
      }
      if (model.score[1] > model.score[0])
      {
         view.displayEndGameText(1, model.getNumCardsRemainingInDeck(),
               model.score[1],
               model.score[0]);
      }
   }

   // Deals cards - initializes a deck of cards
   public void dealCards()
   {
      model.deal();
   }

   // Updates the score in the hand-area of each player
   public void updateScore()
   {
      view.pnlComputerHand.setBorder(
            new TitledBorder("Computer \"Cannot plays\": " + model.score[0]));
      view.pnlHumanHand.setBorder(
            new TitledBorder("Your \"Cannot plays\": " + model.score[1]));
   }

   // Checks if game is over. If so, initialize game over sequence
   public void checkIfGameOver()
   {
      if (model.getNumCardsRemainingInDeck() == 0)
      {
         gameOver();
      }
   }

   // AI for the computer's turn. The computer compares a card in the playing
   // area against all the cards in its hand. If it can play a card, it will
   // play the first one it finds. otherwise it "presses" the cannot play
   // button.
   public void computerTurn()
   {
      // It has to be the computer's turn for it to take its turn
      if (!model.getPlayersTurn())
      {

         // Computer looks at cards in play area
         boolean cardPlayed = false;
         for (int i = 0; i < view.playedCardButtons.length && !cardPlayed; i++)
         {
            // Gets the value of the card in the play area
            String valuesInPlayArea = view.playedCardButtons[i]
                  .getActionCommand();
            String[] buttonData = valuesInPlayArea.split("");
            int playCardValue = cardValueCharToInt(buttonData[1].charAt(0));

            // If button in play area is empty
            if (playCardValue == -1)
            {
               // Play the first card from the hand and assign it to the play
               // area
               Card computerCard = model.getHand(0).playCard(0);

               String newActionString = i + "" + computerCard.getValue();
               view.playedCardButtons[i].setIcon(view.getIcon(computerCard));
               view.playedCardButtons[i].setActionCommand(newActionString);

               // Take a new card from the deck if there are cards remaining
               if (model.getNumCardsRemainingInDeck() > 0)
               {
                  // Take a card from the deck
                  model.getHand(0).takeCard(model.getCardFromDeck());
                  checkIfGameOver();

                  // Refreshes text output in GUI
                  view.displayCurrentDeckSize(
                        model.getNumCardsRemainingInDeck());

                  // Updates game conditions
                  cardPlayed = true;
                  model.previousPlayerCannotPlay = false;
                  model.setPlayersTurn(true);
               }
               else
               {
                  // If there are no more cards, game over
                  gameOver();
               }
            }
            else // If a card is in the play area, use the following logic
            {
               // Computer compares cards in play area to those in hand
               for (int j = 0; j < model.getHand(0).getNumberOfCards()
                     && !cardPlayed; j++)
               {
                  // Set the values that will allow a card to be played
                  int lowerBound = playCardValue - 1;
                  int upperBound = playCardValue + 1;

                  // Inspect the value of the card of the computer
                  int computerCardValue = cardValueCharToInt(
                        model.getHand(0).inspectCard(j).getValue());

                  // If the card is playable in that area
                  if (computerCardValue == lowerBound
                        || computerCardValue == upperBound)
                  {
                     // Play the card
                     Card computerCard = model.getHand(0).playCard(j);

                     // Update the play area with the new card
                     String newActionString = i + "" + computerCard.getValue();
                     view.playedCardButtons[i]
                           .setIcon(view.getIcon(computerCard));
                     view.playedCardButtons[i]
                           .setActionCommand(newActionString);

                     // If there are any remaining cards in deck
                     if (model.getNumCardsRemainingInDeck() > 0)
                     {
                        // Draw a card, check if the game is over, and update
                        // the display
                        model.getHand(0).takeCard(model.getCardFromDeck());
                        checkIfGameOver();
                        view.displayCurrentDeckSize(
                              model.getNumCardsRemainingInDeck());

                     }
                     else // The game is over if no cards remain
                        gameOver();
                     cardPlayed = true;
                     model.previousPlayerCannotPlay = false;
                  }
               }
            }
         }

         // If a card is not played, simulate the press of the "cannot play"
         // button
         if (!cardPlayed)
         {
            // Computer gets a penalty point
            model.score[0]++;
            view.displayText("COMPUTER UNABLE TO MAKE MOVE!!!\n YOUR MOVE");

            // draws 3 cards in the play area
            if (model.previousPlayerCannotPlay)
            {
               // if there are less than 3 cards, game over
               if (model.getNumCardsRemainingInDeck() 
                     < view.playedCardButtons.length)
               {
                  gameOver();
               }
               else
               {
                  // Puts 3 cards into the play area
                  for (int i = 0; i < view.playedCardButtons.length; i++)
                  {
                     Card newCard = model.getCardFromDeck();

                     String newActionString = i + "" + newCard.getValue();
                     view.playedCardButtons[i].setIcon(view.getIcon(newCard));
                     view.playedCardButtons[i]
                           .setActionCommand(newActionString);
                  }

                  // updates display with information
                  view.displayCurrentDeckSize(
                        model.getNumCardsRemainingInDeck());
                  view.displayText(
                        "COMPUTER UNABLE TO MAKE MOVE!!!\n YOUR MOVE");

                  // Update game state
                  model.previousPlayerCannotPlay = false;
               }
            }
            else
            {
               // Update game state
               model.previousPlayerCannotPlay = true;
            }
         }

         // After the computers turn, it is the player's turn
         model.setPlayersTurn(true);
      }
   }

   /**
    * Converts a card value char into its int representation
    *
    * @param cardValue The char representation of the card value
    * @return The integer representation of the card value
    */
   public int cardValueCharToInt(char cardValue)
   {
      // Get valid card values to compare
      char[] validCardValues = Card.valuRanks;

      // Maps char to index location of valuRanks
      for (int i = 0; i < validCardValues.length; i++)
      {
         if (cardValue == validCardValues[i])
         {
            return i;
         }
      }

      // An invalid card value is returned for an invalid card
      return -1;
   }

   /**
    * This class takes an action when a button in the play area is pressed
    */
   public class PlayButtonsPressed implements ActionListener
   {
      /**
       * Checks if a card is playable in the play area. If not, nothing happens
       * until the cannot play button is pressed.
       *
       * @param e A representation of the card in the play area. For example, 01
       *          is the first button (index 0) and a 2 card (value 1).
       */
      public void actionPerformed(ActionEvent e)
      {
         // -1 means the player has not chosen to play a card, so do nothing
         if (model.getIndexOfCardToPlay() != -1)
         {

            // Copy player's card in hand
            Card playerCard = model.getHand(1)
                  .inspectCard(model.getIndexOfCardToPlay());

            // Extract information from action event
            String[] actionCommands = e.getActionCommand().split("");
            int indexOfButtonInPlayArea = Integer.valueOf(actionCommands[0]);
            int valueOfCardInPlayArea = -1;
            if (actionCommands.length == 2)
            {
               valueOfCardInPlayArea = cardValueCharToInt(
                     actionCommands[1].charAt(0));
            }

            // If there is no previous card in the play area
            if (valueOfCardInPlayArea == -1)
            {
               // Remove card from player hand
               model.getHand(1).playCard(model.getIndexOfCardToPlay());

               // Update playArea with user card and correct Action event info
               String newActionString = indexOfButtonInPlayArea + ""
                     + playerCard.getValue();
               view.playedCardButtons[indexOfButtonInPlayArea]
                     .setIcon(view.getIcon(playerCard));
               view.playedCardButtons[indexOfButtonInPlayArea]
                     .setActionCommand(newActionString);

               if (model.getNumCardsRemainingInDeck() > 0)
               {
                  // Take a new card from the deck
                  model.getHand(1).takeCard(model.getCardFromDeck());
                  if (model.getNumCardsRemainingInDeck() == 0)
                     gameOver();
                  view.displayCurrentDeckSize(
                        model.getNumCardsRemainingInDeck());

                  // correct the index of all cards after a card is played
                  for (int i = 0; i < model.getHand(1).getNumberOfCards(); i++)
                  {
                     Card handCard = model.getHand(1).inspectCard(i);
                     view.humanButtons[i].setIcon(view.getIcon(handCard));
                  }

                  // Update game state
                  model.setPlayersTurn(false);
                  model.previousPlayerCannotPlay = false;
               }
               else
               {
                  // if no cards remain, game over
                  gameOver();
               }
            }
            else // there is a card in the specified play area
            {
               // Assign valid card values that can be played
               int lowerBound = valueOfCardInPlayArea - 1;
               int upperBound = valueOfCardInPlayArea + 1;

               // Get the player's card value.
               int playerCardValue = cardValueCharToInt(playerCard.getValue());

               // If the card is playable in the area
               if (playerCardValue == lowerBound
                     || playerCardValue == upperBound)
               {
                  // Play the card from the user's hand
                  model.getHand(1).playCard(model.getIndexOfCardToPlay());

                  // Set the card into the playing area and update the area
                  String newActionString = indexOfButtonInPlayArea + ""
                        + playerCard.getValue();
                  view.playedCardButtons[indexOfButtonInPlayArea]
                        .setIcon(view.getIcon(playerCard));
                  view.playedCardButtons[indexOfButtonInPlayArea]
                        .setActionCommand(newActionString);

                  // Take a new card from the deck
                  if (model.getNumCardsRemainingInDeck() == 0)
                  {
                     // If no cards remain, game over
                     gameOver();
                  }
                  else
                  {
                     // Take a card from the deck
                     model.getHand(1).takeCard(model.getCardFromDeck());

                     checkIfGameOver();

                     // update display
                     view.displayCurrentDeckSize(
                           model.getNumCardsRemainingInDeck());
                  }

                  // correct the index of all cards after a card is played
                  for (int i = 0; i < model.getHand(1).getNumberOfCards(); i++)
                  {
                     Card handCard = model.getHand(1).inspectCard(i);
                     view.humanButtons[i].setIcon(view.getIcon(handCard));
                  }

                  // Update game data
                  model.setPlayersTurn(false);
                  model.previousPlayerCannotPlay = false;
               }
            }

            // Check if game over. if not, its the computer's turn
            checkIfGameOver();
            computerTurn();
            checkIfGameOver();
         }

         // clear card that we got so it is not used for the next round
         model.setIndexOfCardToPlay(-1);
         updateScore();
         view.refreshScreen();
      }
   }

   /**
    * Performs an action when the "I cannot play" button is pressed
    */
   public class CannotPlayPressed implements ActionListener
   {
      @Override
      /**
       * Draws three cards when the cannot play button is pressed
       * 
       * @param f actionevent has its default values passed
       */
      public void actionPerformed(ActionEvent f)
      {
         // Increase the player's penalty score
         model.score[1]++;

         // draw 3 cards in play area if the previous player also didnt play
         if (model.previousPlayerCannotPlay)
         {
            // If there are no cards remaining, game over
            if (model.getNumCardsRemainingInDeck() 
                  <= view.playedCardButtons.length)
            {
               gameOver();
            }
            else
            {
               // Sets 3 cards in the play area
               for (int i = 0; i < view.playedCardButtons.length; i++)
               {
                  Card newCard = model.getCardFromDeck();
                  view.displayCurrentDeckSize(
                        model.getNumCardsRemainingInDeck());

                  String newActionString = i + "" + newCard.getValue();
                  view.playedCardButtons[i].setIcon(view.getIcon(newCard));
                  view.playedCardButtons[i].setActionCommand(newActionString);
               }

               // Updates game state
               model.previousPlayerCannotPlay = false;
            }
         }
         else
         {
            // updates game state
            model.previousPlayerCannotPlay = true;
         }

         // updates game state
         model.setPlayersTurn(false);
         computerTurn();
         updateScore();
         view.refreshScreen();
      }
   }

   /**
    * Performs an action when a button in the player's hand is pressed
    */
   public class HandButtonPressed implements ActionListener
   {
      @Override
      /**
       * Stores the index location of the button pressed in the player's hand.
       *
       * @param e A string that represents the index location of the card
       *          button.
       */
      public void actionPerformed(ActionEvent e)
      {
         // Change string representation of card index into int
         int cardButtonIndex = Integer.valueOf(e.getActionCommand());

         model.setIndexOfCardToPlay(cardButtonIndex);
      }
   }

   /**
    * Action is performed when the clock button is pressed
    */
   private class ToggleClockListener implements ActionListener
   {
      // instance variables
      private boolean playing;
      private Clock clock;

      // Construction that initializes all variables
      public ToggleClockListener()
      {
         playing = true;
         clock = view.getClock();
      }

      @Override
      /**
       * Starts and stops the clock
       * 
       * @param e default event is passed
       */
      public void actionPerformed(ActionEvent e)
      {
         if (playing)
         {
            playing = false;
            view.setClockButtonText("Start Timer");
         }
         else
         {
            playing = true;
            view.setClockButtonText("Stop Timer");
         }
         clock.toggleTimer();
      }
   }
}

class CardGameView extends JFrame
{
   static boolean iconsLoaded = false;
   // Initialize Window labels and buttons
   static JButton cantPlayButton;
   static JButton clockButton;
   static JButton[] humanButtons;
   static JLabel[] computerLabels;
   static JLabel[] playLabelText;
   static JButton[] playedCardButtons;
   static JTextArea gameInfo;

   // Initialize card icon variables
   private static Icon[][] iconCards = new ImageIcon[14][4];
   private static Icon iconBack;
   // Initialize GUI panels
   public JPanel pnlComputerHand;
   public JPanel pnlHumanHand;
   public JPanel pnlPlayArea;
   public JPanel pnlControlArea;
   public JPanel pnlCantPlay;
   public JPanel pnlClock;
   // Initialize instance variables
   private int numCardsPerHand;
   private int numPlayers;
   private Clock clock;
   private boolean setClock;

   // Default constructor calls the constructor with parameters
   CardGameView()
   {
      this("CardTable", 7, 2, true);
   }

   // Constructor with parameters
   // @param title - a string with the title of the GUI
   // @param numCardsPerHand - stores how many cards should be dealt
   // per hand
   // @param numPlayers - int that stores how many players are playing
   // @param setClock - a boolean that checks if a clock is set
   CardGameView(String title, int numCardsPerHand, int numPlayers,
         boolean setClock)
   {
      // Names the program whatever is loaded into title
      super(title);
      this.numCardsPerHand = numCardsPerHand;
      this.numPlayers = numPlayers;
      this.setClock = setClock;

      // Loads Card icons for use
      loadCardIcons();

      // Initialize window labels and buttons
      initializeLabelsAndButtons();

      // Initialized window panels
      initializeWindowArea();
   }

   // Function that disables all buttons when called
   public void disableAllButtons()
   {
      // disable all player buttons
      for (int i = 0; i < humanButtons.length; i++)
      {
         humanButtons[i].setEnabled(false);
      }

      // disable play area buttons
      for (int i = 0; i < playedCardButtons.length; i++)
      {
         playedCardButtons[i].setEnabled(false);
      }

      // disable can't play button
      cantPlayButton.setEnabled(false);
   }

   // accessor function for iconBack
   // @return Icon - returns an Icon object representing the iconBack image
   static public Icon getBackCardIcon()
   {
      return iconBack;
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
      if (card.getErrorFlag())
         return null;

      // returns the specific image using the card value and suit
      return iconCards[valueAsInt(card)][suitAsInt(card)];
   }

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

   // Function that sets the results to textArea at the conclusion of the game
   // @param winner - an int that stores the player who won
   // @param deckSize - an int that stores the current size of the deck
   // @param winnerScore - an int that stores the score of the winner
   // @param loserScore - an int that stores the score of the loser
   public void displayEndGameText(int winner, int deckSize, int winnerScore,
         int loserScore)
   {
      String winnerName = (winner == 1) ? "Player" : "Computer";
      gameInfo.setText(winnerName + " WINS - SCORE: " + winnerScore + " - "
            + loserScore);
      gameInfo.append("\nCurrent Deck Size: " + deckSize + " cards");
      gameInfo.setVisible(true);
   }

   // function that displays current deck size
   // @param deckSize - an int that stores the current size of the deck
   public void displayCurrentDeckSize(int deckSize)
   {
      gameInfo.setText("Current Deck Size: " + deckSize);
   }

   // function that displays a text string passed by caller
   // @param string - a string with a message to be displayed to the user
   public void displayText(String string)
   {
      gameInfo.append("\n" + string);
   }

   // helper function that turns the value of the passed card into an integer
   // @param card - a Card object that represents a playing card
   // @return int - returns the integer value of value of the card object,
   // otherwise
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
   // @return int - returns the integer value of the suit of the card object,
   // otherwise
   // returns -1 if invalid
   static int suitAsInt(Card card)
   {
      // checks to see if the card is valid
      if (card.getErrorFlag())
         return -1;

      // array that stores the suit of each card into an array of suits
      Card.Suit[] suits =
      { Card.Suit.clubs, Card.Suit.diamonds,
            Card.Suit.hearts, Card.Suit.spades, };
      // iterates through the array and uses the current index as the value
      // to return to the user to state the current card suit being looked at
      for (int index = 0; index < suits.length; index++)
         if (suits[index] == card.getSuit())
            return index;

      // if card makes it to this point and doesn't find a matching card return
      // -1
      return -1;
   }

   // helper function that turns an int back into a card suit
   // @param suitIndex - an integer that represents the index of the suit
   // requested
   // @return char - returns the char representing a suit of a card
   private static char intToCardSuit(int suitIndex)
   {
      // an array of CARDSUITS represented as chars
      final char[] CARDSUITS =
      { 'C', 'D', 'H', 'S' };

      // checks if index is valid otherwise return an invalid char
      if (suitIndex >= CARDSUITS.length || suitIndex < 0)
         return 'E';
      else
         return CARDSUITS[suitIndex];
   }

   // helper function similar to intToCardSuit but for the card value
   // @param valueIndex - an integer that represents the index of the value
   // requested
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

   // function that initializes all our labels and buttons and our text area
   // to uneditable by the user
   private void initializeLabelsAndButtons()
   {
      // Initialize Window labels and buttons and text area
      gameInfo = new JTextArea();
      gameInfo.setEditable(false); // makes user unable to edit text area
      cantPlayButton = new JButton("Can't Play");
      clockButton = new JButton("Stop Timer");
      humanButtons = new JButton[numCardsPerHand];
      computerLabels = new JLabel[numCardsPerHand];
      playLabelText = new JLabel[numCardsPerHand];
      playedCardButtons = new JButton[3]; // 3 cards to play
   }

   // function that initializes our Jpanels and gridLayouts
   private void initializeWindowArea()
   {
      // initialize JPanels for separate panels
      pnlComputerHand = new JPanel();
      pnlPlayArea = new JPanel(new GridLayout(1, 3));
      pnlHumanHand = new JPanel();
      pnlControlArea = new JPanel(new GridLayout(3, 1));
      pnlCantPlay = new JPanel();

      // title panel areas
      pnlComputerHand.setBorder(new TitledBorder("Computer Hand"));
      pnlPlayArea.setBorder(new TitledBorder("Playing Area"));
      pnlHumanHand.setBorder(new TitledBorder("Your Hand"));
      pnlControlArea.setBorder(new TitledBorder("Control Area"));
      pnlCantPlay.setBorder(new TitledBorder("Can't Play"));

      // sets the clock if true
      if (setClock)
      {
         clock = new Clock();
         setClockDisplay();
         clock.startTimer();
      }

      // set the border and layout panels according to spec
      add(pnlComputerHand, BorderLayout.NORTH);
      add(pnlPlayArea, BorderLayout.CENTER);
      add(pnlHumanHand, BorderLayout.SOUTH);
      add(pnlControlArea, BorderLayout.EAST);

      // attach panels to the control area for clock/gameInfo/cantPlayButton
      pnlCantPlay.add(cantPlayButton);
      pnlControlArea.add(pnlClock);
      pnlControlArea.add(pnlCantPlay);
      pnlControlArea.add(gameInfo);

      // Set size and close operations
      setSize(900, 700);
      setLocationRelativeTo(null);
      setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

      // Create labels for computer cards
      setComputerLabels();

      // Add buttons to the human panel and labels to the computer panel
      for (int k = 0; k < numCardsPerHand; k++)
      {
         pnlComputerHand.add(computerLabels[k]);
      }

      // Fill playedCardButtons with new and empty labels, then add them
      // to the pnlPlayArea
      int cardsOnTable = 3;
      for (int i = 0; i < cardsOnTable; i++)
      {
         playedCardButtons[i] = new JButton();
         pnlPlayArea.add(playedCardButtons[i]);
      }

      // Display to output
      refreshScreen();
   }

   // function that adds actionlisteners to play area cards with specific action
   // commands
   public void setPlayAreaListeners(CardGameController controller)
   {
      for (int i = 0; i < playedCardButtons.length; i++)
      {
         playedCardButtons[i]
               .addActionListener(controller.new PlayButtonsPressed());
         playedCardButtons[i].setActionCommand(i + "" + 0);
      }
   }

   // accessor for clock that returns clock object
   // @return - returns a clock object
   public Clock getClock()
   {
      return clock;
   }

   // sets the current clock display and adds the stop/start button
   public void setClockDisplay()
   {
      pnlClock = new JPanel();
      pnlClock.setBorder(new TitledBorder("Stopwatch"));
      pnlClock.add(clock);
      pnlClock.add(clockButton);

   }

   // function that sets the buttons for the players hand
   // @param controller - an object that stores variables needed for function
   public void setPlayerButtons(CardGameController controller)
   {
      for (int k = 0; k < numCardsPerHand; k++)
      {
         // Cards in player's hand
         Card newCard = controller.model.getHand(1).inspectCard(k);
         humanButtons[k] = new JButton(getIcon(newCard));
         humanButtons[k].setActionCommand("" + k);
         humanButtons[k].addActionListener(controller.new HandButtonPressed());
         pnlHumanHand.add(humanButtons[k]);
      }
   }

   // function that sets the computers hand area with back icons
   private void setComputerLabels()
   {
      for (int k = 0; k < numCardsPerHand; k++)
      {
         // Back-card icon for computer's hand
         computerLabels[k] = new JLabel(getBackCardIcon());
      }
   }

   // function that refreshes the screen
   public void refreshScreen()
   {
      revalidate();
      repaint();
      setVisible(true);
   }

   // creates the action listener for the clock object button
   public void toggleClockListener(ActionListener l)
   {
      clockButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
      clockButton.addActionListener(l);
   }

   // sets the current text for the clock button when called
   // @return - a boolean upon successful change
   public boolean setClockButtonText(String text)
   {
      if (text == null)
         return false;

      clockButton.setText(text);
      return true;
   }

   // function that creates an action listener for the cantPlayButton
   public void cannotPlayPressed(ActionListener f)
   {
      cantPlayButton.addActionListener(f);
   }
}

class CardGameModel
{
   // Initialize Class variable
   private static final int MAX_PLAYERS = 2;
   private static final int MAX_CARDS_PER_HAND = 8;
   // Instance variables - Track game state
   boolean previousPlayerCannotPlay; // checks if previous player was able to
   // play a card
   int score[];
   int IndexOfCardToPlay;
   // Initialize instance variables
   private int numPlayers;
   private int numPacks; // # standard 52-card packs per deck
   // ignoring jokers or unused cards
   private int numJokersPerPack; // if 2 per pack & 3 packs per deck, get 6
   private int numUnusedCardsPerPack; // # cards removed from each pack
   private int numCardsPerHand; // # cards to deal each player
   // in the game. e.g. pinochle does not
   // use cards 2-8 of any suit
   private Deck deck; // holds the initial full deck and gets
   // smaller (usually) during play
   private Hand[] hand; // one Hand for each player
   private Card[] unusedCardsPerPack; // an array holding the cards not used
   private boolean playersTurn;

   /**
    * Constructor for a new CardGameFramework object
    *
    * @param numPacks              # of standard 52-card packs per deck
    * @param numJokersPerPack      # of jokers to assign to deck
    * @param numUnusedCardsPerPack # of cards removed from each pack
    * @param unusedCardsPerPack    contains card objects removed from each pack
    * @param numPlayers            # of players
    * @param numCardsPerHand       # of cards to deal each player
    */
   public CardGameModel(int numPacks, int numJokersPerPack,
         int numUnusedCardsPerPack, Card[] unusedCardsPerPack,
         int numPlayers, int numCardsPerHand)
   {
      int k;

      // filter bad values
      if (numPacks < 1 || numPacks > 6)
         numPacks = 1;
      if (numJokersPerPack < 0 || numJokersPerPack > 4)
         numJokersPerPack = 0;
      if (numUnusedCardsPerPack < 0 || numUnusedCardsPerPack > 50) // > 1 card
         numUnusedCardsPerPack = 0;
      if (numPlayers < 1 || numPlayers > MAX_PLAYERS)
         numPlayers = 4;
      // one of many ways to assure at least one full deal to all players
      if (numCardsPerHand < 1 ||
            numCardsPerHand > numPacks * (52 - numUnusedCardsPerPack)
                  / numPlayers
            || numCardsPerHand > MAX_CARDS_PER_HAND)
         numCardsPerHand = MAX_CARDS_PER_HAND;

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

      // initialize score array
      this.score = new int[numPlayers];

      // Initialize turns - player always goes first when game is initialized
      previousPlayerCannotPlay = false;
      IndexOfCardToPlay = -1;

      // prepare deck and shuffle
      newGame();
      playersTurn = true;
   }

   /**
    * constructor overload/default for game like bridge
    */
   public CardGameModel()
   {
      this(1, 0, 0, null, 4, 13);
   }

   /**
    * Accessor for playersTurn's value
    *
    * @return the playrsTurn value
    */
   public boolean getPlayersTurn()
   {
      // Returns playersTurn
      return playersTurn;
   }

   // Function that sets playerTurn based on input
   public void setPlayersTurn(boolean turn)
   {
      // Updates playersTurn based on turn's value
      playersTurn = turn;
   }

   /**
    * Accessor for a specific player hand
    *
    * @param k Index location of player's hand
    * @return the player hand at index location k
    */
   public Hand getHand(int k)
   {
      // hands start from 0 like arrays

      // on error return automatic empty hand
      if (k < 0 || k >= numPlayers)
         return new Hand();
      // Returns hand[k]'s value
      return hand[k];
   }

   /**
    * deals a card from the deck
    *
    * @return A random card from the deck
    */
   public Card getCardFromDeck()
   {
      return deck.dealCard();
   }

   /**
    * Accessor for the number of cards remaining in the deck
    *
    * @return the number of cards in the deck
    */
   public int getNumCardsRemainingInDeck()
   {
      return deck.getNumCards();
   }

   /**
    * Accessor for the number of cards each hand will have
    * 
    * @return the number of cards that each hand will have
    */
   public int getNumCardsPerHand()
   {
      return numCardsPerHand;
   }

   /**
    * Accessor for the number of players in the game
    *
    * @return the number of players in the game
    */
   public int getNumPlayers()
   {
      return numPlayers;
   }

   /**
    * resets the game to its initial state
    */
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
         deck.removeCard(unusedCardsPerPack[k]);

      // add jokers
      for (k = 0; k < numPacks; k++)
         for (j = 0; j < numJokersPerPack; j++)
            deck.addCard(new Card('X', Card.Suit.values()[j]));

      // shuffle the cards
      deck.shuffle();
   }

   /**
    * deals cards from deck to all hands
    *
    * @return false if not enough cards for all hands, but deals what it can
    */
   public boolean deal()
   {
      // Declares intagers for loop use
      int k, j;
      // Declares a boolean to return
      boolean enoughCards;

      // clear all hands
      for (j = 0; j < numPlayers; j++)
         hand[j].resetHand();
      // Declare enough hands as true
      enoughCards = true;
      // deals cards from deck to all hands
      for (k = 0; k < numCardsPerHand && enoughCards; k++)
      {
         // Runs as many times as numPlayers
         for (j = 0; j < numPlayers; j++)
            // Checks if there are cards in the deck
            if (deck.getNumCards() > 0)
               /**
                * Takes a card from the deck and adds it to a hand
                */
               hand[j].takeCard(deck.dealCard());
            // Declares enough cards as false and ends the loop
            else
            {
               enoughCards = false;
               break;
            }
      }
      // Returns the value of enoughCards;
      return enoughCards;
   }

   /*
    * Accessor for the index of the card selected
    *
    * @return the index of the card selected
    */
   public int getIndexOfCardToPlay()
   {
      return IndexOfCardToPlay;
   }

   /**
    * General mutator for IndexOfCardToPlay
    *
    * @param indexOfCardInHand The location in the hand of the selected card
    *
    *
    */
   public void setIndexOfCardToPlay(int indexOfCardInHand)
   {
      this.IndexOfCardToPlay = indexOfCardInHand;
   }

   /**
    * Sorts cards by value in all hands
    */
   void sortHands()
   {
      int k;

      for (k = 0; k < numPlayers; k++)
         hand[k].sort();
   }

   /**
    * plays a card from the player's hand
    *
    * @param playerIndex The index location of the player's hand.
    * @param cardIndex   The card index location in the player's hand.
    * @return the card object played.
    */
   Card playCard(int playerIndex, int cardIndex)
   {
      // returns bad card if either argument is bad
      if (playerIndex < 0 || playerIndex > numPlayers - 1 ||
            cardIndex < 0 || cardIndex > numCardsPerHand - 1)
      {
         // Creates a card that does not work
         return new Card('M', Card.Suit.spades);
      }

      // return the card selected for play
      return hand[playerIndex].playCard(cardIndex);

   }

   /**
    * Takes a card from the deck to a hand
    *
    * @param playerIndex The index location of the player's hand
    * @return true if a card was added to the hand, else false
    */
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

/**
 * The Card class represents a playing card with a suit and a value. If card has
 * an invalid suit or value, card is placed in an error state with errorFlag.
 */
class Card
{
   public static char[] valuRanks =
   { 'A', '2', '3', '4', '5', '6', '7', '8',
         '9', 'T', 'J', 'Q', 'K', 'X' };
   // Instance Variables.
   private Suit suit;
   private char value;
   private boolean errorFlag;

   /**
    * Constructor that sets suit and value instance variables.
    *
    * @param value The value of the card
    * @param suit  The suit of the card
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
    * Constructor if the user instantiates with suit but not value.
    *
    * @param suit The suit of the card
    */
   public Card(Suit suit)
   {
      this('A', suit);
   }

   /**
    * Constructor if the user instantiates with value but not suit.
    *
    * @param value The value of a card
    */
   public Card(char value)
   {
      this(value, Suit.spades);
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
      for (int i = 0; i < arraySize - 1; i++)
         for (int j = i + 1; j < arraySize; j++)
            if (CardGameView.valueAsInt(card[i]) > CardGameView
                  .valueAsInt(card[j]))
            {
               Card tempCard;
               tempCard = card[i];
               card[i] = card[j];
               card[j] = tempCard;

            }
   }

   /**
    * Accessor method for suit.
    *
    * @return The suit of the card.
    */
   public Suit getSuit()
   {
      return suit;
   }

   /**
    * Accessor method for value.
    *
    * @return The value of the card.
    */
   public char getValue()
   {
      return value;
   }

   /**
    * Accessor method for errorFlag.
    *
    * @return The errorFlag of the card.
    */
   public boolean getErrorFlag()
   {
      return errorFlag;
   }

   /**
    * General mutator for value and suit. Triggers errorFlag if suit or value
    * create and invalid card. Value and suit are set regardless of validity.
    *
    * @param value The value of the card.
    * @param suit  The suit of the card.
    * @return True if the card is valid, else returns false.
    */
   public boolean set(char value, Suit suit)
   {
      boolean isValidCard = isValid(value, suit);
      errorFlag = !isValidCard; // If card is valid (true), errorFlag is false.

      if (isValidCard)
      {
         this.suit = suit;
         this.value = value;
      }

      return isValidCard;
   }

   /**
    * Checks for card equality by comparing suit and value.
    *
    * @param card A card object that will be compared.
    * @return True if suit and value are the same, else false.
    */
   public boolean equals(Card card)
   {
      return (this.suit == card.suit && this.value == card.value
            && this.errorFlag == card.errorFlag);
   }

   /**
    * Returns a string representation of a card.
    *
    * @return The value and suit of the card if error flag is not raised, else
    *         return a string stating that the card is invalid.
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
    * Helper method - checks if suit and value are valid card arguments.
    *
    * @param value The value of the card.
    * @param suit  The suit of the card.
    * @return True if both suit and value are valid, else return false.
    */
   private boolean isValid(char value, Suit suit)
   {
      // Define the valid values and suits
      char[] validValues =
      { 'X', 'K', 'Q', 'J', 'T',
            '9', '8', '7', '6', '5', '4', '3', '2', 'A' };
      Card.Suit[] validSuits = Card.Suit.values();

      // First loop checks suit.
      for (Suit validSuit : validSuits)
      {
         if (suit == validSuit)
         { // If a valid suit is found, check for a valid value.
            for (char validValue : validValues)
            { // If a valid value is found, return true (valid suit and value)
               if (value == validValue)
                  return true;
            }
         }
      }

      return false;
   }

   // Declares the possible suits of a card.
   public enum Suit
   {
      clubs, diamonds, hearts, spades
   }
}

/**
 * The Hand class represents a hand of playing cards. Array myCards is able to
 * hold MAX_CARDS. numCards describes amount of cards currently in hand.
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
    * Accessor method for numCards.
    *
    * @return Current number of cards in hand.
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
    * Takes a card as an argument and adds it to the hand (myCards).
    *
    * @param card A card object that will be added to this hand
    * @return True if card was successfully added, else false.
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
    * Takes the card element at the cardIndex and return the Card object
    * otherwise return an invalid card
    *
    * @param cardIndex - integer that has the current location of the card in
    *                  regards to hand position.
    * @return A copy of the top card if there are cards in the hand, else null.
    */
   public Card playCard(int cardIndex)
   {
      if (numCards == 0) // error
      {
         // Creates a card that does not work
         return new Card('M', Card.Suit.spades);
      }
      // store the card in the index into a temporary
      // card.
      Card card = myCards[cardIndex];

      // decrement the size of the array by 1
      numCards--;

      // loops through the array shifting the cards over based
      // on the card that was removed
      for (int i = cardIndex; i < numCards; i++)
      {
         myCards[i] = myCards[i + 1];
      }

      // set the last card in the array which should be a duplicate
      // to null
      myCards[numCards] = null;

      // return the stored card
      return card;
   }

   /**
    * A string representation of the cards in the hand.
    *
    * @return A formatted string of the cards in myCards array.
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
    * Checks to see if card at index k in array 'myCards' is valid.
    *
    * @param k The index location to inspect in the myCards array
    * @return A copy of the card at index k in the myCards array, or an invalid
    *         card with a true errorFlag for an invalid index.
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
 * The Deck class represents a deck of playing cards. The deck can hold a max of
 * MAX_CARDS in array 'cards'. topCard is the number of cards in the deck. Array
 * 'masterPack' is used as a blueprint of all the valid cards (suit and value)
 * in a standard 56 card pack.
 */
class Deck
{
   private static Card[] masterPack;
   // Class variables.
   public final int MAX_CARDS = 6 * 56; // 6 packs of 56 cards.
   // Instance variables.
   private Card[] cards;
   private int topCard;
   private int numPacks;

   /**
    * Constructor that initializes all instance and class variables.
    *
    * @param numPacks The amount of 56-card packs to initialize the deck with.
    */
   public Deck(int numPacks)
   {
      allocateMasterPack(); // Initialize array 'masterPack'.
      this.numPacks = numPacks;
      init(numPacks);
   }

   // Parameterless constructor - initiates with 1 pack of cards.
   public Deck()
   {
      this(1);
   }

   /**
    * Helper method - Initializes masterPack array, a blueprint of a standard
    * 56-card pack.
    */
   private static void allocateMasterPack()
   {
      // only runs once when masterPack has not been initialized
      if (masterPack == null)
      {
         // Initialize masterPack
         masterPack = new Card[56];

         // Initialize arrays for valid values and suites.
         char[] validValues =
         { 'X', 'K', 'Q', 'J', 'T',
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

   /**
    * Accessor function for TopCard.
    *
    * @return The amount of cards in the deck.
    */
   public int getTopCard()
   {
      return topCard;
   }

   /**
    * Fills the 'cards' array with cards from the 'masterPack' array. If
    * numPacks creates an invalid number of cards, then initialize the 'cards'
    * array with the default numPacks = 1.
    *
    * @param numPacks The amount of 56-card packs to initialize the deck with.
    */
   public void init(int numPacks)
   {
      // Ensures that amount of cards does not exceed MAX_CARDS or is negative.
      if ((numPacks * 56) < 0 || (numPacks * 56) > MAX_CARDS)
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
    * Deals the topCard in array 'cards' and removes it from the deck.
    *
    * @return A copy of the topmost card, or null if the deck has no cards.
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
    * Checks to see if card at index k in array 'cards' is valid.
    *
    * @param k The index location to inspect in the cards array
    * @return A copy of the card at index k in the cards array, or an invalid
    *         card with a true errorFlag for an invalid index.
    */
   public Card inspectCard(int k)
   {
      if (k < topCard)
         return new Card(cards[k].getValue(), cards[k].getSuit());
      else
         return new Card('D', Card.Suit.diamonds);
   }

   // a function that adds a card to the deck
   // @param card - a card object to be added to the deck
   // @return - returns a boolean if card was succesfully added
   boolean addCard(Card card)
   {
      // checks to see if card exists
      if (card == null)
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
      for (int i = 0; i < topCard; i++)
         if (cards[i].equals(card))
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

// Suppress the serial warning
@SuppressWarnings("serial")
class Clock extends JLabel
{
   // Instance variables
   private Timer timer;
   private String text;

   // constructor for a clock object
   public Clock()
   {
      text = "";
      timer = null;
   }

   // function that creates a timer object and thread object and calls the start
   // function
   public void startTimer()
   {
      timer = new Timer();
      Thread t = new Thread(timer);
      t.start();
   }

   // a clock object function that adds text
   // @param text - a string that is to be set in the setText function
   public void addText(String text)
   {
      this.setText(text);
   }

   // function that calls the timer toggle function
   public void toggleTimer()
   {
      timer.toggleTimer();
   }

   // a private nested class Timer extends Thread
   private class Timer extends Thread
   {
      // instance variables
      private long startTime;
      private long cachedTime;
      private boolean pauseTimer;

      // constructor for a Timer object
      public Timer()
      {
         pauseTimer = false;
         startTime = System.currentTimeMillis();
         cachedTime = 0;
      }

      // overrides the run function from thread and creates the
      // looping function to keep track of the time
      @Override
      public void run()
      {
         while (true)
         {

            long time = timerPaused();

            long sec = time / 1000;
            long min = sec / 60;
            sec = sec % 60;

            text = String.format("%02d:%02d", min, sec);
            addText(text);
         }
      }

      // function that maintains paused status of the clock while pauseTimer is
      // true
      public long timerPaused()
      {

         long time = System.currentTimeMillis();

         while (pauseTimer)
         {
            doNothing(0);
            cachedTime = System.currentTimeMillis() - time;
         }

         return time - startTime - cachedTime;
      }

      // function that modifies the boolean pauseTimer to false or true
      // depending on the current state.
      private void toggleTimer()
      {
         pauseTimer = !pauseTimer;
      }

      // function that puts the timer to sleep based on the time passed
      // @param millis - int that stores the amount to sleep in milliseconds
      private void doNothing(int millis)
      {
         try
         {
            Thread.sleep(millis);
         }
         catch (InterruptedException e)
         {
            System.out.println("Unexpected Interruption");
            System.exit(0);
         }
      }
   }
}