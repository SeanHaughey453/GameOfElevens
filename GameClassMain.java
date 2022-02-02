
import java.util.Scanner;

public class GameClassMain {
        //Scanner which will allow us to read in user input
        public static Scanner scanner = new Scanner(System.in);

        //The shuffled deck of 52 cards to draw from
        public static Deck deck = new Deck(52);
        //The game of 9 cards we can create pairs from
        public static Deck game = new Deck(9);
        //Create queue for storing moves of each attempt
        public static Queue<String> queue = new Queue<String>();
        //Global variables to store the selected cards
        public static Card firstCard = null;
        public static Card secondCard = null;
        public static Card extraRoyalCard = null;
        //Global variables to stored the positions of those cards in the game Deck
        public static int firstCardPosition = -1;
        public static int secondCardPosition = -1;
        public static int extraRoyalCardPosition = -1;
        public static int numOfLoops = 0;
        private static String hint = null;
        public static String storedMove = "";
        public static int[] deckValues = null;
        public static boolean canEnd = false;
        public static boolean watchReplay = false;
        public static boolean playingDemo = false;


        //MAIN
        public static void main(String[] args) {
            System.out.println("Welcome to my game of 11's!!");
            System.out.println("The aim of the game is to match cards values up to 11 or an extra royal");
            beginGameMenu();
            //Get player input about starting game or demo and parse it

            boolean playingGame = handleGameMenu();
            //if playing demo is true then continue to play the demo
            if (playingDemo == true){
                Demo.demoMode();
                //prompt the user if they would wish to continue from the demo
                System.out.println("Would you like to continue? 1 for yes anything else for no");
                char conFromDemo = readInput('2');
                if (conFromDemo == '1'){
                    playingGame = true;
                    System.out.println("You will now begin the game");
                }

            }

            //If player selected to play game, do rest of code.  Else, game and main function finish.
            if (playingGame) {
                //Shuffle deck
                setUpGame(game, deck);

                //loop while playingGame boolean is true.  If player wins, quits, or enters a fail state where they cannot continue, then this boolean will be set to false and the loop will no longer continue
                while (playingGame) {
                    //method- when the deck is less than 9 add card to make it 9
                    addCardToGame(game, deck);
                    //converts the deck of 9 (game) to an int array
                    deckValues = toIntArrayFromObj(game);
                    //check to see if the game can end
                    canEnd = checkIfGameCanEnd(deckValues);
                    //if the games deck of 9 cannot have any more 11's or royals then end
                    if (!canEnd){
                        //if can end is true and the game hasnt been played then reset the decks and try again
                        if (numOfLoops == 0){
                            //reset the game and try again
                            playingGame = true;
                            deck = new Deck(52);
                            game = new Deck(9);
                            clearStoredCardDetails();
                            //Shuffle deck
                            setUpGame(game, deck);

                            System.out.println("Auto Lost so game restarted");

                        }else{//else play the game
                            System.out.println("The game will now end your deck of 9 has ran out of cards that equal 11 or an extra royal...");
                            if (!isDeckEmpty(game)){//this won the game will never actually be displayed because when the game is won because playing game will be true and it wont reloop to here
                                System.out.println("You have won the game!!!!");
                            }else {
                                System.out.println("You have lost!!!");
                            }
                            playingGame = false;
                            watchReplay = false;
                        } //end inner if
                    }//end outer iff
                    else {
                        numOfLoops++;
                        //Begin turn, list the contents of the game Deck, then take a new line
                        System.out.println("-------CURRENT FIELD-------");
                        storedMove = "-------CURRENT FIELD-------\n-----Attempt " + numOfLoops + "----------\n";

                        game.listDeck();
                        storedMove += game.storeCurrentDeckToString();

                        //Prompt player to select their first card.
                        System.out.println("Please select your first card, H for a hint, or select 0 to exit the game:");
                        int firstSelection = Character.getNumericValue(readInput('9')) ;//TESTING

                        //Validate input is a correct value
                        if (firstSelection == 0 || firstSelection == -1) {
                            //if null or 0, break out of the while loop early and exits game
                            break;
                        }
                        handleFirstSelection(firstSelection, game);
                        storedMove += "First Move: " + firstCard.toString() + "\n";
                        System.out.println("Your chosen card is: " + firstCard.toString());

                        //Prompt player to select their second card.
                        System.out.println("Please select your second card, H for a hint, or select 0 to exit the game:");
                        int secondSelection = Character.getNumericValue(readInput('9'));
                        //Validate that the 2 selected positions are not the same.  If they are, loop until secondSelection is different from firstSelection
                        while (secondSelection == firstCardPosition + 1) {
                            System.out.println("That is the same as your first selection.  Try again, or select 0 to exit the game.");
                            secondSelection = Character.getNumericValue(readInput('9'));
                        }



                        //Validate input is a correct value
                        if (secondSelection == 0 || secondSelection == -1) {
                            //if null or 0, break out of the while loop early and exits game
                            break;
                        }
                        //Call function which processes the 2 selected cards.
                        handleSecondSelection(secondSelection, game);
                        storedMove += "Second Move: " + secondCard.toString() + "\n";
                        System.out.println(secondCard.toString());


                        //If the game can continue it should return true, as the while loop will continue.
                        //If the player wins, loses, or quits, then it should return false, as the while loop will stop.
                        playingGame = handleSelectedCards(firstCard, secondCard, game);
                        //Call function which clears the stored card details in preparation for the next turn.
                        storedMove+= "-----------End of Attempt " + numOfLoops;
                        queue.enqueue(storedMove);
                        storedMove = null;
                        clearStoredCardDetails();

                        if (!isDeckEmpty(game)&& playingGame == false){
                            System.out.println("you have won the game!!!!");
                        }
                    }
                    watchReplay = true;

                }//end while
                System.out.println("Press 1 to replay your moves from the start or anything else to exit");
                while (watchReplay){
                    int nextReplay = 1;
                    char inputForReplay = readInput('2');
                    if (!queue.isEmpty()){
                        if (inputForReplay == '1'){

                            System.out.println("Replaying attempt" + nextReplay);
                            System.out.println(queue.dequeue());
                            nextReplay+= 1;

                        }else watchReplay = false;
                    }else{
                        System.out.println("You have replayed your full game the replay will now end");
                        watchReplay = false;}

                }

            }
        }

