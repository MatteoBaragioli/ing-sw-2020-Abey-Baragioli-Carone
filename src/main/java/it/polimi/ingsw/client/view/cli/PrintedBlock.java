package it.polimi.ingsw.client.view.cli;
import it.polimi.ingsw.server.model.Colour;

import java.util.ArrayList;
import java.util.List;

import static it.polimi.ingsw.client.view.cli.Colors.*;

/**
 * class used to represent a block on the game map
 * attributes x & y represent the block's coordinates
 */
public class PrintedBlock {
    private int x;
    private int y;
    private List<String> lines = new ArrayList<>();


    /**
     * this constructor method initialises a block as a ground level block
     * @param x
     * @param y
     */
    public PrintedBlock(int x,int y){ // this method creates a list of strings that if printed, print a ground level block
        this.x=x;
        this.y=y;
        this.lines.add(CYAN_BOLD_BRIGHT+"+-----------------");
        this.lines.add("|                 ");
        this.lines.add("|                 ");
        this.lines.add("|                 ");
        this.lines.add("|                 ");
    }

    /**
     * this method changes the block to a ground floor block in cli
     */
    public void groundFloorBlock(){
        this.lines.clear();
        this.lines.add(CYAN_BRIGHT+"+-----------------");
        this.lines.add(CYAN_BRIGHT+"|                 ");
        this.lines.add(CYAN_BRIGHT+"|                 ");
        this.lines.add(CYAN_BRIGHT+"|                 ");
        this.lines.add(CYAN_BRIGHT+"|                 ");
    }

    /**
     * this method changes the block to a first floor block in cli
     */
    public void firstFloorBlock(){
        this.lines.clear();
        this.lines.add(CYAN_BOLD_BRIGHT+"+-----------------");
        this.lines.add("| "+LIGHTGRAY_BACKGROUND+BLACK_BOLD_BRIGHT+"    1°level    "+CYAN_BRIGHT+" ");
        this.lines.add("| "+LIGHTGRAY_BACKGROUND+"               "+CYAN_BRIGHT+" ");
        this.lines.add("| "+LIGHTGRAY_BACKGROUND+"               "+CYAN_BRIGHT+" ");
        this.lines.add("| "+LIGHTGRAY_BACKGROUND+"               "+CYAN_BRIGHT+" ");

    }


    /**
     * this method changes the block to a second floor block in cli
     */
    public void secondFloorBlock(){
        this.lines.clear();
        this.lines.add(CYAN_BOLD_BRIGHT+"+-----------------");
        this.lines.add("| "+DARKGRAY_BACKGROUND+"  "+LIGHTGRAY_BACKGROUND+BLACK_BOLD_BRIGHT+"  2°level  "+DARKGRAY_BACKGROUND+"  "+CYAN_BRIGHT+" ");
        this.lines.add("| "+DARKGRAY_BACKGROUND+"  "+LIGHTGRAY_BACKGROUND+"           "+DARKGRAY_BACKGROUND+"  "+CYAN_BRIGHT+" ");
        this.lines.add("| "+DARKGRAY_BACKGROUND+"  "+LIGHTGRAY_BACKGROUND+"           "+DARKGRAY_BACKGROUND+"  "+CYAN_BRIGHT+" ");
        this.lines.add("| "+DARKGRAY_BACKGROUND+"  "+LIGHTGRAY_BACKGROUND+"           "+DARKGRAY_BACKGROUND+"  "+CYAN_BRIGHT+" ");

    }

    /**
     * this method changes the block to a third floor block in cli
     */
    public void thirdFloorBlock(){
        this.lines.clear();
        this.lines.add(CYAN_BOLD_BRIGHT+"+-----------------");
        this.lines.add("| "+DARKGRAY_BACKGROUND+"  "+LIGHTGRAY_BACKGROUND+BLACK_BOLD_BRIGHT+"  3°level  "+DARKGRAY_BACKGROUND+"  "+CYAN_BRIGHT+" ");
        this.lines.add("| "+DARKGRAY_BACKGROUND+"  "+WHITE_BACKGROUND+"           "+DARKGRAY_BACKGROUND+"  "+CYAN_BRIGHT+" ");
        this.lines.add("| "+DARKGRAY_BACKGROUND+"  "+WHITE_BACKGROUND+"           "+DARKGRAY_BACKGROUND+"  "+CYAN_BRIGHT+" ");
        this.lines.add("| "+DARKGRAY_BACKGROUND+"  "+LIGHTGRAY_BACKGROUND+"           "+DARKGRAY_BACKGROUND+"  "+CYAN_BRIGHT+" ");

    }

