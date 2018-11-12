package assignment5;
/* CRITTERS Main.java
 * EE422C Project 5 submission by
 * Keiran Crain
 * klc3788
 * Christian Han
 * CJH3752
 * Slip days used: <0>
 * Summer 2018
 */

import javafx.animation.AnimationTimer;
import javafx.application.Application;

import javafx.scene.control.ScrollPane;
import javafx.scene.shape.Rectangle;

import java.lang.reflect.Method;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.scene.text.Text;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;

import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.io.*;

import static assignment5.Critter.*;


/*
 * Usage: java <pkgname>.Main <input file> test
 * input file is optional.  If input file is specified, the word 'test' is optional.
 * May not use 'test' argument without specifying input file.
 */
public class Main extends Application {
    static Scanner kb;    // scanner connected to keyboard input, or input file
    private static String inputFile;    // input file, used instead of keyboard input if specified
    static ByteArrayOutputStream testOutputString;    // if test specified, holds all console output
    private static String myPackage;    // package of Critter file.  Critter cannot be in default pkg.
    private static boolean DEBUG = false; // Use it or not, as you wish!
    static PrintStream old = System.out;    // if you want to restore output to console
    GridPane outputGrid;

    // Gets the package name.  The usage assumes that Critter and its subclasses are all in the same package.
    static {
        myPackage = Critter.class.getPackage().toString().split(" ")[1];
    }



