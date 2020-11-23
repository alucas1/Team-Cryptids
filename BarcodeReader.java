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

class DataMatrix implements BarcodeIO
{
   public static final char BLACK_CHAR = '*';
   public static final char WHITE_CHAR = ' ';
   
   private BarcodeImage image;
   private String text;
   private int actualWidth;
   private int actualHeight;
   
   /* Default Constructor - constructs an empty image and text-value. Actual width
    * and actual height are zero'd
   */
   public DataMatrix()
   {
      text = "";
      image = new BarcodeImage();
      actualWidth = 0;
      actualHeight = 0;
   }
   
   /* Constructor with an object of the BarcodeImage class. Calls the scan()
    * function to take care of the object and set text to empty
    * @param image -- an object of the BarcodeImage class
    */
   public DataMatrix(BarcodeImage image)
   {
      scan(image);
      text = "";
      
   }
   
   /* Constructor with a string as a parameter which calls the readText()
    * function to store the value into the string text. Space is allocated for
    * image of the size BarcodeImage.
    * @param text. A string to be translated into a BarcodeImage object.
    */
   public DataMatrix(String text)
   {
      image = new BarcodeImage();
      readText(text);
   }
   
   /* Accepts a BarcodeImage object and stores a copy of this image. cleanImage()
    * is also called to shift the object to the lower left corner and prepare
    * for translation. The actualWidth and actualHeight are calculated at this
    * point.
    * @param image. An object of the BarcodeImage class.
    * @return. Returns false if image is empty otherwise returns true
    */
   public boolean scan(BarcodeImage image)
   {
      // tests image to see if null and stop function if it is
      if (image == null)
         return false;
      
      // a try catch according to spec of the clone function.
      try
      {
         this.image = (BarcodeImage) image.clone();
      } catch (CloneNotSupportedException e)
      {
         //left intentionally empty as spec specifies this to remain empty.
      }
      // a call to the cleanImage function to shift object to bottom left
      cleanImage();
      
      // compute the actualWidth and actualHeight using compute functions
      actualWidth = computeSignalWidth();
      actualHeight = computeSignalHeight();
      return true;
   }
   
   /* A mutator for text. Checks to see if empty or exceeds length. If not, then
    * sets DataMatrix.text equal to the string.
    * @param - A string with the text to be stored in the class
    * @return. Returns false if text is null or exceeds MAX_WIDTH. Otherwise, 
    * returns true.
    */
   public boolean readText(String text)
   {
      // checks text if null or exceeds length
      if (text == null || text.length() > BarcodeImage.MAX_WIDTH)
         return false;
      // use this.text to differentiate the two texts
      this.text = text;
      return true;
   }
   
   /* Uses the stored text string in DataMatrix to generate an object of the 
    * BarcodeImage class
    * @return. Returns false there is no stored text otherwise returns true.
    */
   public boolean generateImageFromText()
   {
      // tests text to see if there is anything stored
      if(text == null)
         return false;      
      
      // This pair of for loops creates the Closed Limitation Line on the left
      // and bottom of an image. The Max_Height is lessened by 10 due to 8 for
      // the bytes of the message and two for the top and bottom borders
      for(int y = BarcodeImage.MAX_HEIGHT; y >= BarcodeImage.MAX_HEIGHT - 10;
            y--)
         image.setPixel(y, 0, true);
      for(int x = 0; x < text.length() + 1; x++)
         image.setPixel(BarcodeImage.MAX_HEIGHT - 1, x, true);
      
      // This pair of for loops creates the Open Borderline on the top and right
      // of the image. The math is similar to the ClosedLimitation line.
      for(int x = 0; x < text.length() + 2;x++)
         if(x % 2 == 0)
            image.setPixel(BarcodeImage.MAX_HEIGHT - 10, x, true);
      for(int y = BarcodeImage.MAX_HEIGHT; y >= BarcodeImage.MAX_HEIGHT - 10;y--)
         if (y % 2 == 1)
            image.setPixel(y, text.length() + 1, true);
      
      // stores the current char in the loop
      int readIndex = 0;
      for(int col = 1; col <= text.length(); col++, readIndex++)
      {
         //iterates through each individual char and passes the position to the
         // writeCharToCol() function.
         writeCharToCol(col, text.charAt(readIndex));
      }
      
      // compute the actualWidth and actualHeight and store the values
      actualHeight = computeSignalHeight();
      actualWidth = computeSignalWidth();
      return true;
   }
   
   /* Helper method for generateImageFromText() that converts a char to a binary
    * and assigns true or false to the respective location in the array
    * @param col - an integer that has the current column location in the array
    * @param code - the current ascii value in decimal for the character at the
    *                current location
    * @return - Returns false if the values are empty or past scope. Otherwise,
    *          returns true
    */
   private boolean writeCharToCol(int col, int code)
   {
      // checks parameters to see if valid and not past scope
      if(col == BarcodeImage.MAX_WIDTH || col == 0 || code == 0)
         return false;
      
      // for loop for positions within the image.
      for(int row = BarcodeImage.MAX_HEIGHT - 2; row > BarcodeImage.MAX_HEIGHT -
            10; row--, code /= 2)
      {
         // if modulo is 1 then the value is true
         if(code % 2 == 1)
            image.setPixel(row, col, true);
         // if module is 0 then the value is false
         else
            image.setPixel(row, col, false);
         // break out of the loop once code == 0
         if(code == 0)
            break;
      }
      return true;
   }
   
   /*
    * 
    * @return - Returns false if image is null otherwise returns true
    */
   public boolean translateImageToText()
   {
      // tests if image is stored and not null
      if (this.image == null)
      {
         return false;
      }
      // ensures that text has an empty string before translation
      text = "";
      
      // iterate through 
      for (int i = 1; i < getActualWidth() + 1; i++)
      {
         text += readCharFromCol(i);
      }
      return true;
   }
   
