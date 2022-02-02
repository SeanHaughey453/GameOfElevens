import java.util.Random;

public class Deck{

    //global parameter which controls the size of the deck at initialisation
    public Card[] myDeck;
    private boolean initialised = false;

    //Constructor for Deck Class
    //takes a deckSize integer parameter, which determines the length of the array of the deck of cards.
    public Deck(int deckSize)
    {
        //setup array based on parameter size
        myDeck = new Card[deckSize];
        //if size = 52, then also add cards to the deck ready to be shuffled
        if (deckSize == 52){
            initializeDeck();
        }

    }

    //function which adds a full set of playing cards to the deck without any duplicates, in ascending order.
    public void initializeDeck(){
        int count = 0;
        for (int i = 0; i < 13; i++){
            for (int j = 0; j <= 3; j++){
                myDeck[count] = new Card(i, j);
                count++;
            }
        }
    }

    //function which shuffles this class's myDeck array of Cards.
    public void shuffle(){
        //create temporary Card variable
        Card temp = null;
        //define a random number
        Random rand = new Random();
        //create a temporary Card array that will store the shuffled deck
        Card[] tempCard = new Card[myDeck.length];
        //loop through myDeck Card array
        for (int i = 0; i < myDeck.length; i++) {
            //get a random number based on the length of myDeck
            int randomShuffle = rand.nextInt(myDeck.length);
            //get the card with that index from tempCard array
            temp = tempCard[randomShuffle];
            //Validate that the selected element does not already contain a card, and randomise the index until finding an element that is empty
            while(temp != null){
                randomShuffle = rand.nextInt(myDeck.length);
                temp = tempCard[randomShuffle];
            }
            //assign the randomly indexed element to be the current loop's index of myArray
            tempCard[randomShuffle] = myDeck[i];
            //reset the temp Card for the next loop
            temp = null;
        }
        //assign the shuffled tempCard deck back to the myDeck global Card array for this class
        myDeck = tempCard;
    }

    //function which draws a card from this Class's myDeck global Card array
    public Card drawCard(){
        //local Card variable which will store the Card we draw from the deck and return from the function
        Card tempCard = null;
        //loop through the deck, starting at the top of the deck
        for (int i = myDeck.length -1; i >= 0; i--)
        {
            //if the current loop's index in the myDeck array contains something, set the return Card to the indexed card
            if (myDeck[i] != null){
                tempCard = myDeck[i];
                //remove this card from the myDeck array so it is not drawn again
                myDeck[i] = null;
                //exit out of the foor loop early so we get the card at the top of the deck
                break;
            }
        }
        //return our card
        return tempCard;
    }

    //Function which writes the Card contents of this class's myDeck global Card array to the console
    //Cards are written to the console with numericly indexed formatting for clarity.
    public void listDeck(){
        //write the context to console
        for (int i = 0; i < myDeck.length; i++)
        { if (myDeck[i] != null) {
            System.out.println((i + 1) + ": " + myDeck[i].toString());
            //System.out.println(myDeck[].shuffle());
        }else
            System.out.println((i + 1) + ": " + "NULL for now");

        }


    }
    public String storeCurrentDeckToString(){
        //declare and initialize stored string
        String storedString = "";
        //write the deck to a variable
        for (int i = 0; i < myDeck.length; i++)
        { if (myDeck[i] != null) {
            storedString +=(i + 1) + ": " + myDeck[i].toString()+ "\n";

        }else
            storedString +=(i + 1) + ": " + "Empty slot\n";

        }
        return storedString;


    }

}
