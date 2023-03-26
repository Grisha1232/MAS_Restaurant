package org.example.Loggers;

import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class ProcessLogger {
    public static final Logger logger = Logger.getLogger(ProcessLogger.class.getName());
    public static final com.google.gson.Gson gson = new GsonBuilder()
            .setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
            .create();
    static {
        try {
            FileHandler fileHandler = new FileHandler("src/main/java/org/example/output/operation_log.txt");
            fileHandler.setFormatter(new SimpleFormatter());
            logger.addHandler(fileHandler);
            logger.setLevel(Level.ALL);
        } catch (Exception e) {
            logger.warning("Could not create log file: " + e.getMessage());
        }
    }
}
