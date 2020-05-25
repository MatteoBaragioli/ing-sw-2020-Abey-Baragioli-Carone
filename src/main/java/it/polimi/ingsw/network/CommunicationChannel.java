package it.polimi.ingsw.network;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import it.polimi.ingsw.network.exceptions.ChannelClosedException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeoutException;

import static it.polimi.ingsw.network.CommunicationProtocol.*;

public class CommunicationChannel {

    final private BufferedReader in;
    final private PrintWriter out;
    private List<String> buffer = new ArrayList<>();
    private boolean closed = false;
    private boolean ping = false;
    final public String SEPARATOR = "_CONTENT_";
    final private int KEYINDEX = 0;
    final private int CONTENTINDEX = 1;
    final private int CURRENTMESSAGEINDEX = 0;
    final private int EMPTYBUFFER = 0;
    private final int quit = -1;

    private boolean timeout = false;

    public CommunicationChannel(BufferedReader bufferedReader, PrintWriter printWriter) {
        in = bufferedReader;
        out = printWriter;
    }

    public boolean isClosed() {
        return closed;
    }

    public boolean isPinged() {
        return ping;
    }

    private synchronized void ping() {
        ping = true;
    }

    public synchronized void resetPing() {
        ping = false;
    }

    public boolean isTimeout() {
        return timeout;
    }

    public synchronized void timeIsOut() {
        timeout = true;
    }

    public synchronized void resetTimeout() {
        timeout = false;
    }

    /**
     * This method blocks every input and output communication
     */
    public void close() {
        closed = true;
    }

    /**
     * This method reads in the input stream
     * @return string
     * @throws IOException when a network error occurs
     */
    public synchronized String read() throws IOException {
        return in.readLine();
    }

    /**
     * This method adds a message to the buffer
     * @param message
     */
    public synchronized void saveMessage(String message) {
        buffer.add(message);
        System.out.println("Buffer non vuoto");
    }

    /**
     * This method tells if the buffer isn't empty
     * @return false if empty
     */
    public boolean hasMessages() {
        return buffer.size() > EMPTYBUFFER;
    }

    /**
     * This method removes the first message in the buffer
     */
    public synchronized void removeCurrentMessageFromBuffer() {
        buffer.remove(CURRENTMESSAGEINDEX);
    }

