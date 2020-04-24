package it.polimi.ingsw.server.socket;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class SocketServer extends Thread {
    private Socket socket;
    private Scanner inLine = null;
    private PrintWriter outLine = null;
    private boolean close = false;

    public SocketServer(Socket socket) {
        this.socket = socket;
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
            inLine = new Scanner(socket.getInputStream());
            outLine = new PrintWriter(socket.getOutputStream());
            while (!closed()) {}
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
