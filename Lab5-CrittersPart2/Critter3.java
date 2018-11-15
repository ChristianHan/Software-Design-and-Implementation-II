package assignment5;
/* CRITTERS Critter3.java
 * EE422C Project 4 submission by
 * Keiran Crain
 * klc3788
 * Christian Han
 * CJH3752
 * Slip days used: <0>
 * Summer 2018
 */

import javafx.scene.paint.Color;

/*
 *This critter called Critter3 (Terpspinner) does....
 */
public class Critter3 extends Critter {

    private int direction;


    public Critter3(){ //Constructor
        direction = 0;
    }


    @Override
    public String toString() { return "3";}



    @Override
    public void doTimeStep() {

        walk(direction);
        direction = direction + 1;
        if(direction >= 8) {
            direction = 0;
            if(getEnergy() > Params.min_reproduce_energy) {
                Critter3 offspring = new Critter3();
                reproduce(offspring, 0);
            }
        }

    }

    @Override
    public boolean fight(String opponent) {//will only fight if it has energy > 100

            if(getEnergy() > 90)
                return true;
            else if (look(direction, false) != null)
                return true;
            else
                return false;
    }

    @Override
    public CritterShape viewShape() { return CritterShape.STAR; }
    public javafx.scene.paint.Color viewColor() { return Color.NAVY; }





}

