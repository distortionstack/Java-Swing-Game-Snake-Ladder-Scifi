package com.distortionstack.snakeladder.include.config;

import java.awt.Dimension;
import java.awt.Point;

public class GameUI{
        public static final Point DICE_BUTTON_POINT = new Point(1100, 500);
        public static final Dimension DICE_BUTTON_DIMENSION = new Dimension(84, 87);
        public static final Point START_POINT_ONE_PLAYER = new Point(300,570);
        public static final Point INDEX_ONE_ONE_PLAYER = new Point(420, 618);
        public static final Dimension PLAYER_SIZE = new Dimension(15, 25);
        public static final Dimension LADDER_AND_SNAKES_CELL_SIZE = new Dimension(40,42);

         // Cell dimensions (pixels)
        public static final int CELL_WIDTH_PX = 56;
        public static final int CELL_HEIGHT_PX = 58;
        public static final int CELL_OFFSET_ADJUSTMENT_X = 14;
        
        public static final int DICE_PANEL_SIZE = 180; // ลูกเต๋าสี่เหลี่ยมจตุรัส
        // Player rendering
        public static final int PLAYER_SIZE_WIDTH = 32;
        public static final int PLAYER_SIZE_HEIGHT = 32;
        private GameUI() {}
        
    }
