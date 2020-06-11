package it.polimi.ingsw.client.view.cli;

import it.polimi.ingsw.server.model.Colour;

import java.util.HashMap;
import java.util.Map;

public class Colors {

    /**
     * these are color codes for command line interface
     */
    // Reset
    public static final String RESET = "\033[0m";  // Text Reset
    public static final String RESET_BACKGROUND="\033[49m"; //reset background color

    // Regular Colors
    public static final String BLACK = "\033[0;30m";   // BLACK
    public static final String RED = "\033[0;31m";     // RED
    public static final String GREEN = "\033[0;32m";   // GREEN
    public static final String YELLOW = "\033[0;33m";  // YELLOW
    public static final String BLUE = "\033[0;34m";    // BLUE
    public static final String PURPLE = "\033[0;35m";  // PURPLE
    public static final String CYAN = "\033[0;36m";    // CYAN
    public static final String WHITE = "\033[0;37m";   // WHITE

    // Bold
    public static final String BLACK_BOLD = "\033[1;30m";  // BLACK
    public static final String RED_BOLD = "\033[1;31m";    // RED
    public static final String GREEN_BOLD = "\033[1;32m";  // GREEN
    public static final String YELLOW_BOLD = "\033[1;33m"; // YELLOW
    public static final String BLUE_BOLD = "\033[1;34m";   // BLUE
    public static final String PURPLE_BOLD = "\033[1;35m"; // PURPLE
    public static final String CYAN_BOLD = "\033[1;36m";   // CYAN
    public static final String WHITE_BOLD = "\033[1;37m";  // WHITE


    // High Intensity
    public static final String BLACK_BRIGHT = "\033[0;90m";  // BLACK
    public static final String RED_BRIGHT = "\033[0;91m";    // RED
    public static final String GREEN_BRIGHT = "\033[0;92m";  // GREEN
    public static final String YELLOW_BRIGHT = "\033[0;93m"; // YELLOW
    public static final String BLUE_BRIGHT = "\033[0;94m";   // BLUE
    public static final String PURPLE_BRIGHT = "\033[0;95m"; // PURPLE
    public static final String CYAN_BRIGHT = "\033[0;96m";   // CYAN
    public static final String WHITE_BRIGHT = "\033[0;97m";  // WHITE

    // Bold High Intensity
    public static final String BLACK_BOLD_BRIGHT = "\033[1;90m"; // BLACK
    public static final String RED_BOLD_BRIGHT = "\033[1;91m";   // RED
    public static final String GREEN_BOLD_BRIGHT = "\033[1;92m"; // GREEN
    public static final String YELLOW_BOLD_BRIGHT = "\033[1;93m";// YELLOW
    public static final String BLUE_BOLD_BRIGHT = "\033[1;94m";  // BLUE
    public static final String PURPLE_BOLD_BRIGHT = "\033[1;95m";// PURPLE
    public static final String CYAN_BOLD_BRIGHT = "\033[1;96m";  // CYAN
    public static final String WHITE_BOLD_BRIGHT = "\033[1;97m"; // WHITE

    // High Intensity backgrounds
    public static final String BLACK_BACKGROUND_BRIGHT = "\033[0;100m";// BLACK
    public static final String RED_BACKGROUND_BRIGHT = "\033[0;101m";// RED
    public static final String GREEN_BACKGROUND_BRIGHT = "\033[0;102m";// GREEN
    public static final String YELLOW_BACKGROUND_BRIGHT = "\033[0;103m";// YELLOW
    public static final String BLUE_BACKGROUND_BRIGHT = "\033[0;104m";// BLUE

    // PURPLE
    public static final String CYAN_BACKGROUND_BRIGHT = "\033[0;106m";  // CYAN
    public static final String WHITE_BACKGROUND_BRIGHT = "\033[0;107m";   // WHITE

    // Background
    public static final String BLACK_BACKGROUND = "\033[40m";  // BLACK //nota di fra: sul mio terminale questo colore è bianco
    public static final String RED_BACKGROUND = "\033[41m";    // RED
    public static final String GREEN_BACKGROUND = "\033[42m";  // GREEN
    public static final String YELLOW_BACKGROUND = "\033[43m"; // YELLOW
    public static final String BLUE_BACKGROUND = "\033[44m";   // BLUE
    public static final String PURPLE_BACKGROUND = "\033[45m"; // PURPLE
    public static final String CYAN_BACKGROUND = "\033[46m";   // CYAN
    public static final String LIGHTGRAY_BACKGROUND = "\033[47m";  // LIGHTGREY
    public static final String DARKGRAY_BACKGROUND="\033[100m"; //DARKGREY
    public static final String WHITE_BACKGROUND="\033[107m"; //(should be, but it's not) WHITE

    public static final String WHITE_TRY_BACKGROUND="\033[7m";


    private static final HashMap<Colour, String> actualColors ;
    static {
        actualColors= new HashMap<>();
        actualColors.put(Colour.BLUE, Colors.CYAN_BACKGROUND);
        actualColors.put(Colour.GREY, Colors.GREEN_BACKGROUND_BRIGHT);
        actualColors.put(Colour.WHITE, Colors.RED_BACKGROUND);
    }


    public static String getActualColor(Colour colour) {
        return actualColors.get(colour);
    }
    private static final HashMap<Colour, String> actualWrittenColors ;
    static {
        actualWrittenColors= new HashMap<>();
        actualWrittenColors.put(Colour.BLUE, Colors.CYAN_BRIGHT);
        actualWrittenColors.put(Colour.GREY, Colors.GREEN_BRIGHT);
        actualWrittenColors.put(Colour.WHITE, Colors.RED);
    }
    public static String getActualWrittenColor(Colour colour) {
        return actualWrittenColors.get(colour);
    }

}
