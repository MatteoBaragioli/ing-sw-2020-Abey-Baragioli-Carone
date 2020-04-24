package it.polimi.ingsw.server.socket;

import it.polimi.ingsw.server.model.User;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    private int port;
    private List<String> userNames = new ArrayList<>();
    private Map<String, User> users = new HashMap<>();
    private List<SocketServer> unregisteredSockets = new ArrayList<>();

    public Server(int port) {
        this.port = port;
    }

    public void manageNewSocket(Socket socket) {
        try {
            Scanner inLine = new Scanner(socket.getInputStream());
            PrintWriter outLine = new PrintWriter(socket.getOutputStream());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void start() {
        ExecutorService executor = Executors.newCachedThreadPool();
        ServerSocket serverSocket;
        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            System.err.println(e.getMessage()); // Porta non disponibile
            return;
        }
        System.out.println("Server ready");
        while (true) {
            try {
                Socket socket = serverSocket.accept();
                executor.submit(new SocketServer(socket));
            } catch(IOException e) {
                break; // Entrerei qui se serverSocket venisse chiuso
            }
        }
        executor.shutdown();
    }
}
