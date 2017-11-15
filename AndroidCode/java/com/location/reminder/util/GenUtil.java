package com.location.reminder.util;


public class GenUtil {
    public static String capitalize(String line) {

        if (line.trim().length() > 0)
            return Character.toUpperCase(line.charAt(0)) + line.substring(1);
        else
            return line;
    }
}
