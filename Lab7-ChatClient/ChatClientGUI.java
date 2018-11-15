package assignment7;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.SVGPath;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.WindowEvent;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;


import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;

public class ChatClientGUI extends Application implements ClientListener {


    static Scanner kb;

    private GridPane grid = new GridPane();

    private GridPane loginGrid = new GridPane();
    private GridPane createGrid = new GridPane();

    private Client client = new Client();
    private Pane loginCreate = new StackPane();
    private Pane loginPane = new StackPane();

    private Label message1 = new Label("");
    private Label label1 = new Label();

    private TextArea area = new TextArea();

    Label label2 = new Label("Password:");
    Label label3 = new Label("Roomname:");
    Label label4 = new Label("Set Password:");

    private TextField username = new TextField();
    private PasswordField pb = new PasswordField();

    private TextField roomnameToJoin = new TextField();
    private PasswordField passwordToJoin = new PasswordField();

    private final CheckBox chk1 = new CheckBox("Anonymous");
    private final CheckBox makePrivate = new CheckBox("Make Private");
    private PasswordField passwordToCreate = new PasswordField();
    private VBox roomVbox = new VBox();

    private ScrollPane roomList = new ScrollPane();

    //private Text connectedUsers = new Text("");

    private GridPane clientGridBuilder(){
        GridPane clientGrid = new GridPane();

        ColumnConstraints icol0 = new ColumnConstraints();
        ColumnConstraints icol1 = new ColumnConstraints();
        RowConstraints irow0 = new RowConstraints();
        RowConstraints irow1 = new RowConstraints();
        RowConstraints irow2 = new RowConstraints();

        icol0.setPercentWidth(75);
        icol1.setPercentWidth(25);
        irow0.setPercentHeight(15);
        irow1.setPercentHeight(70);
        irow2.setPercentHeight(15);

        clientGrid.getRowConstraints().addAll(irow0, irow1, irow2); //add the row constraints
        clientGrid.getColumnConstraints().addAll(icol0, icol1); //add the column constraints

        return clientGrid;
    }

    private String user = "Anonymous";
    private String message = "";


    private Room[] totalRoomArray = new Room[0];
    private Map<String, ScrollPane> chatWindowMap = new HashMap<>();
    private Map<String, VBox> vBoxMap = new HashMap<>();
    private Map<String, Text> connectedWindowMap = new HashMap<>();

    public static void main(String[] args) {
        kb = new Scanner(System.in); /////where do we create scanner?
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        try {
            client.setGuiListener(this);
            client.initializeNetworking();
            ColumnConstraints jcol0 = new ColumnConstraints();
            RowConstraints jrow0 = new RowConstraints();
            RowConstraints jrow1 = new RowConstraints();
            RowConstraints jrow2 = new RowConstraints();
            RowConstraints jrow3 = new RowConstraints();

            jcol0.setPercentWidth(100);

            jrow0.setPercentHeight(25);
            jrow1.setPercentHeight(25);
            jrow2.setPercentHeight(25);
            jrow3.setPercentHeight(25);

            loginGrid.getRowConstraints().addAll(jrow0, jrow1, jrow2); //add the row constraints
            loginGrid.getColumnConstraints().addAll(jcol0); //add the column constraints

            ColumnConstraints kcol0 = new ColumnConstraints();
            RowConstraints krow0 = new RowConstraints();
            RowConstraints krow1 = new RowConstraints();
            RowConstraints krow2 = new RowConstraints();

            kcol0.setPercentWidth(100);
            krow0.setPercentHeight(40);
            krow1.setPercentHeight(40);
            krow2.setPercentHeight(20);

            createGrid.getColumnConstraints().add(kcol0);
            createGrid.getRowConstraints().addAll(krow0, krow1, krow2);

            ColumnConstraints col0 = new ColumnConstraints();
            ColumnConstraints col1 = new ColumnConstraints();
            RowConstraints row0 = new RowConstraints();

            col0.setPercentWidth(75);
            col1.setPercentWidth(25);
            row0.setPercentHeight(100);

            grid.getColumnConstraints().addAll(col0,col1);
            grid.getRowConstraints().add(row0);

            primaryStage.setTitle("Chat Client");
            primaryStage.setScene(new Scene(loginPaneBuilder(), 300, 150)); /////////////////////////////
            primaryStage.show();


        }catch(Exception e) {
            e.printStackTrace();
        }
    }

