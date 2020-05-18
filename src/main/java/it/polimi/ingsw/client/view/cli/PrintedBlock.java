package it.polimi.ingsw.client.view.cli;
import java.util.ArrayList;
import java.util.List;

public class PrintedBlock {
    private int x;
    private int y;
    private List<String> lines = new ArrayList<>();



    public PrintedBlock(int x,int y){
        this.x=x;
        this.y=y;
        this.lines.add(Colors.CYAN_BOLD_BRIGHT+"+-----------------");
        this.lines.add("|                 ");
        this.lines.add("|                 ");
        this.lines.add("|                 ");
        this.lines.add("|                 ");
    }

    public void groundFloorBlock(){
        this.setLines(new ArrayList<String>());
        this.lines.add(Colors.CYAN_BOLD_BRIGHT+"+-----------------");
        this.lines.add("|                 ");
        this.lines.add("|                 ");
        this.lines.add("|                 ");
        this.lines.add("|                 ");
    }

    public void firstFloorBlock(){
        this.setLines(new ArrayList<String>());
        this.lines.add(Colors.CYAN_BOLD_BRIGHT+"+-----------------");
        this.lines.add("| "+Colors.BLACK_BOLD_BRIGHT+Colors.LIGHTGRAY_BACKGROUND+"    1°level    "+Colors.CYAN_BRIGHT+" ");
        this.lines.add("| "+Colors.LIGHTGRAY_BACKGROUND+"               "+Colors.CYAN_BRIGHT+" ");
        this.lines.add("| "+Colors.LIGHTGRAY_BACKGROUND+"               "+Colors.CYAN_BRIGHT+" ");
        this.lines.add("| "+Colors.LIGHTGRAY_BACKGROUND+"               "+Colors.CYAN_BRIGHT+" ");

    }

    public void secondFloorBlock(){
        this.setLines(new ArrayList<String>());
        this.lines.add(Colors.CYAN_BOLD_BRIGHT+"+-----------------");
        this.lines.add("| "+Colors.DARKGRAY_BACKGROUND+"  "+Colors.LIGHTGRAY_BACKGROUND+Colors.BLACK_BOLD_BRIGHT+"  2°level  "+Colors.DARKGRAY_BACKGROUND+"  "+Colors.CYAN_BRIGHT+" ");
        this.lines.add("| "+Colors.DARKGRAY_BACKGROUND+"  "+Colors.LIGHTGRAY_BACKGROUND+"           "+Colors.DARKGRAY_BACKGROUND+"  "+Colors.CYAN_BRIGHT+" ");
        this.lines.add("| "+Colors.DARKGRAY_BACKGROUND+"  "+Colors.LIGHTGRAY_BACKGROUND+"           "+Colors.DARKGRAY_BACKGROUND+"  "+Colors.CYAN_BRIGHT+" ");
        this.lines.add("| "+Colors.DARKGRAY_BACKGROUND+"  "+Colors.LIGHTGRAY_BACKGROUND+"           "+Colors.DARKGRAY_BACKGROUND+"  "+Colors.CYAN_BRIGHT+" ");

    }

    public void thirdFloorBlock(){
        this.setLines(new ArrayList<String>());
        this.lines.add(Colors.CYAN_BOLD_BRIGHT+"+-----------------");
        this.lines.add("| "+Colors.DARKGRAY_BACKGROUND+"  "+Colors.LIGHTGRAY_BACKGROUND+Colors.BLACK_BOLD_BRIGHT+"  3°level  "+Colors.DARKGRAY_BACKGROUND+"  "+Colors.CYAN_BRIGHT+" ");
        this.lines.add("| "+Colors.DARKGRAY_BACKGROUND+"  "+Colors.BLACK_BACKGROUND+"           "+Colors.DARKGRAY_BACKGROUND+"  "+Colors.CYAN_BRIGHT+" ");
        this.lines.add("| "+Colors.DARKGRAY_BACKGROUND+"  "+Colors.BLACK_BACKGROUND+"           "+Colors.DARKGRAY_BACKGROUND+"  "+Colors.CYAN_BRIGHT+" ");
        this.lines.add("| "+Colors.DARKGRAY_BACKGROUND+"  "+Colors.LIGHTGRAY_BACKGROUND+"           "+Colors.DARKGRAY_BACKGROUND+"  "+Colors.CYAN_BRIGHT+" ");

    }

