package it.polimi.ingsw.network;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class CommunicationChannel {

    final private BufferedReader in;
    final private PrintWriter out;
    private List<String> messages = new ArrayList<>();
    private boolean closed = false;
    final public String SEPARATOR = "_CONTENT_";

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
        return in.readLine();
    }

    public int readNumber() throws IOException {
        Type type = new TypeToken<Integer>() {}.getType();
        return new Gson().fromJson(read(), type);
    }

    public boolean readBoolean() throws IOException {
        Type type = new TypeToken<Boolean>() {}.getType();
        return new Gson().fromJson(read(), type);
    }

    public CommunicationProtocol nextKey() throws IOException {
        Type type = new TypeToken<CommunicationProtocol>() {}.getType();
        return new Gson().fromJson(read(), type);
    }

    public void write(String message) {
        out.println(message);
        out.flush();
    }

    public void writeKeyWord(CommunicationProtocol key) {
        Type type = new TypeToken<CommunicationProtocol>() {}.getType();
        write(new Gson().toJson(key, type));
    }

    public void writeNumber(int num) {
        Type type = new TypeToken<Integer>() {}.getType();
        write(new Gson().toJson(num, type));
    }

    public void writeBoolean(boolean confirmation) {
        Type type = new TypeToken<Boolean>() {}.getType();
        write(new Gson().toJson(confirmation, type));
    }
}
