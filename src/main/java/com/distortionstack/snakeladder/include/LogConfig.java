package com.distortionstack.snakeladder.include;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public final class LogConfig {
    private static final String LOG_DIRECTORY = "logs";
    private static final String LOG_FILE_PATH = LOG_DIRECTORY + "/app.log";
    private static boolean initialized;

    private LogConfig() {
    }

    public static synchronized void initialize() {
        if (initialized) {
            return;
        }

        try {
            Path logDirectory = Paths.get(LOG_DIRECTORY);
            Files.createDirectories(logDirectory);

            Logger rootLogger = LogManager.getLogManager().getLogger("");
            for (Handler handler : rootLogger.getHandlers()) {
                rootLogger.removeHandler(handler);
                handler.close();
            }

            FileHandler fileHandler = new FileHandler(LOG_FILE_PATH, true);
            fileHandler.setFormatter(new SimpleFormatter());
            fileHandler.setLevel(Level.ALL);

            rootLogger.addHandler(fileHandler);
            rootLogger.setLevel(Level.INFO);
            rootLogger.setUseParentHandlers(false);

            Logger.getLogger(LogConfig.class.getName()).info("Logging initialized at logs/app.log");
            initialized = true;
        } catch (IOException | SecurityException e) {
            System.err.println("[LogConfig] Failed to initialize logging: " + e.getMessage());
        }
    }
}
