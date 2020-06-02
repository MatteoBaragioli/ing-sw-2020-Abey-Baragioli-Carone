package it.polimi.ingsw.client.view.cli;

import java.util.HashMap;

public class Coordinates {

    private static final HashMap<String, int[]> chessCoordinates;
    static{
        chessCoordinates=new HashMap<>();
        chessCoordinates.put("A1", new int[]{0,0});
        chessCoordinates.put("A2", new int[]{0,1});
        chessCoordinates.put("A3", new int[]{0,2});
        chessCoordinates.put("A4", new int[]{0,3});
        chessCoordinates.put("A5", new int[]{0,4});

        chessCoordinates.put("B1", new int[]{1,0});
        chessCoordinates.put("B2", new int[]{1,1});
        chessCoordinates.put("B3", new int[]{1,2});
        chessCoordinates.put("B4", new int[]{1,3});
        chessCoordinates.put("B5", new int[]{1,4});

        chessCoordinates.put("C1", new int[]{2,0});
        chessCoordinates.put("C2", new int[]{2,1});
        chessCoordinates.put("C3", new int[]{2,2});
        chessCoordinates.put("C4", new int[]{2,3});
        chessCoordinates.put("C5", new int[]{2,4});

        chessCoordinates.put("D1", new int[]{3,0});
        chessCoordinates.put("D2", new int[]{3,1});
        chessCoordinates.put("D3", new int[]{3,2});
        chessCoordinates.put("D4", new int[]{3,3});
        chessCoordinates.put("D5", new int[]{3,4});

        chessCoordinates.put("E1", new int[]{4,0});
        chessCoordinates.put("E2", new int[]{4,1});
        chessCoordinates.put("E3", new int[]{4,2});
        chessCoordinates.put("E4", new int[]{4,3});
        chessCoordinates.put("E5", new int[]{4,4});


    }
    public static int[] getCartesianCoodinates (String query) {
        return chessCoordinates.get(query);
    }

}
