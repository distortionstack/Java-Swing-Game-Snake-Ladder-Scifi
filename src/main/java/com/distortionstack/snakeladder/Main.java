package com.distortionstack.snakeladder;

import java.util.logging.Logger;

import com.distortionstack.snakeladder.include.LogConfig;
import com.distortionstack.snakeladder.ui.DisplayController;

public class Main {
    private static final Logger LOGGER = Logger.getLogger(Main.class.getName());

    public static void main(String[] args) {
        LogConfig.initialize();
        System.out.println("Starting Snake & Ladder Game...");
        new DisplayController();
    }
}


