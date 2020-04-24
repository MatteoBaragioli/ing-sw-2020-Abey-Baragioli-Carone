package it.polimi.ingsw.client;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class SocketClient extends Thread {
    private String ip;
    private int port;
    private Scanner userLine = new Scanner(System.in);
    private Scanner inLine = null;
    private PrintWriter outLine = null;
    private boolean close = false;

    public SocketClient (String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    public String read() {
        return inLine.nextLine();
    }

    public void write(String message) {
        outLine.println(message);
        outLine.flush();
    }

    public boolean closed() {
        return close;
    }

    public void setClose(boolean close) {
        this.close = close;
    }

    public void close() {
        setClose(true);
    }

    public void run() {
        try {
            Socket socket = new Socket(ip, port);
            System.out.println("Connection established");
            inLine = new Scanner(socket.getInputStream());
            outLine = new PrintWriter(socket.getOutputStream());
            userLine = new Scanner(System.in);
            boolean end = false;
            while (!close) {}
            userLine.close();
            inLine.close();
            outLine.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}