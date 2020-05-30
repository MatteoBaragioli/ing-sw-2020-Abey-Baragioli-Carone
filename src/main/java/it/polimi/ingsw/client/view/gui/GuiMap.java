package it.polimi.ingsw.client.view.gui;

import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;

public class GuiMap extends GridPane {
    private GuiBox[][] boxesList;
    private final int columns;
    private final int rows;


    public GuiMap(int rowsNumber, int columnsNumber, double mapDim, double screenWidth, double screenHeight){
        createMap(mapDim, screenWidth, screenHeight, rowsNumber, columnsNumber);
        rows = rowsNumber;
        columns = columnsNumber;
    }

    public void createMap(double mapDim, double screenWidth, double screenHeight, int rowsNumber, int columnsNumber) {
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

    public void clearMap(){
        int i, j;
        for (i = 0; i < rows; i++) {
            for(j = 0;  j < columns; j++) {
                box(i,j).build(0);
                box(i,j).removeWorker();
            }
        }
    }
}
