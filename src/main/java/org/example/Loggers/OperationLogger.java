package org.example.Loggers;

import com.google.gson.GsonBuilder;

import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class OperationLogger {
    public static final Logger logger = Logger.getLogger(OperationLogger.class.getName());
    public static final com.google.gson.Gson gson = new GsonBuilder()
            .setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
            .create();

    static {
        try {
            FileHandler fileHandler = new FileHandler("src/main/java/logs/process_logs");
            fileHandler.setFormatter(new SimpleFormatter());
            logger.addHandler(fileHandler);
            logger.setLevel(Level.ALL);
        } catch (Exception e) {
            logger.warning("Could not create log file: " + e.getMessage());
        }
    }
}
