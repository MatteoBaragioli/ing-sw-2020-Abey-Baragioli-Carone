package it.polimi.ingsw.client.view.cli;

import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

public class ScreenView {
    private static final int mapDim=5;
    private static final int boxHeight=5;
    private PrintedMap map=new PrintedMap();
    private PrintStream ps;
    private List<String> infoMessageBox=new ArrayList<>();
    private List<String> turnMessageBox=new ArrayList<>();

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

    public List<String> infoMessageBox(){
        return this.infoMessageBox;
    }


    private List<String> getTurnMessageBox(){
        return this.turnMessageBox;
    }

    public void setTurnMessageBox(List<String> lines){
        this.turnMessageBox=boxMessage(lines);
    }

    public List<String> TurnMessageBox(){
        return this.turnMessageBox;
    }

    public PrintedMap map(){
        return this.map;
    }

    public void turn() {
        int i;
        int j;
        int k;

        for (k = 0; k < mapDim; k++){ //k è l'ascisse y


            for (j = 0, i = 0; j < boxHeight; j++) {

                if (j!=2)
                    ps.print (Colors.CYAN_BRIGHT+"  ");
                else
                    ps.print (Colors.CYAN_BRIGHT+(mapDim-k)+" ");

                ps.print(this.map.position(0, mapDim-k-1).getLines().get(j));

                for (i = 1; i <= mapDim - 1; i++) {
                    ps.append(this.map.position(i, mapDim - k - 1).getLines().get(j));
                    if(i==mapDim-1 && j!=0){
                        ps.append('|');
                    }
                    if(i==mapDim-1 && j==0){
                        ps.append('+');
                    }
                }
                if(infoMessageBox.size()>0 && (j+((boxHeight)*k)<infoMessageBox.size())){
                    ps.append("        "+this.infoMessageBox.get(j+((boxHeight)*k)));
                }
                //if((j+((boxHeight)*k)==infoMessageBox.size())) {
                //   ps.append("\n");
                // }
                if((j+((boxHeight)*k)>=infoMessageBox.size()) && turnMessageBox.size()>0 && (j+((boxHeight)*k)-infoMessageBox.size()<turnMessageBox.size())){
                    ps.append("        "+this.turnMessageBox.get(j+((boxHeight)*k)-infoMessageBox.size()));
                }
                ps.append("\n");

            }
        }

        ps.print(Colors.CYAN_BRIGHT+"  +-----------------+-----------------+-----------------+-----------------+-----------------+\n");
        ps.print(Colors.CYAN_BRIGHT+"           A                 B                 C                 D                 E\n"+Colors.RESET);
    }

    public List<String> boxMessage (List<String> message) {
        List<String> messageBox=new ArrayList<>();
        int maxLength=0;
        int i;
        int j=2;
        String buffer;
        messageBox.add(0,Colors.RESET+Colors.RED+"+----");
        messageBox.add(1,Colors.RESET+Colors.RED+"|    ");
        for(i=0 ; i<message.size(); i++, j++){
            messageBox.add(j, Colors.RESET+Colors.RED+"|  "+Colors.RESET+message.get(i)+Colors.RED);
            if (message.get(i).contains("[") ){
                buffer=messageBox.get(j);
                buffer=buffer.concat("           ");
                messageBox.set(j, buffer);
            }

            if( message.get(i).length()>maxLength){
                maxLength=message.get(i).length();
            }
        }

        for (j=2, i=0; i<message.size(); i++, j++){
            int difference=(maxLength-message.get(i).length());
            for(int repeat=0; repeat<difference+2; repeat++){
                buffer=messageBox.get(j);
                buffer=buffer.concat(" ");
                messageBox.set(j, buffer);
            }

        }
        messageBox.add(Colors.RESET+Colors.RED+"|    ");
        messageBox.add(Colors.RESET+Colors.RED+"+----");

        for(i=0; i<maxLength; i++) {
            buffer=messageBox.get(0); //adding to the first string in the list, which is the upper frame, the char "-";
            buffer=buffer.concat("-");
            messageBox.set(0, buffer);

            buffer=messageBox.get(1); //adding to the second string in the list, which is an empty line, the char " ";
            buffer=buffer.concat(" ");
            messageBox.set(1, buffer);

            buffer=messageBox.get(j); //adding to the second last string in the list, which is an empty line, the char " ";
            buffer=buffer.concat(" ");
            messageBox.set(j, buffer);

            buffer=messageBox.get(j+1); //adding to the last string in the list, which is the down frame, the char "-";
            buffer=buffer.concat("-");
            messageBox.set(j+1, buffer);

        }

        buffer=messageBox.get(0); //adding to the first string in the list, which is the upper frame, the char "┐";
        buffer=buffer.concat("+"+Colors.RESET);
        messageBox.set(0, buffer);

        buffer=messageBox.get(j+1); //adding to the last string in the list, which is the down frame, the char "┘";
        buffer=buffer.concat("+"+Colors.RESET);
        messageBox.set(j+1, buffer);

        for (i=1; i<messageBox.size()-1; i++){
            buffer=messageBox.get(i);
            buffer=buffer.concat("|"+Colors.RESET);
            messageBox.set(i, buffer);
        }

        return messageBox;
    }

    public void title()   {
        ps.println("\n");
        ps.println("\n");
        ps.println(Colors.BLUE_BRIGHT+"                                                   Welcome to...");
        ps.println(Colors.BLUE_BRIGHT+"                            _______     _____     ___      ___ ____________  _________   _________   ____  ___      ____ ____            ");
        ps.println(Colors.BLUE_BRIGHT+"                          /    ____|   /      \\  |   \\    |  | |___    ___| /  _____  \\ |   ___   \\  |  |  |   \\    |  | |  |               ");
        ps.println(Colors.BLUE_BRIGHT+"                         |    |___    /   /\\   \\ |     \\  |  |     |  |    |  /     \\  ||  |___|   | |  |  |     \\  |  | |  |          ");
        ps.println(Colors.BLUE_BRIGHT+"                          \\_____   \\ |   ____   ||  |\\   \\|  |     |  |    | |       | ||  _____  /  |  |  |  |\\   \\|  | |  |             ");
        ps.println(Colors.BLUE_BRIGHT+"                           _____|   ||  |    |  ||  |  \\     |     |  |    |  \\_____/  ||  |    \\  \\ |  |  |  |  \\     | |  |              ");
        ps.println(Colors.BLUE_BRIGHT+"                          |________/ |__|    |__||__|    \\___|     | _|     \\_________/ |__|     \\__||__|  |__|    \\___| |__|                  ");
        ps.println(Colors.RESET+"\n");
        ps.println("\n");

    }

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
    }


}


