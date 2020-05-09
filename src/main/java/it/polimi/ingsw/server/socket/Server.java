package it.polimi.ingsw.server.socket;

import it.polimi.ingsw.server.controller.Controller;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    final private int port;

    public Server(int port) {
        this.port = port;
    }

    public void startServer() {
        ExecutorService executor = Executors.newCachedThreadPool();
        ServerSocket serverSocket;
        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            System.err.println(e.getMessage()); // Porta non disponibile
            return;
        }

        Controller controller = new Controller();

        System.out.println("Server ready");
        boolean closed = false;

        while (!closed) {
            try {
                Socket socket = serverSocket.accept();
                System.out.println(socket + " tried to connect");
                executor.submit(new ClientHandler(controller, socket));
            } catch(IOException e) {
                closed = true;
                e.printStackTrace();
                System.err.println("ServerSocket is not accepting");
            }
        }
        executor.shutdown();
    }

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        System.out.println("Write port:");
        int port = in.nextInt();
        Server server = new Server(port);
        server.startServer();
    }
}