        //function which handles setting up the game, by drawing the top 9 cards of the shuffled deck
        public static void setUpGame(Deck aGame, Deck aDeck) {
            aDeck.shuffle();
            for (int i = 0; i < aGame.myDeck.length; i++) {
                aGame.myDeck[i] = aDeck.drawCard();
            }
        }
        public static void addCardToGame(Deck aGame, Deck aDeck)
        {for (int i = 0; i < aGame.myDeck.length; i++)
        {
            if (aGame.myDeck[i] == null)
            {
                aGame.myDeck[i] = aDeck.drawCard();
            }
        }
        }
        //function which handles writing the begin game menu to the console
        public static void beginGameMenu() {
            System.out.println("----------ELEVENS----------");
            System.out.println("---------------------------");
            System.out.println("1 - Begin a game of Elevens");
            System.out.println("2 - Play Demo");
            System.out.println("3 - Exit");
            System.out.println("---------------------------");


        }

        //functions which handles processing the user input for the begin game menu
        public static boolean handleGameMenu() {
            boolean playingGame = false;
            int selection = Character.getNumericValue(readInput('3'));
            switch (selection) {
                case 1:
                    System.out.println("Starting game.");
                    System.out.println("---------------------------");
                    playingGame = true;
                    break;

                case 2:
                    System.out.println("Playing Demo Mode");
                    System.out.println("---------------------------");
                    playingDemo = true;
                    playingGame = false;
                    break;

                case 3:
                    System.out.println("Exiting game.");
                    System.out.println("---------------------------");
                    playingGame = false;
                    break;

                default:
                    System.out.println("Invalid input, exiting game.");
                    System.out.println("---------------------------");
                    playingGame = false;
                    break;
            }
            return playingGame;
        }

        //function which handles storing the details of the first valid card
        public static void handleFirstSelection(int selection, Deck aGame) {
            //remove 1 from the selection to get the correct array index (selection 1-9 -> indexes 0-8)
            firstCardPosition = selection - 1;
            firstCard = aGame.myDeck[firstCardPosition];
        }

        //function which handles storing the details of the second valid card
        public static void handleSecondSelection(int selection, Deck aGame) {
            //remove 1 from the selection to get the correct array index (selection 1-9 -> indexes 0-8)
            secondCardPosition = selection - 1;
            secondCard = aGame.myDeck[secondCardPosition];
        }

        //function which handles storing the details of the extra royal valid card
        public static void handleExtraRoyalSelection(int selection, Deck aGame) {
            //remove 1 from the selection to get the correct array index (selection 1-9 -> indexes 0-8)
            extraRoyalCardPosition = selection - 1;
            extraRoyalCard = aGame.myDeck[extraRoyalCardPosition];
        }

