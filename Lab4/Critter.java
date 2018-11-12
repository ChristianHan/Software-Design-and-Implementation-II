package assignment4;
/* CRITTERS Critter.java
 * EE422C Project 4 submission by
 * Keiran Crain
 * klc3788
 * Christian Han
 * CJH3752
 * Slip days used: <0>
 * Summer 2018
 */

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

/* see the PDF for descriptions of the methods and fields in this class
 * you may add fields, methods or inner classes to Critter ONLY if you make your additions private
 * no new public, protected or default-package code or data can be added to Critter
 */

public abstract class Critter {
	//Collections
	private	static List<Critter> population = new java.util.ArrayList<Critter>();
	private static List<Critter> babies = new java.util.ArrayList<Critter>();
    private static List<Critter>[][] currentState = new java.util.ArrayList[Params.world_height][Params.world_width];

    //Local Variables
    private int energy = 0;
    private static boolean initializeFlag = false;
    private boolean hasMoved = false;
    private int x_coord;
    private int y_coord;
    private static String myPackage;

    //Movement Related Functions
	//TODO: Write walk - Finsihed: Keiran
    /**
     *This function allows a critter to walk (1 space) in a given direction.
     * @param direction
     *
     */
	protected final void walk(int direction) {
	    if(!initializeFlag)
	        initialize();
        this.energy-=Params.walk_energy_cost;
        if(!hasMoved) {
            currentState[y_coord][x_coord].remove(this);
            move(direction, 1);
            currentState[y_coord][x_coord].add(this);
            hasMoved = true;
        }
	}

	//TODO: Write run - Finished: Keiran
    /**
     *This function allows a critter to run (2 spaces) in a given direction.
     * @param direction
     *
     */
	protected final void run(int direction) {
        if(!initializeFlag)
            initialize();
        this.energy-=Params.run_energy_cost;
        if(!hasMoved) {
            currentState[y_coord][x_coord].remove(this);
            move(direction, 2);
            currentState[y_coord][x_coord].add(this);
            hasMoved = true;
        }

    }

    //TODO: Write look. Returns the direction of the nearest empty space. Else, returns -1. -Finished: Keiran
    /**
     *This method finds the direction of the nearest empty space. If there are none, return -1.
     * @param distance
     *@return direction of nearest empty space, else -1
     */
    private int look(int distance){
        if(!initializeFlag)
            initialize();
	    int x = x_coord, y = y_coord;
	    move(0,distance);
	    if(currentState[y_coord][x_coord].size()<2){
	        x_coord = x;
	        y_coord = y;
	        return 0;
        }
        move(1,distance);
        if(currentState[y_coord][x_coord].size()<2){
            x_coord = x;
            y_coord = y;
            return 1;
        }
        move(2,distance);
        if(currentState[y_coord][x_coord].size()<2){
            x_coord = x;
            y_coord = y;
            return 2;
        }
        move(3,distance);
        if(currentState[y_coord][x_coord].size()<2){
            x_coord = x;
            y_coord = y;
            return 3;
        }
        move(4,distance);
        if(currentState[y_coord][x_coord].size()<2){
            x_coord = x;
            y_coord = y;
            return 4;
        }
        move(5,distance);
        if(currentState[y_coord][x_coord].size()<2){
            x_coord = x;
            y_coord = y;
            return 5;
        }
        move(6,distance);
        if(currentState[y_coord][x_coord].size()<2){
            x_coord = x;
            y_coord = y;
            return 6;
        }
        move(7,distance);
        if(currentState[y_coord][x_coord].size()<2){
            x_coord = x;
            y_coord = y;
            return 7;
        }
        x_coord = x;
        y_coord = y;
        return -1;

    }
    /**
     *This method sets the x and y coordinates of a critter based on a given direction and distance.
     * @param direction, distance
     *
     */

