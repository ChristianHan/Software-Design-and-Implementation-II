/* WORD LADDER Main.java
 * EE422C Project 3 submission by
 * Replace <...> with your actual data.
 * <Student1 Name>
 * <Student1 EID>
 * <Student1 5-digit Unique No.>
 * <Student2 Name>
 * <Student2 EID>
 * <Student2 5-digit Unique No.>
 * Slip days used: <0>
 * Git URL:
 * Fall 2017
 */

package assignment3;

import javax.lang.model.type.ArrayType;
import java.lang.reflect.Array;
import java.util.*;
import java.io.*;

public class Main {
	
	// static variables and constants only here.
	public static Set<String> dict;
	public static  char[] ALPHABET ;

	public static void main(String[] args) throws Exception {
		
		Scanner kb;	// input Scanner for commands
		PrintStream ps;	// output file, for student testing and grading only
		// If arguments are specified, read/write from/to files instead of Std IO.
		if (args.length != 0) {
			kb = new Scanner(new File(args[0]));
			ps = new PrintStream(new File(args[1]));
			System.setOut(ps);			// redirect output to ps
		} else {
			kb = new Scanner(System.in);// default input from Stdin
			ps = System.out;			// default output to Stdout
		}
		initialize();

		// TESTING IN MAIN

        //Test case 1
        //Actually looking for a ladder
		String string1 = "cream";//TYPE start here
		String string2 = "apple";//TYPE end here

		ArrayList<String> bfsTest = getWordLadderBFS(string1 , string2);
		ArrayList<String> dfsTest = getWordLadderDFS(string1 , string2);

		System.out.println("DFS ladder: " + validate(dfsTest));
		System.out.println("BFS ladder: " + validate(bfsTest));
		printLadder(bfsTest);
        System.out.println();


		//Test case 2
        //No ladder exists
        string1 = "guava";//TYPE start here
        string2 = "xylyl";//TYPE end here

        bfsTest = getWordLadderBFS(string1 , string2);
        dfsTest = getWordLadderDFS(string1 , string2);

        System.out.println("DFS ladder: " + validate(dfsTest));
        System.out.println("BFS ladder: " + validate(bfsTest));
        printLadder(bfsTest);
        System.out.println();
        /*
         */

        //Test case 3
        //Case Sensitivity
        string1 = "mElOn";//TYPE start here
        string2 = "ToXiC";//TYPE end here

        bfsTest = getWordLadderBFS(string1 , string2);
        dfsTest = getWordLadderDFS(string1 , string2);

        System.out.println("DFS ladder: " + validate(dfsTest));
        System.out.println("BFS ladder: " + validate(bfsTest));
        printLadder(bfsTest);
        System.out.println();

        //Test case 4
        //Random Test
        string1 = "lemon";//TYPE start here
        string2 = "mango";//TYPE end here

        bfsTest = getWordLadderBFS(string1 , string2);
        dfsTest = getWordLadderDFS(string1 , string2);

        System.out.println("DFS ladder: " + validate(dfsTest));
        System.out.println("BFS ladder: " + validate(bfsTest));
        printLadder(bfsTest);
        System.out.println();

        //Test case 5
        //Random Test
        string1 = "peach";//TYPE start here
        string2 = "prune";//TYPE end here

        bfsTest = getWordLadderBFS(string1 , string2);
        dfsTest = getWordLadderDFS(string1 , string2);

        System.out.println("DFS ladder: " + validate(dfsTest));
        System.out.println("BFS ladder: " + validate(bfsTest));
        printLadder(bfsTest);
        System.out.println();

        //Input test case
        ArrayList<String> input = parse(kb);
        while(input.size() != 0){
            String i1 = input.get(0);//TYPE start here
            String i2 = input.get(1);//TYPE end here

            bfsTest = getWordLadderBFS(i1 , i2);
            dfsTest = getWordLadderDFS(i1 , i2);

            System.out.println("DFS ladder: " + validate(dfsTest));
            System.out.println("BFS ladder: " + validate(bfsTest));
            printLadder(bfsTest);
            System.out.println();

            input = parse(kb);
        }

	}

