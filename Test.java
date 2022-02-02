import java.sql.Array;
import java.util.Scanner;


//THIS CLASS IS FOR TESTING PURPOSES ONLY  THE ACTUAL GAME IS RUN IN THE GAME CLASS
public class Test {
    public static Scanner scanner = new Scanner(System.in);
    //The shuffled deck of 52 cards to draw from
    public static Deck deck = new Deck(52);
    //The game of 9 cards we can create pairs from
    public static Deck game = new Deck(9);
    private static String hint = null;


    public static void main(String[] args) {
       boolean canEnd = false;
       boolean test11 = false;
       int[] deckValues;
       // setUpGame();
       // game.listDeck();
        //emptyDeckTest();
        //deck.listDeck();

//        int test11Num1 = 12;
//        int test11Num2 = 13;
//        int test11Num3 = 13;
//        test11 = isEqualTo11(test11Num1,test11Num2,test11Num3);
         // System.out.println(test11);

//        deckValues = toIntArrayFromObj();
//        canEnd = checkIfGameCanEnd(deckValues);
//        System.out.println(canEnd);
        hint = "bleep bloop";
    int num1 = Character.getNumericValue(readInput('9'));


        System.out.println(num1);
    }

    public static void setUpGame() {
        deck.shuffle();
        for (int i = 0; i < game.myDeck.length; i++) {
            game.myDeck[i] = deck.drawCard();
        }
    }

    public static void emptyDeckTest(){

        System.out.println("START**************************");
        for (int i=0; i < game.myDeck.length; i++){
            game.myDeck[i] = deck.drawCard();
            //System.out.println(deck.myDeck[i]);

        }
        System.out.println("END");
    }

    public static int[] toIntArrayFromObj(){
        //initilize and declare ace value
        int ace = 1;
        //initilze string and int array for deck
        String[] stringDeck = new String[9];
        int[] deckValues = new int[9];
        //for loop to store the card object to a string array and then string array to int array
        for (int i = 0; i< game.myDeck.length; i++){
            //store current card postion i to postion i in string deck
            stringDeck[i] = game.myDeck[i].toString();
            //store the first cvharacter of the string to a char variable
            char myChar = stringDeck[i].charAt(0);
            //System.out.println(myChar);
            //if char is less than 10 store at in array position i
            if (Character.getNumericValue(myChar) < 10){
                deckValues[i] = Character.getNumericValue(myChar);
                //else store letter with chosen values A for 1, K for 12, Q for 13, J for 14
            }else if (myChar == 'A'){
                deckValues[i] = ace;
            }else if (myChar == 'K'){
                deckValues[i] = 11;
            }else if (myChar == 'Q'){
                deckValues[i] = 12;
            }else if (myChar == 'J'){
                deckValues[i] = 14;
            }//end if else
        }
        System.out.println("NUMBERS*******************************");
        for (int i = 0; i< deckValues.length; i++){
            System.out.println(deckValues[i]);
        }
        return deckValues;
    }//end method

    public static boolean isEqualTo11(int i, int j, int k){
        //initilse eleven and extra royal variable
        int ELEVEN = 11;
        int EXTRAROYAL = 37;
        // initialize boolean value to return if equals 11 or royal
        boolean isEleven = false;
        //add the first to ints
        int sum = i + j;
        //if equal to 11 return true
        if (sum == ELEVEN){
            isEleven = true;
        }else{//if not check for extra royal
            int extraRoyalSum = sum + k;
            // if extra royal return true
            if (extraRoyalSum == EXTRAROYAL){
                isEleven = true;
            }else{//if not return false
                isEleven = false;
            }
        }
        return isEleven;
    }//end method

    public static boolean checkIfGameCanEnd(int[] myArray){
        boolean isElevenOrRoyal = false;

        //triple for loop to test all combinations to end game
        for (int i = 0; i< myArray.length - 1; i++){
            for (int j = 0; j<  myArray.length - 1; j++){
                for (int k = 0; k < myArray.length - 1; k++){
                    //boolean value to check and see if it is 11 or royal
                    isElevenOrRoyal = isEqualTo11(myArray[i],myArray[j],myArray[k]);
                        //if true then display hint
                    if (isElevenOrRoyal == true){
                        //if equal to 11 then display a hint based on this then break out of for loop
                        if (i + j == 11){
                            hint = "The values of the cards are: " + i + "and " + j;
                            break;
                        }else{//if not then display a hint based on the royal cards then break out of for loop
                            hint = "It is possible that there is an extra royal";
                            break;
                        }
                    }
                }//end for loop k
                //if hint it not null then break out of next for loop
                if (hint != null){
                    break;
                }
            }//end for loop j
            //if hint it not null then break out of next for loop
            if (hint != null){
                break;
            }
        }//end for loop i
        if (hint == null){
            hint = "There is no hint the game should have ended ";
        }//if true game can end

        return isElevenOrRoyal;
    }//end method


    public static void testDeckfor11s(Deck currentGame) {
        int TempCard;
        int[] TempValDeck = null;
        for (int i = 0; i < currentGame.myDeck.length; i++) {
            TempValDeck[i] = currentGame.myDeck[i].getRankValue();
            System.out.println(TempValDeck[i]);


        }
    }

    public static char readInput(char limit) {

        char selection = 'x';

        while (selection =='h' || selection == 'H'|| selection == 'x'){
            String input = scanner.nextLine();
            char inputChar = input.charAt(0);
        try {

            selection = inputChar;
            if (selection == 'h'|| selection == 'H' && limit =='9'){
                System.out.println(hint);
            }else if (selection > limit) {
                selection = limit ;

            }
        } catch (Exception e) {
            selection = '0';
            System.out.println("you have not entered a number between 1 and 9" +
                    " or have note entered a number at all because of this the game will exit.");
        }
        }


        return selection;
    }


    public static char readInput() {
        char limit= '9';
        char selection = '0';
        String input = scanner.nextLine();

        char inputChar = input.charAt(0);
        try {
            selection = inputChar;

            if (selection > limit) {
                selection = limit ;

            }
        } catch (Exception e) {
            selection = '0';
            System.out.println("you have not entered a number between 1 and 9" +
                    " or have note entered a number at all because of this the game will exit.");
        }


        return selection;
    }


}