	private final void move (int direction, int distance){
        if(!initializeFlag)
            initialize();
		while (distance != 0){
			distance--;
			switch(direction){
				case 0: x_coord++; //Moving East
						if (x_coord == Params.world_width)
							x_coord = 0;
						break;
				case 1: x_coord++; //Moving Northeast
						if (x_coord == Params.world_width)
							x_coord = 0;
						y_coord--;
						if (y_coord == -1)
							y_coord = Params.world_height - 1;
						break;
				case 2: y_coord--; //Moving North
						if (y_coord == -1)
							y_coord = Params.world_height - 1;
						break;
				case 3: x_coord--; //Moving Northwest
						if (x_coord == -1)
							x_coord = Params.world_width -1;
						y_coord--;
						if (y_coord == -1)
							y_coord = Params.world_height - 1;
						break;
				case 4: x_coord--; //Moving West
						if (x_coord == -1)
							x_coord = Params.world_width -1;
						break;
				case 5: x_coord--; //Moving Southwest
						if (x_coord == -1)
							x_coord = Params.world_width -1;
						y_coord++;
						if (y_coord == Params.world_height)
							y_coord = 0;
						break;
				case 6: y_coord++; //Moving South
						if (y_coord == Params.world_height)
							y_coord = 0;
						break;
				case 7: x_coord++; //Moving Southeast
						if (x_coord == Params.world_width)
							x_coord = 0;
						y_coord++;
						if (y_coord == Params.world_height)
							y_coord = 0;
						break;
				default: break;	//Instructions say this will never happen
			}
		}
	}


	//Essential Game Functions
	//TODO: Write reproduce -Finished by Christian
	/*
	    /**
     *This function creates another critter based on its class;
     * @param Critter, direction
     *
     */
	protected final void reproduce(Critter offspring, int direction) {
        if(!initializeFlag)
            initialize();
		if(energy < Params.min_reproduce_energy){return;}

		offspring.energy = energy/2;//offspring gets half of parents energy rounded down
		energy = (int) Math.ceil((double)(energy/2));//reassign parent's energy to half (rounded up)

		offspring.x_coord = x_coord;
		offspring.y_coord = y_coord;
		offspring.move(direction,1);
        babies.add(offspring);
	}

	/**
	 * create and initialize a Critter subclass.
	 * critter_class_name must be the unqualified name of a concrete subclass of Critter, if not,
	 * an InvalidCritterException must be thrown.
	 * (Java weirdness: Exception throwing does not work properly if the parameter has lower-case instead of
	 * upper. For example, if craig is supplied instead of Craig, an error is thrown instead of
	 * an Exception.)
	 * @param critter_class_name
	 * @throws InvalidCritterException
	 */
	//TODO: Write makeCrittter FINISHED-Keiran
	public static void makeCritter(String critter_class_name) throws InvalidCritterException {
        if(!initializeFlag)
            initialize();
		Class<?> myCritter = null;
		Constructor<?> constructor = null;
		Object instanceOfMyCritter = null;
		String fullName = myPackage + "." + critter_class_name;
		try{
			myCritter = Class.forName(fullName);
		}
		catch (ClassNotFoundException e){ //How should we catch the casing error?
			throw new InvalidCritterException(critter_class_name);
		}

		try{
			constructor = myCritter.getConstructor();
			instanceOfMyCritter = constructor.newInstance();
		}
		catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e){
			throw new InvalidCritterException(critter_class_name);
		}
		if(!Critter.class.isAssignableFrom(myCritter)){
			throw new InvalidCritterException(critter_class_name);
		}
		Critter me = (Critter)instanceOfMyCritter;
		me.energy = Params.start_energy;

		/*
		me.x_coord = getRandomInt(Params.world_width);
		me.y_coord = getRandomInt(Params.world_height);
		population.add(me);
		currentState[me.x_coord][me.y_coord].add(me);
        */

