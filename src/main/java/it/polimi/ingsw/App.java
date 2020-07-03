package it.polimi.ingsw;

import it.polimi.ingsw.client.view.cli.Cli;
import it.polimi.ingsw.client.view.gui.Gui;
import it.polimi.ingsw.server.socket.Server;
import javafx.application.Application;

public class App {
    public static void main(String[] args) {
        if (args.length > 0) {
            String parameter = args[0];
            if (parameter.equals("-cli"))
                new Cli().run();
            else if (parameter.equals("-server"))
               new Server(args).run();
        }
        else {
                Application.launch(Gui.class, args);
        }
    }
}