        //function which handles the clearing of stored details about selected cards
        public static void clearStoredCardDetails() {
            firstCardPosition = -1;
            secondCardPosition = -1;
            extraRoyalCardPosition = -1;
            firstCard = null;
            secondCard = null;
            extraRoyalCard = null;
            hint = null;
            deckValues = null;

        }

        public static boolean handleSelectedCards(Card card1, Card card2, Deck aGame) {
            boolean playing = false;
            int ELEVEN = 11;
            int ACE = 1;//declare and initialize ace as ace rank value is 12 in obj
            int cardAns, card1Val, card2Val;
            //check rank and see if its less than 10
            if (card1.getRankValue() < 9 || card1.getRankValue() > 11){//the magic numbers are rank positions
                if (card1.getRankValue() != 12){
                    card1Val = Integer.parseInt(card1.getRank());}
                else{card1Val = ACE;}
            }else {//if above ie jack king queen then set to zero as these will get checked on royal cards
                card1Val = 0;
            }
            if (card2.getRankValue() < 9 || card2.getRankValue() > 11){
                if (card2.getRankValue() != 12){
                    card2Val = Integer.parseInt(card2.getRank());}
                else{ card2Val = ACE; }
            }else {
                card2Val = 0;
            }
            //Check if selected cards are 2 numbers
            if (card1Val != -1 && card2Val != -1) {
                //if true, add together and check if values equal 11
                cardAns = card1Val + card2Val;
                if (cardAns == ELEVEN) {
                    //if 11, remove the 2 cards using the saved data and draw 2 new cards to their position
                    aGame.myDeck[firstCardPosition] = null;
                    aGame.myDeck[secondCardPosition] = null;

                    aGame.myDeck[firstCardPosition] = aGame.drawCard();
                    aGame.myDeck[secondCardPosition] = aGame.drawCard();
                    System.out.println("MATCH!! Your selected card are equal to 11!");
                    storedMove += "MATCH!! Your selected card are equal to 11!\n";

                } else{//if not 2 numbers are not values equal to 11, check if 2 cards are royal cards

                    if (card1.getRankValue() == 9 && card2.getRankValue() == 10 || card1.getRankValue() == 9 && card2.getRankValue() == 11 ||
                            card1.getRankValue() == 10 && card2.getRankValue() == 9 || card1.getRankValue() == 10 && card2.getRankValue() == 11 ||
                            card1.getRankValue() == 11 && card2.getRankValue() == 9 || card1.getRankValue() == 11 && card2.getRankValue() == 10  ){//if true, get 3rd card selection and process it
                        //select an extra card
                        System.out.println("Please select your third card");
                        int thirdSelection = Character.getNumericValue(readInput('9'));
                        //Validate input is a correct value
                        while(thirdSelection == 0 ) {
                            //if null or 0, break out of the while loop early and exits game
                            System.out.println("error cannot be null or 0");
                            thirdSelection = Character.getNumericValue(readInput('9'));
                        }

                        //call the extra royal method
                        handleExtraRoyalSelection(thirdSelection, aGame);
                        while (extraRoyalCard.getRankValue() == card1.getRankValue()|| extraRoyalCard.getRankValue() == card2.getRankValue()){
                            System.out.println("Cannot be the same rank as card 1 or 2. Please try again: ");
                            thirdSelection = Character.getNumericValue(readInput('9'));
                            handleExtraRoyalSelection(thirdSelection, aGame);
                        }
                        storedMove += "Third Move: " + extraRoyalCard.toString() +"\n";
                        //display to user the chosen card
                        System.out.println("Your chosen card is: " + extraRoyalCard.toString());
                        //if the card rank is a numbered card
                        if (extraRoyalCard.getRankValue() < 9){
                            System.out.println("this is not a royal card... unlucky! ");
                            storedMove += "this is not a royal card... unlucky!\n ";

                        }else {//if not then display royal card
                            System.out.println("This is a royal card... lucky!");
                            storedMove += "This is a royal card... lucky!\n";
                            aGame.myDeck[firstCardPosition] = null;
                            aGame.myDeck[secondCardPosition] = null;
                            aGame.myDeck[extraRoyalCardPosition] = null;

                            aGame.myDeck[firstCardPosition] = aGame.drawCard();
                            aGame.myDeck[secondCardPosition] = aGame.drawCard();
                            aGame.myDeck[extraRoyalCardPosition] = aGame.drawCard();
                        }
                    }else{
                        System.out.println("Your Cards do not equal 11!");
                        storedMove += "Your Cards do not equal 11!\n";
                    }
                }
            }else {
                System.out.println("There has been an error please try again!");
                storedMove +="There has been an error please try again!\n";

            }
            playing = isDeckEmpty(aGame);


            return playing;
        }

