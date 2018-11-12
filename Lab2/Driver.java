package assignment2;


public class Driver {

            /*
    greetings();

    while playerWantsToPlay():
        runGame();

    runGame(boolean testing) {
        if testing:
            print("the code is XXXX")
        boolean won = false;
        numguess = 0;
        while (!won and numGuess < XXX):
            input = getline();
            //process input * check whether valid imput

     */

    public static void main(String args[]) {

        Game game = new Game();

        game.sayGreetings();

        while (game.playerWantsToPlay()){

            game.runGame(args.length > 0 && "1".equals(args[0]));

        }



    }
}