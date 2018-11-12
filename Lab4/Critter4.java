package assignment4;
/* CRITTERS Critter4.java
 * EE422C Project 4 submission by
 * Keiran Crain
 * klc3788
 * Christian Han
 * CJH3752
 * Slip days used: <0>
 * Summer 2018
 */

/*  (Vanir)
    The Critter4 are peaceful critters who instead of fighting, they clone reproduce. If there is no encounter occurring within their cell, just chill.
    Critter4 will always flee in the same direction, assigned at birth. They also reproduce in the opposite direction
 */
public class Critter4 extends Critter {
    private int dir;
    private int spawnDir;

    public Critter4() {
        dir = getRandomInt(8);
        spawnDir = Math.abs(this.dir-4);
    }

    @Override
    public String toString() {
        return "4";
    }

    @Override
    public void doTimeStep() {
        run(dir);
    }

    @Override
    public boolean fight(String opponent) {
        if (getEnergy() > Params.min_reproduce_energy){
            Critter4 child = new Critter4();
            reproduce(child,spawnDir);
        }
        return false;
    }
}
