package it.polimi.ingsw.server.socket;

import it.polimi.ingsw.server.model.User;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Server extends Thread{
    private int port;
    private List<String> userNames = new ArrayList<>();
    private Map<String, User> users = new HashMap<>();
    private List<User> twoPlayerQueue = new ArrayList<>();
    private List<User> threePlayerQueue = new ArrayList<>();

    public Server(int port) {
        this.port = port;
    }

    public int port() {
        return port;
    }

    public List<String> userNames() {
        return userNames;
    }

    public Map<String, User> users() {
        return users;
    }

    public List<User> twoPlayerQueue() {
        return twoPlayerQueue;
    }

    public List<User> threePlayerQueue() {
        return threePlayerQueue;
    }

    public void manageSocket(Socket socket) {
        new UserManager(socket, userNames(), users(), twoPlayerQueue(), threePlayerQueue()).start();
    }

    @Override
    public void run() {
        ServerSocket serverSocket;
        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            System.err.println(e.getMessage()); //unavailable port
            return;
        }
        System.out.println("Server ready");
        while (true) {
            try {
                Socket socket = serverSocket.accept();
                manageSocket(socket);
            } catch(IOException e) {
                break; //ServerSocket is closed
            }
        }
    }
}
