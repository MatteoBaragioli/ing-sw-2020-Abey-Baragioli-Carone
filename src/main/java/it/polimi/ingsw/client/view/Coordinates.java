package it.polimi.ingsw.client.view;

public class Coordinates{

    /**
     * This method converts numeric server coordinates into letter-number coordinates
     * For example (0,0) is A1
     * @param coordinates Numeric coordinates to convert
     * @return Converted coordinates
     */
    public static String getChessCoordinates(int[] coordinates){
        String x = Character.toString((char)(coordinates[0]+65));
        String y = String.valueOf(coordinates[1] + 1);
        return x+y;
    }

    /**
     * This method converts letter-number coordinates into server's numeric coordinates
     * For example A1 is (0,0)
     * @param coordinates Coordinates to convert
     * @return Converted coordinates
     */
    public static int[] getCartesianCoordinates(String coordinates){
        int x= Character.getNumericValue(coordinates.charAt(0))-10;
        int y= Character.getNumericValue(coordinates.charAt(1))-1;
        return new int[]{x,y};
    }
}
