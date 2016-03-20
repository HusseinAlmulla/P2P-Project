package edu.polyu.comp.util;

import java.util.logging.Level;
import java.util.logging.Logger;

public class LoggerUtil {
	public static void info(String className, String msg) {
        Logger.getLogger(className).log(Level.INFO, msg);
    }

    public static void warning(String className, String msg) {
        Logger.getLogger(className).log(Level.WARNING, msg);
    }

    public static void error(String className, String msg) {
        Logger.getLogger(className).log(Level.SEVERE, msg);
    }
}
