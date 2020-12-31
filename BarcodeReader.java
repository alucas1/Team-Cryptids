/**
 * Group: Team Cryptids
 * Authors: Emerald Kunkle, Bryan Aguiar, Gabriel De Leon, Alberto Lucas
 * Class: CST338
 * Date: 11/24/2020
 *
 * Program Name: BarcodeReader
 * Description: This program defines two classes and one interface
 * that are able to emulate the behavior of a datamatrix optical scanner.
 * The program accepts a 1D array of strings and transforms it into a 2d
 * image. The 2D array is then parsed to extract the information from it.
 * Each column in the barcode denotes an ASCII char, with each column
 * in the 2D array image representing a byte of data.
 */

public class BarcodeReader
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

/**
 * Defines the I/O and basic methods of any barcode class which might implement
 * it.
 */
interface BarcodeIO
{
   /**
    * Accepts some image, represented as a BarcodeImage and stores a copy of
    * this image. No translation is done here.
    * 
    * @param bc The barcode image that will be stored.
    * @return True if stored successfully, else false.
    */
   public boolean scan(BarcodeImage bc);

   /**
    * Accepts a text string to be eventually encoded in an image. No translation
    * is done here.
    * 
    * @param text The string of text to encode as an image.
    * @return True if successfully set, else false.
    */
   public boolean readText(String text);

   /**
    * Looks at the internal text stored in the implementing class and produces a
    * companion BarcodeImage, internally. After this is called, the implementing
    * object should contain a fully-defined image and text that are in agreement
    * with each other.
    * 
    * @return True if successfully generated, else false.
    */
   public boolean generateImageFromText();

   /**
    * Looks at the internal image stored in the implementing class, and produces
    * a companion text string, internally. After this is called, the
    * implementing object should contain a fully defined image and text that are
    * in agreement with each other.
    * 
    * @return True if successfully set, else false.
    */
   public boolean translateImageToText();

   /**
    * Prints out the text string to the console.
    */
   public void displayTextToConsole();

   /**
    * Prints out the image to the console.
    */
   public void displayImageToConsole();
}

/**
 * This class describes a 2D dot-matrix pattern (imageData). Contains methods
 * for storing, modifying and retrieving the data in a 2D image. This class only
 * manages the image data. Implements cloneable to provide a means for a deep
 * copy.
 */
class BarcodeImage implements Cloneable
{
   // Initialize class variables - Maximum dimensions of barcode
   public static final int MAX_HEIGHT = 30;
   public static final int MAX_WIDTH = 65;

   // Declare instance variables - dotmatrix pattern
   private boolean[][] imageData;

   /**
    * Parameterless constructor - initializes imageData with the maximum
    * dimensions and fills it with blanks (false).
    */
   public BarcodeImage()
   {
      imageData = new boolean[MAX_HEIGHT][MAX_WIDTH];

      // Sets all elements in imageData to false.
      for (int row = 0; row < MAX_HEIGHT; row++)
      {
         for (int col = 0; col < MAX_WIDTH; col++)
         {
            imageData[row][col] = false;
         }
      }
   }

   /**
    * Constructor - takes a 1D string array of and translates it into a 2D
    * boolean array. When translating, False = ' ' and True = '*'. strData must
    * meet size requirements for successful transformation. 1D array is inserted
    * into the bottom-left corner of 2D array.
    * 
    * @param strData The 1D string array that is to be transformed.
    */
   public BarcodeImage(String[] strData)
   {
      this();

      if (checkSize(strData))
      {
         // Sets barcode height with respect to the bottom of the 2D array.
         int height = MAX_HEIGHT - strData.length;

         // Outer loop starts at barcode height and works its way down the
         // 2D matrix, aligning the barcode to the bottom left.
         for (int row = height; row < MAX_HEIGHT; row++)
         {
            // Inner loop translates each char in the 1D string array
            // into its boolean counterpart.
            int arrIndex = row - height;
            for (int col = 0; col < strData[arrIndex].length(); col++)
            {
               if (strData[arrIndex].charAt(col) == '*')
               {
                  imageData[row][col] = true;
               }
            }
            arrIndex++;
         }
      }
   }

