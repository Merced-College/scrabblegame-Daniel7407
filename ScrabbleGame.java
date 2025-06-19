import java.io.File; 
import java.io.FileNotFoundException; 
import java.util.Scanner; 
import java.util.ArrayList; 
import java.util.Random; 

public class ScrabbleGame{
    private ArrayList<Word> wordList = new ArrayList<>(); 
    private final String charsList = "abcdefghijklmnopqrstuvwxyz";

    /*
     * Sets up wordList by processing all the words in 
     * CollinsScrabbleWords_2019.txt and converting them
     * into Word objects
    */

    public ScrabbleGame(){
        try {
            File file = new File("CollinsScrabbleWords_2019.txt");
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()){
                wordList.add(new Word(scanner.nextLine()));
            }
            scanner.close(); 
        } catch (FileNotFoundException error){
            System.out.println("Couldn't find file."); 
        }
    }

    // Uses binary search to check if a word is found in wordList
    public boolean validateWord(Word word){
        int leftBound = 0; 
        int rightBound = wordList.size() - 1;

        while(leftBound <= rightBound){
            int mid = leftBound + (rightBound - leftBound) / 2;
            int comparisonValue = word.compareTo(wordList.get(mid));

            if (comparisonValue == 0){ // word was found if comparisonValue is 0
                return true; 
            } else if (comparisonValue < 0){ // word might be in before middle
                rightBound = mid - 1; 
            } else { // word might be after middle
                leftBound = mid + 1; 
            }
        }

        return false; 
    }

    // Generates a list of unique letters that has numChars 
    // If numChars > 26 the lsit still stays a size of 26
    public char[] genRandomChars(int numChars){
        Random r = new Random(); 
        String tempChars = charsList; 
        int currentNumOfChars = 0; 

        if(numChars > 26){
            numChars = 26;
        }

        char[] chars = new char[numChars]; 

        while (currentNumOfChars < numChars){
            int index = r.nextInt(tempChars.length()); 
            chars[currentNumOfChars] = tempChars.charAt(index);
            tempChars = tempChars.substring(0, index) + tempChars.substring(index + 1);
            currentNumOfChars++; 
        }

        return chars; 
    }

    public static void main(String[] args){
        
    }

}