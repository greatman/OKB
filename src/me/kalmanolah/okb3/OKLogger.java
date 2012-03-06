package me.kalmanolah.okb3;

import java.util.logging.Level;
import java.util.logging.Logger;

public class OKLogger {
	private static Logger log;
	private static String prefix;

	public static void initialize(Logger newLog) {
		OKLogger.log = newLog;
		prefix = "[" + OKmain.name + "] ";
	}

	public static Logger getLog() {
		return log;
	}

	public static String getPrefix() {
		return prefix;
	}

	public static void setPrefix(String prefix) {
		OKLogger.prefix = prefix;
	}

	public static void info(String message) {
		log.info(prefix + message);
	}

	public static void dbinfo(String message) {
		log.info(prefix + "[DB] " + message);
	}

	public static void error(String message) {
		log.severe(prefix + message);
	}

	public static void warning(String message) {
		log.warning(prefix + message);
	}

	public static void config(String message) {
		log.config(prefix + message);
	}

	public static void log(Level level, String message) {
		log.log(level, prefix + message);
	}
}