    /**
     * this method displays a dome on block and depending on the block's level writes on what level the dome is
     */
    public void domeBlock(){
        if (getLines().get(1).contains("3°level")) {
            this.lines.clear();
            this.lines.add(CYAN_BOLD_BRIGHT + "+-----------------");
            this.lines.add("|     " + BLUE_BACKGROUND + "       " + RESET_BACKGROUND + "     ");
            this.lines.add("|   " + BLUE_BACKGROUND + BLACK_BOLD_BRIGHT + "   tower   " + RESET_BACKGROUND + CYAN_BOLD_BRIGHT + "   ");
            this.lines.add("|   " + BLUE_BACKGROUND+ BLACK_BOLD_BRIGHT  + " complete! " + RESET_BACKGROUND + CYAN_BOLD_BRIGHT + "   ");
            this.lines.add("|     " + BLUE_BACKGROUND + "       " + RESET_BACKGROUND + "     ");

        }
        else if (getLines().get(1).contains("2°level")) {
            this.lines.clear();
            this.lines.add(CYAN_BOLD_BRIGHT + "+-----------------");
            this.lines.add("|     " + BLUE_BACKGROUND + "       " + RESET_BACKGROUND + "     ");
            this.lines.add("|   " + BLUE_BACKGROUND + BLACK_BOLD_BRIGHT + "  dome on  " + RESET_BACKGROUND + CYAN_BOLD_BRIGHT + "   ");
            this.lines.add("|   " + BLUE_BACKGROUND + BLACK_BOLD_BRIGHT + " 2° level  " + RESET_BACKGROUND + CYAN_BOLD_BRIGHT + "   ");
            this.lines.add("|     " + BLUE_BACKGROUND + "       " + RESET_BACKGROUND + "     ");

        }
        else if (getLines().get(1).contains("1°level")) {
            this.lines.clear();
            this.lines.add(CYAN_BOLD_BRIGHT + "+-----------------");
            this.lines.add("|     " + BLUE_BACKGROUND + "       " + RESET_BACKGROUND + "     ");
            this.lines.add("|   " + BLUE_BACKGROUND + BLACK_BOLD_BRIGHT + "  dome on  " + RESET_BACKGROUND + CYAN_BOLD_BRIGHT + "   ");
            this.lines.add("|   " + BLUE_BACKGROUND + BLACK_BOLD_BRIGHT + " 1° level  " + RESET_BACKGROUND + CYAN_BOLD_BRIGHT + "   ");
            this.lines.add("|     " + BLUE_BACKGROUND + "       " + RESET_BACKGROUND + "     ");

        }
        else {
            this.lines.clear();
            this.lines.add(CYAN_BOLD_BRIGHT + "+-----------------");
            this.lines.add("|     " + BLUE_BACKGROUND + "       " + RESET_BACKGROUND + "     ");
            this.lines.add("|   " + BLUE_BACKGROUND + BLACK_BOLD_BRIGHT + "  dome on  " + RESET_BACKGROUND + CYAN_BOLD_BRIGHT + "   ");
            this.lines.add("|   " + BLUE_BACKGROUND + BLACK_BOLD_BRIGHT + "  ground   " + RESET_BACKGROUND + CYAN_BOLD_BRIGHT + "   ");
            this.lines.add("|     " + BLUE_BACKGROUND + "       " + RESET_BACKGROUND + "     ");

        }


    }


    public List<String> getLines(){
        return this.lines;
    }

    public void setLines(List<String> newLines){
        this.lines=newLines;

    }
    public void setLine(String line, int index){
        this.lines.remove(index);
        this.lines.add(index, line);

    }

    public int getX(){
        return this.x;

    }
    public int getY(){
        return this.y;

    }

    public void customConcat(int indexOfLine , String lineToAdd){
        String buffer=new String();
        buffer=this.getLines().get(indexOfLine);
        buffer=buffer.concat(lineToAdd);
        this.setLine(buffer, indexOfLine);
    }

    /**
     * this method displays a worker on a ground level block
     * @param gender gender of the worker
     * @param color worker's colour
     */
    public void groundFloorWithWorker(boolean gender, Colour color){
        char genderChar;
        if (gender==true){
            genderChar='♀';
        }
        else
            genderChar='♂';
        this.lines.clear();
        this.lines.add(CYAN_BOLD_BRIGHT+"+-----------------");
        this.lines.add("|                 ");
        this.lines.add("|     "+getActualColor(color)+WHITE_BOLD_BRIGHT+"   W   "+RESET_BACKGROUND+"    "+CYAN_BRIGHT+" ");
        this.lines.add("|     "+getActualColor(color)+WHITE_BOLD_BRIGHT+"   "+genderChar+"   "+RESET_BACKGROUND+"    "+CYAN_BRIGHT+" ");
        this.lines.add("|                "+CYAN_BRIGHT+" ");

    }