   /**
    * Accessor for a specific element in imageData.
    * 
    * @param row The specific row index of the element.
    * @param col The specific column index of the element.
    * @return The state of the element (True or False), or False for invalid
    *         locations.
    */
   public boolean getPixel(int row, int col)
   {
      // Checks if row and col provide a valid index location.
      if (row < MAX_HEIGHT && col < MAX_WIDTH && row >= 0 && col >= 0)
      {
         return imageData[row][col];
      }
      else
      {
         return false;
      }
   }

   /**
    * Mutator for a specific element in imageData.
    * 
    * @param row   The specific row index of the element.
    * @param col   The specific column index of the element.
    * @param value The value that will be set.
    * @return True if successfully set, else false.
    */
   public boolean setPixel(int row, int col, boolean value)
   {
      // Checks if row and col provide a valid index location.
      if (row < MAX_HEIGHT && col < MAX_WIDTH && row >= 0 && col >= 0)
      {
         imageData[row][col] = value;
         return true;
      }
      else
      {
         return false;
      }
   }

   /**
    * Helper function - Checks the incoming data for size or null errors.
    * 
    * @param data The 1D string array that is to be checked.
    * @return True if there are no size or null errors, else false.
    */
   private boolean checkSize(String[] data)
   {
      // Checks if array is null or empty
      if (data == null || data.length == 0)
      {
         return false;
      }

      // Checks if array meets size boundaries
      int height = data.length;
      if (height <= MAX_HEIGHT) // Checks height requirements
      {
         for (int i = 0; i < height; i++) // Checks width requirements
         {
            if (data[i].length() > MAX_WIDTH)
            {
               return false; // Data exceeds MAX_WIDTH.
            }
         }
         return true; // Data met all size requirements.
      }
      else
      {
         return false; // Data exceeds MAX_HEIGHT.
      }

   }

   /**
    * Outputs imageData as a 2D string (False = ' ', True = '*')
    */
   public void displayToConsole()
   {
      for (int row = 0; row < MAX_HEIGHT; row++)
      {
         for (int col = 0; col < MAX_WIDTH; col++)
         {
            System.out.print(imageData[row][col] ? '*' : ' ');
         }
         System.out.println();
      }
   }

   /**
    * Clone method overriding from cloneable
    */
   @Override
   public Object clone() throws CloneNotSupportedException
   {
      BarcodeImage copy = (BarcodeImage) super.clone();

      // Copies 2D array to prevent privacy leak.
      boolean[][] imageDataCopy = new boolean[MAX_HEIGHT][];
      for (int i = 0; i < MAX_HEIGHT; i++)
      {
         imageDataCopy[i] = imageData[i].clone();
      }
      copy.imageData = imageDataCopy;

      return copy;
   }
}

/**
 * This class represents a pseudo Datamatrix data structure (does not contain
 * any error correction or encoding). It contains methods for interpreting a 2D
 * matrix from text to image and vice versa. Implements BarcodeIO to use basic
 * methods for barcode I/O.
 */
class DataMatrix implements BarcodeIO
{
   // Initialize class variables - Datamatrix symbols
   public static final char BLACK_CHAR = '*';
   public static final char WHITE_CHAR = ' ';

   // Declare instance variables
   private BarcodeImage image;
   private String text;
   private int actualWidth;
   private int actualHeight;

   /**
    * Default Constructor - Constructs an empty image (all white) and text
    * value. Actual width and height are set to their defaults (0).
    */
   public DataMatrix()
   {
      image = new BarcodeImage();
      text = "";
      actualWidth = 0;
      actualHeight = 0;
   }

   /**
    * Constructor - Accepts a BarcodeImage object to initialize the "image"
    * instance variable. Other values are initialized to their defaults.
    * 
    * @param image The image to be set.
    */
   public DataMatrix(BarcodeImage image)
   {
      this();
      scan(image); // Verifies and sets the image.
   }

   /**
    * Constructor - Accepts a String object to initialize the "text" instance
    * variable. Other values are initialized to their defaults.
    * 
    * @param text A string to be translated into a BarcodeImage object.
    */
   public DataMatrix(String text)
   {
      this();
      readText(text); // Verifies and sets the text.
   }

   /**
    * Accessor for actualWidth
    * 
    * @return An int representing the actual width of the signal image
    */
   public int getActualWidth()
   {
      return actualWidth;
   }

