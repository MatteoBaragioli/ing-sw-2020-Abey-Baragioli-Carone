package it.polimi.ingsw.server.socket;

import it.polimi.ingsw.network.CommunicationChannel;
import it.polimi.ingsw.network.exceptions.ChannelClosedException;
import it.polimi.ingsw.server.controller.DataBase;
import it.polimi.ingsw.server.model.User;

public class UserManager extends Thread {

    final private DataBase dataBase;
    final private CommunicationChannel communicationChannel;

    public UserManager(DataBase dataBase, CommunicationChannel communicationChannel) {
        this.dataBase = dataBase;
        this.communicationChannel = communicationChannel;
    }

    public DataBase dataBase() {
        return dataBase;
    }

    public CommunicationChannel communicationChannel() {
        return communicationChannel;
    }

    /**
     * This method creates a user for a socket
     */
    public void run() {
        String message = null;
        try {
            message = communicationChannel().askUsername();
        } catch (ChannelClosedException e) {
            e.printStackTrace();
            //dataBase.deleteConnection(communicationChannel);
            return;
        }
        boolean valid = false;
        while (!valid && !communicationChannel().isClosed()) {
            if (dataBase().userNameExists(message)) {
                try {
                    message = communicationChannel().askUniqueUsername();
                } catch (ChannelClosedException e) {
                    e.printStackTrace();
                    //dataBase.deleteConnection(communicationChannel);
                    return;
                }
            }
            else
                valid = true;
        }
        if (!communicationChannel().isClosed()) {
            System.out.println("Registering new user " + message);
            User user = new User(message, communicationChannel());
            dataBase().addUser(user);
            System.out.println("Assigning lobby to " + message);
            dataBase().assignUserToLobby(user);
        }
    }
}
