package it.polimi.ingsw.client.view.cli;

import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import static it.polimi.ingsw.client.view.cli.Colors.*;

public class ScreenView {
    private static final int mapDim=5;
    private static final int boxHeight=5;
    private PrintedMap map=new PrintedMap();
    private PrintStream ps;
    private List<String> infoMessageBox=new ArrayList<>();
    private List<String> turnMessageBox=new ArrayList<>();
    private List<String> infoMessage=new ArrayList<>();
    private List<String> turnMessage=new ArrayList<>();
    public ScreenView(PrintStream ps, List<String> infoMessage){
        this.ps=ps;
        this.setInfoMessageBox(infoMessage);
    }
    public ScreenView(PrintStream ps ){
        this.ps=ps;
    }

    private List<String> getInfoMessageBox(){
        return this.infoMessageBox;
    }

    public void setInfoMessageBox(List<String> lines){
        this.infoMessageBox=boxMessage(lines);
    }

    public  List<String> getInfoMessage(){
        return this.infoMessage;
    }

    public void setInfoMessage(List<String> lines){
            this.infoMessage = lines;
            setInfoMessageBox(lines);

    }

    public List<String> infoMessageBox(){
        return this.infoMessageBox;
    }

    public List<String> infoMessage(){
        return this.infoMessage;
    }

    private List<String> getTurnMessageBox(){
        return this.turnMessageBox;
    }

    public void setTurnMessageBox(List<String> lines){
        this.turnMessageBox=boxMessage(lines);
    }

    public  List<String> getTurnMessage(){
        return this.turnMessage;
    }

    public void setTurnMessage(List<String> lines){
            this.turnMessage = lines;
            setTurnMessageBox(lines);
    }

    public List<String> TurnMessageBox(){
        return this.turnMessageBox;
    }

    public List<String> turnMessage(){
        return this.infoMessage;
    }

    public PrintedMap map(){
        return this.map;
    }

    /**
     * this method prints the game screen, including the map, the opponents info and the last turn events
     */
    public void turn() {
        int i;
        int j;
        int k;
        //i am so sorry for what you are about to witness
        //these three for cycles are to correctly print the game map, which is made up of 'blocks' that are lists of strings
        //this algorithm prints the map line per line
        for (k = 0; k < mapDim; k++){ //k represents the y abscissa

            for (j = 0, i = 0; j < boxHeight; j++) { //j represents the index of the string contained in the block to print

                if (j!=2)                        //at the second line of every row of blocks print the value of the Y coordinate
                    ps.print (CYAN_BRIGHT+"  "); //or else print the equivalent empty space
                else
                    ps.print (CYAN_BRIGHT+(mapDim-k)+" ");

                ps.print(this.map.position(0, mapDim-k-1).getLines().get(j));

                for (i = 1; i <= mapDim - 1; i++) {
                    ps.append(this.map.position(i, mapDim - k - 1).getLines().get(j)); //with method append multiple strings back to back can be printed
                    if(i==mapDim-1 && j!=0){ //when the strings of 5 (mapDim-1) blocks have been appended, the character '|' gets appended
                        ps.append('|');      //to confine the map
                    }
                    if(i==boxHeight-1 && j==0){ //also when a complete row has been printed
                        ps.append('+');         //the character '+' gets appended to confine the map and to divide rows
                    }
                }
                if(infoMessageBox.size()>0 && (j+((boxHeight)*k)<infoMessageBox.size())){ // this verifies the info message size
                    ps.append("        "+this.infoMessageBox.get(j+((boxHeight)*k)));     // and prints it line per line next to the map
                }

                if((j+((boxHeight)*k)>=infoMessageBox.size()) && turnMessageBox.size()>0 && (j+((boxHeight)*k)-infoMessageBox.size()<turnMessageBox.size())){ // this verifies the turn message size
                    ps.append("        "+this.turnMessageBox.get(j+((boxHeight)*k)-infoMessageBox.size()));                                                   // and prints it line per line next to the map
                                                                                                                                                              // and under the info message box
                }
                ps.append("\n");

            }
        }

        ps.print(CYAN_BRIGHT+"  +-----------------+-----------------+-----------------+-----------------+-----------------+\n");
        ps.print(CYAN_BRIGHT+"           A                 B                 C                 D                 E\n"+RESET);
    }

