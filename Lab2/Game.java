package assignment2;

import java.util.Scanner;

public class Game {

    Scanner sc = new Scanner(System.in); //creating new scanner

    public void sayGreetings(){

        System.out.println("Welcome to Mastermind.");

    }

    public void winningMessage(){
        System.out.println("You win!");
    }

    boolean playerWantsToPlay() {

        System.out.println("Do you want to play a new game? (Y/N):");
        String flag = sc.nextLine();
        return flag.equals("Y");

    }

    void runGame(boolean testing){

        String code = SecretCodeGenerator.getInstance().getNewSecretCode();//String code

        String [] history = new String[GameConfiguration.guessNumber];

        if (testing){
            System.out.println("Secret code: " + code);
        }
        boolean won = false;
        int numGuesses = 0;

        //System.out.println(code);////////testing only testing testing tesT!!!!!!!

        while(!won && numGuesses < GameConfiguration.guessNumber){

            System.out.println("");
            System.out.println("You have " + (GameConfiguration.guessNumber - numGuesses) +  " guess(es) left.");
            System.out.println("Enter guess:");

            String guess = sc.nextLine();

            if (guess.equals("HISTORY")){

                for(int i = 0; i < numGuesses; i++){
                    System.out.println(history[i]);
                }
            }

            if(validGuess(guess) == true){
                int[] guessArray = codeToInt(guess);
                int[] codeArray = codeToInt(code);
                int[] pegArray = getPegInfo(codeArray, guessArray);


                System.out.println(guess + " -> " + pegArray[0]+ "b" + "_" + pegArray[1] + "w");


                history[numGuesses] = (guess + " -> " + Integer.toString(pegArray[0]) + "b" + "_" + Integer.toString(pegArray[1]) + "w");

                numGuesses = numGuesses + 1;


                if(pegArray[0] == 4){
                    won = true;
                    winningMessage();
                    break;
                }



                if(numGuesses == GameConfiguration.guessNumber){

                    System.out.println("You lose! The pattern was " + code);
                    System.out.println("");
                    break;

                }
            }
            if(validGuess(guess) == false && !guess.equals("HISTORY")){
                System.out.println("INVALID_GUESS");
            }

        }

    }


    public char[] stringToCharArray(String [] string){
        String[] sArray = string;
        String s = "";
        for (String n:sArray)
            s+= n;
        char[] c = s.toCharArray();
        return c;
    }

    public boolean validGuess (String guess){ //returns true if valid guess. false for invalid guess.

        boolean flag = false;
        int flagSum = 0;
        char[] colors1 = stringToCharArray(GameConfiguration.colors);


        if(guess.length() == GameConfiguration.pegNumber){

            char[] charArray = guess.toCharArray();

            for(int i = 0; i < charArray.length; i++) {
                for (int j = 0; j < colors1.length; j++) {
                    if (charArray[i]== colors1[j]) {
                        flagSum = flagSum + 1;
                    }
                }
            }

        }
        else{
            flag = false;
            return flag;
        }
        if(flagSum == GameConfiguration.pegNumber){
            flag = true;
        }
        return flag;
    }

    public int [] codeToInt (String code) {

        int[] intCode = new int[GameConfiguration.pegNumber];
        char[] charCode = code.toCharArray();

        for (int i = 0; i < intCode.length; i++) {

            switch (charCode[i]){
                case 'B': intCode[i] = 0;
                    break;

                case 'G': intCode[i] = 1;
                    break;

                case 'O': intCode[i] = 2;
                    break;

                case 'P': intCode[i] = 3;
                    break;

                case 'R': intCode[i] = 4;
                    break;

                case 'Y': intCode[i] = 5;
                    break;
            }
        }
        return intCode;
    }

    public int [] getPegInfo (int [] code, int [] guess){
        int [] pegInfo = new int[2]; //number of black pegs in pegInfo[0] and number of white pegs in pegInfo[1]
        int [] testCode = new int [GameConfiguration.pegNumber];
        int [] testGuess = new int [GameConfiguration.pegNumber];

        for(int i = 0; i < GameConfiguration.pegNumber; i++){ //creates copies of arrays to test with
            testCode[i] = code[i];
            testGuess[i] = guess[i];
        }

        int blackPegs = 0;//initialize number of blackPegs
        int whitePegs = 0;//initialize number of whitePegs

        for(int i = 0; i < GameConfiguration.pegNumber; i++){ //finds number of black pegs
            if(testGuess[i] == testCode[i]){
                blackPegs++;
                testCode[i] = -1;
                testGuess[i] = -1;
            }
        }

        for(int i = 0; i < GameConfiguration.pegNumber; i++){//finds number of white pegs
            if(testGuess[i] == -1){
                continue;
            }
            for(int j = 0; j < GameConfiguration.pegNumber; j++){
                if(testCode[j] == -1){
                    continue;
                }
                if(testGuess[i] == testCode[j]) {
                    whitePegs++;
                    testCode[i] = -1;
                    testGuess[i] = -1;
                }
            }
        }

        pegInfo[0] = blackPegs;
        pegInfo[1] = whitePegs;

        //System.out.println(pegInfo[0]+ "b" + "_" + pegInfo[1] + "w");

        return pegInfo;

    }

}

