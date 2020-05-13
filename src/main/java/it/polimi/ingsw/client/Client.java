package it.polimi.ingsw.client;

import it.polimi.ingsw.client.view.cli.Cli;
import it.polimi.ingsw.client.view.gui.Gui;

public class Client {
    public static void main(String[] args) {
        if (args.length > 2 && args[2].equals("-cli"))
            Cli.main(args);
        else
            Gui.main(args);
    }
}