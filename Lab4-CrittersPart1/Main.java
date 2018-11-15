package assignment4;
/* CRITTERS Main.java
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
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.io.*;



/*
 * Usage: java <pkgname>.Main <input file> test
 * input file is optional.  If input file is specified, the word 'test' is optional.
 * May not use 'test' argument without specifying input file.
 */
public class Main {

    static Scanner kb;    // scanner connected to keyboard input, or input file
    private static String inputFile;    // input file, used instead of keyboard input if specified
    static ByteArrayOutputStream testOutputString;    // if test specified, holds all console output
    private static String myPackage;    // package of Critter file.  Critter cannot be in default pkg.
    private static boolean DEBUG = false; // Use it or not, as you wish!
    static PrintStream old = System.out;    // if you want to restore output to console


    // Gets the package name.  The usage assumes that Critter and its subclasses are all in the same package.
    static {
        myPackage = Critter.class.getPackage().toString().split(" ")[1];
    }


    /**
     * Main method.
     *
     * @param args args can be empty.  If not empty, provide two parameters -- the first is a file name,
     *             and the second is test (for test output, where all output to be directed to a String), or nothing.
     */
    public static void main(String[] args) {
        if (args.length != 0) {
            try {
                inputFile = args[0];
                kb = new Scanner(new File(inputFile));
            } catch (FileNotFoundException e) {
                System.out.println("USAGE: java Main OR java Main <input file> <test output>");
                e.printStackTrace();
            } catch (NullPointerException e) {
                System.out.println("USAGE: java Main OR java Main <input file>  <test output>");
            }
            if (args.length >= 2) {
                if (args[1].equals("test")) { // if the word "test" is the second argument to java
                    // Create a stream to hold the output
                    testOutputString = new ByteArrayOutputStream();
                    PrintStream ps = new PrintStream(testOutputString);
                    // Save the old System.out.
                    old = System.out;
                    // Tell Java to use the special stream; all console output will be redirected here from now
                    System.setOut(ps);
                }
            }
        } else { // if no arguments to main
            kb = new Scanner(System.in); // use keyboard and console
        }

        // TODO: 7/15/18 if its one word command: follow quit error processing
        // TODO: 7/15/18 for make and step: similar to quit processing, also check for case where next input wasn't an integer.

        while (true) {
            System.out.println("critter>");
            String input = kb.nextLine();

            String[] parts = input.split(" ");
            String part1 = parts[0];
            //String part2 = parts[1];
            //String part3 = parts[2];

            if (part1.equals("quit")) {
                if (parts.length == 1){
                    break;
                }
                else
                    errorProcessing(parts);
            }
            else if (part1.equals("show")) {
                if (parts.length == 1){
                    Critter.displayWorld();
                }
                else
                    errorProcessing(parts);
            }
            else if (part1.equals("step")) {
                if(parts.length == 2) {
                    try {
                        for (int i = 0; i < Integer.parseInt(parts[1]); i++) { //try/catch exception on parts[1] is an integer or not (numberformatexception)
                            Critter.worldTimeStep();
                        }
                    } catch (NumberFormatException e) {
                        errorProcessing(parts);
                    }
                }
                else if (parts.length >=3){
                    errorProcessing(parts);
                }
                else
                    Critter.worldTimeStep();
            }
            else if (part1.equals("seed")) {
                if(parts.length != 2)
                    errorProcessing(parts);
                else {
                    try {
                        Critter.setSeed(Long.parseLong(parts[1]));
                    } catch (NumberFormatException e) {
                        errorProcessing(parts);
                    }
                }
            }
            else if (part1.equals("make")) {
                if(parts.length == 3)
                    try {
                        for (int i = 0; i < Integer.parseInt(parts[2]); i++)
                        {
                            Critter.makeCritter(parts[1]);
                        }

                } catch (InvalidCritterException | NumberFormatException e) {
                        errorProcessing(parts);
                }
                else if (parts.length > 3){
                    errorProcessing(parts);
                }
                else
                    try{
                    Critter.makeCritter(parts[1]);
                    }
                    catch (InvalidCritterException e){
                        errorProcessing(parts);
                    }

            }
            else if (part1.equals("stats")) {
                if (parts.length != 2)
                    errorProcessing(parts);
                else{
                    try {
                        List<Critter> result = Critter.getInstances(parts[1]);
                        Class<?> myCritter = null;
                        String fullName = myPackage + "." + parts[1];
                        try {
                            myCritter = Class.forName(fullName);
                        } catch (ClassNotFoundException e) { //How should we catch the casing error?
                            throw new InvalidCritterException(parts[1]);
                        }
                        if (!Critter.class.isAssignableFrom(myCritter)) {
                            throw new InvalidCritterException(parts[1]);
                        }
                        try {
                            Method m = myCritter.getMethod("runStats", List.class);
                            m.invoke(List.class, result);
                        } catch (NoSuchMethodException e) {
                            Critter.runStats(result);
                        } catch (IllegalAccessException e) {
                            errorProcessing(parts);
                        } catch (InvocationTargetException e) {
                            errorProcessing(parts);
                        }
                    } catch (InvalidCritterException e) {
                        errorProcessing(parts);
                    }
                }
            }
            else{
                String error = "invalid command:";
                for(int i = 0; i < parts.length; i++)
                    error = error + " " + parts[i];
                System.out.println(error);
            }


            System.out.flush();

        }

    }

    private static void errorProcessing(String[] array){
        String error = "error processing:";
        for (int i = 0; i < array.length; i++)
            error = error + " " + array[i];
        System.out.println(error);
    }
}