   /**
    * Accessor for actualHeight
    * 
    * @return An int representing the actual height of the signal image
    */
   public int getActualHeight()
   {
      return actualHeight;
   }

   /**
    * Accepts some image, represented as a BarcodeImage and stores a copy of
    * this image. No translation is done here.
    * 
    * @param image The barcode image that will be stored.
    * @return True if stored successfully, else false.
    */
   public boolean scan(BarcodeImage image)
   {
      // Checks if image is null
      if (image == null)
      {
         return false;
      }

      // Copies the image argument to the image instance variable.
      try
      {
         this.image = (BarcodeImage) image.clone();
      }
      catch (CloneNotSupportedException e)
      {
         // Empty per specifications.
      }

      // Shifts the signal data to the bottom left of the image
      cleanImage();

      // Set actual width and height.
      actualWidth = computeSignalWidth();
      actualHeight = computeSignalHeight();

      return true;
   }

   /**
    * Accepts a text string to be eventually encoded in an image. No translation
    * is done here.
    * 
    * @param text The string of text to encode as an image.
    * @return True if successfully set, else false.
    */
   public boolean readText(String text)
   {
      // Checks if text is null or too long.
      if (text == null || text.length() > BarcodeImage.MAX_WIDTH)
      {
         return false;
      }
      else
      {
         this.text = text;
         return true;
      }
   }

   /**
    * Looks at the stored text and produces a companion BarcodeImage. After this
    * is called, the object contains a fully-defined image and text that are in
    * agreement with each other.
    * 
    * @return True if successfully generated, else false.
    */
   public boolean generateImageFromText()
   {
      // Resets the "canvas" for the image.
      clearImage();

      // Checks if text is null or empty.
      if (text == null || text.isEmpty())
      {
         return false;
      }

      // The Max_Height is reduced by 10, which represents a height of 10 for
      // the signal image with respect to the bottom-left of the image. This
      // provides 8 elements to represent bits, and 2 elements for the
      // limitation lines.
      int signalImageHeight = BarcodeImage.MAX_HEIGHT - 10;

      // These loops creates the Closed Limitation Line on the
      // left and bottom of an image.
      for (int y = BarcodeImage.MAX_HEIGHT; y >= signalImageHeight; y--)
      {
         image.setPixel(y, 0, true);
      }
      for (int x = 0; x < text.length() + 1; x++)
      {
         image.setPixel(BarcodeImage.MAX_HEIGHT - 1, x, true);
      }

      // This pair of loops creates the Open Borderline on the top and
      // right of the signal image. Borderline is created with respect to the
      // bottom-left corner of the image.
      for (int x = 0; x < text.length() + 2; x++)
      {
         if (x % 2 == 0)
         {
            image.setPixel(signalImageHeight, x, true);
         }
      }
      for (int y = BarcodeImage.MAX_HEIGHT; y >= signalImageHeight; y--)
      {
         if (y % 2 == 1)
         {
            image.setPixel(y, text.length() + 1, true);
         }
      }

      // Translates each char in text into a binary column of bits in
      // the signal image.
      int readIndex = 0;
      for (int col = 1; col <= text.length(); col++, readIndex++)
      {
         writeCharToCol(col, text.charAt(readIndex));
      }

      // Set actual width and height.
      actualHeight = computeSignalHeight();
      actualWidth = computeSignalWidth();

      return true;
   }

   /**
    * Helper method - Converts a char to a binary representation and assigns the
    * bits as booleans in the datamatrix image. Char bytes are inserted as
    * columns, with the bottom-most bit being 2^0 and the top 2^7
    * 
    * @param col  An integer that has the current column location in the array
    * @param code The current ASCII value in decimal for the character at the
    *             current location
    * @return Returns false if the values are empty or past scope. Otherwise,
    *         returns true.
    */
   private boolean writeCharToCol(int col, int code)
   {
      // Checks parameters to see if valid and not past scope
      if (col == BarcodeImage.MAX_WIDTH || col == 0 || code == 0)
      {
         return false;
      }

      // Sets positions for writing signal image. Prints from the
      // bottom to the top of the column. Signal height is 10 elements
      // from the bottom (8 elements for bits, 2 for limit lines).
      int signalImageHeight = BarcodeImage.MAX_HEIGHT - actualHeight - 10;
      int signalImageBottom = BarcodeImage.MAX_HEIGHT - 2;
      // code /= 2 deconstructs the binary representation of the char
      for (int row = signalImageBottom; row > signalImageHeight
            && code != 0; row--, code /= 2)
      {
         if (code % 2 == 1)
         {
            // If the code bit = 1, sets value to true
            image.setPixel(row, col, true);
         }
         else
         {
            // If the code bit = 0, sets value to false
            image.setPixel(row, col, false);
         }
      }

      return true;
   }