    public void domeBlock(){
        if (getLines().get(1).contains("3°level")) {
            this.setLines(new ArrayList<String>());
            this.lines.add(Colors.CYAN_BOLD_BRIGHT + "+-----------------");
            this.lines.add("|     " + Colors.BLUE_BACKGROUND + "       " + Colors.RESET_BACKGROUND + "     ");
            this.lines.add("|   " + Colors.BLUE_BACKGROUND + Colors.BLACK_BOLD_BRIGHT + "   tower   " + Colors.RESET_BACKGROUND + Colors.CYAN_BOLD_BRIGHT + "   ");
            this.lines.add("|   " + Colors.BLUE_BACKGROUND+ Colors.BLACK_BOLD_BRIGHT  + " complete! " + Colors.RESET_BACKGROUND + "   ");
            this.lines.add("|     " + Colors.BLUE_BACKGROUND + "       " + Colors.RESET_BACKGROUND + "     ");

        }
        else if (getLines().get(1).contains("2°level")) {
            this.setLines(new ArrayList<String>());
            this.lines.add(Colors.CYAN_BOLD_BRIGHT + "+-----------------");
            this.lines.add("|     " + Colors.BLUE_BACKGROUND + "       " + Colors.RESET_BACKGROUND + "     ");
            this.lines.add("|   " + Colors.BLUE_BACKGROUND + Colors.BLACK_BOLD_BRIGHT + "  dome on  " + Colors.RESET_BACKGROUND + Colors.CYAN_BOLD_BRIGHT + "   ");
            this.lines.add("|   " + Colors.BLUE_BACKGROUND + Colors.BLACK_BOLD_BRIGHT + " 2° level  " + Colors.RESET_BACKGROUND + "   ");
            this.lines.add("|     " + Colors.BLUE_BACKGROUND + "       " + Colors.RESET_BACKGROUND + "     ");

        }
        else if (getLines().get(1).contains("1°level")) {
            this.setLines(new ArrayList<String>());
            this.lines.add(Colors.CYAN_BOLD_BRIGHT + "+-----------------");
            this.lines.add("|     " + Colors.BLUE_BACKGROUND + "       " + Colors.RESET_BACKGROUND + "     ");
            this.lines.add("|   " + Colors.BLUE_BACKGROUND + Colors.BLACK_BOLD_BRIGHT + "  dome on  " + Colors.RESET_BACKGROUND + Colors.CYAN_BOLD_BRIGHT + "   ");
            this.lines.add("|   " + Colors.BLUE_BACKGROUND + Colors.BLACK_BOLD_BRIGHT + " 1° level  " + Colors.RESET_BACKGROUND + "   ");
            this.lines.add("|     " + Colors.BLUE_BACKGROUND + "       " + Colors.RESET_BACKGROUND + "     ");

        }
        else {
            this.setLines(new ArrayList<String>());
            this.lines.add(Colors.CYAN_BOLD_BRIGHT + "+-----------------");
            this.lines.add("|     " + Colors.BLUE_BACKGROUND + "       " + Colors.RESET_BACKGROUND + "     ");
            this.lines.add("|   " + Colors.BLUE_BACKGROUND + Colors.BLACK_BOLD_BRIGHT + "  dome on  " + Colors.RESET_BACKGROUND + Colors.CYAN_BOLD_BRIGHT + "   ");
            this.lines.add("|   " + Colors.BLUE_BACKGROUND + Colors.BLACK_BOLD_BRIGHT + "  ground   " + Colors.RESET_BACKGROUND + "   ");
            this.lines.add("|     " + Colors.BLUE_BACKGROUND + "       " + Colors.RESET_BACKGROUND + "     ");

        }


    }

