import java.util.Random;

public class Demo {
    public static void demoMode(){
        int demoLoops = 0;
        boolean playingDemo = true;

        System.out.println("Playing demo");
        //a deck for the demo
        Deck deckDemo = new Deck(52);
        Deck gameDemo = new Deck(9);
        //shuffle demo deck and deal demo cards
        GameClassMain.setUpGame(gameDemo,deckDemo);


        //if the games deck of 9 cannot have any more 11's or royals then end
        while (playingDemo){

            GameClassMain.addCardToGame(gameDemo, deckDemo);
            //convert obj to intarray
            GameClassMain.deckValues = GameClassMain.toIntArrayFromObj(gameDemo);

            GameClassMain.canEnd = GameClassMain.checkIfGameCanEnd(GameClassMain.deckValues);

            if (!GameClassMain.canEnd){
                if (demoLoops == 0){
                    //reset the game and try again
                    deckDemo = new Deck(52);
                    gameDemo = new Deck(9);
                    GameClassMain.clearStoredCardDetails();
                    //Shuffle deck
                    GameClassMain.setUpGame(gameDemo, deckDemo);

                    System.out.println("Demo Auto Lost so restarted");
                }else{
                    System.out.println("The Demo has now ended");
                    if (!GameClassMain.isDeckEmpty(gameDemo)){
                        System.out.println("The demo has won!!!!");
                    }else {
                        System.out.println("The demo has lost ran out of matchable cards!!!");
                    }
                    playingDemo = false;
                }//end if else


            }else{ demoLoops++;

                int firstPick = 0, secondPick = 0,royalPick = 0,firstPickVal = 0,secondPickVal = 0, royalPickVal = 0;

                botBestOutcome(firstPick,secondPick,firstPickVal,secondPickVal,gameDemo);
                if (GameClassMain.isDeckEmpty(gameDemo)){
                    gameDemo.listDeck();
                    System.out.println("----------------");
                    //display selected cards
                    System.out.println("System picked: "+ GameClassMain.firstCard+ " for card 1");
                    System.out.println("System picked: " + GameClassMain.secondCard+ " for card 2");
                }
                //display current hand

                playingDemo = handleSelectedDemoCards(GameClassMain.firstCard, GameClassMain.secondCard,gameDemo, royalPick, royalPickVal);

                GameClassMain.clearStoredCardDetails();

                try {
                    System.out.println("Press 1 to continue or 0 to exit");
                    int con = GameClassMain.readInput('2');
                }catch (Exception e){}

                if (!GameClassMain.isDeckEmpty(gameDemo)&& playingDemo == false){
                    System.out.println("The demo has won!!!!");
                }

            }
        }//end while
    }//demo mode
    public static void botBestOutcome(int pFirstPick , int pSecondPick, int pFirstPickVal, int pSecondPickVal, Deck aGameDemo){


        while (!isEqualTo11Demo(pFirstPickVal,pSecondPickVal) || pFirstPick == 9){
            Random rand = new Random();
            pFirstPick = rand.nextInt(aGameDemo.myDeck.length - 1);
            pFirstPickVal = GameClassMain.deckValues[pFirstPick];
            if (pFirstPickVal == 110 || pFirstPickVal == 1260 || pFirstPickVal == 15000){
                for (int j = 0; j < GameClassMain.deckValues.length; j++) {
                    if ( GameClassMain.deckValues[j] == 110 && pFirstPickVal== 1260|| GameClassMain.deckValues[j] == 110 && pFirstPickVal == 15000 ){
                        pSecondPickVal = GameClassMain.deckValues[j];
                        pSecondPick = j;
                        break;
                    }else if (GameClassMain.deckValues[j] == 1260 && pFirstPickVal== 110|| GameClassMain.deckValues[j] == 1260 && pFirstPickVal == 15000){
                        pSecondPickVal = GameClassMain.deckValues[j];
                        pSecondPick = j;
                        break;
                    }else if (GameClassMain.deckValues[j] == 15000 && pFirstPickVal== 110|| GameClassMain.deckValues[j] == 15000 &&  pFirstPickVal == 1260){
                        pSecondPickVal = GameClassMain.deckValues[j];
                        pSecondPick = j;
                        break;
                    }else{
                        pSecondPick = j;
                        if (pSecondPick == pFirstPick){
                            pSecondPick = -1;
                        }

                    }


                }//end forloop
            }else {
                pSecondPickVal = 11 - pFirstPickVal;
                for (int j = 0; j < GameClassMain.deckValues.length; j++) {
                    if (pSecondPickVal == GameClassMain.deckValues[j]) {
                        pSecondPick = j;
                        break;
                    } else{
                        pSecondPick = -1;
                    }
                }//end for
            }
            if (pSecondPick == -1){
                pSecondPickVal =0;
                pFirstPick++;
            }
        }//end while
        if (pFirstPick != 9){
            pFirstPick++;
        }
        pSecondPick++;

        GameClassMain.handleFirstSelection(pFirstPick, aGameDemo);
        GameClassMain.handleSecondSelection(pSecondPick, aGameDemo);

    }

