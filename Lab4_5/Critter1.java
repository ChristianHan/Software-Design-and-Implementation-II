package assignment5;
/* CRITTERS Critter1.java
 * EE422C Project 4 submission by
 * Keiran Crain
 * klc3788
 * Christian Han
 * CJH3752
 * Slip days used: <0>
 * Summer 2018
 */

/* The Critter1 (Aesir) will wander the world looking for a fight. The longer the go without a fight, the more likely they are to want
    to battle. Following a won battle, they relax where they are and reproduce.
*/
public class Critter1 extends Critter{

    private boolean recentBattle;
    private int bloodlust;
    private int movementOdds;
    private int dir;

    public Critter1(){
        recentBattle = false;
        bloodlust = 1;
        movementOdds = getRandomInt(10);
        dir = getRandomInt(8);
    }

    @Override
    public String toString() {
        return "1";
    }

    @Override
    public void doTimeStep() {
        //Will I leave my spot in search of a battle
        int check = getRandomInt(10);
        for (int i = 0; i < bloodlust; i++){
            if (check == movementOdds){
                if(look(dir, false) != null)
                    walk(dir);
                else
                    run(dir);
                dir = getRandomInt(8);
                movementOdds = getRandomInt(10);
                break;
            }
        }
        //If they have recently won a battle, they will try to reproduce
        if(recentBattle){
            recentBattle = false;
            bloodlust = 1;
            if(getEnergy() > Params.min_reproduce_energy){
                Critter1 child = new Critter1();
                reproduce(child,getRandomInt(8));
            }
        }
        bloodlust++;
    }

    @Override
    public boolean fight(String opponent) {
        recentBattle = true;
        return true;
    }

    @Override
    public CritterShape viewShape() { return CritterShape.SQUARE; }
    public javafx.scene.paint.Color viewColor() { return javafx.scene.paint.Color.RED; }
}