	/*
    initialize your static variables or constants here.
     We will call this method before running our JUNIT tests.  So call it
     only once at the start of main
	*/
	public static void initialize() {

		dict = makeDictionary();
		ALPHABET = new char[]{'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'};
    }

    /*
     * @param keyboard Scanner connected to System.in
     * @return ArrayList of Strings containing start word and end word.
     * If command is /quit, return empty ArrayList.
     */
    public static ArrayList<String> parse(Scanner keyboard) {

	    String temp = keyboard.next();
        ArrayList<String> list = new ArrayList<String>();
        //Checks to see if terminating command
        if(temp.equals("/quit"))
            return list;
        list.add(temp);
        temp = keyboard.next();
        list.add(temp);
        return list;
    }

    /**
     * This method is used to find a valid word ladder using BFS between two words.
     * @param start This is the first parameter to getWordLadderBFS method
     * @param end This is the second parameter to getWordLadderBFS method
     * @return ArrayList<String> containing a word ladder between start and end if it exists.
     */
    public static ArrayList<String> getWordLadderBFS(String start, String end) {

        start = start.toUpperCase();
        end = end.toUpperCase();


        Queue<Word> graph = new LinkedList<>();
        Set<String> visited = new HashSet<>();

        ArrayList<String> ladder = new ArrayList<>();
        ladder.add(start);
        ladder.add(end);

        boolean adder = graph.add(new Word(start, null));
        adder = visited.add(start);

        while (graph.size() != 0) {
            Word currentWord = graph.poll();
            String currentItem = currentWord.getName();
            if (currentItem.equals(end)) {
                ladder = generateLadder(currentWord);
            } else {
                ArrayList<String> nextTier = getNeighbors(currentItem, dict);
                Set<String> nextTierSet = new HashSet<>();
                adder = nextTierSet.addAll(nextTier);
                ArrayList<String> removeList = new ArrayList<>();
                for (String word : nextTierSet) {
                    if (visited.contains(word))
                        removeList.add(word);
                }
                adder = nextTierSet.removeAll(removeList);
                for (String word : nextTierSet) {
                    adder = graph.add(new Word(word, currentWord));
                }
                adder = visited.addAll(nextTierSet);
            }
        }
        return ladder; // replace this line later with real return

    }

    /**
     * This method is used to find a valid word ladder using DFS between two words.
     * @param start This is the first parameter to getWordLadderDFS method
     * @param end This is the second parameter to getWordLadderDFS method
     * @return ArrayList<String> containing a word ladder between start and end if it exists.
     */
    public static ArrayList<String> getWordLadderDFS(String start, String end) {

        ArrayList<String> wordLadder = new ArrayList<String>();
        HashSet<String> visitedNodes = new HashSet<String>();


        start = start.toUpperCase();
        end = end.toUpperCase();

        if(dfsFind(start, end, visitedNodes, wordLadder)){
            //wordLadder.add(start);
            //wordLadder.add(end);
            Collections.reverse(wordLadder);
            return wordLadder;
        }

        else{
            ArrayList<String> emptyLadder = new ArrayList<>();
            emptyLadder.add(start);
            emptyLadder.add(end);
            return emptyLadder;
        }
    }

    /**
     * @param ArrayList<String> ladder contains either a valid word ladder to be printed, or just two words that signifies
     *  no word was found.
     * @output Prints the word ladder or that no word ladder was found.
     */
    public static void printLadder(ArrayList<String> ladder) {

        if(ladder.size() == 2)
            System.out.println("no word ladder can be found between " + ladder.get(0).toLowerCase() + " and " + ladder.get(1).toLowerCase() + ".");
        else{
            System.out.println("a " + (ladder.size() - 2) + "-rung word ladder exists between " + ladder.get(0) + " and " + ladder.get(ladder.size()-1) + ".");
            for (int i = 0; i < ladder.size(); i++)
                System.out.println(ladder.get(i).toLowerCase());
        }
    }

