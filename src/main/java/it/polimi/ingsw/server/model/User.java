package it.polimi.ingsw.server.model;

import it.polimi.ingsw.network.CommunicationChannel;
import it.polimi.ingsw.network.CommunicationProtocol;

import java.io.IOException;

import static it.polimi.ingsw.network.CommunicationProtocol.*;

public class User {
    final private CommunicationChannel communicationChannel;
    final private String name;

    public User(String name, CommunicationChannel communicationChannel){
        this.communicationChannel = communicationChannel;
        this.name = name;
    }

    public CommunicationChannel communicationChannel() {
        return communicationChannel;
    }

    public String name(){
        return name;
    }

    public boolean hasChannel() {
        return communicationChannel() != null;
    }

    public String hear() throws IOException {
        return communicationChannel().read();
    }

    public int hearNumber() throws IOException {
        return communicationChannel().readNumber();
    }

    public void tell(String message) {
        communicationChannel().write(message);
    }

    private void tell(CommunicationProtocol key) {
        communicationChannel().writeKeyWord(key);
    }

    public int askTwoOrThreePlayerMatch() throws IOException {
        tell(MATCHTYPE);
        int answer = hearNumber();
        System.out.println(answer);
        boolean valid = false;
        while (!valid) {
            if (answer == 1 || answer == 2)
                valid = true;
            else {
                tell(MATCHTYPE);
                answer = hearNumber();
            }
        }
        if (answer == 2)
            return 3;
        return 2;
    }
}
