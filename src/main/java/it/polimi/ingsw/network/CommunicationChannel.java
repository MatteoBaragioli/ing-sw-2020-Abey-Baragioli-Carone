package it.polimi.ingsw.network;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import it.polimi.ingsw.network.exceptions.ChannelClosedException;
import it.polimi.ingsw.network.objects.GodCardProxy;

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
    static final public String SEPARATOR = "_CONTENT_";
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
    public String read() throws IOException {
        synchronized (in) {
            return in.readLine();
        }
    }

    /**
     * This method adds a message to the buffer
     * @param message
     */
    public synchronized void saveMessage(String message) {
        buffer.add(message);
        notifyAll();
       // System.out.println("Buffer non vuoto");
    }

    /**
     * This method tells if the has a message containing the key isn't empty
     * @return false if not
     * @param key
     */
    public synchronized boolean hasMessages(CommunicationProtocol key) {
        for (String message: buffer) {
            if (getKey(message) == key)
                return true;
        }
        return false;
    }

    public synchronized boolean isEmpty() {
        return buffer.isEmpty();
    }

    public synchronized CommunicationProtocol popKey() throws ChannelClosedException {
        while (!isClosed()) {
            if (!isEmpty())
                return getKey(buffer.get(0));
            else {
                //System.out.println("Buffer vuoto");
                try {
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    System.err.println("Non waita");
                }
            }
        }
        throw new ChannelClosedException();
    }

    public synchronized String popMessage() throws ChannelClosedException {
        while (!isClosed()) {
            if (!isEmpty()) {
                String message = buffer.get(0);
                buffer.remove(0);
                return message;
            }
            else {
                //System.out.println("Buffer vuoto");
                try {
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    System.err.println("Non waita");
                }
            }
        }
        throw new ChannelClosedException();
    }

    /**
     * This method pops the first message in the buffer
     * @return The popped message
     * @throws ChannelClosedException if connection is lost
     */
    public synchronized String nextMessage(CommunicationProtocol key) throws ChannelClosedException {
        while (!isClosed()) {
            if (hasMessages(key)) {
                String message;
                for (int i = 0; i < buffer.size(); i++) {
                    message = buffer.get(i);
                    if (getKey(message) == key) {
                        buffer.remove(message);
                        return message;
                    }
                }
            }
            else {
                //System.out.println("Buffer vuoto");
                try {
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    System.err.println("Non waita");
                }
            }
        }
        throw new ChannelClosedException();
    }

    public void countdown(int i) throws ChannelClosedException {
        write(keyToString(COUNTDOWN) + SEPARATOR + i);
    }

    /**
     * This method pops the first message in the buffer if it arrives in time
     * @return The popped message
     * @throws TimeoutException if the time is out
     * @throws ChannelClosedException if there's no connection
     */
    public synchronized String nextGameMessage(CommunicationProtocol key) throws ChannelClosedException, TimeoutException {
        resetTimeout();
        CountDown countDown = new CountDown(this, key);
        countDown.start();

        while (!isClosed() && countDown.isAlive()) {
            if (hasMessages(key)) {
                countDown.finish();
                return nextMessage(key);
            }
            else {
               // System.out.println("Buffer vuoto");
                try {
                    if (!hasMessages(key))
                        wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }

        if (isClosed())
            throw new ChannelClosedException();
        writeKeyWord(TIMEOUT);
        throw new TimeoutException();
    }

    /**
     * This method converts a key to a string
     * @param key keyword
     * @return related string
     */
    public static String keyToString(CommunicationProtocol key) {
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
        System.out.println("\nReceiving:\n" + message + "\n");
        CommunicationProtocol key = getKey(message);

        if (key != PING && key != PONG)
            saveMessage(message);
        else
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
     * This method converts the content in a boolean value
     * @param message String
     * @return content
     */
    public boolean readBoolean(String message) {
        int answer = readNumber(message);
        if (answer == 0)
            return true;
        return false;
    }

    /**
     * This method send a string
     * @param message string
     * @throws ChannelClosedException if connection is lost
     */
    public void write(String message) throws ChannelClosedException {
        synchronized (out) {
            if (!isClosed()) {
                System.out.println("\nSending:\n" + message + "\n");
                out.println(message);
                out.flush();
            } else
                throw new ChannelClosedException();
        }
    }

    /**
     * This method sends a single key
     * @param key key
     * @throws ChannelClosedException if connection is lost
     */
    public void writeKeyWord(CommunicationProtocol key) throws ChannelClosedException {
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
            String message = nextMessage(USERNAME);
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
    public String askUniqueUsername() throws ChannelClosedException {
        while (!isClosed()){
            writeKeyWord(UNIQUEUSERNAME);
            String message = nextMessage(USERNAME);
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
    public int askMatchType() throws ChannelClosedException {
        while (!isClosed()){
            writeKeyWord(MATCHTYPE);
            String message = nextMessage(MATCHTYPE);
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
            String message = nextGameMessage(WORKER);
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
        return getKey(nextMessage(RECEIVED)) == RECEIVED;
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
     * This method sends a player and tells if it was received
     * @param currentPlayer Sent player
     * @return boolean
     * @throws ChannelClosedException if there's no connection
     */
    public boolean sendCurrentPlayer(String currentPlayer) throws ChannelClosedException {
        write(keyToString(CURRENTPLAYER) + SEPARATOR + currentPlayer);
        return copy();
    }

    /**
     * this method sends to the user the map and tells if it was received
     * @param map
     * @return
     * @throws ChannelClosedException
     */
    public boolean sendMap(String map)throws ChannelClosedException {
        write(keyToString(MAP) + SEPARATOR + map);
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
     * This method sends the user an index of a card list
     * @param key Type of list
     * @param indexes int
     * @throws ChannelClosedException if there's no connection
     */
    public void writeChoicesFromList(CommunicationProtocol key, int[] indexes) throws ChannelClosedException{
        Type listType = new TypeToken<int[]>() {}.getType();
        write(keyToString(key) + SEPARATOR + new Gson().toJson(indexes, listType));
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
            String message = nextGameMessage(STARTPOSITION);
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
            String message = nextGameMessage(DESTINATION);
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
            String message = nextGameMessage(BUILD);
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
        if (!isClosed()) {
            write(keyToString(REMOVAL) + SEPARATOR + removals);
            String message = nextGameMessage(REMOVAL);
            return readNumber(message);
        }
        return -1;
    }

    public int askCard(String cards) throws TimeoutException, ChannelClosedException {
        if (!isClosed()) {
            write(keyToString(CARD) + SEPARATOR + cards);
            String message = nextGameMessage(CARD);
            return readNumber(message);
        }
        return -1;
    }

    public int[] askDeck(String deck) throws TimeoutException, ChannelClosedException {
        if (!isClosed()) {
            write(keyToString(DECK) + SEPARATOR + deck);
            String message = nextGameMessage(DECK);
            Type listType = new TypeToken<int[]>() {}.getType();
            return new Gson().fromJson(getContent(message), listType);
        }
        int[] quit = new int[1];
        quit[0] = -1;
        return quit;
    }

    /**
     * This method asks for confirmation  and waits for a reply
     * @param key key of Communication Protocol
     * @return boolean value of confirmation
     * @throws ChannelClosedException if there's no connection
     */
    public boolean askConfirmation(CommunicationProtocol key) throws TimeoutException, ChannelClosedException {
        if (!isClosed()) {
            write(keyToString(key));
            String message = nextGameMessage(key);
            return readBoolean(message);
        }
        throw new ChannelClosedException();
    }

    public void writeConfirmation(CommunicationProtocol key, int confirmation) throws ChannelClosedException {
        if (confirmation == quit)
            writeKeyWord(QUIT);
        else
            write(keyToString(key) + SEPARATOR + confirmation);
    }
}
