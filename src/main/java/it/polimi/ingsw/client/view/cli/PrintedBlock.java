package it.polimi.ingsw.client.view.cli;
import it.polimi.ingsw.server.model.Colour;

import java.util.ArrayList;
import java.util.List;

import static it.polimi.ingsw.client.view.cli.Colors.*;

public class PrintedBlock {
    private int x;
    private int y;
    private List<String> lines = new ArrayList<>();



    public PrintedBlock(int x,int y){ // this method creates a list of strings that if printed, print a ground level block
        this.x=x;
        this.y=y;
        this.lines.add(CYAN_BOLD_BRIGHT+"+-----------------");
        this.lines.add("|                 ");
        this.lines.add("|                 ");
        this.lines.add("|                 ");
        this.lines.add("|                 ");
    }

    public void groundFloorBlock(){
        this.setLines(new ArrayList<String>());
        this.lines.add(CYAN_BRIGHT+"+-----------------");
        this.lines.add(CYAN_BRIGHT+"|                 ");
        this.lines.add(CYAN_BRIGHT+"|                 ");
        this.lines.add(CYAN_BRIGHT+"|                 ");
        this.lines.add(CYAN_BRIGHT+"|                 ");
    }

    public void firstFloorBlock(){
        this.setLines(new ArrayList<String>());
        this.lines.add(CYAN_BOLD_BRIGHT+"+-----------------");
        this.lines.add("| "+BLACK_BOLD_BRIGHT+LIGHTGRAY_BACKGROUND+"    1°level    "+CYAN_BRIGHT+" ");
        this.lines.add("| "+LIGHTGRAY_BACKGROUND+"               "+CYAN_BRIGHT+" ");
        this.lines.add("| "+LIGHTGRAY_BACKGROUND+"               "+CYAN_BRIGHT+" ");
        this.lines.add("| "+LIGHTGRAY_BACKGROUND+"               "+CYAN_BRIGHT+" ");

    }

    public void secondFloorBlock(){
        this.setLines(new ArrayList<String>());
        this.lines.add(CYAN_BOLD_BRIGHT+"+-----------------");
        this.lines.add("| "+DARKGRAY_BACKGROUND+"  "+LIGHTGRAY_BACKGROUND+BLACK_BOLD_BRIGHT+"  2°level  "+DARKGRAY_BACKGROUND+"  "+CYAN_BRIGHT+" ");
        this.lines.add("| "+DARKGRAY_BACKGROUND+"  "+LIGHTGRAY_BACKGROUND+"           "+DARKGRAY_BACKGROUND+"  "+CYAN_BRIGHT+" ");
        this.lines.add("| "+DARKGRAY_BACKGROUND+"  "+LIGHTGRAY_BACKGROUND+"           "+DARKGRAY_BACKGROUND+"  "+CYAN_BRIGHT+" ");
        this.lines.add("| "+DARKGRAY_BACKGROUND+"  "+LIGHTGRAY_BACKGROUND+"           "+DARKGRAY_BACKGROUND+"  "+CYAN_BRIGHT+" ");

    }

    public void thirdFloorBlock(){
        this.setLines(new ArrayList<String>());
        this.lines.add(CYAN_BOLD_BRIGHT+"+-----------------");
        this.lines.add("| "+DARKGRAY_BACKGROUND+"  "+LIGHTGRAY_BACKGROUND+BLACK_BOLD_BRIGHT+"  3°level  "+DARKGRAY_BACKGROUND+"  "+CYAN_BRIGHT+" ");
        this.lines.add("| "+DARKGRAY_BACKGROUND+"  "+BLACK_BACKGROUND+"           "+DARKGRAY_BACKGROUND+"  "+CYAN_BRIGHT+" ");
        this.lines.add("| "+DARKGRAY_BACKGROUND+"  "+BLACK_BACKGROUND+"           "+DARKGRAY_BACKGROUND+"  "+CYAN_BRIGHT+" ");
        this.lines.add("| "+DARKGRAY_BACKGROUND+"  "+LIGHTGRAY_BACKGROUND+"           "+DARKGRAY_BACKGROUND+"  "+CYAN_BRIGHT+" ");

    }

