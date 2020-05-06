package it.polimi.ingsw.server.socket;

import it.polimi.ingsw.server.controller.Controller;
import it.polimi.ingsw.server.model.User;

import java.io.IOException;

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
    public synchronized void run() {
        communicationChannel().write("USERNAME");
        String message = null;
        try {
            message = communicationChannel().read();
        } catch (IOException e) {
            e.printStackTrace();
        }
        boolean valid = false;
        while (!valid) {
            if (controller().userNameExists(message)) {
                communicationChannel().write("UNIQUEUSERNAME");
                try {
                    message = communicationChannel().read();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            else
                valid = true;
        }
        controller().userNames().add(message);
        controller().users().put(message, new User(message, communicationChannel()));
        try {
            controller().assignUserToLobby(controller().findUser(message));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
