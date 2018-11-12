package assignment7;


import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

public class Server extends Observable{
    private List<Client> clients = new ArrayList<Client>();
    private List<Room> rooms = new ArrayList<Room>();
    private List<ClientHandler> handlers = new ArrayList<>();
    private int anonCounter = 0;

    public static void main(String[] args){
        try{
            new Server().initialize();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private void initialize() throws Exception{
        ServerSocket welcomeSocket = new ServerSocket(3788);
        while(true){
            Socket clientSocket = welcomeSocket.accept();
            Client client = new Client();
            clients.add(client);
            ClientHandler c = new ClientHandler(clientSocket,client);
            handlers.add(c);
            Thread clientThread = new Thread(c);
            clientThread.start();
        }
    }



    class ClientHandler implements Runnable, Observer{
        private BufferedReader reader;
        private PrintWriter writer;
        Client client;

        public ClientHandler(Socket clientSocket, Client client) {
            try {
                reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                writer = new PrintWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
                this.client = client;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void update(Observable o, Object arg) {
            String sentMessage = (String)arg;
            writer.println(sentMessage);
            writer.flush();
        }

        public void run() {
            String message;
            try {
                while ((message = reader.readLine()) != null) {
                    Object parser = new JSONParser().parse(message);
                    JSONObject jo = (JSONObject)parser;
                    String command = (String)jo.get("command");
                    System.out.println("server received: " + message);
                    switch (command) {
                        case "create":
                            parseCreate(jo);
                            //Todo: Notify observers that the room lists has changed
                            break;
                        case "join":
                            parseJoin(jo);
                            //Todo: Pass the chat history
                            break;
                        case "send":
                            parseSend(jo);
                            break;
                        case "login":
                            parseLogin(jo);
                            break;
                        case "createUser":
                            parseCreateUser(jo);
                            break;
                        case "roomDisconnect":
                            parseRoomDisconnect(jo);
                            break;
                        default:
                            System.out.println("Invalid Command");
                            break;
                    }
                }
            } catch (ParseException | IOException e) {
                e.printStackTrace();
            }

        }

        public void notifyNewRoom(Room r){
            String roomName = r.getRoomName();
            String roomStatus = r.getStatus();
            JSONObject response = new JSONObject();
            response.put("observable", "server");
            response.put("command", "notifyNewRoom");
            response.put("roomName", roomName);
            response.put("roomStatus", roomStatus);
            sendResponse(response);
        }

        private void parseCreate(JSONObject jo){
            String status = (String)jo.get("status");
            JSONObject response = new JSONObject();
            response.put("command", "roomCreated");
            response.put("observable", "server");
            if(status.equals("publicRoom")){
                String roomName = (String)jo.get("roomName");
                if(findRoom(roomName) != null){
                    response.put("status", "fail");
                    sendResponse(response);
                    return;
                }
                response.put("status", "success");
                response.put("roomName", roomName);
                Client client = findClient((String)jo.get("client1"));
                Room r = new Room(status,client,null, roomName, null);
                r.addObserver(this);
                rooms.add(r);
                StringWriter out = new StringWriter();
                sendResponse(response);
                for(int i = 0; i < handlers.size(); i++){
                    handlers.get(i).notifyNewRoom(r);;
                }
            }
            else if(status.equals("privateRoom")){
                String roomName = (String)jo.get("roomName");
                if(findRoom(roomName) != null){
                    response.put("status", "fail");
                    sendResponse(response);
                    return;
                }
                response.put("roomName", roomName);
                response.put("status", "success");
                String password = (String)jo.get("password");
                Client client = findClient((String)jo.get("client1"));
                Room r = new Room(status,client,null,roomName,password);
                r.addObserver(this);
                rooms.add(r);
                sendResponse(response);
                for(int i = 0; i < handlers.size(); i++){
                    handlers.get(i).notifyNewRoom(r);;
                }
            }
            else{
                System.out.println("Invalid room");
            }
        }

        private void parseJoin(JSONObject jo){
            String status = (String)jo.get("status");
            JSONObject response = new JSONObject();
            response.put("observable", "server");
            response.put("command", "joined");
            if(status.equals("publicRoom")){
                Room room = findRoom((String)jo.get("roomName"), status);
                if(room == null) {
                    response.put("status", "fail");
                    sendResponse(response);
                }
                else{
                    Client client = findClient((String)jo.get("client1"));
                    room.notifyNewClient(client);
                    room.addClient(client);
                    room.addObserver(this);
                    response.put("status", "success");
                    response.put("roomName", room.getRoomName());
                    response.put("connectedUsers", generateConnectedList(room));
                    response.put("messageHistory", room.getHistory());
                    sendResponse(response);
                }
            }
            else if (status.equals("privateRoom")){
                Room room = findRoom((String)jo.get("roomName"), status, (String)jo.get("password"));
                if(room == null) {
                    response.put("status", "fail");
                    sendResponse(response);
                }
                else{
                    Client client = findClient((String)jo.get("client1"));
                    room.notifyNewClient(client);
                    room.addClient(client);
                    room.addObserver(this);
                    response.put("roomName", room.getRoomName());
                    response.put("status", "success");
                    response.put("connectedUsers", generateConnectedList(room));
                    response.put("messageHistory", room.getHistory());
                    sendResponse(response);
                }
            }
            else{
                System.out.println("Invalid Status");
            }
        }

        private void parseSend(JSONObject jo){
            String status = (String)jo.get("status");
            Client client = findClient((String)jo.get("sender"));
            String message = (String)jo.get("message");
            if(status.equals("room")){
                Room room = findRoom((String)jo.get("roomName"));
                room.sendMessage(client,message);
            }
            else if(status.equals("directChat")){
                Room room = findChat((String)jo.get("sender"), (String)jo.get("recipient"));
                room.sendMessage(client,message);
            }
            else
                System.out.println("Unable to send message");
        }

        private void parseLogin(JSONObject jo){
            String anonStatus = (String)jo.get("anonStatus");
            JSONObject response = new JSONObject();
            response.put("observable", "server");
            response.put("command", "login");
            if(anonStatus.equals("true")){
                String username = "Anonymous" + anonCounter;
                response.put("username", username);
                anonCounter++;
                client.setUsername(username);
                response.put("status", "success");
                response.put("username", username);
                response.put("password", null);
                response.put("roomList", generateRoomLists());
            }
            else{
                String username = (String)jo.get("username");
                response.put("username", username);
                String password = (String)jo.get("password");
                Client searchedClient = findClient(username);
                if(searchedClient == null)
                    response.put("status", "fail");
                else if(!searchedClient.getPassword().equals(password)){
                        response.put("status", "fail");
                }
                else{
                    client = searchedClient;
                    response.put("status", "success");
                    response.put("username", username);
                    response.put("password", password);
                    response.put("roomList", generateRoomLists());
                }
            }
            sendResponse(response);
        }

        private void parseCreateUser(JSONObject jo){
            String username = (String)jo.get("username");
            String password = (String)jo.get("password");
            JSONObject response = new JSONObject();
            response.put("observable", "server");
            response.put("command", "createUser");
            response.put("username", username);
            if(username.contains("Anonymous") || findClient(username) != null){
                response.put("status", "fail");
            }
            else {
                response.put("status", "success");
                client.setUsername(username);
                client.setPassword(password);
            }
            sendResponse(response);
        }

        private JSONArray generateConnectedList(Room r){
            JSONArray roomClients = new JSONArray();
            List<String> clients = r.getClients();
            for(int i = 0; i < clients.size(); i++){
                JSONObject jo = new JSONObject();
                jo.put(Integer.toString(i), clients.get(i));
                roomClients.add(jo);
            }
            return roomClients;

        }

        private void parseRoomDisconnect(JSONObject jo){
            Room room = findRoom((String)jo.get("roomName"));
            Client client = findClient((String)jo.get("username"));
            room.deleteClient(client);
            room.deleteObserver(this);
        }

        private JSONArray generateRoomLists(){
            JSONArray roomArray = new JSONArray();
            for(int i = 0; i < rooms.size(); i++){
                JSONObject room = new JSONObject();
                room.put("roomName", rooms.get(i).getRoomName());
                room.put("status", rooms.get(i).getStatus());
                roomArray.add(room);
            }
            return roomArray;
        }

        private void sendResponse(JSONObject response){
            StringWriter out = new StringWriter();
            try {
                response.writeJSONString(out);
                String text = out.toString();
                System.out.println("Server sent: " + text);
                writer.println(text);
                writer.flush();
            } catch (IOException e){
                e.getStackTrace();
            }
        }

        private Client findClient(String username){
            for(int i = 0; i < clients.size(); i++){
                if(clients.get(i).getUsername() != null && clients.get(i).getUsername().equals(username)) {
                    return clients.get(i);
                }
            }
            return null;
        }

        private Room findRoom(String roomName){
            for(int i = 0; i < rooms.size(); i++){
                if(rooms.get(i).getRoomName().equals(roomName))
                    return rooms.get(i);
            }
            return null;
        }

        private Room findChat(String client1, String client2){
            for(int i = 0; i < rooms.size(); i++){
                if(rooms.get(i).getStatus().equals("directChat")){
                    if((rooms.get(i).getClient1().equals(client1) && rooms.get(i).getClient2().equals(client2)) ||
                            (rooms.get(i).getClient1().equals(client2) && rooms.get(i).getClient2().equals(client1)) )
                        return rooms.get(i);
                }
            }
            return null;
        }

        private Room findRoom(String roomName, String status){
            for(int i = 0; i < rooms.size(); i++){
                if(rooms.get(i).getRoomName().equals(roomName) && rooms.get(i).getStatus().equals(status))
                    return rooms.get(i);
            }
            return null;
        }

        private Room findRoom(String roomName, String status, String password){
            for(int i = 0; i < rooms.size(); i++){
                if(rooms.get(i).getRoomName().equals(roomName) && rooms.get(i).getStatus().equals(status)
                        && rooms.get(i).getPassword().equals(password))
                    return rooms.get(i);
            }
            return null;
        }
    }
}