    public void domeBlock(){
        if (getLines().get(1).contains("3°level")) {
            this.setLines(new ArrayList<String>());
            this.lines.add(CYAN_BOLD_BRIGHT + "+-----------------");
            this.lines.add("|     " + BLUE_BACKGROUND + "       " + RESET_BACKGROUND + "     ");
            this.lines.add("|   " + BLUE_BACKGROUND + BLACK_BOLD_BRIGHT + "   tower   " + RESET_BACKGROUND + CYAN_BOLD_BRIGHT + "   ");
            this.lines.add("|   " + BLUE_BACKGROUND+ BLACK_BOLD_BRIGHT  + " complete! " + RESET_BACKGROUND + CYAN_BOLD_BRIGHT + "   ");
            this.lines.add("|     " + BLUE_BACKGROUND + "       " + RESET_BACKGROUND + "     ");

        }
        else if (getLines().get(1).contains("2°level")) {
            this.setLines(new ArrayList<String>());
            this.lines.add(CYAN_BOLD_BRIGHT + "+-----------------");
            this.lines.add("|     " + BLUE_BACKGROUND + "       " + RESET_BACKGROUND + "     ");
            this.lines.add("|   " + BLUE_BACKGROUND + BLACK_BOLD_BRIGHT + "  dome on  " + RESET_BACKGROUND + CYAN_BOLD_BRIGHT + "   ");
            this.lines.add("|   " + BLUE_BACKGROUND + BLACK_BOLD_BRIGHT + " 2° level  " + RESET_BACKGROUND + CYAN_BOLD_BRIGHT + "   ");
            this.lines.add("|     " + BLUE_BACKGROUND + "       " + RESET_BACKGROUND + "     ");

        }
        else if (getLines().get(1).contains("1°level")) {
            this.setLines(new ArrayList<String>());
            this.lines.add(CYAN_BOLD_BRIGHT + "+-----------------");
            this.lines.add("|     " + BLUE_BACKGROUND + "       " + RESET_BACKGROUND + "     ");
            this.lines.add("|   " + BLUE_BACKGROUND + BLACK_BOLD_BRIGHT + "  dome on  " + RESET_BACKGROUND + CYAN_BOLD_BRIGHT + "   ");
            this.lines.add("|   " + BLUE_BACKGROUND + BLACK_BOLD_BRIGHT + " 1° level  " + RESET_BACKGROUND + CYAN_BOLD_BRIGHT + "   ");
            this.lines.add("|     " + BLUE_BACKGROUND + "       " + RESET_BACKGROUND + "     ");

        }
        else {
            this.setLines(new ArrayList<String>());
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

    public void groundFloorWithWorker(boolean gender, Colour color){
        char genderChar;
        if (gender==true){
            genderChar='♀';
        }
        else
            genderChar='♂';
        this.setLines(new ArrayList<String>());
        this.lines.add(CYAN_BOLD_BRIGHT+"+-----------------");
        this.lines.add("|                 ");
        this.lines.add("|     "+getActualColor(color)+BLACK_BOLD_BRIGHT+"   W   "+RESET_BACKGROUND+"    "+CYAN_BRIGHT+" ");
        this.lines.add("|     "+getActualColor(color)+BLACK_BOLD_BRIGHT+"   "+genderChar+"   "+RESET_BACKGROUND+"    "+CYAN_BRIGHT+" ");
        this.lines.add("|                "+CYAN_BRIGHT+" ");

    }
    public void firstFloorWithWorker(boolean gender, Colour color){
        this.setLines(new ArrayList<String>());
        char genderChar;
        if (gender==true){
            genderChar='♀';
        }
        else
            genderChar='♂';
        this.lines.add(CYAN_BOLD_BRIGHT+"+-----------------");
        this.lines.add("| "+LIGHTGRAY_BACKGROUND+BLACK_BOLD_BRIGHT+"    1°level    "+CYAN_BRIGHT+" ");
        this.lines.add("| "+LIGHTGRAY_BACKGROUND+"    "+getActualColor(color)+BLACK_BOLD_BRIGHT+"   W   "+LIGHTGRAY_BACKGROUND+"    "+CYAN_BRIGHT+" ");
        this.lines.add("| "+LIGHTGRAY_BACKGROUND+"    "+getActualColor(color)+BLACK_BOLD_BRIGHT+"   "+genderChar+"   "+LIGHTGRAY_BACKGROUND+"    "+CYAN_BRIGHT+" ");
        this.lines.add("| "+LIGHTGRAY_BACKGROUND+"               "+CYAN_BRIGHT+" ");

    }

    public void secondFloorWithWorker(boolean gender, Colour color){
        char genderChar;
        if (gender==true){
            genderChar='♀';
        }
        else
            genderChar='♂';
        this.setLines(new ArrayList<String>());
        this.lines.add(CYAN_BOLD_BRIGHT+"+-----------------");
        this.lines.add("| "+DARKGRAY_BACKGROUND+"  "+LIGHTGRAY_BACKGROUND+BLACK_BOLD_BRIGHT+"  2°level  "+DARKGRAY_BACKGROUND+"  "+CYAN_BRIGHT+" ");
        this.lines.add("| "+DARKGRAY_BACKGROUND+"  "+LIGHTGRAY_BACKGROUND+"  "+getActualColor(color)+BLACK_BOLD_BRIGHT+"   W   "+LIGHTGRAY_BACKGROUND+"  "+DARKGRAY_BACKGROUND+"  "+CYAN_BRIGHT+" ");
        this.lines.add("| "+DARKGRAY_BACKGROUND+"  "+LIGHTGRAY_BACKGROUND+"  "+getActualColor(color)+BLACK_BOLD_BRIGHT+"   "+genderChar+"   "+LIGHTGRAY_BACKGROUND+"  "+DARKGRAY_BACKGROUND+"  "+CYAN_BRIGHT+" ");
        this.lines.add("| "+DARKGRAY_BACKGROUND+"  "+LIGHTGRAY_BACKGROUND+"           "+DARKGRAY_BACKGROUND+"  "+CYAN_BRIGHT+" ");


    }

    public void thirdFloorWithWorker(boolean gender, Colour color){
        char genderChar;
        if (gender==true){
            genderChar='♀';
        }
        else
            genderChar='♂';
        this.setLines(new ArrayList<String>());
        this.lines.add(CYAN_BOLD_BRIGHT+"+-----------------");
        this.lines.add("| "+DARKGRAY_BACKGROUND+"  "+LIGHTGRAY_BACKGROUND+BLACK_BOLD_BRIGHT+"  3°level  "+DARKGRAY_BACKGROUND+"  "+CYAN_BRIGHT+" ");
        this.lines.add("| "+DARKGRAY_BACKGROUND+"  "+BLACK_BACKGROUND+"  "+getActualColor(color)+BLACK_BOLD_BRIGHT+"   W   "+BLACK_BACKGROUND+"  "+DARKGRAY_BACKGROUND+"  "+CYAN_BRIGHT+" ");
        this.lines.add("| "+DARKGRAY_BACKGROUND+"  "+BLACK_BACKGROUND+"  "+getActualColor(color)+BLACK_BOLD_BRIGHT+"   "+genderChar+"   "+BLACK_BACKGROUND+"  "+DARKGRAY_BACKGROUND+"  "+CYAN_BRIGHT+" ");
        this.lines.add("| "+DARKGRAY_BACKGROUND+"  "+LIGHTGRAY_BACKGROUND+"           "+DARKGRAY_BACKGROUND+"  "+CYAN_BRIGHT+" ");

    }


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

