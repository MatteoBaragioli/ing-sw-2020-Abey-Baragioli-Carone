package it.polimi.ingsw.client.view;

public class Coordinates{

    public static String getChessCoordinates(int[] coordinates){
        String x = Character.toString((char)(coordinates[0]+65));
        String y = String.valueOf(coordinates[1] + 1);
        return x+y;
    }

    public static int[] getCartesianCoordinates(String coordinates){
        int x= Character.getNumericValue(coordinates.charAt(0))-10;
        int y= Character.getNumericValue(coordinates.charAt(1))-1;
                //int x = Integer.parseInt(String.valueOf(coordinates.charAt(0)))-65;
        //int y = Integer.parseInt(String.valueOf(coordinates.charAt(1))) -1;
       // int x = ((int) coordinates.charAt(0)) - 65;
        //int y = ((int) coordinates.charAt(1)) -49;
        return new int[]{x,y};
    }
}
