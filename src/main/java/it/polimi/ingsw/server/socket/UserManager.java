package it.polimi.ingsw.server.socket;

import it.polimi.ingsw.server.model.User;

import java.net.Socket;
import java.util.List;
import java.util.Map;

public class UserManager extends Thread{
    private SocketServer socketServer;
    private List<String> userNames;
    private Map<String, User> users;
    private List<User> twoPlayerQueue;
    private List<User> threePlayerQueue;

    public UserManager(Socket socket, List<String> userNames, Map<String, User> users, List<User> twoPlayerQueue, List<User> threePlayerQueue) {
        this.socketServer = new SocketServer(socket);
        this.userNames = userNames;
        this.users = users;
        this.twoPlayerQueue = twoPlayerQueue;
        this.threePlayerQueue = threePlayerQueue;
        socketServer.start();
    }

    public SocketServer socketServer() {
        return socketServer;
    }

    public List<String> userNames() {
        return userNames;
    }

    public Map<String, User> users() {
        return users;
    }

    public List<User> twoPlayerQueue() {
        return twoPlayerQueue;
    }

    public List<User> threePlayerQueue() {
        return threePlayerQueue;
    }

    /**
     * This method tells if a userName is contained in the userNames List
     * @param username String
     * @return boolean
     */
    public boolean userNameExists(String username) {
        return userNames().contains(username);
    }

    /**
     * This method finds a user related to an existing userName
     * @param username String
     * @return User
     */
    public User findUser(String username) {
        if (users().containsKey(username))
            return users().get(username);
        return null;
    }

    /**
     * This method puts a user in the queue chosen by him
     * @param user User
     */
    public void assignQueueToUser(User user) {
        if (user.askTwoOrThreePlayerQueue())
            threePlayerQueue().add(user);
        else
            twoPlayerQueue().add(user);
    }

    public void run() {
        socketServer().write("Enter your username to login:");
        String message = socketServer().read();
        boolean valid = false;
        while (!valid) {
            if (userNameExists(message) && findUser(message).hasSocket()) {
                socketServer().write("Your username must be unique to login.\n" + "Enter another username:");
                message = socketServer().read();
            }
            else
                valid = true;
        }
        if (!userNameExists(message)) {
            userNames().add(message);
            users().put(message, new User(message, socketServer()));
        }
        else
            findUser(message).setSocket(socketServer());
        assignQueueToUser(findUser(message));
    }
}
