package assignment7;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

public class Room extends Observable {
    String status;
    String roomName;
    String password;
    String client1;
    String client2;
    List<String> clients = new ArrayList<String>();
    List<Message> history;



    public Room(String status, Client client1, Client client2, String roomName, String password){
        this.status = status;
        this.roomName = roomName;
        this.password = password;
        clients.add(client1.getUsername());
        if(status.equals("directChat")) {
            this.client1 = client1.getUsername();
            this.client2 = client2.getUsername();
            clients.add(client2.getUsername());
        }
        history = new ArrayList<Message>();
    }

    public List<String> getClients() {
        return clients;
    }

    public Room(String roomName){
        this.roomName =roomName;
        history = new ArrayList<Message>();
    }
    public Room(String roomName, String status){
        this.roomName = roomName;
        this.status = status;
    }

    public void addClient(Client client){
        clients.add(client.getUsername());
    }

    public void notifyNewClient(Client client){
        JSONObject obj = new JSONObject();
        obj.put("observable", "room");
        obj.put("roomName", roomName);
        obj.put("command", "notifyNewClient");
        obj.put("client", client.getUsername());
        StringWriter out = new StringWriter();
        try {
            obj.writeJSONString(out);
            String text = out.toString();
            setChanged();
            notifyObservers(text);
        } catch (IOException e){
        }
    }

    public void deleteClient(Client client){
        clients.remove(client.getUsername());
        JSONArray newClientArray = new JSONArray();
        for(int i = 0; i < clients.size(); i++){
            newClientArray.add(clients.get(i));
        }
        StringWriter out = new StringWriter();
        try {
            JSONObject clientsObject = new JSONObject();
            clientsObject.put("observable", "room");
            clientsObject.put("command", "connectedList");
            clientsObject.put("size", Integer.toString(clients.size()));
            clientsObject.put("list", newClientArray);
            clientsObject.writeJSONString(out);
            String text = out.toString();
            setChanged();
            notifyObservers(text);
        } catch (IOException e){
        }
    }

    public void sendMessage(Client sender, String message){
        Message m = new Message(sender.getUsername(), message);
        history.add(m);
        JSONObject sentMessage = new JSONObject();
        sentMessage.put("observable", "room");
        sentMessage.put("command", "message");
        sentMessage.put("roomName", roomName);
        sentMessage.put("sender", sender.getUsername());
        sentMessage.put("message", message);
        StringWriter out = new StringWriter();
        try{
            sentMessage.writeJSONString(out);
            String text = out.toString();
            setChanged();
            notifyObservers(text);
        }catch (IOException e){
            System.out.println("IO Exception");
        }

    }

    public String getPassword() {
        return password;
    }

    public String getStatus() {
        return status;
    }

    public String getClient1() {
        return client1;
    }

    public String getClient2() {
        return client2;
    }

    public JSONArray getHistory() {
        JSONArray historyArray = new JSONArray();
        for(int i = 0; i < history.size(); i++){
            JSONObject obj = new JSONObject();
            obj.put("sender", history.get(i).getSender());
            obj.put("message", history.get(i).getMessage());
            historyArray.add(obj);
        }
        return historyArray;
    }

    public String getRoomName() {
        return roomName;
    }

    class Message{
        String sender;
        String message;

        public Message(String sender, String message){
            this.sender = sender;
            this.message = message;
        }

        public String getSender() {
            return sender;
        }

        public String getMessage() {
            return message;
        }
    }
}