    public static boolean handleSelectedDemoCards(Card card1, Card card2, Deck aGame, int pRoyalPick, int pRoyalPickVal) {
        boolean playing = false;
        int ELEVEN = 11;
        int ACE = 1;//declare and initialize ace as ace rank value is 12 in obj
        int jack = 15000;
        int queen = 1260;
        int king = 110;
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
                aGame.myDeck[GameClassMain.firstCardPosition] = null;
                aGame.myDeck[GameClassMain.secondCardPosition] = null;

                aGame.myDeck[GameClassMain.firstCardPosition] = aGame.drawCard();
                aGame.myDeck[GameClassMain.secondCardPosition] = aGame.drawCard();
                System.out.println("MATCH!! Your selected card are equal to 11!");


            }else{//if not 2 numbers are not values equal to 11, check if 2 cards are royal cards

                if (card1.getRankValue() == 9 && card2.getRankValue() == 10){
                    pRoyalPickVal = king;
                }
                else if (card1.getRankValue() == 9 && card2.getRankValue() == 11 ){
                    pRoyalPickVal = queen;
                }
                else if (card1.getRankValue() == 10 && card2.getRankValue() == 9){
                    pRoyalPickVal = king;
                }
                else if (card1.getRankValue() == 10 && card2.getRankValue() == 11){
                    pRoyalPickVal = jack;
                }
                else if(card1.getRankValue() == 11 && card2.getRankValue() == 9 ){
                    pRoyalPickVal = queen;
                }
                else if (card1.getRankValue() == 11 && card2.getRankValue() == 10 ) {
                    pRoyalPickVal = jack;
                }else{pRoyalPickVal = 999;}

                for (int i = 0; i < GameClassMain.deckValues.length; i++) {
                    if (pRoyalPickVal == GameClassMain.deckValues[i]){
                        pRoyalPick = i +1;
                        break;
                    }else{pRoyalPick = -1;}
                }
                if (pRoyalPick < 9&& pRoyalPick >= 0){
                    //call the extra royal method
                    GameClassMain.handleExtraRoyalSelection(pRoyalPick, aGame);
                    //display to user the chosen card
                    System.out.println("System picked : " + GameClassMain.extraRoyalCard.toString() + " as the third card");

                    System.out.println("This is a royal card... lucky!");

                    aGame.myDeck[GameClassMain.firstCardPosition] = null;
                    aGame.myDeck[GameClassMain.secondCardPosition] = null;
                    aGame.myDeck[GameClassMain.extraRoyalCardPosition] = null;

                    aGame.myDeck[GameClassMain.firstCardPosition] = aGame.drawCard();
                    aGame.myDeck[GameClassMain.secondCardPosition] = aGame.drawCard();
                    aGame.myDeck[GameClassMain.extraRoyalCardPosition] = aGame.drawCard();
                }else{
                    System.out.println("This is not a royal match");
                }

            }
        }else{
            System.out.println("Your Cards do not equal 11!");

        }

        playing = GameClassMain.isDeckEmpty(aGame);

        return playing;
    }//end method

    public static boolean isEqualTo11Demo(int i, int j){


        //initilse eleven and extra royal variable
        int ELEVEN = 11;
        int jack = 15000;
        int queen = 1260;
        int king = 110;
        // initialize boolean value to return if equals 11 or royal
        boolean isEleven = false;
        //add the first to ints
        int sum = i + j;
        //if equal to 11 return true
        if (sum == ELEVEN){
            isEleven = true;
        }else{//if not check for extra royal
            if (sum == (king + queen)|| sum == (king + jack)||
                    sum == (queen + king)|| sum == (queen + jack) ||
                    sum == (jack + king) || sum == (jack + queen)){
                isEleven = true;
            }else { isEleven = false;} //end inner if else
        }//end outer if else
        return isEleven;
    }//end method


}