   /**
    * Looks at the internal image stored and produces a companion text string.
    * After this is called, the object contains a fully defined image and text
    * that are in agreement with each other.
    * 
    * @return True if successfully set, else false.
    */
   public boolean translateImageToText()
   {
      // Ensures that text has an empty string before translation.
      text = "";

      // Tests if image is stored and not null
      if (this.image == null || actualHeight == 0 || actualWidth == 0)
      {
         return false;
      }

      // Shifts the signal data to the bottom left of the image
      cleanImage();

      // Translates each column in the signal image into a char.
      for (int i = 1; i < actualWidth + 1; i++)
      {
         text += readCharFromCol(i);
      }
      return true;
   }

   /**
    * Reads each column in the signal image and translates the byte of data into
    * its equivalent char value.
    * 
    * @param col An integer that has the current column location in the array
    * @return A char representing translated from the column of bits.
    */
   private char readCharFromCol(int col)
   {
      char translatedCharacter = 0;
      int exponent = 0; // Exponent of 2^n

      // Goes through the defined column in the signal image. Ignores closed
      // limitation line and open border line.
      int signalImageHeight = BarcodeImage.MAX_HEIGHT - actualHeight - 1;
      int signalImageBottom = BarcodeImage.MAX_HEIGHT - 2;
      for (int row = signalImageBottom; row > signalImageHeight; row--)
      {
         // If a True value is found, add its binary value to char.
         if (image.getPixel(row, col))
         {
            translatedCharacter += Math.pow(2, exponent);
         }
         exponent++;
      }

      return translatedCharacter;
   }

   /**
    * Prints out the text string to the console.
    */
   public void displayTextToConsole()
   {
      System.out.println("Secret Message: " + text);
   }

   /**
    * Prints out the image to the console.
    */
   public void displayImageToConsole()
   {
      // Checks if image is valid for output.
      if (image != null & actualHeight != 0 & actualWidth != 0)
      {
         // Adds the top line '-' border that matches spec.
         for (int col = 0; col < actualWidth + 4; col++)
         {
            System.out.print("-");
         }
         System.out.println();

         // Printing starts at the top of signal image and ends at the
         // max_height, the bottom of the image.
         int signalImageHeight = BarcodeImage.MAX_HEIGHT - actualHeight - 2;
         for (int row = signalImageHeight; row < BarcodeImage.MAX_HEIGHT; row++)
         {
            // Left border
            System.out.print('|');

            // Translates boolean image to a String image using '*' for
            // true and ' ' for false.
            for (int col = 0; col < actualWidth + 2; col++)
            {
               System.out
                     .print(image.getPixel(row, col) ? BLACK_CHAR : WHITE_CHAR);
            }

            // Right border
            System.out.println('|');
         }
      }
   }

   /**
    * Computes the true width of the BarcodeImage, minus the borders
    * 
    * @return An int with the width of the BarcodeImage.
    */
   private int computeSignalWidth()
   {
      int width = 0;

      // We use the bottom row closed limitation line to count width.
      // When a whitespace is encountered, the count ends.
      while (image.getPixel(BarcodeImage.MAX_HEIGHT - 1, width))
      {
         width++;
      }

      // Subtract 2 to ignore open borderline and closed limitation line.
      // If width <= 2, then no count was done or there is no data within
      // the limitation lines.
      if (width <= 2)
      {
         return 0;
      }
      else
      {
         return width - 2;
      }
   }

   /**
    * Computes the true height of the BarcodeImage, minus the borders
    * 
    * @return Returns an int with the height of the BarcodeImage
    */
   private int computeSignalHeight()
   {
      int bottomRowIndex = BarcodeImage.MAX_HEIGHT - 1;
      int height = 0;

      // Checks each position in the first column, bottom to top. Counts each
      // * as a height, and when a space(false) is encountered, stops.
      while (image.getPixel(bottomRowIndex, 0))
      {
         bottomRowIndex--;
         height++;
      }

      // Subtract 2 to ignore open borderline and closed limitation line.
      // If height <= 2, then no count was done or there is no data within
      // the limitation lines.
      if (height <= 2)
      {
         return 0;
      }
      else
      {
         return height - 2;
      }
   }

