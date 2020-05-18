package it.polimi.ingsw.server.socket;

import it.polimi.ingsw.network.CommunicationChannel;
import it.polimi.ingsw.server.controller.DataBase;
import it.polimi.ingsw.server.model.User;

import java.io.IOException;

import static it.polimi.ingsw.network.CommunicationProtocol.UNIQUEUSERNAME;
import static it.polimi.ingsw.network.CommunicationProtocol.USERNAME;

public class UserManager {

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
        communicationChannel().writeKeyWord(USERNAME);
        String message = null;
        try {
            message = communicationChannel().read();
        } catch (IOException e) {
            communicationChannel().close();
            e.printStackTrace();
            System.err.println("Can't get username from client");
            System.exit(1);
        }
        boolean valid = false;
        while (!valid) {
            if (dataBase().userNameExists(message)) {
                communicationChannel().writeKeyWord(UNIQUEUSERNAME);
                try {
                    message = communicationChannel().read();
                } catch (IOException e) {
                    communicationChannel().close();
                    e.printStackTrace();
                    System.err.println("Can't get another username from client");
                    System.exit(1);
                }
            }
            else
                valid = true;
        }

        System.out.println("Registering new user " + message);
        dataBase().addUser(new User(message, communicationChannel()));
        System.out.println("Assigning lobby to " + message);
        dataBase().assignUserToLobby(dataBase().findUser(message));
    }
}
