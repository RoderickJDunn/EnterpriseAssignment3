package utils;

/**
 * Created by Roderick on 2016-10-25.
 */
public class LogIt {
    static int LEVEL = 1;

    public static final String RED = "\u001B[31m";
    public static final String BLACK = "\u001B[30m";
    public static final String BLUE = "\u001B[34m";
    public static final String GREEN = "\u001B[32m";

    public static void error(String s, Object... args) {
        String stub = String.format(RED + "ERROR: %s\n" + RED, s);
        System.out.printf(stub, args);
    }

    public static void info(String s, Object... args) {
        if (LEVEL > 0) {
            String stub = String.format(BLACK + "INFO: %s\n" + BLACK, s);
            System.out.printf(stub, args);
        }
    }

    public static void debug(String s, Object... args) {
        if (LEVEL > 1) {
            String stub = String.format(BLUE + "DEBUG: %s\n" + BLUE, s);
            System.out.printf(stub, args);
        }
    }

    public static void verbose(String s, Object... args) {
        if (LEVEL > 2){
            String stub = String.format(GREEN + "VERBOSE: %s\n" + GREEN, s);
            System.out.printf(stub, args);
        }
    }
}