    /**
     * this method given a list of strings puts a frame around it for it to be printed in cli
     * @param message
     * @return the same message framed
     */
    public List<String> boxMessage (List<String> message) {
        List<String> messageBox=new ArrayList<>();
        int maxLength=0;
        int i;
        int j=2;
        String buffer;
        if(!message.isEmpty()) {
            messageBox.add(0, RESET + RED + "+----");
            messageBox.add(1, RESET + RED + "|    ");
            for (i = 0; i < message.size(); i++, j++) {
                messageBox.add(j, RESET + RED + "|  " + RESET + message.get(i) + RED);
                if (message.get(i).contains("[")) {
                    buffer = messageBox.get(j);
                    buffer = buffer.concat("           ");
                    messageBox.set(j, buffer);
                }

                if (message.get(i).length() > maxLength) {
                    maxLength = message.get(i).length();
                }
            }

            for (j = 2, i = 0; i < message.size(); i++, j++) {
                int difference = (maxLength - message.get(i).length());
                for (int repeat = 0; repeat < difference + 2; repeat++) {
                    buffer = messageBox.get(j);
                    buffer = buffer.concat(" ");
                    messageBox.set(j, buffer);
                }

            }
            messageBox.add(RESET + RED + "|    ");
            messageBox.add(RESET + RED + "+----");

            for (i = 0; i < maxLength; i++) {
                buffer = messageBox.get(0); //adding to the first string in the list, which is the upper frame, the char "-";
                buffer = buffer.concat("-");
                messageBox.set(0, buffer);

                buffer = messageBox.get(1); //adding to the second string in the list, which is an empty line, the char " ";
                buffer = buffer.concat(" ");
                messageBox.set(1, buffer);

                buffer = messageBox.get(j); //adding to the second last string in the list, which is an empty line, the char " ";
                buffer = buffer.concat(" ");
                messageBox.set(j, buffer);

                buffer = messageBox.get(j + 1); //adding to the last string in the list, which is the down frame, the char "-";
                buffer = buffer.concat("-");
                messageBox.set(j + 1, buffer);

            }

            buffer = messageBox.get(0); //adding to the first string in the list, which is the upper frame, the char "┐";
            buffer = buffer.concat("+" + RESET);
            messageBox.set(0, buffer);

            buffer = messageBox.get(j + 1); //adding to the last string in the list, which is the down frame, the char "┘";
            buffer = buffer.concat("+" + RESET);
            messageBox.set(j + 1, buffer);

            for (i = 1; i < messageBox.size() - 1; i++) {
                buffer = messageBox.get(i);
                buffer = buffer.concat("|" + RESET);
                messageBox.set(i, buffer);
            }
        }
        return messageBox;
    }

    /**
     * this method prints the title of the game
     */
    public void title()   {
        ps.println("\n");
        ps.println("\n");
        ps.println(BLUE_BRIGHT+"                                               Welcome to...");
        ps.println(BLUE_BRIGHT+"                        _______     _____     ___      ___ ____________  _________   _________   ____  ___      ____ ____        ");
        ps.println(BLUE_BRIGHT+"                      /    ____|   /      \\  |   \\    |  | |___    ___| /  _____  \\ |   ___   \\  |  |  |   \\    |  | |  |   ");
        ps.println(BLUE_BRIGHT+"                     |    |___    /   /\\   \\ |     \\  |  |     |  |    |  /     \\  ||  |___|   | |  |  |     \\  |  | |  |   ");
        ps.println(BLUE_BRIGHT+"                      \\_____   \\ |   ____   ||  |\\   \\|  |     |  |    | |       | ||  _____  /  |  |  |  |\\   \\|  | |  |  ");
        ps.println(BLUE_BRIGHT+"                       _____|   ||  |    |  ||  |  \\     |     |  |    |  \\_____/  ||  |    \\  \\ |  |  |  |  \\     | |  |   ");
        ps.println(BLUE_BRIGHT+"                      |________/ |__|    |__||__|    \\___|     | _|     \\_________/ |__|     \\__||__|  |__|    \\___| |__|    ");
        ps.println(RESET+"\n");
        ps.println("\n");

    }

    /**
     * this method clears the screen in cli
     */
    public void clearScreen()
    {
        //Clears Screen in java
        try {
            if (System.getProperty("os.name").contains("Windows"))
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            else {
                System.out.print("\033[H\033[2J");
                System.out.flush();
                //Runtime.getRuntime().exec("clear");
            }
        } catch (IOException | InterruptedException ignore) {}
        System.out.println(" ");
    }


}


