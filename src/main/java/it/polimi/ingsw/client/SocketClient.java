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
        return reader.next();
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
        Socket socket;
        try {
            socket = new Socket(ip, port);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Non si connette il socket client");
            return;
        }
        System.out.println("Connection established");
        try {
            reader = new Scanner(socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Non imposta lo scanner");
        }
        try {
            writer = new PrintWriter(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Non imposta il printer");
        }
        userLine = new Scanner(System.in);
        boolean end = false;
        while (!isClosed()) {}
        userLine.close();
        reader.close();
        writer.close();
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Non chiude il socket");
        }
    }
}