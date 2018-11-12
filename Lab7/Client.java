package assignment7;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class Client extends Observable{
    private BufferedReader reader;
    private PrintWriter writer;
    private String username;
    private String password;
    private String sentMessage;
    private ClientListener guiListener;
    private List<Room> roomList = new ArrayList<Room>();


    public void initializeNetworking() throws Exception{
        Socket welcomeSocket = new Socket ("localhost", 3788);
        InputStreamReader streamReader = new InputStreamReader(welcomeSocket.getInputStream());
        reader = new BufferedReader(streamReader);
        writer = new PrintWriter(welcomeSocket.getOutputStream());
        Thread readerThread = new Thread(new ServerReader());
        readerThread.start();
    }

    public void guiJoin(String roomName, String password){
        try {
            JSONObject objJoin = new JSONObject();
            objJoin.put("command", "join");
            objJoin.put("roomName", roomName);
            objJoin.put("client1", username);

            if(password.equals("")){
                objJoin.put("status", "publicRoom");
            }

            else{
                objJoin.put("status", "privateRoom");
                objJoin.put("password", password);
            }

            sendObject(objJoin);


            //((Node)(event.getSource())).getScene().getWindow().hide();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void guiCreate(String roomName, String password, boolean makePrivate){
        try {
            JSONObject obj = new JSONObject();
            obj.put("command", "create");
            obj.put("roomName", roomName);
            obj.put("client1", username);
            if(makePrivate) {
                obj.put("status", "privateRoom");
                obj.put("password", password);

            }
            else
                obj.put("status", "publicRoom");
            sendObject(obj);

        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void guiSendRoom(String roomName, String message){
        JSONObject obj = new JSONObject();
        obj.put("command", "send");
        obj.put("status", "room");
        obj.put("roomName", roomName);
        obj.put("sender", username);
        obj.put("message", message);
        sendObject(obj);

    }

    public void guiDisconnect(String roomName){
        JSONObject obj = new JSONObject();
        obj.put("command", "roomDisconnect");
        obj.put("roomName", roomName);
        obj.put("username", username);
        sendObject(obj);
    }

    private void sendObject(JSONObject obj){
        StringWriter out = new StringWriter();
        try{
            obj.writeJSONString(out);
            String text = out.toString();
            writer.println(text);
            writer.flush();
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    public String getUsername(){
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public PrintWriter getWriter() {
        return writer;
    }

    public BufferedReader getReader() {
        return reader;
    }


    public String getSentMessage(){
        return sentMessage;
    }

    public void setGuiListener(ClientListener guiListener) {
        this.guiListener = guiListener;
    }

    class ServerReader implements Runnable{

        @Override
        public void run() {
            try{
                String message;
                while ((message = reader.readLine()) != null) {
                    sentMessage = message;
                    guiListener.clientHasChanged();
                }
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }
}
