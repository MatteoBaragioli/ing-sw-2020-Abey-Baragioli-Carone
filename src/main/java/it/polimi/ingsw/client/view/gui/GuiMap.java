package it.polimi.ingsw.client.view.gui;

import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;

public class GuiMap extends GridPane {
    private GuiBox[][] boxesList;


    public GuiMap(int rowsNumber, int columnsNumber, double mapDim, double screenWidth, double screenHeight){
        createMap(rowsNumber, columnsNumber, mapDim, screenWidth, screenHeight);
    }

    public void createMap(int rowsNumber, int columnsNumber, double mapDim, double screenWidth, double screenHeight) {
        int i, j, k;
        boxesList = new GuiBox[rowsNumber][columnsNumber];
        for (i = 0; i < columnsNumber; i++) {
            ColumnConstraints column = new ColumnConstraints();
            column.setPercentWidth(100.0 / columnsNumber);
            getColumnConstraints().add(column);
            column.setHalignment(HPos.CENTER);
        }
        for (i = 0; i < rowsNumber; i++) {
            RowConstraints row = new RowConstraints();
            row.setPercentHeight(100.0 / rowsNumber);
            getRowConstraints().add(row);
            row.setValignment(VPos.CENTER);
        }

        double boxWidth = mapDim/columnsNumber-mapDim/20;
        double boxHeight = mapDim/rowsNumber-mapDim/20;
        for (j = 0, k=rowsNumber-1; j < rowsNumber; j++, k--) {
            for(i = 0;  i < columnsNumber; i++){
                GuiBox guiBox = new GuiBox(i, k, boxWidth, boxHeight, screenWidth, screenHeight);
                guiBox.setPrefWidth(boxWidth-boxWidth/5);
                guiBox.setPrefHeight(boxHeight-boxHeight/5);
                guiBox.setOpacity(0);
                add(guiBox, i, j);
                boxesList[i][k] = guiBox;
            }
        }
    }

    public GuiBox box(int x, int y){
        return boxesList[x][y];
    }
}
