package utils;

/**
 * Created by Roderick on 2016-10-25.
 */
public class LogIt {
    static int LEVEL = 1;
    public static void debug(String s) {
        if (LEVEL > 1) System.out.printf("DEBUG: %s \n", s);
    }

    public static void info(String s) {
        System.out.printf("INFO: %s \n", s);
    }
}