   private char readCharFromCol(int col)
   {
      char output = 0;
      int exp = 0;
      for (int row = BarcodeImage.MAX_HEIGHT - 2; 
         row > BarcodeImage.MAX_HEIGHT - getActualHeight() - 1; row--)
      {
         if (image.getPixel(row, col))
         {
            output += Math.pow(2, exp);
         }
         exp++;
      }
      return output;
   }
   
   /* Displays the message currently stored in our text string
    */
   public void displayTextToConsole()
   {
      System.out.println("Secret Message: " + text);
   }
   
   /* Displays the current Image within our BarcodeImage object.
    */
   public void displayImageToConsole()
   {
      // a for loop to add the top line border that matches spec. actualWidth +
      // 4 because there are or will be two extra borders added to our actual 
      // width 
      for (int col = 0; col < getActualWidth() + 4; col++)
         System.out.print("-");
      System.out.println();
      
      // a for loop that starts above our image and ends at the max_height
      for(int row = BarcodeImage.MAX_HEIGHT - getActualHeight() - 2; row <
            BarcodeImage.MAX_HEIGHT; row++)
      {
         // prints out the side borders
         System.out.print('|');
         // a for loop that prints out a black or white char using a conditional
         // statement.
         for (int col = 0; col < getActualWidth() + 2; col++)
            System.out.print(image.getPixel(row, col) ? BLACK_CHAR : WHITE_CHAR);
         System.out.println('|');
      }
   }

   /* An accessor function for the actualWidth
    * @return - Returns an int with the actualWidth
    */
   public int getActualWidth()
   {
      return actualWidth;
   }
   
   /* An accessor function for the actualHeight
    * @return - Returns an int with the actualHeight
    */
   public int getActualHeight()
   {
      return actualHeight;
   }
   
   /* Computes the true width of the BarcodeImage. Minus the borders
    * @return - Returns an int with the width of the BarcodeImage.
    */
   private int computeSignalWidth() 
   {
      // An iterator that tracks the width of our object
      int width = 0;
      // we start at 1 because we know that i = 0 is just a border
      for (int i = 1; i < BarcodeImage.MAX_WIDTH; i++)
      {
         // we use the bottom border because every value should be true for the
         // full length of the BarcodeImage object
         if (image.getPixel(BarcodeImage.MAX_HEIGHT - 1, i))
         {
            // only increase if getPixel is true
            width++;
         }
      }
      // returns -1 because the last column is a border and not actually the
      // image
      return width - 1;
   }
   
   /* Similar to the computeSignalWidth function but for height
    * @return - Returns an int with the height of the BarcodeImage
    */
   private int computeSignalHeight()
   {
      // An iterator that tracks the height of our object
      int height = 0;
      // We start at the row above the border since the border starts at - 1
      for (int i = BarcodeImage.MAX_HEIGHT - 2; i >= 0; i--)
      {
         // We use the far left border because the values should be true to the
         // top of the image
         if (image.getPixel(i, 0))
         {
            // should only increment height if getPixel() is true
            height ++;
         }
      }
      // returns height - 1 because the array at (0,0) is the top of the border
      // thus we can remove it from calculation
      return height - 1;
   }
   
   /* A function to move the image to the bottom left by calling the 
    * moveImageToLowerLeft() function
    */
   private void cleanImage()
   {
      moveImageToLowerLeft();
   }
   
   /* A function that goes through the array and calculates how far down the 
    * image needs to be moved
    */
   private void moveImageToLowerLeft()
   {
      int col = 0;
      int row = 0;
      boolean tracker = false;
      
      // these for loops traverse through the array until the top left corner is
      // found
      for(col = 0; col < BarcodeImage.MAX_WIDTH; col++)
      {
         for (row = 0; row < BarcodeImage.MAX_HEIGHT; row++)
         {
            tracker = image.getPixel(row, col);
            if (tracker)
               break;
         }
         if (tracker)
            break;
      }
      
      // keep iterating until the bottom left corner is found
      while(tracker)
      {
         tracker = image.getPixel(row, col);
         row++;
      }
      shiftImageLeft(col);
      shiftImageDown(BarcodeImage.MAX_HEIGHT - row);
   }
   
   /* A helper function that shifts the entire image down
    * @param offset - an integer with how much the image needs to be shifted
    * down
    */
   private void shiftImageDown(int offset)
   {
      // test to see if the image needs to be moved
      if (offset == 0) 
      {
         return;
      }
      BarcodeImage tempImage = new BarcodeImage();
      for (int x = 0; x < BarcodeImage.MAX_WIDTH; x++)
      {
         for (int y = 0; y <  BarcodeImage.MAX_HEIGHT; y++)
         {
            tempImage.setPixel(y + offset + 1, x, image.getPixel(y, x));
         }
      }
      image = tempImage;
   }
   
   /* A helper function that shifts the entire image to the left
    * @param offset - an integer with how much the image needs to be shifted 
    * to the left
    */
   private void shiftImageLeft(int offset)
   {
      // test to see if the image needs to be moved at all
      if (offset == 0) 
      {
         return;
      }
      
      
      BarcodeImage tempImage = new BarcodeImage();
      for (int x = 0; x < BarcodeImage.MAX_WIDTH; x++)
      {
         for (int y = 0; y < BarcodeImage.MAX_HEIGHT; y++)
         {
            tempImage.setPixel(y, x - offset, image.getPixel(y, x));
         }       
      }
      image = tempImage;
   }  
}
