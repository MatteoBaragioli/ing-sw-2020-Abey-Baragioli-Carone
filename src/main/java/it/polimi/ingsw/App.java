package it.polimi.ingsw;

import it.polimi.ingsw.server.controller.Controller;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        System.out.println( "Hello World!" );
        Controller controller = new Controller();
        controller.start();
    }
}