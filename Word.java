public class Word implements Comparable<Word>{
    private String word; 

    //Intializes word and standarizes it by make it all uppercase 
    public Word(String word){
        this.word = word.toUpperCase(); 
    }

    //Allows access to private word variable
    public String getWord(){
        return word; 
    }

    //Returns the word string stored
    @Override 
    public String toString() {
        return word; 
    }

    //Uses .compareTo to compare two word objects 
    @Override
    public int compareTo(Word other){
        return word.compareTo(other.getWord());
    }
}