   /**
    * A function to move the signal image to the bottom left of the image
    */
   private void cleanImage()
   {
      moveImageToLowerLeft();
   }

   /**
    * A function that goes through the array and calculates how far down the
    * image needs to be moved
    */
   private void moveImageToLowerLeft()
   {
      int col = 0;
      int row = 0;
      boolean tracker = false;

      // These loops traverse through the array until the top left corner
      // is found. The column location is saved for shifting left.
      for (col = 0; col < BarcodeImage.MAX_WIDTH; col++)
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

      // This loop finds the bottom left corner and saves the row location.
      // The row location is used for shifting down.
      while (tracker)
      {
         tracker = image.getPixel(row, col);
         row++;
      }

      // After finding the corners, shift the signal image to the bottom left
      shiftImageLeft(col);
      shiftImageDown(BarcodeImage.MAX_HEIGHT - row);
   }

   /**
    * A helper function that shifts the entire image down.
    * 
    * @param offset An integer with how much the image needs to be shifted down.
    */
   private void shiftImageDown(int offset)
   {
      // Checks if offset is a valid value
      if (offset != 0)
      {
         BarcodeImage tempImage = new BarcodeImage();

         // Shifts image down by offset value
         for (int row = 0; row < BarcodeImage.MAX_WIDTH; row++)
         {
            for (int col = 0; col < BarcodeImage.MAX_HEIGHT; col++)
            {
               tempImage.setPixel(col + offset + 1, row,
                     image.getPixel(col, row));
            }
         }
         image = tempImage;
      }
   }

   /**
    * A helper function that shifts the entire image to the left.
    * 
    * @param offset An integer with how much the image needs to be shifted to
    *               the left.
    */
   private void shiftImageLeft(int offset)
   {
      // Test to see if the image needs to be moved at all
      if (offset != 0)
      {
         BarcodeImage tempImage = new BarcodeImage();

         // Shifts image left by offset value.
         for (int row = 0; row < BarcodeImage.MAX_WIDTH; row++)
         {
            for (int col = 0; col < BarcodeImage.MAX_HEIGHT; col++)
            {
               tempImage.setPixel(col, row - offset,
                     image.getPixel(col, row));
            }
         }
         image = tempImage;
      }
   }

   /**
    * Helper function - Clears the current image
    */
   private void clearImage()
   {
      this.image = new BarcodeImage();
   }
}

/********************TEST OUTPUT****************************
Secret Message: CSUMB CSIT online program is top notch.
-------------------------------------------
|* * * * * * * * * * * * * * * * * * * * *|
|*                                       *|
|****** **** ****** ******* ** *** *****  |
|*     *    ******************************|
|* **    * *        **  *    * * *   *    |
|*   *    *  *****    *   * *   *  **  ***|
|*  **     * *** **   **  *    **  ***  * |
|***  * **   **  *   ****    *  *  ** * **|
|*****  ***  *  * *   ** ** **  *   * *   |
|*****************************************|
Secret Message: You did it!  Great work.  Celebrate.
----------------------------------------
|* * * * * * * * * * * * * * * * * * * |
|*                                    *|
|**** *** **   ***** ****   *********  |
|* ************ ************ **********|
|** *      *    *  * * *         * *   |
|***   *  *           * **    *      **|
|* ** * *  *   * * * **  *   ***   *** |
|* *           **    *****  *   **   **|
|****  *  * *  * **  ** *   ** *  * *  |
|**************************************|
Secret Message: What a great resume builder this is!
----------------------------------------
|* * * * * * * * * * * * * * * * * * * |
|*                                    *|
|***** * ***** ****** ******* **** **  |
|* ************************************|
|**  *    *  * * **    *    * *  *  *  |
|* *               *    **     **  *  *|
|**  *   * * *  * ***  * ***  *        |
|**      **    * *    *     *    *  * *|
|** *  * * **   *****  **  *    ** *** |
|**************************************|
 *****************END TEST OUTPUT**************************/