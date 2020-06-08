package it.polimi.ingsw.client.view;

public class Coordinates{

    public static String getViewCoordinates(int[] coordinates){
        String x = Character.toString((char)(coordinates[0]+65));
        String y = String.valueOf(coordinates[1] + 1);
        return x+y;
    }

    public static int[] getServerCoordinates(String coordinates){
        int x = Integer.parseInt(String.valueOf(coordinates.charAt(0))) - 65;
        int y = Integer.parseInt(String.valueOf(coordinates.charAt(1))) -1;
        return new int[] {x,y};
    }
}
