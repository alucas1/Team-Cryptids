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
   
   public DataMatrix()
   {
      text = null;
      image = new BarcodeImage();
      actualWidth = 0;
      actualHeight = 0;
   }
   public DataMatrix(BarcodeImage image)
   {
      scan(image);
      text = null;
      
   }
   public DataMatrix(String text)
   {
      image = new BarcodeImage();
      readText(text);
   }
   
   public boolean scan(BarcodeImage image)
   {
      if (image == null)
         return false;
      try
      {
         this.image = (BarcodeImage) image.clone();
      } catch (CloneNotSupportedException e)
      {
      }
      cleanImage();
      actualWidth = computeSignalWidth();
      actualHeight = computeSignalHeight();
      return true;
   }
   
   public boolean readText(String text)
   {
      if (text == null || text.length() > BarcodeImage.MAX_WIDTH)
         return false;
      this.text = text;
      return true;
   }
   
   public boolean generateImageFromText()
   {
      if(text == null)
         return false;
      
      int readIndex = 0;
      
      for(int y = BarcodeImage.MAX_HEIGHT; y > BarcodeImage.MAX_HEIGHT - 11;y--)
         image.setPixel(y, 0, true);
      for(int x = 0; x < text.length() + 1; x++)
         image.setPixel(BarcodeImage.MAX_HEIGHT - 1, x, true);
      
      for(int x = 0; x < text.length() + 2;x++)
         if(x % 2 == 0)
            image.setPixel(BarcodeImage.MAX_HEIGHT - 10, x, true);
      for(int y = BarcodeImage.MAX_HEIGHT; y > BarcodeImage.MAX_HEIGHT - 11;y--)
         if (y % 2 == 1)
            image.setPixel(y, text.length() + 1, true);
      
      for(int col = 1; col < text.length() + 1; col++)
      {
         writeCharToCol(col, text.charAt(readIndex));
         readIndex++;
      }
      
      actualHeight = computeSignalHeight();
      actualWidth = computeSignalWidth();
      return true;
   }
   
   private boolean writeCharToCol(int col, int code)
   {
      if(col == BarcodeImage.MAX_WIDTH || col == 0 || code == 0)
         return false;
      for(int row = BarcodeImage.MAX_HEIGHT - 2; row > BarcodeImage.MAX_HEIGHT -
            11; row--)
      {
         if(code % 2 == 1)
            image.setPixel(row, col, true);
         code /= 2;
         if(code == 0)
            break;
      }
      
      return true;
   }
   
   public boolean translateImageToText()
   {
      text = "";
      if (this.image == null)
      {
         return false;
      }
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
   
   public void displayTextToConsole()
   {
      System.out.println("The message is: " + text);
   }
   
   public void displayImageToConsole()
   {
      for (int col = 0; col < getActualWidth() + 4; col++)
         System.out.print("-");
      System.out.println();
      
      for(int row = BarcodeImage.MAX_HEIGHT - getActualHeight() - 2; row <
            BarcodeImage.MAX_HEIGHT; row++)
      {
         System.out.print('|');
         for (int col = 0; col < getActualWidth() + 2; col++)
            System.out.print(image.getPixel(row, col) ? BLACK_CHAR : WHITE_CHAR);
         System.out.println('|');
      }
   }
      
   public int getActualWidth()
   {
      return actualWidth;
   }
   
   public int getActualHeight()
   {
      return actualHeight;
   }
   
   private int computeSignalWidth() 
   {
      int width = 0;
      for (int i = 1; i < BarcodeImage.MAX_WIDTH; i++)
      {
         if (image.getPixel(BarcodeImage.MAX_HEIGHT - 1, i))
         {
            width++;
         }
      }
      return width - 1;
   }
   
   private int computeSignalHeight()
   {
      int height = 0;
      for (int i = BarcodeImage.MAX_HEIGHT - 1; i >= 0; i--)
      {
         if (image.getPixel(i, 0))
         {
            height ++;
         }
      }
      return height - 2;
   }
   
   private void cleanImage()
   {
      int col = 0;
      int row = 0;
      boolean tracker = false;
      
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
      
      while(tracker)
      {
         tracker = image.getPixel(row, col);
         row++;
      }
      
      moveImageToLowerLeft(col, BarcodeImage.MAX_HEIGHT - row);
   }
   
   private void moveImageToLowerLeft(int rowOffSet, int colOffSet)
   {
      shiftImageLeft(rowOffSet);
      shiftImageDown(colOffSet);
   }
   
   private void shiftImageDown(int offset)
   {
      if (offset < 0) 
      {
         return;
      }
      boolean pixelValue;
      BarcodeImage tempImage = new BarcodeImage();
      for (int x = 0; x < BarcodeImage.MAX_WIDTH; x++)
      {
         for (int y = 0; y <  BarcodeImage.MAX_HEIGHT; y++)
         {
            pixelValue = image.getPixel(y, x);
            tempImage.setPixel(y + offset + 1, x, pixelValue);
         }
      }
      image = tempImage;
   }
   
   private void shiftImageLeft(int offset)
   {
      // test to see if the image needs to be moved at all
      if (offset < 0) 
      {
         return;
      }
      boolean pixelValue;
      for (int x = 0; x < BarcodeImage.MAX_WIDTH; x++)
      {
         for (int y = 0; y < BarcodeImage.MAX_HEIGHT; y++)
         {
            pixelValue = image.getPixel(y, x);
            image.setPixel(y, x - offset, pixelValue);
         }
      }
   }  
}
   
