package assignment5;

import com.sun.javafx.tools.packager.Param;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public abstract class Critter {
    /* NEW FOR PROJECT 5 */
    public enum CritterShape {
        CIRCLE,
        SQUARE,
        TRIANGLE,
        DIAMOND,
        STAR
    }

    /* the default color is white, which I hope makes critters invisible by default
     * If you change the background color of your View component, then update the default
     * color to be the same as you background
     *
     * critters must override at least one of the following three methods, it is not
     * proper for critters to remain invisible in the view
     *
     * If a critter only overrides the outline color, then it will look like a non-filled
     * shape, at least, that's the intent. You can edit these default methods however you
     * need to, but please preserve that intent as you implement them.
     */
    public javafx.scene.paint.Color viewColor() {
        return Color.ALICEBLUE;
    }

    public javafx.scene.paint.Color viewOutlineColor() {
        return viewColor();
    }

    public javafx.scene.paint.Color viewFillColor() {
        return viewColor();
    }

    public abstract CritterShape viewShape();

    //Collections
    private static List<assignment5.Critter> population = new java.util.ArrayList<assignment5.Critter>();
    private static List<assignment5.Critter> babies = new java.util.ArrayList<assignment5.Critter>();
    private static List<assignment5.Critter>[][] currentState = new java.util.ArrayList[Params.world_height][Params.world_width];

    //Local Variables
    private int energy = 0;
    private static boolean initializeFlag = false;
    private boolean hasMoved = false;
    private int x_coord;
    private int y_coord;
    private int old_x_coord;
    private int old_y_coord;
    private static String myPackage;

    //Movement Related Functions
    //TODO: Write walk - Finsihed: Keiran
    protected final void walk(int direction) {
        if (!initializeFlag)
            initialize();
        this.energy -= Params.walk_energy_cost;
        if (!hasMoved) {
            move(direction, 1);
            hasMoved = true;
        }
    }

    //TODO: Write run - Finished: Keiran
    protected final void run(int direction) {
        if (!initializeFlag)
            initialize();
        this.energy -= Params.run_energy_cost;
        if (!hasMoved) {
            move(direction, 2);
            hasMoved = true;
        }

    }

    //TODO: Write look. Returns the direction of the nearest empty space. Else, returns -1. -Finished: Keiran
    protected final String look(int direction, boolean steps) {
        this.energy -= Params.look_energy_cost;
        int distance;
        int currentX = this.x_coord;
        int currentY = this.y_coord;
        String cString = null;
        if (steps)
            distance = 2;
        else
            distance = 1;
        move(direction,distance);
        if(currentState[this.y_coord][this.x_coord].size() > 1) {
            cString = currentState[this.y_coord][this.x_coord].get(0).toString();
            this.y_coord = currentY;
            this.x_coord = currentX;
        }
        return cString;
    }

    private final void move(int direction, int distance) {
        if (!initializeFlag)
            initialize();
        while (distance != 0) {
            distance--;
            switch (direction) {
                case 0:
                    x_coord++; //Moving East
                    if (x_coord == Params.world_width)
                        x_coord = 0;
                    break;
                case 1:
                    x_coord++; //Moving Northeast
                    if (x_coord == Params.world_width)
                        x_coord = 0;
                    y_coord--;
                    if (y_coord == -1)
                        y_coord = Params.world_height - 1;
                    break;
                case 2:
                    y_coord--; //Moving North
                    if (y_coord == -1)
                        y_coord = Params.world_height - 1;
                    break;
                case 3:
                    x_coord--; //Moving Northwest
                    if (x_coord == -1)
                        x_coord = Params.world_width - 1;
                    y_coord--;
                    if (y_coord == -1)
                        y_coord = Params.world_height - 1;
                    break;
                case 4:
                    x_coord--; //Moving West
                    if (x_coord == -1)
                        x_coord = Params.world_width - 1;
                    break;
                case 5:
                    x_coord--; //Moving Southwest
                    if (x_coord == -1)
                        x_coord = Params.world_width - 1;
                    y_coord++;
                    if (y_coord == Params.world_height)
                        y_coord = 0;
                    break;
                case 6:
                    y_coord++; //Moving South
                    if (y_coord == Params.world_height)
                        y_coord = 0;
                    break;
                case 7:
                    x_coord++; //Moving Southeast
                    if (x_coord == Params.world_width)
                        x_coord = 0;
                    y_coord++;
                    if (y_coord == Params.world_height)
                        y_coord = 0;
                    break;
                default:
                    break;    //Instructions say this will never happen
            }
        }
    }


    //Essential Game Functions
    //TODO: Write reproduce -Finished by Christian
	/*
	refer to instructions in pdf
	 */
    protected final void reproduce(assignment5.Critter offspring, int direction) {
        if (!initializeFlag)
            initialize();
        if (energy < Params.min_reproduce_energy) {
            return;
        }

        offspring.energy = energy / 2;//offspring gets half of parents energy rounded down
        energy = (int) Math.ceil((double) (energy / 2));//reassign parent's energy to half (rounded up)

        offspring.x_coord = x_coord;
        offspring.y_coord = y_coord;
        offspring.old_x_coord = offspring.x_coord;
        offspring.old_y_coord = offspring.y_coord;

        offspring.move(direction, 1);
        babies.add(offspring);
    }

    /**
     * create and initialize a Critter subclass.
     * critter_class_name must be the unqualified name of a concrete subclass of Critter, if not,
     * an InvalidCritterException must be thrown.
     * (Java weirdness: Exception throwing does not work properly if the parameter has lower-case instead of
     * upper. For example, if craig is supplied instead of Craig, an error is thrown instead of
     * an Exception.)
     *
     * @param critter_class_name
     * @throws InvalidCritterException
     */
    //TODO: Write makeCrittter FINISHED-Keiran
    public static void makeCritter(String critter_class_name) throws InvalidCritterException {
        if (!initializeFlag)
            initialize();
        Class<?> myCritter = null;
        Constructor<?> constructor = null;
        Object instanceOfMyCritter = null;
        String fullName = myPackage + "." + critter_class_name;
        try {
            myCritter = Class.forName(fullName);
        } catch (ClassNotFoundException e) { //How should we catch the casing error?
            throw new InvalidCritterException(critter_class_name);
        }

        try {
            constructor = myCritter.getConstructor();
            instanceOfMyCritter = constructor.newInstance();
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new InvalidCritterException(critter_class_name);
        }
        if (!assignment5.Critter.class.isAssignableFrom(myCritter)) {
            throw new InvalidCritterException(critter_class_name);
        }
        assignment5.Critter me = (assignment5.Critter) instanceOfMyCritter;
        me.energy = Params.start_energy;

		/*
		me.x_coord = getRandomInt(Params.world_width);
		me.y_coord = getRandomInt(Params.world_height);
		population.add(me);
		currentState[me.x_coord][me.y_coord].add(me);
        */

        me.x_coord = getRandomInt(Params.world_width);
        me.y_coord = getRandomInt(Params.world_height);
        me.old_x_coord = me.x_coord;
        me.old_y_coord = me.y_coord;
        population.add(me);
        currentState[me.y_coord][me.x_coord].add(me);
    }

    /**
     * Gets a list of critters of a specific type.
     *
     * @param critter_class_name What kind of Critter is to be listed.  Unqualified class name.
     * @return List of Critters.
     * @throws InvalidCritterException
     */
    //TODO: Write getInstance
    public static List<assignment5.Critter> getInstances(String critter_class_name) throws InvalidCritterException {
        if (!initializeFlag)
            initialize();
        List<assignment5.Critter> result = new java.util.ArrayList<assignment5.Critter>();
        Class<?> myCritter = null;
        Constructor<?> constructor = null;
        Object instanceOfMyCritter = null;
        String fullName = myPackage + "." + critter_class_name;
        try {
            myCritter = Class.forName(fullName);
        } catch (ClassNotFoundException e) { //How should we catch the casing error?
            throw new InvalidCritterException(critter_class_name);
        }

        try {
            constructor = myCritter.getConstructor();
            instanceOfMyCritter = constructor.newInstance();
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new InvalidCritterException(critter_class_name);
        }
        if (!assignment5.Critter.class.isAssignableFrom(myCritter)) {
            throw new InvalidCritterException(critter_class_name);
        }
        for (int i = 0; i < population.size(); i++) {
            if (myCritter.isInstance(population.get(i)))
                result.add(population.get(i));
        }
        return result;
    }

    /*
     * invoke the doTimeStep method on every living critter in the critter collection
     * hopefully the dead are removed from this collection
     */
    //TODO: worldTimeStep - Finsihed by Keiran
    public static void worldTimeStep() {
        if (!initializeFlag)
            initialize();
        //Simulate the time steps for everyone
        for (int i = 0; i < population.size(); i++) {//iterating through every critter in list of Critters
            population.get(i).doTimeStep();
        }

        //Simulates everyone moving at the same time
        for (int i = 0; i < population.size(); i++){
            Critter c = population.get(i);
            currentState[c.old_y_coord][c.old_x_coord].remove(c);
            currentState[c.y_coord][c.x_coord].add(c);
        }

        //Simulate the encounters for everyone after every critter has done their doTimeStep function
        encounter();

        //Add the babies to population and current state and clear the babies list
        for (int i = 0; i < babies.size(); i++) {
            assignment5.Critter c = babies.get(i);
            population.add(c);
            currentState[c.y_coord][c.x_coord].add(c);
            babies.remove(c);
        }

        //Decrementing every Critter by restEnergy
        for (int i = 0; i < currentState.length; i++) {
            for (int j = 0; j < currentState[i].length; j++)
                for (int z = 0; z < currentState[i][j].size(); z++)
                    currentState[i][j].get(z).energy -= Params.rest_energy_cost;
        }

        //Removing all of the dead critters
        for (int i = 0; i < population.size(); i++)
            if (population.get(i).energy <= 0) {
                population.remove(i);
                i--;
            }

        for (int i = 0; i < currentState.length; i++) {
            for (int j = 0; j < currentState[i].length; j++)
                for (int z = 0; z < currentState[i][j].size(); z++)
                    if (currentState[i][j].get(z).energy <= 0) {
                        currentState[i][j].remove(z);
                        z--;
                    }

        }

        //Resetting hasMoved variable
        for (int i = 0; i < population.size(); i++) {
            population.get(i).hasMoved = false;
        }
        //Adding Algae
        for (int i = 0; i < Params.refresh_algae_count; i++) {
            try {
                makeCritter("Algae");
            } catch (InvalidCritterException e) {
                System.out.println("Something is wrong.");
            }
        }

        //Updating old x and old y coord
        for (int i = 0; i < population.size(); i++){
            Critter c = population.get(i);
            c.old_x_coord = c.x_coord;
            c.old_y_coord = c.y_coord;
        }

    }

/*
    me.x_coord = getRandomInt(Params.world_width);
    me.y_coord = getRandomInt(Params.world_height);
    population.add(me);
    currentState[me.x_coord][me.y_coord].add(me);
*/

    //TODO: displayWorld - Finished: Keiran
    public static void displayWorld(GridPane grid) {
        if (!initializeFlag)
            initialize();
        double rowHeight = grid.getHeight()/Params.world_height;
        double colWidth = grid.getWidth()/Params.world_width;
        double min;
        if(rowHeight < colWidth)
            min = rowHeight;
        else
            min = colWidth;
        min -= min *0.2;
        for(int y = 0; y < Params.world_height; y++){
            for(int x = 0; x < Params.world_width; x++){
                StackPane cell = (StackPane)grid.getChildren().get(x*Params.world_height + y);
                if(cell.getChildren().size() == 3)
                    cell.getChildren().remove(cell.getChildren().size()-1);
                if (currentState[y][x].size() != 0) {
                    cell.getChildren().add(currentState[y][x].get(0).getShape(min));
                }
            }
        }
    }

    private Shape getShape(double min){
        Shape s = new Polygon();
        if(this.viewShape() == CritterShape.CIRCLE) {
            s = new Circle(min/2);
            s.setFill(this.viewFillColor());
            s.setStroke(this.viewOutlineColor());
        }
        else if (this.viewShape() == CritterShape.SQUARE) {
            s = new Rectangle(min, min);
            s.setFill(this.viewFillColor());
            s.setStroke(this.viewOutlineColor());
        }
        else if (this.viewShape() == CritterShape.TRIANGLE){
            ((Polygon) s).getPoints().addAll(min/2, 0.0, 0.0, min/2, min, min/2);
            s.setFill(this.viewFillColor());
            s.setStroke(this.viewOutlineColor());
        }
        else if (this.viewShape() == CritterShape.DIAMOND){
            ((Polygon) s).getPoints().addAll(min/2, 0.0, 0.0, min/2, min/2, min, min, min/2);
            s.setFill(this.viewFillColor());
            s.setStroke(this.viewOutlineColor());
        }
        else{ //Star
            ((Polygon) s).getPoints().addAll(min/2, 0.0, 0.65*min, 0.35 * min, min, 0.4 * min, 0.75 *min, 0.65 * min, 0.85 * min, min,
                    min/2, 0.82 * min, 0.14 * min, min, 0.25 * min, 0.65 * min, 0.0, 0.4 *min, 0.36 * min, 0.36*min);
            s.setFill(this.viewFillColor());
            s.setStroke(this.viewOutlineColor());
        }
        return s;
    }
    /**
     * Clear the world of all critters, dead and alive
     */
    //TODO: Clear World - Finished: -Keiran
    public static void clearWorld() {
        if (!initializeFlag)
            initialize();
        // Complete this method.
        population = new java.util.ArrayList<assignment5.Critter>();
        babies = new java.util.ArrayList<assignment5.Critter>();
        currentState = new ArrayList[Params.world_height][Params.world_width];
        initializeFlag = false;
    }

    //Helper function for worldTimeStep that handles encounter
    //If a creature if want to flee but their is a creature in the way, it should die.
    private static void encounter() {
        if (!initializeFlag)
            initialize();
        for (int i = 0; i < currentState.length; i++) {
            for (int j = 0; j < currentState[i].length; j++) {
                while (currentState[i][j].size() > 1) {
                    //Retrieve the two fighters from the cell
                    assignment5.Critter fighterA = currentState[i][j].get(0);
                    assignment5.Critter fighterB = currentState[i][j].get(1);

                    //Check fighting conditions of both
                    boolean aWish = fighterA.fight(fighterB.toString());
                    if(fighterA.x_coord != j || fighterA.y_coord != i && fighterA.energy >0){
                        currentState[i][j].remove(fighterA);
                        currentState[fighterA.y_coord][fighterA.x_coord].add(fighterA);
                    }
                    boolean bWish = fighterB.fight(fighterA.toString());
                    if(fighterB.x_coord != j || fighterB.y_coord != i && fighterB.energy >0){
                        currentState[i][j].remove(fighterB);
                        currentState[fighterB.y_coord][fighterB.x_coord].add(fighterB);
                    }
                    //Act on fighting conditions of both
                    if (fighterA.energy <= 0) {
                        currentState[i][j].remove(fighterA);
                        population.remove(fighterA);
                    }
                    if (fighterB.energy <= 0) {
                        currentState[i][j].remove(fighterB);
                        population.remove(fighterB);
                    }
                    if (fighterA.energy > 0 && fighterB.energy > 0 &&
                            fighterA.x_coord == j && fighterB.x_coord == j &&
                            fighterA.y_coord == i && fighterB.y_coord == i) {
                        int aOdds = 0, bOdds = 0;
                        if (aWish)
                            aOdds = getRandomInt(fighterA.energy);
                        if (bWish)
                            bOdds = getRandomInt(fighterB.energy);
                        //If there is a winner of a battle, do the energy mumbo jumbo
                        if (aOdds >= bOdds) { //A is the winner
                            fighterA.energy += (fighterB.energy / 2);
                            currentState[i][j].remove(fighterB);
                            population.remove(fighterB);
                        } else { //B is the winner
                            fighterB.energy += (fighterA.energy / 2);
                            currentState[i][j].remove(fighterA);
                            population.remove(fighterA);
                        }
                    }

                }

            }
        }
    }
    //Abstract Methods

    //TODO: Write doTimeStep in Subclasses
    public abstract void doTimeStep();

    //TODO: Write fight in Subclasses
    public abstract boolean fight(String opponent);

    private static void initialize() {
        for (int i = 0; i < currentState.length; i++)
            for (int j = 0; j < currentState[i].length; j++)
                currentState[i][j] = new ArrayList<assignment5.Critter>();
        initializeFlag = true;
    }

    //Provided Functions
    static {
        myPackage = assignment5.Critter.class.getPackage().toString().split(" ")[1];

    }

    protected int getEnergy() {
        return energy;
    }

    //getRandomInt functionality
    private static java.util.Random rand = new java.util.Random();

    public static int getRandomInt(int max) {
        return rand.nextInt(max);
    }

    public static void setSeed(long new_seed) {
        rand = new java.util.Random(new_seed);
    }

    /* a one-character long string that visually depicts your critter in the ASCII interface */
    public String toString() {
        return "";
    }

    /**
     * Prints out how many Critters of each type there are on the board.
     *
     * @param critters List of Critters.
     */
    public static String runStats(List<assignment5.Critter> critters) {

       // System.out.print("" + critters.size() + " critters as follows -- ");

        String stats = "" + critters.size() + " critters as follows -- ";////////////////////////

        java.util.Map<String, Integer> critter_count = new java.util.HashMap<String, Integer>();
        for (assignment5.Critter crit : critters) {
            String crit_string = crit.toString();
            Integer old_count = critter_count.get(crit_string);
            if (old_count == null) {
                critter_count.put(crit_string, 1);
            } else {
                critter_count.put(crit_string, old_count.intValue() + 1);
            }
        }
        String prefix = "";
        for (String s : critter_count.keySet()) {

           // System.out.print(prefix + s + ":" + critter_count.get(s));

            stats = stats + prefix + s + ":" + critter_count.get(s);///////////////////

            prefix = ", ";
        }

       // System.out.println();


        return stats;///////////////need to return some type of runStats string
    }

    //TestCritter Class
    /* the TestCritter class allows some critters to "cheat". If you want to
     * create tests of your Critter model, you can create subclasses of this class
     * and then use the setter functions contained here.
     *
     * NOTE: you must make sure that the setter functions work with your implementation
     * of Critter. That means, if you're recording the positions of your critters
     * using some sort of external grid or some other data structure in addition
     * to the x_coord and y_coord functions, then you MUST update these setter functions
     * so that they correctly update your grid/data structure.
     */
    static abstract class TestCritter extends assignment5.Critter {
        protected void setEnergy(int new_energy_value) {
            super.energy = new_energy_value;
        }

        protected void setX_coord(int new_x_coord) {
            super.x_coord = new_x_coord;
            currentState[super.y_coord][super.x_coord].add(this);
        }

        protected void setY_coord(int new_y_coord) {
            super.y_coord = new_y_coord;
            currentState[super.y_coord][super.x_coord].add(this);
        }

        protected int getX_coord() {
            return super.x_coord;
        }

        protected int getY_coord() {
            return super.y_coord;
        }


        /*
         * This method getPopulation has to be modified by you if you are not using the population
         * ArrayList that has been provided in the starter code.  In any case, it has to be
         * implemented for grading tests to work.
         */
        protected static List<assignment5.Critter> getPopulation() {
            return population;
        }

        /*
         * This method getBabies has to be modified by you if you are not using the babies
         * ArrayList that has been provided in the starter code.  In any case, it has to be
         * implemented for grading tests to work.  Babies should be added to the general population
         * at either the beginning OR the end of every timestep.
         */
        protected static List<assignment5.Critter> getBabies() {
            return babies;
        }


    }
}
