package it.polimi.ingsw.client;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class SocketClient extends Thread {
    private String ip;
    private int port;
    private Scanner userLine = new Scanner(System.in);
    private Scanner reader = null;
    private PrintWriter writer = null;
    private boolean closed = false;

    public SocketClient (String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    public String read() {
        return reader.nextLine();
    }

    public void write(String message) {
        writer.println(message);
        writer.flush();
    }

    public void write(int message) {
        writer.println(message);
        writer.flush();
    }

    public void write(boolean message) {
        writer.println(message);
        writer.flush();
    }

    public boolean isClosed() {
        return closed;
    }

    public void setClosed(boolean closed) {
        this.closed = closed;
    }

    public void close() {
        setClosed(true);
    }

    public void run() {
        try {
            Socket socket = new Socket(ip, port);
            System.out.println("Connection established");
            reader = new Scanner(socket.getInputStream());
            writer = new PrintWriter(socket.getOutputStream());
            userLine = new Scanner(System.in);
            boolean end = false;
            while (!isClosed()) {}
            userLine.close();
            reader.close();
            writer.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}