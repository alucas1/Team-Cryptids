//Team Cryptids

//Emerald Kunkle
//Bryan Aguiar
//Gabriel De Leon
//Alberto Lucas  

//Optical Barcode Readers and Writers Assignment

//ignore the package thing below, that's how netbeans handle projects
package pkgclass.stuff;

public class BarcodeReader{
    public static void main(String[] args) {
        
        //array to use later-- the spot in the array translate to the ASCII value
        char[] ascii = {
            //1 thru 31 are not actual characters so they return a ! to represent error
            '!','!','!','!','!','!','!','!','!','!','!','!','!','!','!','!','!','!','!','!','!','!','!','!','!','!','!','!','!','!','!','!',
            //32 to 36
            ' ', '!', '\"', '#', '$',
            //37 to 41
            '%','&','`','(',')',
            //42 to 46
            '*','+','\'','-','.',
            //47 to 51
            '/','0','1','2','3',
            //52 to 56
            '4','5','6','7','8',
            //57 to 61
            '9',':',';','<','=',    
            //62 to 66
            '>','?','@','A','B',    
            //67 to 71
            'C','D','E','F','G',    
            //72 to 76
            'H','I','J','K','L',    
            //77 to 81
            'M','N','O','P','Q',    
            //82 to 86
            'R','S','T','U','V',    
            //87 to 91
            'W','X','Y','Z',']',    
            //92 to 96
            '\\',']','^','_','`',    
            //97 to 101
            'a','b','c','d','e',    
            //102 to 106
            'f','g','h','i','j',    
            //107 to 111
            'k','l','m','n','o',    
            //112 to 116
            'p','q','r','s','t',
            //117 to 121
            'u','v','w','x','y',
            //122 to 126
            'z','{','|','}','~'
        };
        
    }
    
////////////////////////////////////////////////////////////////////////////
//////////////////   Phase 1: BarcodeIO                 ////////////////////
////////////////////////////////////////////////////////////////////////////
    
    interface BarcodeIO{
        public boolean scan(BarcodeImage bc);
        public boolean readText(String text);
        public boolean generateImageFromText();
        public boolean translateImageToText();
        public void displayTextToConsole();
        public void displayImageToConsole();
    }

////////////////////////////////////////////////////////////////////////////
//////////////////   Phase 2: BarcodeImage                 /////////////////
////////////////////////////////////////////////////////////////////////////
    
    public class BarcodeImage implements Cloneable{
        public static final int MAX_HEIGHT = 30;
        public static final int MAX_WIDTH = 65;
        
        private boolean[][] imageData;
        
        //default constructor, makes a 2D array of the maximum diamensions and fills it with blanks (false)
        public BarcodeImage(){
            imageData = new boolean[MAX_HEIGHT][MAX_WIDTH];
                for (int r = 0; r < MAX_HEIGHT; r++){
                    for (int c = 0; c < MAX_WIDTH; c++){
                        imageData[r][c] = false;
                    }
                }
        }
        
        //constructor that takes a string array of any size and puts it in a 2D array, and if its smaller than the max size then it gets banished to the lower left corner
        public BarcodeImage(String[] strData){
            //each element of strData reprsents a row for our 2D array
            
            //filled the whole 2D array with 'false' to start
            imageData = new boolean[MAX_WIDTH][MAX_HEIGHT];
                for (int r = 0; r < MAX_HEIGHT; r++){
                    for (int c = 0; c < MAX_WIDTH; c++){
                        imageData[r][c] = false;
                    }
                }
            
            //finds the height spot to being printing the barcode, since it as to be at the bottom left corner
            int startingHeight = MAX_HEIGHT - strData.length;
            //stringSpot will manage our placement in array strData
            int stringSpot = 0;
            
            //starting at our new height, as long as we are below our maximum height, increase the height
            for(int i = startingHeight; i < MAX_HEIGHT; i++){
                //converts the current element into an array of chars to read
                char[] thisRow = strData[stringSpot].toCharArray();
                //loops through our new array of chars, to read each character in the row for a pixel (used in this assignment as a '*' star)
                for(int j = 0; j < thisRow.length; j++){
                    //if a spot in the row indeeds equals a '*' star, save the current row (i) and column (j)
                    if(thisRow[j] == '*')
                        imageData[i][j] = true;
                }
                //increases the stringSpot we look are with each pass; it should be impossible for this value to go out of bounds as long as it stays in this for loop
                stringSpot++;
            }
        }
        
        //my getter (accessor? i think. i know set methods as getter)
        public boolean getPixel(int row, int col){
            if(row < MAX_HEIGHT && col < MAX_WIDTH && row >= 0 && col >= 0)
                return imageData[col][row];
            else
                return false;
            
        }
        
        //my setter (mutator? not sure)
        public boolean setPixel(int row, int col){
            if(row < MAX_HEIGHT && col < MAX_WIDTH && row >= 0 && col >= 0){
                imageData[col][row] = true;
                return imageData[col][row];
            }
            else
                return false;
            
        }
        
        //optional utility method that is *highly* recommended
        private boolean checkSize(String[] data){
            
            if(!(data == null)){
                if(data.length <= MAX_HEIGHT){
                    for(int i = 0; i < data.length; i++){
                        if(data[i].toCharArray().length > MAX_WIDTH)
                            return false; //data element was found to exceed MAX_WIDTH
                    }
                    return true; //data met all size requires 
                }
                else
                    return false; //data array was found to exceed MAX_HEIGHT
            }
            else    
                return false; //data array was found to be null
        }
        
        //optional method that was not highly recommended but here it is anyways
        public void displayToConsole(){
            System.out.println("\n");
            for (int r = 0; r < MAX_HEIGHT; r++){
                    for (int c = 0; c < MAX_WIDTH; c++){
                        System.out.print(imageData[r][c]);
                    }
                    System.out.print("\n");
                }
            System.out.print("\n");
        }
        
        //clone method overriding from cloneable
        @Override
        public Object clone() throws CloneNotSupportedException{
            return (BarcodeImage) super.clone();
        }
    }

////////////////////////////////////////////////////////////////////////////
//////////////////   Phase 3: DataMatrix                   /////////////////
////////////////////////////////////////////////////////////////////////////
    
    public class DataMatrix implements BarcodeIO{
        @Override
        public boolean scan(BarcodeImage bc){return true;}; //ignore return, it's a placement value
        
        @Override
        public boolean readText(String text){return true;}; //ignore return, it's a placement value
        
        @Override
        public boolean generateImageFromText(){return true;}; //ignore return, it's a placement value
        
        @Override
        public boolean translateImageToText(){return true;}; //ignore return, it's a placement value
        
        @Override
        public void displayTextToConsole(){};
        
        @Override
        public void displayImageToConsole(){};
    }
}
        