    /**
     * this method displays a worker on a first level block
     * @param gender gender of the worker
     * @param color worker's colour
     */
    public void firstFloorWithWorker(boolean gender, Colour color){
        this.lines.clear();
        char genderChar;
        if (gender==true){
            genderChar='♀';
        }
        else
            genderChar='♂';
        this.lines.add(CYAN_BOLD_BRIGHT+"+-----------------");
        this.lines.add("| "+LIGHTGRAY_BACKGROUND+BLACK_BOLD_BRIGHT+"    1°level    "+CYAN_BRIGHT+" ");
        this.lines.add("| "+LIGHTGRAY_BACKGROUND+"    "+getActualColor(color)+WHITE_BOLD_BRIGHT+"   W   "+LIGHTGRAY_BACKGROUND+"    "+CYAN_BRIGHT+" ");
        this.lines.add("| "+LIGHTGRAY_BACKGROUND+"    "+getActualColor(color)+WHITE_BOLD_BRIGHT+"   "+genderChar+"   "+LIGHTGRAY_BACKGROUND+"    "+CYAN_BRIGHT+" ");
        this.lines.add("| "+LIGHTGRAY_BACKGROUND+"               "+CYAN_BRIGHT+" ");

    }

    /**
     * this method displays aa worker on a second level block
     * @param gender gender of the worker
     * @param color worker's colour
     */
    public void secondFloorWithWorker(boolean gender, Colour color){
        char genderChar;
        if (gender==true){
            genderChar='♀';
        }
        else
            genderChar='♂';
        this.lines.clear();
        this.lines.add(CYAN_BOLD_BRIGHT+"+-----------------");
        this.lines.add("| "+DARKGRAY_BACKGROUND+"  "+LIGHTGRAY_BACKGROUND+BLACK_BOLD_BRIGHT+"  2°level  "+DARKGRAY_BACKGROUND+"  "+CYAN_BRIGHT+" ");
        this.lines.add("| "+DARKGRAY_BACKGROUND+"  "+LIGHTGRAY_BACKGROUND+"  "+getActualColor(color)+WHITE_BOLD_BRIGHT+"   W   "+LIGHTGRAY_BACKGROUND+"  "+DARKGRAY_BACKGROUND+"  "+CYAN_BRIGHT+" ");
        this.lines.add("| "+DARKGRAY_BACKGROUND+"  "+LIGHTGRAY_BACKGROUND+"  "+getActualColor(color)+WHITE_BOLD_BRIGHT+"   "+genderChar+"   "+LIGHTGRAY_BACKGROUND+"  "+DARKGRAY_BACKGROUND+"  "+CYAN_BRIGHT+" ");
        this.lines.add("| "+DARKGRAY_BACKGROUND+"  "+LIGHTGRAY_BACKGROUND+"           "+DARKGRAY_BACKGROUND+"  "+CYAN_BRIGHT+" ");


    }

    /**
     * this method displays aa worker on a third level block
     * @param gender gender of the worker
     * @param color worker's colour
     */
    public void thirdFloorWithWorker(boolean gender, Colour color){
        char genderChar;
        if (gender==true){
            genderChar='♀';
        }
        else
            genderChar='♂';
        this.lines.clear();
        this.lines.add(CYAN_BOLD_BRIGHT+"+-----------------");
        this.lines.add("| "+DARKGRAY_BACKGROUND+"  "+LIGHTGRAY_BACKGROUND+BLACK_BOLD_BRIGHT+"  3°level  "+DARKGRAY_BACKGROUND+"  "+CYAN_BRIGHT+" ");
        this.lines.add("| "+DARKGRAY_BACKGROUND+"  "+WHITE_BACKGROUND+"  "+getActualColor(color)+WHITE_BOLD_BRIGHT+"   W   "+WHITE_BACKGROUND+"  "+DARKGRAY_BACKGROUND+"  "+CYAN_BRIGHT+" ");
        this.lines.add("| "+DARKGRAY_BACKGROUND+"  "+WHITE_BACKGROUND+"  "+getActualColor(color)+WHITE_BOLD_BRIGHT+"   "+genderChar+"   "+WHITE_BACKGROUND+"  "+DARKGRAY_BACKGROUND+"  "+CYAN_BRIGHT+" ");
        this.lines.add("| "+DARKGRAY_BACKGROUND+"  "+LIGHTGRAY_BACKGROUND+"           "+DARKGRAY_BACKGROUND+"  "+CYAN_BRIGHT+" ");

    }

    /**
     * this method, depending on the case, uses the three previous methods to display a worker on a generic block
     * @param gender
     * @param color
     */
    public void withWorker(boolean gender, Colour color) {
        if (getLines().get(1).contains("2°level")) {
            secondFloorWithWorker(gender, color);
        }

        else if (getLines().get(1).contains("3°level"))
            thirdFloorWithWorker(gender, color);

        else if(getLines().get(1).contains("1°level"))
            firstFloorWithWorker( gender,  color);
        else
            groundFloorWithWorker(gender,  color);
    }

}