        //Function which takes in the user input from the console and parses it as an integer
        //Returns this parsed integer
        //Takes an integer limit parameter, which defines the maximum number which can be parsed
        //integers above this value get set to the limit
        //invalid inputs are set to null
        public static char readInput(char limit) {

            char selection = 'x';

            while (selection =='h' || selection == 'H'|| selection == 'x'){
                String input = scanner.nextLine();
                //validate if null is entered program will exit
                if (input.isEmpty()) input = "0";
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
                            " or have not entered a number at all because of this the game will exit.");
                }
            }


            return selection;
        }





        public static int[] toIntArrayFromObj(Deck aGame){
            //initilize and declare ace value
            int ace = 1;
            //initilze string and int array for deck
            String[] stringDeck = new String[9];
            int[] deckValues = new int[9];
            //for loop to store the card object to a string array and then string array to int array
            for (int i = 0; i< aGame.myDeck.length; i++){

                if (aGame.myDeck[i] == null) stringDeck[i] = "Null for now";//store current card postion i to postion i in string deck
                else stringDeck[i] = aGame.myDeck[i].toString(); //store current card postion i to postion i in string deck
                //if card has value of 10
                if (stringDeck[i].charAt(0) == '1'){
                    deckValues[i] = 10;//10 is the value of cards with 10
                }else {//if not then take first value of card and store as a char to process
                    //store the first cvharacter of the string to a char variable
                    char myChar = stringDeck[i].charAt(0);
                    //System.out.println(myChar);
                    //if char is less than 10 store at in array position i
                    if (Character.getNumericValue(myChar) < 10) {
                        deckValues[i] = Character.getNumericValue(myChar);
                        //else store letter with chosen values A for 1, K for 110, Q for 1260, J for 15000, N for 999(null value)
                    } else if (myChar == 'A') {
                        deckValues[i] = ace;
                    } else if (myChar == 'K') {
                        deckValues[i] = 110;
                    } else if (myChar == 'Q') {
                        deckValues[i] = 1260;
                    } else if (myChar == 'J') {
                        deckValues[i] = 15000;
                    }else if (myChar == 'N'){
                        deckValues[i] = 999;
                    }//end if else
                }//end out if
            }
            //used when testing
//            for (int i = 0; i< deckValues.length; i++){
//                System.out.println(deckValues[i]);
//            }

            stringDeck = null;
            return deckValues;
        }//end method

        public static boolean isEqualTo11(int i, int j, int k){
            //initilse eleven and extra royal variable
            int ELEVEN = 11;
            int EXTRAROYAL = 16370;
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
            boolean canBreak = false;

            //triple for loop to test all combinations to end game
            for (int i = 0; i< myArray.length; i++){
                for (int j = 0; j<  myArray.length; j++){
                    for (int k = 0; k < myArray.length; k++){
                        //boolean value to check and see if it is 11 or royal
                        isElevenOrRoyal = isEqualTo11(myArray[i],myArray[j],myArray[k]);
                        //if true then display hint
                        if (isElevenOrRoyal){
                            //if equal to 11 then display a hint based on this then break out of for loop
                            if (myArray[i] + myArray[j] == 11){
                                hint = "The values of the cards are: " + myArray[i] + " and " + myArray[j];
                                canBreak = true;
                                break;
                            }else{//if not then display a hint based on the royal cards then break out of for loop
                                hint = "It is possible that there is an extra royal";
                                canBreak = true;
                                break;
                            }
                        }
                    }//end for loop k
                    //if hint it not null then break out of next for loop
                    if (canBreak){
                        break;
                    }
                }//end for loop j
                //if hint it not null then break out of next for loop
                if (canBreak){
                    break;
                }
            }//end for loop i
            if (hint == null){
                hint = "The game will end better lucky next time  ";
            }//if true game can end
            return isElevenOrRoyal;
        }//end method

         public static boolean isDeckEmpty(Deck aGame){
             boolean empty;
             int emptyDeck = 0;
             for (int i = 0; i < aGame.myDeck.length; i++)
                if (aGame.myDeck[i] == null){
                emptyDeck +=1;
                }
             if (emptyDeck == 9){
               empty = false;
             }else {
               empty = true;
            }
            return empty;
    }
}//class


