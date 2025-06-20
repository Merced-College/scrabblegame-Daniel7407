/*
 * Daniel Pulido-Alaniz
 * 6/19/2025
 * Features I added: 
 * - Swap one letter from letter bank
 * - Checking if a word uses only letters from letter bank
 * - Option for multiple players and custom letter bank size 
 */


import java.io.File; 
import java.io.FileNotFoundException; 
import java.util.Scanner; 
import java.util.ArrayList; 
import java.util.Random; 
import java.util.Arrays; 

public class ScrabbleGame{
    private ArrayList<Word> wordList = new ArrayList<>(); 
    private final String lettersList = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"; 

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
    public boolean isWord(Word word){
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
    // If numChars > 26 the list still stays a size of 26
    public char[] genRandomChars(int numChars){
        Random r = new Random(); 
        String tempChars = lettersList; 
        int currentNumOfChars = 0; 

        if(numChars > 26){
            numChars = 26;
        }

        char[] chars = new char[numChars]; 

        // Generate letter and remove it from appearing again
        while (currentNumOfChars < numChars){
            int index = r.nextInt(tempChars.length()); 
            chars[currentNumOfChars] = tempChars.charAt(index);
            tempChars = tempChars.substring(0, index) + tempChars.substring(index + 1);
            currentNumOfChars++; 
        }

        return chars; 
    }

    /*
     * ADDITIONAL FEATURE:
     * This function allows the user to swap one of their letters
     * for a new random letter. 
     */

    public char[] swapOneLetter(char letter, char[] charList){
        int index = -1; 
        String charListString = Arrays.toString(charList); 

        // Find index of letter to swap in charList
        for (int i = 0; i < charList.length; i++){
            if (charList[i] == letter){
                index = i; 
            }
        }

        char newChar = genRandomChars(1)[0];

        //Generate a new letter until a unique one is found 
        while (charListString.indexOf(newChar) != -1){
            newChar = genRandomChars(1)[0];
        }

        // Swap out the old letter for a new one
        charList[index] = newChar; 

        return charList;
    }

    /*
     * ADDITIONAL FEATURE: 
     * This function checks that the user created the word
     * using only letters from their letter bank. Using a letter
     * more than once is allowed. 
     */
    public boolean wereLettersUsed(Word word, char[] charList){
        String charListString = Arrays.toString(charList); 
        for (int i = 0; i < word.getWord().length(); i++){
            // Checks if the current letter is in the word bank
            if (charListString.indexOf(word.getWord().charAt(i)) == -1){
                return false; 
            }
        }

        return true; 
    }

    /*
     * This function takes in a list of words and returns
     * the indices + 1 of the words with the longest length. 
     * It is used to identify winners.
     */
    public ArrayList<Integer> getWinners(Word[] players){
        ArrayList<Integer> winners = new ArrayList<>(); 
        winners.add(1); 

        int longestLength = players[0].getWord().length();
        
        // Iterate through each word and compare it to current
        // largest word length to see if it qualifies as winner 
        for(int i = 1; i < players.length; i++){
            int currLength = players[i].getWord().length(); 
            if (currLength > longestLength){
                longestLength = currLength; 
                winners.clear();
                winners.add(i + 1); 
            } else if (currLength == longestLength){
                winners.add(i + 1); 
            }
        }

        return winners; 
    }

    /*
     * ADDITIONAL FEATURE:
     * This function runs the game and handles invalid words. 
     * This function takes in two parameters which allow the user 
     * to choose how many players will play the game and 
     * how many words they are given. 
     */
    public void runGame(int numOfPlayers, int letterBankSize){
        Scanner scanner = new Scanner(System.in); 
        Word[] wordList = new Word[numOfPlayers]; 
        int currentPlayer = 0; 

        while(numOfPlayers > 0){ // Continue as long as player turns are left 
            System.out.println("Player " + (currentPlayer + 1) + "'s turn.");
            char[] letterBank = genRandomChars(letterBankSize);

            // Print whose turn it is and instructions
            System.out.println("Here is your letter bank: " + Arrays.toString(letterBank));
            System.out.println("You are only allowed to use letters found in your letter bank. Using duplicates are allowed."); 

            // Gives user option to swap out one letter from letter bank
            System.out.print("Do you want to swap a letter? (Y/N): ");
            String answer = scanner.nextLine();
            if (answer.equalsIgnoreCase("Y")){
                System.out.print("Which letter to swap? "); 
                char letter = scanner.nextLine().charAt(0); 
                letterBank = swapOneLetter(letter, letterBank);
                System.out.println("Your new letter bank: " + Arrays.toString(letterBank));
                System.out.println(""); 
            }

            System.out.print("Enter a word: "); 

            Word word = new Word(scanner.nextLine()); 
            System.out.println(""); 

            // Continue prompting user until they give a valid word
            while (!wereLettersUsed(word, letterBank) || !isWord(word)){
                if (!wereLettersUsed(word, letterBank)){
                    System.out.println("You used letters you don't have! Try again. "); 
                } else {
                    System.out.println("Your word is not a valid Scrabble word! Try again."); 
                }

                System.out.print("Enter a word: "); 
                word = new Word(scanner.nextLine()); 
                System.out.println(""); 
            }
            wordList[currentPlayer] = word; 
            currentPlayer++; 
            numOfPlayers--; 
        }

        scanner.close(); 

        // Get winners and display them in terminal 
        ArrayList<Integer> winners = getWinners(wordList); 
        System.out.println("Congratulations to the following players for winning: "); 
        for (int i = 0; i < winners.size(); i++){
            System.out.println(winners.get(i)); 
        }

    }

    public static void main(String[] args){
        Scanner scanner = new Scanner(System.in); 
        int numOfPlayers; 
        int letterBankSize; 

        System.out.print("Enter how many people will be playing: "); 
        numOfPlayers = scanner.nextInt(); 

        System.out.println(""); 
        System.out.print("Enter how large each letter bank should be: "); 
        letterBankSize = scanner.nextInt(); 

        System.out.println(""); 

        ScrabbleGame game = new ScrabbleGame();
        game.runGame(numOfPlayers, letterBankSize); 
    }

}