    /**
     * This method pops the first message in the buffer
     * @return The popped message
     * @throws ChannelClosedException if connection is lost
     */
    public synchronized String nextMessage() throws ChannelClosedException {
        while (!hasMessages() && !isClosed()) {
            System.out.println("Buffer vuoto");
            try {
                wait();

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        if (!isClosed()) {
            String message = buffer.get(CURRENTMESSAGEINDEX);
            removeCurrentMessageFromBuffer();
            return message;
        }
        throw new ChannelClosedException();
    }

    public synchronized void countdown(int i) throws ChannelClosedException {
        write(keyToString(COUNTDOWN) + SEPARATOR + i);
    }

    /**
     * This method pops the first message in the buffer if it arrives in time
     * @return The popped message
     * @throws TimeoutException if the time is out
     * @throws ChannelClosedException if there's no connection
     */
    public synchronized String nextGameMessage() throws ChannelClosedException, TimeoutException {
        resetTimeout();
        CountDown countDown = new CountDown(this);
        countDown.start();
        while (!isClosed() && !isTimeout() && !hasMessages()) {
            System.out.println("Buffer vuoto");
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if (isClosed())
            throw new ChannelClosedException();
        if (hasMessages())
            return nextMessage();
        writeKeyWord(TIMEOUT);
        throw new TimeoutException();
    }

    /**
     * This method converts a key to a string
     * @param key keyword
     * @return related string
     */
    public String keyToString(CommunicationProtocol key) {
        Type type = new TypeToken<CommunicationProtocol>() {}.getType();
        return new Gson().toJson(key, type);
    }

    /**
     * This method recognizes a key from a string
     * @param string unknown key
     * @return converted key
     */
    public CommunicationProtocol stringToKey(String string) {
        Type type = new TypeToken<CommunicationProtocol>() {}.getType();
        CommunicationProtocol key;
        try {
            key = new Gson().fromJson(string, type);
        } catch (JsonSyntaxException e) {
            //non existing key
            key = INVALID;
        }
        return key;
    }

    /**
     * This method gets a key from a message
     * @param message string
     * @return recognized key
     */
    public CommunicationProtocol getKey(String message) {
        if (message != null) {
            String[] content = message.split(SEPARATOR);
            return stringToKey(content[KEYINDEX]);
        }
        return INVALID;
    }

    /**
     * This method gets the content from a message
     * @param message string
     * @return recognized content
     */
    public String getContent(String message) {
        if (message != null) {
            String[] content = message.split(SEPARATOR);
            if (content.length == 2)
                return content[CONTENTINDEX];
            if (content.length < 2)
                return keyToString(EMPTY);
        }
        return keyToString(INVALID);
    }

    private boolean hasContent(String message) {
        return stringToKey(getContent(message)) != EMPTY;
    }

    /**
     * This method reads the key of the incoming message and saves it if it is neither a PING nor a QUIT
     * @return key
     * @throws IOException in case of network error
     * @throws ChannelClosedException if connection is lost
     */
    public CommunicationProtocol nextKey() throws IOException {
        String message = read();
        System.out.println(message);
        CommunicationProtocol key = getKey(message);
        if (key == RECEIVED || hasContent(message))
            saveMessage(message);
        if (key == PONG)
            ping();
        return key;
    }

    /**
     * This method converts the content in a number
     * @param message String
     * @return content
     * @throws JsonSyntaxException in case the content is invalid
     */
    public int readNumber(String message) throws JsonSyntaxException {
        Type type = new TypeToken<Integer>() {}.getType();
        return new Gson().fromJson(getContent(message), type);
    }

    /**
     * This method send a string
     * @param message string
     * @throws ChannelClosedException if connection is lost
     */
    public synchronized void write(String message) throws ChannelClosedException {
        if (!isClosed()) {
            out.println(message);
            out.flush();
        }
        else
            throw new ChannelClosedException();
    }

    /**
     * This method sends a single key
     * @param key key
     * @throws ChannelClosedException if connection is lost
     */
    public synchronized void writeKeyWord(CommunicationProtocol key) throws ChannelClosedException {
        write(keyToString(key));
    }

    /**
     * This method sends the keyword USERNAME and waits for a reply
     * @return String
     * @throws ChannelClosedException if there's no connection
     */
    public String askUsername() throws ChannelClosedException {
        while (!isClosed()){
            System.out.println("Chiedo Username");
            writeKeyWord(USERNAME);
            String message = nextMessage();
            if (message != null && getKey(message) == USERNAME)
                return getContent(message);
        }
        throw new ChannelClosedException();
    }


    /**
     * This method sends the keyword UNIQUEUSERNAME and waits for a reply
     * @return String
     * @throws ChannelClosedException if there's no connection
     */
    public synchronized String askUniqueUsername() throws ChannelClosedException {
        while (!isClosed()){
            writeKeyWord(UNIQUEUSERNAME);
            String message = nextMessage();
            if (message != null && getKey(message) == USERNAME)
                return getContent(message);
        }
        throw new ChannelClosedException();
    }

    /**
     * This method writes USERNAME followed by the content
     * @param userName string
     * @throws ChannelClosedException
     */
    public synchronized void writeUsername(String userName) throws ChannelClosedException {
        write(keyToString(USERNAME) + SEPARATOR + userName);
    }

    /**
     * This method sends the keyword MATCHTYPE and waits for a reply
     * @return int
     * @throws ChannelClosedException if there's no connection
     */
    public synchronized int askMatchType() throws ChannelClosedException {
        while (!isClosed()){
            writeKeyWord(MATCHTYPE);
            String message = nextMessage();
            if (message != null && getKey(message) == MATCHTYPE)
                return readNumber(message);
        }
        throw new ChannelClosedException();
    }

    /**
     * This method writes a reply to a MATCHTYPE request
     * @param matchType answer
     * @throws ChannelClosedException if there's no connection
     */
    public synchronized void writeMatchType(int matchType) throws ChannelClosedException {
        String message = nextMessage();
        if (getKey(message) == MATCHTYPE)
            if (matchType == quit)
                writeKeyWord(QUIT);
            else
                write(keyToString(MATCHTYPE) + SEPARATOR + matchType);
    }

    /**
     * This method sends a list of positions as json object and waits for a reply
     * @param workers Box coordinates
     * @return int list index
     * @throws TimeoutException if the time is out
     * @throws ChannelClosedException if there's no connection
     */
    public int askWorker(String workers) throws TimeoutException, ChannelClosedException {
        while (!isClosed()) {
            write(keyToString(WORKER) + SEPARATOR + workers);
            String message = nextGameMessage();
            if (message != null && getKey(message) == WORKER)
                return readNumber(message);
        }
        throw new ChannelClosedException();
    }

    /**
     * This method checks if the other side received what was sent before
     * @return boolean
     * @throws ChannelClosedException if there's no connection
     */
    public boolean copy() throws ChannelClosedException {
        return getKey(nextMessage()) == RECEIVED;
    }

    /**
     * This method sends a player and tells if it was received
     * @param player Sent player
     * @return boolean
     * @throws ChannelClosedException if there's no connection
     */
    public boolean sendMyPlayer(String player) throws ChannelClosedException {
        write(keyToString(MYPLAYER) + SEPARATOR + player);
        return copy();
    }

    /**
     * This method sends to the user his player and tells if it was received
     * @param opponents Opponents
     * @return boolean
     * @throws ChannelClosedException if there's no connection
     */
    public boolean sendOpponents(String opponents) throws ChannelClosedException {
        write(keyToString(OPPONENTS) + SEPARATOR + opponents);
        return copy();
    }

    /**
     * This method sends the user an index of a card list
     * @param key Type of list
     * @param index int
     * @throws ChannelClosedException if there's no connection
     */
    public void writeChoiceFromList(CommunicationProtocol key, int index) throws ChannelClosedException {
        write(keyToString(key) + SEPARATOR + index);
    }

    /**
     * This method sends a list of positions as json object and waits for a reply
     * @param positions Box coordinates
     * @return int list index
     * @throws TimeoutException if the time is out
     * @throws ChannelClosedException if there's no connection
     */
    public int askStartPosition(String positions) throws ChannelClosedException, TimeoutException {
        while (!isClosed()) {
            write(keyToString(STARTPOSITION) + SEPARATOR + positions);
            String message = nextGameMessage();
            if (message != null && getKey(message) == STARTPOSITION)
                return readNumber(message);
        }
        throw new ChannelClosedException();
    }

    /**
     * This method sends a list of destinations as json object and waits for a reply
     * @param destinations Box coordinates
     * @return int list index
     * @throws TimeoutException if the time is out
     * @throws ChannelClosedException if there's no connection
     */
    public int askDestination(String destinations) throws TimeoutException, ChannelClosedException {
        while (!isClosed()) {
            write(keyToString(DESTINATION) + SEPARATOR + destinations);
            String message = nextGameMessage();
            if (message != null && getKey(message) == DESTINATION)
                return readNumber(message);
        }
        throw new ChannelClosedException();
    }

    /**
     * This method sends a list of locations as json object and waits for a reply
     * @param builds Box coordinates
     * @return int list index
     * @throws TimeoutException if the time is out
     * @throws ChannelClosedException if there's no connection
     */
    public int askBuild(String builds) throws TimeoutException, ChannelClosedException {
        while (!isClosed()) {
            write(keyToString(BUILD) + SEPARATOR + builds);
            String message = nextGameMessage();
            if (message != null && getKey(message) == BUILD)
                return readNumber(message);
        }
        throw new ChannelClosedException();
    }

    /**
     * This method sends a list of locations as json object and waits for a reply
     * @param removals Box coordinates
     * @return int list index
     * @throws TimeoutException if the time is out
     * @throws ChannelClosedException if there's no connection
     */
    public int askRemoval(String removals) throws TimeoutException, ChannelClosedException {
        while (!isClosed()) {
            write(keyToString(REMOVAL) + SEPARATOR + removals);
            String message = nextGameMessage();
            if (message != null && getKey(message) == REMOVAL)
                return readNumber(message);
        }
        return 0;
    }

    public void writeConfirmation(CommunicationProtocol key, String confirmation) throws ChannelClosedException {
        write(keyToString(key) + SEPARATOR + confirmation);
    }
}
