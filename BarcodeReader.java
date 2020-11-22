/*******************************************************************************
 * Group: Team Cryptids
 * Authors: Emerald Kunkle, Bryan Aguiar, Gabriel De Leon, Alberto Lucas
 * Class: CST338
 * Date: 11/24/2020
 *    
 * Program Name: Assig4
 * Description
 * 
 ******************************************************************************/
public class Assig4
{

   public static void main(String[] args) 
   {
      
      String[] sImageIn =
      {
         "                                               ",
         "                                               ",
         "                                               ",
         "     * * * * * * * * * * * * * * * * * * * * * ",
         "     *                                       * ",
         "     ****** **** ****** ******* ** *** *****   ",
         "     *     *    ****************************** ",
         "     * **    * *        **  *    * * *   *     ",
         "     *   *    *  *****    *   * *   *  **  *** ",
         "     *  **     * *** **   **  *    **  ***  *  ",
         "     ***  * **   **  *   ****    *  *  ** * ** ",
         "     *****  ***  *  * *   ** ** **  *   * *    ",
         "     ***************************************** ",  
         "                                               ",
         "                                               ",
         "                                               "

      };      
            
         
      
      String[] sImageIn_2 =
      {
            "                                          ",
            "                                          ",
            "* * * * * * * * * * * * * * * * * * *     ",
            "*                                    *    ",
            "**** *** **   ***** ****   *********      ",
            "* ************ ************ **********    ",
            "** *      *    *  * * *         * *       ",
            "***   *  *           * **    *      **    ",
            "* ** * *  *   * * * **  *   ***   ***     ",
            "* *           **    *****  *   **   **    ",
            "****  *  * *  * **  ** *   ** *  * *      ",
            "**************************************    ",
            "                                          ",
            "                                          ",
            "                                          ",
            "                                          "

      };
     
      BarcodeImage bc = new BarcodeImage(sImageIn);
      //bc.displayToConsole();
      DataMatrix dm = new DataMatrix(bc);
     
      // First secret message
      dm.translateImageToText();
      dm.displayTextToConsole();
      dm.displayImageToConsole();
      
      // second secret message
      bc = new BarcodeImage(sImageIn_2);
      dm.scan(bc);
      dm.translateImageToText();
      dm.displayTextToConsole();
      dm.displayImageToConsole();
      
      // create your own message
      dm.readText("What a great resume builder this is!");
      dm.generateImageFromText();
      dm.displayTextToConsole();
      dm.displayImageToConsole();

   }
}

interface BarcodeIO
{
   public boolean scan(BarcodeImage bc);
   public boolean readText(String text);
   public boolean generateImageFromText();
   public boolean translateImageToText();
   public void displayTextToConsole();
   public void displayImageToConsole();
}

class BarcodeImage implements Cloneable
{
   public static final int MAX_HEIGHT = 30;
   public static final int MAX_WIDTH = 65;
   
   private boolean[][] imageData;
   
   /* default constructor, makes 2D array of the maximum dimensions and fills
    * it with blanks (false)
    */
   public BarcodeImage()
   {
      imageData = new boolean[MAX_HEIGHT][MAX_WIDTH];
      for (int r = 0; r < MAX_HEIGHT; r++)
      {
         for (int c = 0; c < MAX_WIDTH; c++)
         {
            imageData[r][c] = false;
         }
      }
   }
   
   /* constructor that takes a string array of any size and puts it in a 2D
    * array, and if it is smaller than the max size then it gets banished to the
    * lower left corner
    */
   public BarcodeImage(String[] strData)
   {
      // each element of strData represents a row for our 2D array
      
      // filled with whole 2D array with 'false' to start
      this();
    
      /* finds the height spot to begin printing the barcode, since it has to be
       * at the bottom left corner 
       */
      int startingHeight = MAX_HEIGHT - strData.length;
      //stringSpot will manage our placement in array strData
      int stringSpot = 0;
    
      /* starting at our new height, as long as we are below maximum height,
       * increase the height
       */
      for(int i = startingHeight; i < MAX_HEIGHT; i++)
      {
         // converts the current element into an array of chars to read
         char[] thisRow = strData[stringSpot].toCharArray();
         /* loops through our new array of chars, to read each character in the
          * row for a pixel (used in this assignment as a '*' star)
          */
         for(int j = 0; j < thisRow.length; j++)
         {
            /* if a spot in this row indeed equals a '*' star, save the current
             * row (i) and column(j)
             */
            if(thisRow[j] == '*')
               imageData[i][j] = true;
         }
         /* increases the stringSpot we look with each pass; it should be
          * impossible for this value to go out of bounds as long as it stays in
          * this for loop
          */
         stringSpot++;
      }      
   }
   
   // my getter (accessor? i think. i know set methods as getter.)
   public boolean getPixel(int row, int col)
   {
      if(row < MAX_HEIGHT && col < MAX_WIDTH && row >= 0 && col >= 0)
         return imageData[row][col];
      else
         return false;
   }
   
   // my setter (mutator? not sure)
   public boolean setPixel(int row, int col, boolean value)
   {
      if(row < MAX_HEIGHT && col < MAX_WIDTH && row >= 0 && col >= 0)
      {
         imageData[row][col] = value;
         return true;
      }
      else
         return false;
   }
   
   //optional utility method that is *highly* recommended
   private boolean checkSize(String[] data)
   {
      if(!(data == null))
      {
         if(data.length <= MAX_HEIGHT)
         {
            for(int i = 0; i < data.length; i++)
            {
               if(data[i].toCharArray().length > MAX_WIDTH)
                  return false; // data element was found to exceed MAX_WIDTH
            }
            return true; // data met all size requries
         }
         else
            return false; // data array was found to exceed MAX_HEIGHT
      }
      else
         return false; // data array was found to be null
   }
   
   // optional method that was not highly recommended by here it is anyway
   public void displayToConsole()
   {
      System.out.println("\n");
      for(int r = 0; r < MAX_HEIGHT; r++)
      {
         for(int c = 0; c < MAX_WIDTH; c++)
         {
            System.out.print(imageData[r][c] ? '*' : ' ');
         }
         System.out.println();
      }
      System.out.println();    
   }
   
   // clone method overriding from cloneable
   @Override
   public Object clone() throws CloneNotSupportedException
   {
      return (BarcodeImage) super.clone();
   }
   
}