    private static boolean dfsFind (String current, String end, HashSet<String> visitedNodes, ArrayList<String> wordLadder){

		if(current.equals(end)){
			wordLadder.add(current);
			return true;
		}

		if(visitedNodes.contains(current)){ return false; }

		visitedNodes.add(current);//marks node as visited

		ArrayList<String> neighbors = getNeighbors(current, dict);//gets neighbors of current

		//https://courses.cs.washington.edu/courses/cse326/03su/homework/hw3/dfs.html
        //Sorts arrayList

        //Selection Sort
        for (int i = 0; i < neighbors.size(); i++){
            int currentPos = i;
            for (int j = 0; j < neighbors.size(); j++){
                if(neighborDifference(neighbors.get(j),end) < neighborDifference(neighbors.get(i), end))
                    currentPos = j;
            }
            String temp = neighbors.get(currentPos);
            neighbors.set(currentPos, neighbors.get(i));
            neighbors.set(i, temp);
        }

        for(int i = 0; i < neighbors.size(); i++){//want to iterate neighbor over list of neighbors
            String neighbor = neighbors.get(i);
			if(!visitedNodes.contains(neighbor)){ //if neighbor is unvisited
				if(dfsFind(neighbor, end, visitedNodes, wordLadder)) {
					wordLadder.add(current);
					return true;
				}
			}
		}
		return false;
	}

	private static int neighborDifference (String string1, String string2) {//string1.length == string2.length

		int difference = 0;

		for (int i = 0; i < string1.length(); i++) {

			if (string1.charAt(i) != string2.charAt(i)) {
				difference = difference + 1;
			}

		}

		return difference;
	}

    /**
     * This method is used to find all the neighbors of a String that are in the dictionary.
     * @param word This is the first parameter to getNeighbors method
     * @param dict This is the second parameter to getNeighbors method
     * @return ArrayList<String> of all the neighbors of word in the Set dict.
     */
	public static ArrayList<String> getNeighbors(String word, Set<String> dict){


		ArrayList<String> allPossibleWords = new ArrayList<String>();

		char [] tempChar = word.toCharArray();

		for(int i = 0; i < tempChar.length; i++){

			tempChar = word.toCharArray();

			for(int j = 0; j < ALPHABET.length; j++){

				tempChar[i] = ALPHABET[j];
				String tempName = new String(tempChar).toUpperCase();
				allPossibleWords.add(tempName);

			}
		}

		ArrayList<String> tempRemove = new ArrayList<String>();

		tempRemove.add(word);
		allPossibleWords.removeAll(tempRemove);

		ArrayList<String> finalPossibleWords = new ArrayList<String>();

		for(int i = 0; i < allPossibleWords.size(); i++){
			if(dict.contains(allPossibleWords.get(i))){
				finalPossibleWords.add(allPossibleWords.get(i));
				}
			}
		return finalPossibleWords;
	}

	private static ArrayList<String> generateLadder(Word end){
			ArrayList<String> ladder = new ArrayList<String>();
			Word temp = end;
			while(temp != null){
				ladder.add(temp.getName());
				temp = temp.getParent();
			}
			Collections.reverse(ladder);
			return ladder;
		}

	public static Set<String>  makeDictionary () {
		Set<String> words = new HashSet<String>();
		Scanner infile = null;
		try {
			infile = new Scanner (new File("five_letter_words.txt"));
		} catch (FileNotFoundException e) {
			System.out.println("Dictionary File not Found!");
			e.printStackTrace();
			System.exit(1);
		}
		while (infile.hasNext()) {
			words.add(infile.next().toUpperCase());
		}
		return words;
	}

    private static boolean validate(ArrayList<String> ladder){
	    Set<String> ladderSet = new HashSet<>();
	    ladderSet.addAll(ladder);
	    if(ladder.size() != ladderSet.size())
	        return false;
	    for(int i = 1; i < ladder.size(); i++)
	        if(!isNeighbor(ladder.get(i), ladder.get(i-1)))
	            return false;
	    if (ladder.size() == 2)
	        return false;
	    return true;
    }

    private static boolean isNeighbor (String string1, String string2){
        int difference = 0;
        for(int i = 0; i < string1.length(); i++){
            if(string1.charAt(i) != string2.charAt(i)){
                difference++;
            }
            if(difference > 1){
                return false;
            }
        }
        return true;
    }
}

