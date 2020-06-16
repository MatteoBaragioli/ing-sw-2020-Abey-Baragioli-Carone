package it.polimi.ingsw.server.socket;

import it.polimi.ingsw.server.controller.DataBase;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
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

        DataBase dataBase = new DataBase();

        System.out.println("Server ready");
        boolean closed = false;

        while (!closed) {
            Socket socket = null;
            try {
                socket = serverSocket.accept();
            } catch(IOException e) {
                socket = null;
                closed = true;
                e.printStackTrace();
                System.err.println("ServerSocket is not accepting");
            }
            if(socket!=null) {
                System.out.println(socket + " tried to connect");
                executor.submit(new ClientHandler(dataBase, socket));
            }
        }
        executor.shutdown();
    }

    public static void main(String[] args) {
        boolean valid = false;
        int port = 0;
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        while (!valid) {
            System.out.println("Write port:");
            try {
                port = Integer.parseInt(in.readLine());
            } catch (IOException e) {
                e.printStackTrace();
            } catch (NumberFormatException e) {
                port = 0;
            }
            if (port > 1023)
                valid = true;
            else
                System.out.println("Not valid port. Try again");
        }

        Server server = new Server(port);
        server.startServer();
    }
}
