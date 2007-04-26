/**
 * Bolzplatz 2006
 * Copyright (C) 2006 by Xenoage Software
 * 
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA
 */
package com.xenoage.bp2k6.util;

import com.xenoage.bp2k6.GameEngine;

import java.io.*;
import java.text.DateFormat;
import java.util.Date;


/**
 * A Class for logging into the file "log.txt"
 * 
 * @author Johannes Loepelmann
 */
public class Logging {
    private static File logFile = null;
    private static PrintStream writer = null;
    private static int logLevel = 0;

    public static final int LEVEL_ALL = 0;
    public static final int LEVEL_DEBUG = 1;
    public static final int LEVEL_MESSAGES = 2;
    public static final int LEVEL_WARNINGS = 3;
    public static final int LEVEL_ERRORS = 4;

    /**
     * static function for initializing the logger
     *
     */

    public static void init()
    {
        logFile = new File("log.txt");
        if (logFile.exists())
        {
            logFile.delete();
            try {
                logFile.createNewFile();
            } catch (IOException e) {
                //Logging.log(null,e);
                System.exit(1);
            }

        }
        else
        {
            try {
                logFile.createNewFile();
            } catch (IOException e) {
                //Logging.log(null,e);
                System.exit(1);
            }
        }
        try
        {
            writer = new PrintStream(new FileOutputStream(logFile));
            writer.print("\n" + time() + " " +
              getLevelString(LEVEL_MESSAGES) + " Log started for: " +
              GameEngine.PROJECT_NAME_VERSION);
        }
        catch (IOException e)
        {
            // Logging.log(null,e);
            System.exit(1);
        }
    }

    /**
     * Writes a message to the log.
     * @param level The LoggingLevel -> if the message occurs in the log, or not.
     * @param sender Object, which send the message, in most cases <code>this</code>
     * @param msg The Message
     */

    public static void log(int level, Object sender, String msg) {
        if (writer == null) {
            init();
        }

        if (logLevel <= level) {
            if (sender == null) {
                sender = new Object();
            }
            writer.print("\n" + time() + " " +
              getLevelString(level) + " " + sender.getClass().getName() + ": " + msg);
            writer.flush();
        }

    }

    /**
     * Writing a message without a sender
     * @param level LoggingLevel
     * @param msg The Message
     */

    public static void log(int level, String msg) {
        if (writer == null) {
            init();
        }


        if (logLevel <= level)
            writer.print("\n" + time() + " " +
              getLevelString(level) + " Message: " + msg);
            writer.flush();
    }

    /**
     * Writes an exception to the log.
     * @param sender The Object, that caused the exception
     * @param e The thrown exception
     */
    public static void log(Object sender, Exception e)
    {
      log(LEVEL_ERRORS, sender, e);
    }
    
    
    /**
     * Writes an exception to the log.
     * @param level LoggingLevel
     * @param sender The Object, that caused the exception
     * @param e The thrown exception
     */
    public static void log(int level, Object sender, Exception e)
    {
      if (sender == null) {
          sender = new Object();
      }
      if (writer == null) {
          init();
      }
      writer.print("\n" + time() + " " +
        getLevelString(level) + " " + sender.getClass().getName() + " threw an exception: " + e.getMessage() +
        "\nStack Trace:\n");
      e.printStackTrace(writer);
      writer.flush();
    }
    

    /**
     * Sets the log-level to some value, look at the constants
     * @param Level The new logginglevel
     */

    public static void setLoggingLevel(int Level) {
        logLevel = Level;

    }

    /**
     * Closes and flushes the logging-File
     */
    public static void close() {
        writer.print("\n" + time() + " " +
          getLevelString(LEVEL_MESSAGES) + " Logging closed.");
        writer.flush();
        writer.close();
    }

    /**
     * Returns the current time in the format HH:MM:SS.
     * @return a string containing the time
     */
    private static String time() {
        DateFormat df = DateFormat.getTimeInstance(DateFormat.MEDIUM);

        String s = null;
        s = df.format(new Date());
        return s;
    }

    /**
     * Returns a string that representates a log level
     */
    private static String getLevelString(int level)
    {
      if (level == LEVEL_MESSAGES)
        return "       ";
      else if (level == LEVEL_WARNINGS)
        return "WARNING";
      else if (level == LEVEL_ERRORS)
        return "ERROR  ";
      else if (level == LEVEL_DEBUG)
        return "DEBUG  ";
      else
        return "???    ";
    }

}
