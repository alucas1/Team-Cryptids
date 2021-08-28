//Team Cryptids

//Emerald Kunkle
//Bryan Aguiar
//Gabriel De Leon
//Alberto Lucas  

package pkgclass.stuff;

import javax.swing.*;
import java.awt.*;
   
public class Assig5 {
   // static for the 57 icons and their corresponding labels
   // normally we would not have a separate label for each card, but
   // if we want to display all at once using labels, we need to.
   
   static final int NUM_CARD_IMAGES = 57; // 52 + 4 jokers + 1 back-of-card image
   static Icon[] icon = new ImageIcon[NUM_CARD_IMAGES];
    
   //IMPORTANT NOTE: change the sourceFolder string to whatever folder the images are in for your project, or else it won't work
   static final String sourceFolder = "src\\pkgclass\\stuff\\";
   
   static void loadCardIcons(){
      // Build the file names  for all cards inside of an array of Strings to be read
      String[] fileNames = {"2C.gif","2D.gif","2H.gif","2S.gif",    //Declaration for all 2 cards
                            "3C.gif","3D.gif","3H.gif","3S.gif",    //Declaration for all 3 cards
                            "4C.gif","4D.gif","4H.gif","4S.gif",    //Declaration for all 4 cards
                            "5C.gif","5H.gif","5D.gif","5S.gif",    //Declaration for all 5 cards
                            "6C.gif","6D.gif","6H.gif","6S.gif",    //Declaration for all 6 cards
                            "7C.gif","7D.gif","7H.gif","7S.gif",    //Declaration for all 7 cards
                            "8C.gif","8D.gif","8H.gif","8S.gif",    //Declaration for all 8 cards
                            "9C.gif","9D.gif","9H.gif","9S.gif",    //Declaration for all 9 cards
                            "TC.gif","TD.gif","TH.gif","TS.gif",    //Declaration for all 10 cards
                            "JC.gif","JD.gif","JH.gif","JS.gif",    //Declaration for all Jack cards
                            "QC.gif","QD.gif","QH.gif","QS.gif",    //Declaration for all Queen cards
                            "KC.gif","KD.gif","KH.gif","KS.gif",    //Declaration for all King cards
                            "AC.gif","AD.gif","AH.gif","AS.gif",    //Declaration for all Ace cards
                            "XC.gif","XD.gif","XH.gif","XS.gif",    //Declaration for all Joker cards
                            "BK.gif"};                              //Declaration for the back of all cards
      
      // Short loop to create a new ImageIcon based off each filename and then add it to the icon array
      for(int i = 0; i < NUM_CARD_IMAGES; i++)
          //IMPORTANT NOTE: please change the sourceFolder to work
          icon[i] = new ImageIcon(sourceFolder + fileNames[i]);
   }
   
   /**
    * Accepts a integer value and returns a string value corresponding to 
    * the card's type
    * 
    * @param integer between 0 and 13 representing the card's value
    * @return String representing the value of the card
    */
   static String turnIntIntoCardValue(int k){
       // switch statement returns the coresponding card value based on the int k provided
       switch (k) {
           case 0:
               return "A";
           case 1:
               return "2";
           case 2:
               return "3";
           case 3:
               return "4";
           case 4:
               return "5";
           case 5:
               return "6";
           case 6:
               return "7";
           case 7:
               return "8";
           case 8:
               return "9";
           case 9:
               return "T";
           case 10:
               return "J";
           case 11:
               return "Q";
           case 12:
               return "K";
           case 13:
               return "X";
           default:
               break;
       }
       
      return "ERROR";
   }
   
   /**
    * Accepts a integer value and returns a string value corresponding to 
    * the card's suit
    * 
    * @param integer between 0 and 3 representing the card's suit
    * @return String representing the suit of the card
    */
   static String turnIntIntoCardSuit(int j){
      // switch statement returns the coresponding card value based on the int k provided
       switch (j) {
           case 0:
               return "C";
           case 1:
               return "D";
           case 2:
               return "H";
           case 3:
               return "S";
           default:
               break;
       }
       
      return "ERROR";
   }
   
   // a simple main to throw all the JLabels out there for the world to see
   public static void main(String[] args){
      int k;
      
      // prepare the image icon array
      loadCardIcons();
      
      // establish main frame in which program will run
      JFrame frmMyWindow = new JFrame("Card Room");
      frmMyWindow.setSize(1150, 650);
      frmMyWindow.setLocationRelativeTo(null);
      frmMyWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      
      // set up layout which will control placement of buttons, etc.
      FlowLayout layout = new FlowLayout(FlowLayout.CENTER, 5, 20);   
      frmMyWindow.setLayout(layout);
      
      // prepare the image label array
      JLabel[] labels = new JLabel[NUM_CARD_IMAGES];
      
      for (k = 0; k < NUM_CARD_IMAGES; k++)
         labels[k] = new JLabel(icon[k]);
      
      // place your 3 controls into frame
      for (k = 0; k < NUM_CARD_IMAGES; k++)
         frmMyWindow.add(labels[k]);
         
      // show everything to the user
      frmMyWindow.setVisible(true);
   }
}
