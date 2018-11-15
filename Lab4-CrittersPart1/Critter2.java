package assignment4;
/* CRITTERS Critter2.java
 * EE422C Project 4 submission by
 * Keiran Crain
 * klc3788
 * Christian Han
 * CJH3752
 * Slip days used: <0>
 * Summer 2018
 */

/*
 *This Critter called Critter2 (Deem) will fight anything if it is alive.
 */
public class Critter2 extends Critter {

private int direction;

    @Override
    public String toString() {
        return "2";
    }


    public Critter2(){ //constructor

        direction = 0;
    }


    @Override
    public void doTimeStep() {
        direction = getRandomInt(8);
        run(direction);
        if(direction == 3){
            Critter2 offspring = new Critter2();
            reproduce(offspring, direction);
        }

    }

    @Override
    public boolean fight(String opponent) {
        if(getEnergy() > 0)
            return true;
        else
            return false;
    }





}