        me.x_coord = getRandomInt(Params.world_width);
        me.y_coord = getRandomInt(Params.world_height);
        population.add(me);
        currentState[me.y_coord][me.x_coord].add(me);
	}

	/**
	 * Gets a list of critters of a specific type.
	 * @param critter_class_name What kind of Critter is to be listed.  Unqualified class name.
	 * @return List of Critters.
	 * @throws InvalidCritterException
	 */
	//TODO: Write getInstance
	public static List<Critter> getInstances(String critter_class_name) throws InvalidCritterException {
        if(!initializeFlag)
            initialize();
		List<Critter> result = new java.util.ArrayList<Critter>();
        Class<?> myCritter = null;
        Constructor<?> constructor = null;
        Object instanceOfMyCritter = null;
        String fullName = myPackage + "." + critter_class_name;
        try{
            myCritter = Class.forName(fullName);
        }
        catch (ClassNotFoundException e){ //How should we catch the casing error?
            throw new InvalidCritterException(critter_class_name);
        }

        try{
            constructor = myCritter.getConstructor();
            instanceOfMyCritter = constructor.newInstance();
        }
        catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e){
            throw new InvalidCritterException(critter_class_name);
        }
        if(!Critter.class.isAssignableFrom(myCritter)){
            throw new InvalidCritterException(critter_class_name);
        }
	    for(int i = 0; i < population.size(); i++){
            if(myCritter.isInstance(population.get(i)))
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
        if(!initializeFlag)
            initialize();
	    //Simulate the time steps for everyone
		for(int i = 0; i < population.size();i++) {//iterating through every critter in list of Critters
            population.get(i).doTimeStep();
        }

        //Simulate the encounters for everyone after every critter has done their doTimeStep function
        encounter();

        //Add the babies to population and current state and clear the babies list
        for(int i = 0; i < babies.size();i++){
            Critter c = babies.get(i);
            population.add(c);
            currentState[c.y_coord][c.x_coord].add(c);
            babies.remove(c);
        }

        //Decrementing every Critter by restEnergy
        for(int i = 0; i < currentState.length; i++) {
            for (int j = 0; j < currentState[i].length; j++)
                for (int z = 0; z < currentState[i][j].size(); z++)
                    currentState[i][j].get(z).energy -= Params.rest_energy_cost;
        }

        //Removing all of the dead critters
        for(int i = 0; i < population.size(); i++)
            if(population.get(i).energy <= 0)
                population.remove(i);

        for(int i = 0; i < currentState.length; i++) {
            for (int j = 0; j < currentState[i].length; j++)
                for (int z=0 ; z < currentState[i][j].size(); z++)
                    if (currentState[i][j].get(z).energy <= 0)
                        currentState[i][j].remove(z);
        }

        //Resetting hasMoved variable
        for(int i = 0; i < population.size(); i++) {
            population.get(i).hasMoved = false;
        }
        //Adding Algae
        for(int i = 0; i < Params.refresh_algae_count; i++)
            try {
                makeCritter("Algae");
            }
            catch (InvalidCritterException e){
                System.out.println("Something is wrong.");
            }
	}


	//TODO: displayWorld - Finished: Christian
    /**
     *This function when called prints the current world to the console.
     *
     */
	public static void displayWorld() {
        if(!initializeFlag)
            initialize();
		int height = Params.world_height + 2;
		int width = Params.world_width + 2;
		String world[][] = new String [height][width];

		for(int col = 0; col < width; col++){ //prints the top border of the world
			if(col == 0 || col == width - 1) {
				world[0][col] = "+";
			}
			else
				world[0][col] = "-";
		}

		for(int row = 1; row < height - 1; row++){//prints the side borders of the world
			world[row][0] = "|";
			world[row][width - 1] = "|";
		}

		for(int col = 0; col < width; col++){//prints the botton border of the world
			if(col == 0 || col == width - 1) {
				world[height - 1][col] = "+";
			}
			else
				world[height - 1][col] = "-";
		}

		//puts the critters on the board
        for(int i = 0; i < population.size(); i++){
		    int x= population.get(i).x_coord + 1;
		    int y = population.get(i).y_coord + 1;
		    world[y][x] = population.get(i).toString();
		}


		for(int y = 0; y < height; y++){
			for (int x = 0; x < width; x++){
				if (world[y][x] != null)
					System.out.print(world[y][x]);
				else
					System.out.print(" ");
			}
			System.out.println();
		}
	}

    /**
     * Clears the world of all critters, dead and alive
     */
    //TODO: Clear World - Finished: -Keiran
    public static void clearWorld() {
        if(!initializeFlag)
            initialize();
        // Complete this method.
        population = new java.util.ArrayList<Critter>();
        babies = new java.util.ArrayList<Critter>();
        currentState = new ArrayList[Params.world_height][Params.world_width];
        initializeFlag = false;
    }

	//Helper function for worldTimeStep that handles encounter
    //If a creature if want to flee but their is a creature in the way, it should die.
	private static void encounter(){
        if(!initializeFlag)
            initialize();
        for(int i = 0; i < currentState.length; i ++){
            for(int j = 0; j < currentState[i].length; j++){
                while(currentState[i][j].size() > 1){
                    //Retrieve the two fighters from the cell
                    Critter fighterA = currentState[i][j].get(0);
                    Critter fighterB = currentState[i][j].get(1);

                    //Check fighting conditions of both
                    boolean aWish = fighterA.fight(fighterB.toString());
                    boolean bWish = fighterB.fight(fighterA.toString());

                    //Act on fighting conditions of both
                    if(fighterA.energy <= 0) {
                        currentState[i][j].remove(fighterA);
                        population.remove(fighterA);
                    }
                    if(fighterB.energy <= 0) {
                        currentState[i][j].remove(fighterB);
                        population.remove(fighterB);
                    }
                    if(fighterA.energy > 0 && fighterB.energy > 0 &&
                            fighterA.x_coord == j && fighterB.x_coord ==j &&
                            fighterA.y_coord == i && fighterB.y_coord ==i){
                        int aOdds = 0, bOdds = 0;
                        if(aWish)
                            aOdds = getRandomInt(fighterA.energy);
                        if(bWish)
                            bOdds = getRandomInt(fighterB.energy);
                        //If there is a winner of a battle, do the energy mumbo jumbo
                        if(aOdds >= bOdds){ //A is the winner
                            fighterA.energy += (fighterB.energy/2);
                            currentState[i][j].remove(fighterB);
                            population.remove(fighterB);
                        }
                        else{ //B is the winner
                            fighterB.energy += (fighterA.energy/2);
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

    private static void initialize(){
        for(int i = 0; i < currentState.length; i++)
            for(int j = 0; j < currentState[i].length; j++)
                currentState[i][j] = new ArrayList<Critter>();
        initializeFlag = true;
    }

    //Provided Functions
    static {
        myPackage = Critter.class.getPackage().toString().split(" ")[1];

    }

    protected int getEnergy() { return energy; }

    //getRandomInt functionality
    private static java.util.Random rand = new java.util.Random();

    public static int getRandomInt(int max) {
        return rand.nextInt(max);
    }

    public static void setSeed(long new_seed) {
        rand = new java.util.Random(new_seed);
    }

    /* a one-character long string that visually depicts your critter in the ASCII interface */
    public String toString() { return ""; }
    /**
     * Prints out how many Critters of each type there are on the board.
     * @param critters List of Critters.
     */
    public static void runStats(List<Critter> critters) {
        System.out.print("" + critters.size() + " critters as follows -- ");
        java.util.Map<String, Integer> critter_count = new java.util.HashMap<String, Integer>();
        for (Critter crit : critters) {
            String crit_string = crit.toString();
            Integer old_count = critter_count.get(crit_string);
            if (old_count == null) {
                critter_count.put(crit_string,  1);
            } else {
                critter_count.put(crit_string, old_count.intValue() + 1);
            }
        }
        String prefix = "";
        for (String s : critter_count.keySet()) {
            System.out.print(prefix + s + ":" + critter_count.get(s));
            prefix = ", ";
        }
        System.out.println();
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
    static abstract class TestCritter extends Critter {
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
        protected static List<Critter> getPopulation() {
            return population;
        }

        /*
         * This method getBabies has to be modified by you if you are not using the babies
         * ArrayList that has been provided in the starter code.  In any case, it has to be
         * implemented for grading tests to work.  Babies should be added to the general population
         * at either the beginning OR the end of every timestep.
         */
        protected static List<Critter> getBabies() {
            return babies;
        }
    }
}
