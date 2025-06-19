public class Word{
    private String word; 

    //Intialize word
    public Word(String word){
        this.word = word; 
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

    //Uses .equals to compare two word objects 
    @Override
    public boolean equals(Object other){
        return word.equals(other.getWord());
    }
}