    public void externalBlock(){ // this method creates a list of strings that if printed, print a ground level block
        this.setLines(new ArrayList<String>());
        this.lines.add(Colors.CYAN_BOLD_BRIGHT+"+-----------------+");
        this.lines.add("|                 |");
        this.lines.add("|                 |");
        this.lines.add("|                 |");
        this.lines.add("|                 |");
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

    public void groundFloorWithWorker(/**passerò il colore del player che ha questa view*/){
        this.setLines(new ArrayList<String>());
        this.lines.add(Colors.CYAN_BOLD_BRIGHT+"+-----------------");
        this.lines.add("|                 ");
        this.lines.add("|     "+Colors.RED_BACKGROUND+Colors.BLACK_BOLD_BRIGHT+"   W   "+Colors.RESET_BACKGROUND+"    "+Colors.CYAN_BRIGHT+" ");
        this.lines.add("|     "+Colors.RED_BACKGROUND+"       "+Colors.RESET_BACKGROUND+"    "+Colors.CYAN_BRIGHT+" ");
        this.lines.add("|                "+Colors.CYAN_BRIGHT+" ");

    }
    public void firstFloorWithWorker(/**passerò il colore del player che ha questa view*/){
        this.setLines(new ArrayList<String>());
        this.lines.add(Colors.CYAN_BOLD_BRIGHT+"+-----------------");
        this.lines.add("| "+Colors.LIGHTGRAY_BACKGROUND+Colors.BLACK_BOLD_BRIGHT+"    1°level    "+Colors.CYAN_BRIGHT+" ");
        this.lines.add("| "+Colors.LIGHTGRAY_BACKGROUND+"    "+Colors.RED_BACKGROUND+Colors.BLACK_BOLD_BRIGHT+"   W   "+Colors.LIGHTGRAY_BACKGROUND+"    "+Colors.CYAN_BRIGHT+" ");
        this.lines.add("| "+Colors.LIGHTGRAY_BACKGROUND+"    "+Colors.RED_BACKGROUND+"       "+Colors.LIGHTGRAY_BACKGROUND+"    "+Colors.CYAN_BRIGHT+" ");
        this.lines.add("| "+Colors.LIGHTGRAY_BACKGROUND+"               "+Colors.CYAN_BRIGHT+" ");

    }

    public void secondFloorWithWorker(/**passerò il colore del player che ha questa view*/){
        this.setLines(new ArrayList<String>());
        this.lines.add(Colors.CYAN_BOLD_BRIGHT+"+-----------------");
        this.lines.add("| "+Colors.DARKGRAY_BACKGROUND+"  "+Colors.LIGHTGRAY_BACKGROUND+Colors.BLACK_BOLD_BRIGHT+"  2°level  "+Colors.DARKGRAY_BACKGROUND+"  "+Colors.CYAN_BRIGHT+" ");
        this.lines.add("| "+Colors.DARKGRAY_BACKGROUND+"  "+Colors.LIGHTGRAY_BACKGROUND+"  "+Colors.GREEN_BACKGROUND+Colors.BLACK_BOLD_BRIGHT+"   W   "+Colors.LIGHTGRAY_BACKGROUND+"  "+Colors.DARKGRAY_BACKGROUND+"  "+Colors.CYAN_BRIGHT+" ");
        this.lines.add("| "+Colors.DARKGRAY_BACKGROUND+"  "+Colors.LIGHTGRAY_BACKGROUND+"  "+Colors.GREEN_BACKGROUND+"       "+Colors.LIGHTGRAY_BACKGROUND+"  "+Colors.DARKGRAY_BACKGROUND+"  "+Colors.CYAN_BRIGHT+" ");
        this.lines.add("| "+Colors.DARKGRAY_BACKGROUND+"  "+Colors.LIGHTGRAY_BACKGROUND+"           "+Colors.DARKGRAY_BACKGROUND+"  "+Colors.CYAN_BRIGHT+" ");


    }

    public void thirdFloorWithWorker(/**passerò il colore del player che ha questa view*/){
        this.setLines(new ArrayList<String>());
        this.lines.add(Colors.CYAN_BOLD_BRIGHT+"+-----------------");
        this.lines.add("| "+Colors.DARKGRAY_BACKGROUND+"  "+Colors.LIGHTGRAY_BACKGROUND+Colors.BLACK_BOLD_BRIGHT+"  3°level  "+Colors.DARKGRAY_BACKGROUND+"  "+Colors.CYAN_BRIGHT+" ");
        this.lines.add("| "+Colors.DARKGRAY_BACKGROUND+"  "+Colors.BLACK_BACKGROUND+"  "+Colors.CYAN_BACKGROUND+Colors.BLACK_BOLD_BRIGHT+"   W   "+Colors.BLACK_BACKGROUND+"  "+Colors.DARKGRAY_BACKGROUND+"  "+Colors.CYAN_BRIGHT+" ");
        this.lines.add("| "+Colors.DARKGRAY_BACKGROUND+"  "+Colors.BLACK_BACKGROUND+"  "+Colors.CYAN_BACKGROUND+"       "+Colors.BLACK_BACKGROUND+"  "+Colors.DARKGRAY_BACKGROUND+"  "+Colors.CYAN_BRIGHT+" ");
        this.lines.add("| "+Colors.DARKGRAY_BACKGROUND+"  "+Colors.LIGHTGRAY_BACKGROUND+"           "+Colors.DARKGRAY_BACKGROUND+"  "+Colors.CYAN_BRIGHT+" ");

    }


    public void withWorker() {
        if (getLines().get(1).contains("2°level")) {
            secondFloorWithWorker();
        }

        else if (getLines().get(1).contains("3°level"))
            thirdFloorWithWorker();

        else if(getLines().get(1).contains("1°level"))
            firstFloorWithWorker();
        else
            groundFloorWithWorker();
    }
}