    private Pane loginPaneBuilder(){
        Pane loginPaneresult = new StackPane();

        HBox hbox_username = new HBox();
        hbox_username.setSpacing(8);
        Text title5 = new Text("Username:");
        title5.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        hbox_username.getChildren().add(title5);

        hbox_username.getChildren().add(username);

        hbox_username.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        loginGrid.add(hbox_username, 0, 0);
        GridPane.setFillHeight(hbox_username, true);
        GridPane.setFillWidth(hbox_username, true);


        Label label = new Label("Password:");
        label.setFont(Font.font("Arial", FontWeight.BOLD, 14));

//----------------------------------------------------------------------------------------------------
        //Todo: PASSWORD FIELD SET ON ACTION AREA

        pb.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {

                JSONObject object = new JSONObject();
                object.put("command","login");
                object.put("anonStatus", "false");
                object.put("username", username.getText());
                object.put("password", pb.getText());

                StringWriter out = new StringWriter();
                try {
                    object.writeJSONString(out);
                    String text = out.toString();
                    client.getWriter().println(text);
                    client.getWriter().flush();

                } catch ( IOException er){
                    System.out.println("IO Exception");
                }
                pb.clear();
            }
        });
//---------------------------------------------------------------------------------------------------------------------

        VBox vboxForPassword = new VBox();
        vboxForPassword.setSpacing(8);

        HBox hbox_password = new HBox();
        hbox_password.setSpacing(8);

        hbox_password.getChildren().addAll(label, pb);
        vboxForPassword.getChildren().addAll(hbox_password, message1);

        hbox_password.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        loginGrid.add(hbox_password, 0, 1);
        loginGrid.setFillHeight(hbox_password, true);
        loginGrid.setFillWidth(hbox_password, true);

        vboxForPassword.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        loginGrid.add(vboxForPassword, 0, 2);
        loginGrid.setFillHeight(vboxForPassword, true);
        loginGrid.setFillWidth(vboxForPassword, true);

        HBox hbox_anonymous = new HBox();
        hbox_anonymous.setSpacing(8);

        chk1.setSelected(false);

//---------------------------------------------------------------------------------------------------------------------
        //Todo: Checkbox event handler

        EventHandler eh = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (event.getSource() instanceof CheckBox) {
                    CheckBox chk = (CheckBox) event.getSource();
                    JSONObject object = new JSONObject();
                    object.put("command","login");
                    object.put("anonStatus", "true");

                    StringWriter out = new StringWriter();
                    try {
                        object.writeJSONString(out);
                        String text = out.toString();
                        client.getWriter().println(text);
                        client.getWriter().flush();

                    } catch ( IOException er){ }
                }
            }
        };

        chk1.setOnAction(eh);
//---------------------------------------------------------------------------------------------------------------------

        hbox_anonymous.getChildren().addAll(chk1);

        Button btn_loginCreate = new Button();
        btn_loginCreate.setText("Create User");


        HBox createUser = new HBox();
        createUser.setSpacing(8);
        Text title9 = new Text("Username:");
        title9.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        createUser.getChildren().add(title9);

        TextField usernameCreated = new TextField();
        createUser.getChildren().add(usernameCreated);

        HBox createUser1 = new HBox();
        createUser1.setSpacing(8);
        Text title10 = new Text("Password:");
        title10.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        createUser1.getChildren().add(title10);
        TextField passwordCreate = new TextField();
        createUser1.getChildren().add(passwordCreate);

        HBox boxForCreate = new HBox();
        boxForCreate.setSpacing(10);
        Button createUsernameFinal = new Button();
        createUsernameFinal.setText("Create");
        boxForCreate.getChildren().addAll(createUsernameFinal, label1);