    @Override
    public void start(Stage primaryStage) throws Exception {
        try {
            GridPane grid = new GridPane();
            Pane p = new StackPane();
            ColumnConstraints col0 = new ColumnConstraints();
            ColumnConstraints col1 = new ColumnConstraints();
            RowConstraints row0 = new RowConstraints();
            Button btn_stats = new Button();


            col0.setPercentWidth(25);
            col1.setPercentWidth(75);
            row0.setPercentHeight(100);
            grid.getColumnConstraints().addAll(col0,col1);
            grid.getRowConstraints().add(row0);

            GridPane inputGrid = new GridPane();
            ColumnConstraints icol0 = new ColumnConstraints();
            RowConstraints irow0 = new RowConstraints();
            RowConstraints irow1 = new RowConstraints();
            RowConstraints irow2 = new RowConstraints();
            RowConstraints irow3 = new RowConstraints();
            RowConstraints irow4 = new RowConstraints();
            RowConstraints irow5 = new RowConstraints();
            RowConstraints irow6 = new RowConstraints();
            RowConstraints irow7 = new RowConstraints();

            icol0.setPercentWidth(100);
            irow0.setPercentHeight(25);
            irow1.setPercentHeight(10);
            irow2.setPercentHeight(10);
            irow3.setPercentHeight(10);
            irow4.setPercentHeight(10);
            irow5.setPercentHeight(5);
            irow6.setPercentHeight(5);
            irow7.setPercentHeight(25);

            inputGrid.getRowConstraints().addAll(irow0,irow1,irow2,irow3,irow4,irow5,irow6);
            inputGrid.getColumnConstraints().add(icol0);

            //TODO: VBOX START
            VBox vbox = new VBox();
            //vbox.setPadding(new Insets(10));
            //vbox.setSpacing(8);

            Text title = new Text("World");
            title.setFont(Font.font("Arial", FontWeight.BOLD, 14));
            vbox.getChildren().add(title);
            Text choices[] = new Text[] {
                    new Text("Set Seed"),
                    new Text("Set X"),
                    new Text("Set Y")};
            TextField fields[] = new TextField[]{
                    new TextField("enter seed"),
                    new TextField(((Integer)Params.world_height).toString()),
                    new TextField(((Integer)Params.world_width).toString())
            };


            for (int i=0; i<choices.length; i++) {
                //VBox.setMargin(choices[i], new Insets(0, 0, 0, 1));
                vbox.getChildren().add(choices[i]);
                //VBox.setMargin(fields[i], new Insets(0, 0, 0, 1));
                vbox.getChildren().add(fields[i]);
            }

            //VBox.setVgrow(, Priority.ALWAYS);

            Button btn_makeWorld = new Button();
            btn_makeWorld.setText("Make World");

            btn_makeWorld.setOnAction(new EventHandler<ActionEvent>() {

                @Override
                public void handle(ActionEvent event) {
                    try {
                        try {
                            setSeed(Integer.parseInt(fields[0].getText()));
                        }catch (NumberFormatException e){
                            System.out.println("No seed found.");
                        }
                        int cols = Integer.parseInt(fields[1].getText());
                        int rows = Integer.parseInt(fields[2].getText());
                        Params.world_height = rows;
                        Params.world_width = cols;
                        double rowHeight = grid.getHeight()/rows;
                        double colWidth = (grid.getWidth() * 0.75)/cols;
                        if(outputGrid != null && outputGrid.getChildren().size() !=0)
                            outputGrid.getChildren().clear();
                        outputGrid = initializeOutputGrid(rows,rowHeight, cols, colWidth);
                        outputGrid.setMaxSize(grid.getWidth()*0.75, grid.getHeight());
                        outputGrid.setHgap(0);
                        outputGrid.setVgap(0);
                        grid.add(outputGrid, 1, 0);
                        clearWorld();
                    }catch (NumberFormatException e){
                        //TODO: Change this when output console is set up
                        System.out.println("You done goofed");
                    }

                }
            });

            vbox.getChildren().add(btn_makeWorld);

            vbox.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
            inputGrid.add(vbox,0,0);
            GridPane.setFillWidth(vbox, true);
            GridPane.setFillHeight(vbox,true);



            //TODO: VBOX END

            HBox hbox = new HBox();
            hbox.setSpacing(8);
            Text title1 = new Text("Make Critter");
            title1.setFont(Font.font("Arial", FontWeight.BOLD, 14));
            hbox.getChildren().add(title1);

//////////////////////////


            ObservableList<String> options = FXCollections.observableArrayList();

            String[] allCritters = getCritterClasses(myPackage);


            final ComboBox critterComboBox = new ComboBox(options);
            for(int i = 0; i < allCritters.length; i++){
                critterComboBox.getItems().add(allCritters[i]);
            }



/////////////////////////

            hbox.getChildren().add(critterComboBox);

            hbox.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
            inputGrid.add(hbox, 0, 1);
            GridPane.setFillHeight(hbox, true);
            GridPane.setFillWidth(hbox, true);

            HBox hbox1 = new HBox();
            hbox1.setSpacing(8);
            Text title2 = new Text("# of Critters: ");
            title2.setFont(Font.font("Arial", FontWeight.BOLD, 14));
            hbox1.getChildren().add(title2);

            TextField textfield1 = new TextField("1");
            hbox1.getChildren().add(textfield1);

            Button btn_make = new Button();
            btn_make.setText("Make");

            btn_make.setOnAction(new EventHandler<ActionEvent>() {

                @Override
                public void handle(ActionEvent event) {
                    try{
                        String critterToMake = critterComboBox.getSelectionModel().getSelectedItem().toString();

                        int numCritterToMake = Integer.parseInt(textfield1.getText());

                        if(numCritterToMake == (int)numCritterToMake){///how to check if textfield1 is an integer
                            for(int i = 0; i < numCritterToMake; i++){
                                Critter.makeCritter(critterToMake);
                            }
                        }

                        else{
                            Critter.makeCritter(critterToMake);
                        }
                        displayWorld(outputGrid);

                    }catch(NumberFormatException | InvalidCritterException e){

                    }

                }
            });

            hbox1.getChildren().add(btn_make);


            hbox1.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
            inputGrid.add(hbox1, 0, 2);
            GridPane.setFillHeight(hbox, true);
            GridPane.setFillWidth(hbox, true);

            HBox hbox2 = new HBox();
            hbox2.setSpacing(8);
            Text title3 = new Text("# of Steps: ");
            title3.setFont(Font.font("Arial", FontWeight.BOLD, 14));
            hbox2.getChildren().add(title3);
            TextField textfield3 = new TextField("1");
            hbox2.getChildren().add(textfield3);//puts a text field with "#"


            Button btn_step = new Button();
            btn_step.setText("Step");
            
            btn_step.setOnAction(new EventHandler<ActionEvent>() {

                @Override
                    public void handle(ActionEvent event) {
                    try{
                        int stepCount = Integer.parseInt(textfield3.getText());
                        for(int i = 0; i < stepCount; i++)
                            Critter.worldTimeStep();
                    }catch (NumberFormatException e){ //////////are we to print something here like stack?

                    }
                    displayWorld(outputGrid);
                    btn_stats.fire();
                }
            });

            hbox2.getChildren().add(btn_step);


            hbox2.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
            inputGrid.add(hbox2, 0, 3);
            GridPane.setFillHeight(hbox, true);
            GridPane.setFillWidth(hbox, true);

            HBox hbox3 = new HBox();
            hbox3.setSpacing(8);
            Text title4 = new Text("Run Stats");
            title4.setFont(Font.font("Arial", FontWeight.BOLD, 14));
            hbox3.getChildren().add(title4);

            ObservableList<String> options1 = FXCollections.observableArrayList();

            String[] allCritters1 = getCritterClasses(myPackage);


            final ComboBox comboBox1 = new ComboBox(options1);
            comboBox1.getItems().add("");
            comboBox1.getSelectionModel().selectFirst();
            for(int i = 0; i < allCritters1.length; i++){
                comboBox1.getItems().add(allCritters1[i]);
            }
            hbox3.getChildren().add(comboBox1);

            btn_stats.setText("Stats");

            HBox hboxanimation = new HBox();
            hboxanimation.setSpacing(8);
            Text titleAnimation = new Text("Animation");
            titleAnimation.setFont(Font.font("Arial", FontWeight.BOLD, 14));
            hboxanimation.getChildren().add(titleAnimation);

            TextField textfieldAnimation = new TextField("Enter Step #");
            hboxanimation.getChildren().add(textfieldAnimation);//puts a text field with "#"

            Button btn_startAnimation = new Button();
            btn_startAnimation.setText("Start");
            AnimationTimer timer = new AnimationTimer() {
                private long lastUpdate = 0;
                @Override
                public void handle(long now) {
                    if (now - lastUpdate >= 200000000) {
                        try {
                            int stepCount = Integer.parseInt(textfieldAnimation.getText());
                            for (int i = 0; i < stepCount; i++)
                                Critter.worldTimeStep();
                        } catch (NumberFormatException e) { //////////are we to print something here like stack?
                            System.out.println("Invalid Animation input");
                        }
                        displayWorld(outputGrid);
                        lastUpdate = now;
                        btn_stats.setDisable(false);
                        btn_stats.fire();
                        btn_stats.setDisable(true);
                    }
                }
            };
            btn_startAnimation.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    btn_make.setDisable(true);
                    critterComboBox.setDisable(true);
                    textfield1.setDisable(true);
                    btn_make.setDisable(true);
                    textfield3.setDisable(true);
                    btn_step.setDisable(true);
                    btn_stats.setDisable(true);
                    btn_startAnimation.setDisable(true);
                    textfieldAnimation.setDisable(true);
                    comboBox1.setDisable(true);
                   timer.start();
                }
            });
            Button btn_stopAnimation = new Button();
            btn_stopAnimation.setText("Stop");
            btn_stopAnimation.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    timer.stop();
                    btn_make.setDisable(false);
                    critterComboBox.setDisable(false);
                    textfield1.setDisable(false);
                    btn_make.setDisable(false);
                    textfield3.setDisable(false);
                    btn_step.setDisable(false);
                    btn_stats.setDisable(false);
                    btn_startAnimation.setDisable(false);
                    textfieldAnimation.setDisable(false);
                    comboBox1.setDisable(false);
                }
            });

            hboxanimation.getChildren().add(btn_startAnimation);
            hboxanimation.getChildren().add(btn_stopAnimation);


            hboxanimation.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
            inputGrid.add(hboxanimation, 0, 4);
            GridPane.setFillHeight(hboxanimation, true);
            GridPane.setFillWidth(hboxanimation, true);


            BorderPane bp = new BorderPane();

            VBox vBox1 = new VBox();
            vBox1.setSpacing(8);
            Text runStatsbox = new Text("Stats");
            runStatsbox.setFont(Font.font("Arial", FontWeight.BOLD, 14));
            vBox1.getChildren().add(runStatsbox);
            ScrollPane scroll = new ScrollPane();

            btn_stats.setOnAction(new EventHandler<ActionEvent>() { /////////////// fill code with action on stats

                @Override
                public void handle(ActionEvent event) {

                    String critterToMake = comboBox1.getSelectionModel().getSelectedItem().toString();

                    if(critterToMake != "") {
                        try {
                            List<Critter> result = Critter.getInstances(critterToMake);
                            Class<?> myCritter = null;
                            String fullName = myPackage + "." + critterToMake;
                            try {
                                myCritter = Class.forName(fullName);
                            } catch (ClassNotFoundException e) { //How should we catch the casing error?
                                throw new InvalidCritterException(critterToMake);
                            }
                            if (!Critter.class.isAssignableFrom(myCritter)) {
                                throw new InvalidCritterException(critterToMake);
                            }
                            try {
                                Method m = myCritter.getMethod("runStats", List.class);
                                String s = (String) m.invoke(List.class, result);
                                Label label = new Label(s);
                                label.setWrapText(true);
                                vBox1.getChildren().addAll(label);
                                scroll.setVvalue(1.0);

                            } catch (NoSuchMethodException e) {
                                Critter.runStats(result);
                            } catch (IllegalAccessException e) {
                                //errorProcessing(parts);
                            } catch (InvocationTargetException e) {
                                //errorProcessing(parts);
                            }
                        } catch (InvalidCritterException e) {
                            // errorProcessing(parts);
                        }
                    }
                }
            });

            hbox3.getChildren().add(btn_stats);

            hbox3.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
            inputGrid.add(hbox3, 0, 5);
            GridPane.setFillHeight(hbox, true);
            GridPane.setFillWidth(hbox, true);

            HBox hbox4 = new HBox();
            Button btn_quit = new Button();

            btn_quit.setText("Quit");

            btn_quit.setOnAction(new EventHandler<ActionEvent>() {

                @Override
                public void handle(ActionEvent event) {

                    System.exit(0);

                }
            });
            hbox4.getChildren().add(btn_quit);

            hbox4.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
            inputGrid.add(hbox4, 0, 6);
            GridPane.setFillHeight(hbox, true);
            GridPane.setFillWidth(hbox, true);

            scroll.setContent(vBox1);
            scroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
            bp.setCenter(scroll);


            bp.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
            inputGrid.add(bp, 0, 7);
            GridPane.setFillHeight(hbox, true);
            GridPane.setFillWidth(hbox, true);





            grid.add(inputGrid, 0 , 0);
            p.getChildren().add(grid);
            Scene scene = new Scene(p);
            primaryStage.setMaximized(true);
            primaryStage.setScene(scene);
            primaryStage.show();
            // Paints the icons.
            //Painter.paint();

        } catch(Exception e) {
            e.printStackTrace();
        }

    }
    //TODO: Maybe have runStats print into a scrollPane. Parameters are textArea and dimensions
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

        launch(args);

        // TODO: 7/15/18 if its one word command: follow quit error processing
        // TODO: 7/15/18 for make and step: similar to quit processing, also check for case where next input wasn't an integer.

    }

    private static void errorProcessing(String[] array){
        String error = "error processing:";
        for (int i = 0; i < array.length; i++)
            error = error + " " + array[i];
        System.out.println(error);
    }
    private StackPane initializeCell(double colWidth, double rowHeight){
        StackPane cell = new StackPane();
        Rectangle rect = new Rectangle(colWidth,rowHeight, Color.WHITE);
        Rectangle rect2 = new Rectangle(colWidth - (colWidth*0.075), rowHeight - (rowHeight*0.075), Color.ALICEBLUE);
        cell.getChildren().add(rect);
        cell.getChildren().add(rect2);
        return cell;
    }

    private GridPane initializeOutputGrid(int rows, double rowHeight, int cols, double colWidth){
        GridPane grid = new GridPane();

        for(int x = 0; x < cols; x++){
            ColumnConstraints c = new ColumnConstraints();
            c.setPercentWidth(colWidth);
            c.setFillWidth(true);
            grid.getColumnConstraints().add(c);
        }

        for(int y = 0; y < rows; y++) {
            RowConstraints r = new RowConstraints();
            r.setPercentHeight(rowHeight);
            r.setFillHeight(true);
            grid.getRowConstraints().add(r);
        }

        for(int x = 0; x < cols; x++){
            for (int y = 0; y < rows; y++){
                Node cell = initializeCell(colWidth, rowHeight);
                ((StackPane) cell).setMaxSize(colWidth,rowHeight);
                ((StackPane) cell).setMinSize(0,0);
                grid.add(cell,x,y);
            }
        }

        return grid;
    }

    private String[] getCritterClasses(String f){
        
        String[] allFiles = new File(f).list();
        List<String> critterLists = new ArrayList<String>();
        for(int i = 0; i < allFiles.length; i++){
            allFiles[i] = allFiles[i].replace('.', ' ');
            String[] temp = allFiles[i].split(" ");
            if (temp[0] != null && temp[0] != "" && temp[0] != " ") {
                allFiles[i] = temp[0];
                try {
                    Class<?> myCritter = Class.forName(myPackage + "." + allFiles[i]);
                    if (assignment5.Critter.class.isAssignableFrom(myCritter)) {
                        critterLists.add(allFiles[i]);
                    }
                }catch (ClassNotFoundException e){

                }
            }
        }
        critterLists.remove("Critter");
        return critterLists.toArray(new String[critterLists.size()]);
    }

}

