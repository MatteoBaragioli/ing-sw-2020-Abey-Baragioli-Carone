package it.polimi.ingsw.server.socket;

import it.polimi.ingsw.network.CommunicationChannel;
import it.polimi.ingsw.server.controller.Controller;
import it.polimi.ingsw.server.model.User;

import java.io.IOException;

import static it.polimi.ingsw.network.CommunicationProtocol.*;

public class UserManager {
    final private Controller controller;
    final private CommunicationChannel communicationChannel;

    public UserManager(Controller controller, CommunicationChannel communicationChannel) {
        this.controller = controller;
        this.communicationChannel = communicationChannel;
    }

    public Controller controller() {
        return controller;
    }

    public CommunicationChannel communicationChannel() {
        return communicationChannel;
    }

    /**
     * This method
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
            if (controller().userNameExists(message)) {
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
        controller().addUser(new User(message, communicationChannel()));
        System.out.println("Assigning lobby to " + message);
        controller().assignUserToLobby(controller().findUser(message));
    }
}