//-------------------------------------------------------------------------------------------------------------------------

        //Todo: ACTION HANDLER FOR CREATE BUTTON IN USER CREATION WINDOW

        createUsernameFinal.setOnAction(new EventHandler<ActionEvent>() {   ///use this to set send on action button
            public void handle(ActionEvent event) {

                try {

                    String username = usernameCreated.getText();
                    String password = passwordCreate.getText();

                    JSONObject object = new JSONObject();
                    object.put("command", "createUser");
                    object.put("username", username);
                    object.put("password", password);

                    StringWriter out = new StringWriter();
                    try{

                        object.writeJSONString(out);
                        String text = out.toString();
                        client.getWriter().println(text);
                        client.getWriter().flush();
                        //createGrid.getScene().getWindow().hide();
                        //System.out.println();
                        /*BufferedReader reader = client.getReader();
                        while(client.getHasChanged() == false){}
                        String message = client.getSentMessage();
                        Object parser = new JSONParser().parse(message);
                        JSONObject jo = (JSONObject) parser;
                        String command = (String)jo.get("status");
                        if(command.equals("fail")){//duplicate username
                            //Todo: Create message duplicate username invalid
                            label1.setText("Invalid Username");
                            label1.setTextFill(Color.rgb(210, 39, 30)); }
                        else{
                            ((Node)(event.getSource())).getScene().getWindow().hide();
                        }
                        */
                    }catch (IOException e){
                        e.printStackTrace();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

//------------------------------------------------------------------------------------------------------------------------
        createUser.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        createGrid.add(createUser, 0, 0);
        createGrid.setFillHeight(createUser, true);
        createGrid.setFillWidth(createUser, true);

        createUser1.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        createGrid.add(createUser1, 0, 1);
        createGrid.setFillHeight(createUser1, true);
        createGrid.setFillWidth(createUser1, true);

        boxForCreate.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        createGrid.add(boxForCreate, 0, 2);
        createGrid.setFillHeight(boxForCreate, true);
        createGrid.setFillWidth(boxForCreate, true);

        Stage stageLogin = new Stage();
        stageLogin.setTitle("Create User");
        stageLogin.setScene(new Scene(loginCreate, 300, 150));
        loginCreate.getChildren().add(createGrid);

        btn_loginCreate.setOnAction(new EventHandler<ActionEvent>() {   ///use this to set send on action button
            public void handle(ActionEvent event) {

                try {
                    usernameCreated.setText("");
                    passwordCreate.setText("");
                    stageLogin.show();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        hbox_anonymous.getChildren().add(btn_loginCreate);

        hbox_anonymous.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        loginGrid.add(hbox_anonymous, 0, 3);
        GridPane.setFillHeight(hbox_anonymous, true);
        GridPane.setFillWidth(hbox_anonymous, true);

        loginPaneresult.getChildren().add(loginGrid);

        return loginPaneresult;
    }

//--------------------------------------------------------------------------------------------------------------------------------

    private Pane roomBuilder(){
        Pane p = new StackPane();
        VBox vbox = new VBox();
        vbox.setSpacing(8);
        Text title = new Text("Rooms");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        vbox.getChildren().add(title);

        Label label2 = new Label("Password:");
        Label label3 = new Label("Set Roomname:");
        Label label4 = new Label("Set Password:");
        Label label5 = new Label("Roomname");


        Button btn_join = new Button();
        btn_join.setText("Join");


        btn_join.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
               client.guiJoin(roomnameToJoin.getText(), passwordToJoin.getText());
               roomnameToJoin.setText("");
               passwordToJoin.setText("");
            }
        });


        vbox.getChildren().add(roomList);//////////////////////////////////////////////////////////////////////////////////


        label5.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        vbox.getChildren().addAll(label5, roomnameToJoin);

        label2.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        vbox.getChildren().addAll(label2, passwordToJoin);
        vbox.getChildren().add(btn_join);

        vbox.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        //inputGrid.add(vbox, 0, 0);
        GridPane.setFillHeight(vbox, true);
        GridPane.setFillWidth(vbox, true);


        VBox vbox1 = new VBox();
        vbox1.setSpacing(8);

        TextField roomName = new TextField();
        //Label label3 = new Label("Set Roomname:");
        label3.setFont(Font.font("Arial", FontWeight.BOLD, 14));

//---------------------------------------------------------------------------------------------------------------------//---------------------------------------------------------------------------------------------------------------------
        Button btn_create = new Button();
        btn_create.setText("Create");

        //TODO: need to setContent of roomList scrollpane.

        btn_create.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {

                client.guiCreate(roomName.getText(), passwordToCreate.getText(), makePrivate.isSelected());
                passwordToCreate.setText("");
                roomName.setText("");
                makePrivate.setSelected(false);

            }
        });

//---------------------------------------------------------------------------------------------------------------------
        makePrivate.setSelected(false);//sets makePrivate checkbox unchecked

        vbox1.getChildren().addAll(label3,roomName);
        vbox1.getChildren().add(makePrivate);

        label4.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        vbox1.getChildren().addAll(label4, passwordToCreate);

        vbox1.getChildren().add(btn_create);

        grid.add(vbox, 0 , 0);
        grid.add(vbox1, 1, 0);

        p.getChildren().add(grid);
        return p;
    }

    private Stage stageBuilder(){
        Stage stage = new Stage();
        return stage;
    }

    //------------------------------------------------------------------------------------------------------------------------------------------------

    private Pane chatClientBuilder(String roomName, String[] array) {
        //Todo: Add connected and chat scrollpanes to there respective maps using the roomName as the key

        Pane clientWindow = new StackPane();
        GridPane clientGrid = clientGridBuilder();

        HBox hbox = new HBox();
        

        hbox.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        clientGrid.add(hbox, 0, 0);
        GridPane.setFillWidth(hbox, true);
        GridPane.setFillHeight(hbox, true);

        HBox hbox1 = new HBox();
        hbox1.setSpacing(8);
        Text title1 = new Text("User:");
        title1.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        hbox1.getChildren().add(title1);

        Text clientUserFromText = new Text();
        clientUserFromText.setText(client.getUsername());
        hbox1.getChildren().add(clientUserFromText);

        hbox1.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        clientGrid.add(hbox1, 1, 0);
        GridPane.setFillWidth(hbox1, true);
        GridPane.setFillHeight(hbox1, true);

        VBox vbox2 = new VBox();
        vbox2.setSpacing(8);
        Text title2 = new Text("Conversation:");
        title2.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        vbox2.getChildren().add(title2);


        VBox messageContainer = new VBox(5);
        ScrollPane chatWindow = new ScrollPane(messageContainer);

        chatWindow.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        chatWindow.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        //chatWindow.setPrefHeight(300);
        //chatWindow.prefWidthProperty().bind(messageContainer.prefWidthProperty().subtract(5));
        chatWindow.setFitToWidth(true);


        //Make the scroller scroll to the bottom when a new message is added
        /*speechBubbles.addListener((ListChangeListener<Node>) change -> {
            while (change.next()) {
                if(change.wasAdded()){
                    chatWindow.setVvalue(chatWindow.getVmax());
                }
            }
        });*/

        chatWindow.setPrefSize(Double.MAX_VALUE, Double.MAX_VALUE);
        chatWindow.setPrefSize(120, 150);
        chatWindowMap.put(roomName, chatWindow);
        vbox2.getChildren().add(chatWindow);

        vbox2.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        clientGrid.add(vbox2, 0, 1);
        GridPane.setFillWidth(vbox2, true);
        GridPane.setFillHeight(vbox2, true);

        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        HBox hbox2 = new HBox();
        hbox2.setSpacing(8);
        Text title3 = new Text("Message:");
        title3.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        hbox2.getChildren().add(title3);

        area.setMaxSize(800, 50);

        hbox2.getChildren().add(area);

        hbox2.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        clientGrid.add(hbox2, 0, 2);
        GridPane.setFillWidth(hbox2, true);
        GridPane.setFillHeight(hbox2, true);

        VBox vbox3 = new VBox();
        vbox3.setSpacing(8);
        Text title4 = new Text("Connected:");
        title4.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        vbox3.getChildren().add(title4);

        String appendConnectedUsers = "";

        for(int i = 0; i < array.length; i++){

            if(i == 0){
                appendConnectedUsers = appendConnectedUsers + array[i];
            }
            if(i > 0){
                appendConnectedUsers = appendConnectedUsers + "\n" + array[i];
            }
        }

        Text connectedUsers = new Text();
        connectedWindowMap.put(roomName, connectedUsers);
        connectedUsers.setText(appendConnectedUsers);

        ScrollPane usersConnected = new ScrollPane();
        usersConnected.setPrefSize(Double.MAX_VALUE, Double.MAX_VALUE);
        usersConnected.setPrefSize(120, 150);
        usersConnected.setContent(connectedUsers);

        vbox3.getChildren().add(usersConnected);
        vbox3.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        clientGrid.add(vbox3, 1, 1);
        GridPane.setFillWidth(vbox3, true);
        GridPane.setFillHeight(vbox3, true);

        HBox hbox4 = new HBox();
        Button btn_send = new Button();
        btn_send.setText("Send");

        VBox vboxThatGrows = new VBox();
        vBoxMap.put(roomName, vboxThatGrows);

        btn_send.setOnAction(new EventHandler<ActionEvent>() {   ///use this to set send on action button
            public void handle(ActionEvent event) {
                try {
                    client.guiSendRoom(roomName, area.getText());

                    //vboxThatGrows.getChildren().add(messageBuilder("right"));/////////////// need to figure out if sender or receiver (left/right)

                    //chatWindow.setContent(vboxThatGrows);  ////////////////////////////////////////////////////////////////////////////////////////////
                    area.clear();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        hbox4.getChildren().add(btn_send);

        hbox4.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        clientGrid.add(hbox4, 1, 2);
        GridPane.setFillWidth(hbox4, true);
        GridPane.setFillHeight(hbox4, true);
        
        clientWindow.getChildren().add(clientGrid);

        return clientWindow;

    }

    private HBox messageBuilder (String direction, String message, String sent){
        HBox messageContainer = new HBox();

        SVGPath directionIndicator = new SVGPath();

        Text user = new Text(client.getUsername());

        Text sender = new Text(sent);


        sender.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        user.setFont(Font.font("Arial", FontWeight.BOLD, 14));

        Label displayedText;
        displayedText = new Label(message); //Change to server message
        displayedText.setPadding(new Insets(5));
        displayedText.setWrapText(true);

        Color DEFAULT_SENDER_COLOR = Color.GOLD;
        Color DEFAULT_RECEIVER_COLOR = Color.LIMEGREEN;
        Background DEFAULT_SENDER_BACKGROUND, DEFAULT_RECEIVER_BACKGROUND;

        DEFAULT_SENDER_BACKGROUND = new Background(new BackgroundFill(DEFAULT_SENDER_COLOR, new CornerRadii(5,0,5,5,false), Insets.EMPTY));
        DEFAULT_RECEIVER_BACKGROUND = new Background(new BackgroundFill(DEFAULT_RECEIVER_COLOR, new CornerRadii(0,5,5,5,false), Insets.EMPTY));

        if(direction.equals("left")){
            messageContainer.setAlignment(Pos.CENTER_LEFT);
            directionIndicator.setContent("M0 0 L10 0 L10 10 Z");
            directionIndicator.setFill(DEFAULT_RECEIVER_COLOR);
            displayedText.setBackground(DEFAULT_RECEIVER_BACKGROUND);

        }
        if(direction.equals("right")){
            messageContainer.setAlignment(Pos.CENTER_RIGHT);
            directionIndicator.setContent("M10 0 L0 10 L0 0 Z");
            directionIndicator.setFill(DEFAULT_SENDER_COLOR);
            displayedText.setBackground(DEFAULT_SENDER_BACKGROUND);
        }

        if(direction.equals("left")){
            messageContainer.getChildren().addAll(sender, directionIndicator, displayedText);
        }

        if(direction.equals("right")){
            messageContainer.getChildren().addAll(displayedText, directionIndicator, user);
        }


        return messageContainer;
    }

    @Override
    public void clientHasChanged() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                try {
                    String message = client.getSentMessage();
                    Object parser = new JSONParser().parse(message);
                    JSONObject jo = (JSONObject)parser;
                    String observable = (String)jo.get("observable");
                    String command = (String)jo.get("command");
                    if(observable.equals("server")){
                        switch (command) {
                            case "login":
                                parseServerLogin(jo);
                                break;
                            case "createUser":
                                parseServerCreateUser(jo);
                                break;
                            case "roomList":
                                String sizeString = (String) jo.get("size");
                                int size = Integer.parseInt(sizeString);
                                totalRoomArray = getRoomsList((JSONArray) jo.get("list"), size);
                                System.out.println("Check Room Array Values");
                                break;
                            case "roomCreated":
                                parseServerRoomCreated(jo);
                                break;
                            case "joined":
                                parseServerJoined(jo);
                                break;
                            case "notifyNewRoom":
                                parseNotifyNewRoom(jo);
                                break;
                            default:
                                System.out.println("Invalid command");
                                break;
                        }
                    }
                    else if(observable.equals("room")){
                        if(command.equals("message")){
                            parseRoomMessage(jo);
                        }
                        if(command.equals("notifyNewClient")){
                            parseRoomNewClient(jo);
                        }
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    private void parseRoomMessage(JSONObject jo){
        String roomName = (String)jo.get("roomName");
        String sender = (String)jo.get("sender");
        String sentMessage = (String)jo.get("message");
        ScrollPane chatPane = chatWindowMap.get(roomName);
        VBox vBox = vBoxMap.get(roomName);
        if(sender.equals(client.getUsername()))
            vBox.getChildren().add(messageBuilder("right", sentMessage, sender));
        else
            vBox.getChildren().add(messageBuilder("left", sentMessage, sender));
        chatPane.setContent(vBox);
        chatPane.setVvalue(1.0);


    }

    private void parseServerLogin(JSONObject jo){
        String status = (String)jo.get("status");
        if(status.equals("success")){
            client.setGuiListener(this);
            loginGrid.getScene().getWindow().hide();
            client.setUsername((String)jo.get("username"));
            client.setPassword((String)jo.get("password"));
            try {
                Stage Roomstage2 = new Stage();
                Roomstage2.setTitle("Chat Client");
                Roomstage2.setScene(new Scene(roomBuilder(), 500, 250));
                Roomstage2.show();
                JSONArray rooms = (JSONArray)jo.get("roomList");
                int size = rooms.size();
                Room[] roomList = new Room[size];
                Iterator<JSONObject> it = rooms.iterator();
                for(int i = 0;it.hasNext(); i++){
                    JSONObject obj = it.next();
                    roomList[i] = new Room((String)obj.get("roomName"), (String)obj.get("status"));
                }
                roomListGenerator(roomList);
            }
            catch (Exception er) {
                er.printStackTrace();
            }

        }
        else{
            message1.setText("Your username and/or password is incorrect!");
            message1.setTextFill(Color.rgb(210, 39, 30));
        }
    }

    private void parseServerCreateUser(JSONObject jo){
        String status = (String)jo.get("status");
        if(status.equals("success")){
            createGrid.getScene().getWindow().hide();
            username.setText(null);
            pb.setText(null);
        }
        else{
            label1.setText("Invalid Username!");
            label1.setTextFill(Color.rgb(210, 39, 30));
        }
    }

    private void parseServerRoomCreated(JSONObject jo){
        String status = (String)jo.get("status");
        if(status.equals("success")){
           String roomName = (String)jo.get("roomName");
           String[] connectedUsers = {client.getUsername()};
           generateChatWindow(roomName, connectedUsers);
        } else {

            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setHeaderText("Invalid Roomname");
            errorAlert.setContentText("The roomname is already taken or invalid.");
            errorAlert.showAndWait();
        }
    }

    private void parseServerJoined(JSONObject jo){
        String status = (String)jo.get("status");
        if(status.equals("success")){
            String roomname = (String)jo.get("roomName");
            JSONArray connected = (JSONArray)jo.get("connectedUsers");
            int size = connected.size();
            String[] connectedUsers = new String[size];
            Iterator<JSONObject> it = connected.iterator();
            for(int i = 0;it.hasNext(); i++){
                connectedUsers[i] = (String)it.next().get(Integer.toString(i));
            }
            generateChatWindow(roomname, connectedUsers);

            JSONArray messageHistory = (JSONArray)jo.get("messageHistory");
            it = messageHistory.iterator();
            ScrollPane chatPane = chatWindowMap.get(roomname);
            VBox vBox = vBoxMap.get(roomname);
            while(it.hasNext()){
                JSONObject obj = it.next();
                String message = (String)obj.get("message");
                String sender = (String)obj.get("sender");
                if(sender.equals(client.getUsername()))
                    vBox.getChildren().add(messageBuilder("right", message, sender));
                else
                    vBox.getChildren().add(messageBuilder("left", message, sender));
                chatPane.setContent(vBox);
                chatPane.setVvalue(1.0);
            }
        }
        else{
            //Todo No room with given password/name combo found
            Alert errorAlert1 = new Alert(Alert.AlertType.ERROR);
            errorAlert1.setHeaderText("Invalid Input");
            errorAlert1.setContentText("No room with given password/name combo found.");
            errorAlert1.showAndWait();
        }
    }

    private void parseRoomNewClient(JSONObject jo){
        String roomName = (String)jo.get("roomName");
        String newClient = (String)jo.get("client");
        Text connectedText = connectedWindowMap.get(roomName);
        String t = connectedText.getText();
        t += "\n" + newClient;
        connectedText.setText(t);

    }

    private Text textRoomGenerator(){
        Text text = new Text();
        return text;
    }

    private void parseNotifyNewRoom(JSONObject jo){
        String roomName = (String)jo.get("roomName");
        String status = (String)jo.get("roomStatus");
        Room[] r = new Room[1];
        r[0] = new Room(roomName, status);
        roomListGenerator(r);

    }

    private void roomListGenerator(Room[] rooms){
        //create vbox here

        for(int i = 0; i < rooms.length; i++){
            if(i < 0 && rooms[i].getRoomName().equals(rooms[i-1].getRoomName())){
                continue;
            }
            HBox hbox = new HBox();

            String roomName = rooms[i].getRoomName();
            String status = rooms[i].getStatus();
            String roomStatus;
            if(status.equals("publicRoom"))
                roomStatus = "Public";
            else
                roomStatus = "Private";

            Text roomNameText = new Text(roomName);
            Text roomStatusText = new Text(roomStatus);

            Label label = new Label("RoomName: ");
            label.setFont(Font.font("Arial", FontWeight.BOLD, 14));

            Label label1 = new Label("Status: ");
            label1.setFont(Font.font("Arial", FontWeight.BOLD, 14));

            hbox.getChildren().addAll(label, roomNameText, label1, roomStatusText);
            //Add these two to the hBox

            roomVbox.getChildren().add(hbox);
            //Add hBox to the vBox
        }
        //Add the vBox to the scrollpane
        roomList.setPrefSize(500, 100);
        roomList.setContent(null);
        roomList.setContent(roomVbox);

    }

    private HBox textHbox(){
        HBox hBox = new HBox();


        return hBox;
    }

    private void generateChatWindow(String roomName, String[] connectedUsers){
        Scene chatClient = new Scene(chatClientBuilder(roomName, connectedUsers), 500, 350);
        Stage stageChatWindow = stageBuilder();
        stageChatWindow.setTitle(roomName);
        stageChatWindow.setScene(chatClient);
        stageChatWindow.setOnCloseRequest(new EventHandler<WindowEvent>() {
            public void handle(WindowEvent we) {
                client.guiDisconnect(roomName);
            }});
        stageChatWindow.show();
    }

    private Room[] getRoomsList(JSONArray jArr, int size) {
            Room[] arr = new Room[size];
            Iterator<JSONObject> it = jArr.iterator();
            for(int i = 0;it.hasNext(); i++){
                JSONObject room = it.next();
                arr[i] = new Room((String) room.get("roomName"), (String) room.get("status"));
            }
            return arr;
    }
}