package it.polimi.ingsw.server.socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

public class CommunicationChannel {
    final private BufferedReader in;
    final private PrintWriter out;
    private boolean closed = false;

    public CommunicationChannel(BufferedReader bufferedReader, PrintWriter printWriter) {
        in = bufferedReader;
        out = printWriter;
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

    public String read() throws IOException {
        String message = in.readLine();
        System.out.println(message);
        return message;
    }

    public void write(String message) {
        out.println(message);
        out.flush();
    }

    public void write(int index) {
        out.println(index);
        out.flush();
    }

    public void write(boolean confirmation) {
        out.println(confirmation);
        out.flush();
    }
}
