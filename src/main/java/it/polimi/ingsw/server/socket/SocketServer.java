package it.polimi.ingsw.server.socket;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class SocketServer extends Thread {
    private Socket socket;
    private Scanner reader = null;
    private PrintWriter writer = null;
    private boolean closed = false;

    public SocketServer(Socket socket) {
        this.socket = socket;
    }

    public Socket socket() {
        return socket;
    }

    public String read() {
        return reader.nextLine();
    }

    public void write(String message) {
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
        notifyAll();
    }

    public void run() {
        try {
            while (!socket().isConnected()) {
                System.out.println("ServerSocket Not connected");
                wait(1000);
            }
            reader = new Scanner(socket().getInputStream());
            writer = new PrintWriter(socket().getOutputStream());
            while (!isClosed()) {
                wait();
            }
            reader.close();
            writer.close();
            socket.close();
        } catch (InterruptedException e) {
            //Nani
        } catch (IOException e) {
            //inform user
        }
    }
}
