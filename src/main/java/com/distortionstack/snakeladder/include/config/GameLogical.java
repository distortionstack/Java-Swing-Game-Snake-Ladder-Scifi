package com.distortionstack.snakeladder.include.config;

public class GameLogical {
        public static String [] SKINCODE_ARRAY = {"red","yellow","green","blue","purple","white"};
        public static int SKINCODE_ARRAY_LENGTH = SKINCODE_ARRAY.length;

        public static final int START_INDEX = 0;
        public static final int END_INDEX   = 100;

        public static int INDEX_AMOUNT = 101;
        public static final int[][] LADDERS_UP = {
            {1, 38}, {4, 14}, {9, 31}, {21, 42}, {28, 84}, 
            {36, 44}, {51, 67}, {61, 81}, {71, 91}
        };

        public static final int[][] SNAKES_DOWN = {
            {16, 6}, {47, 26}, {49, 11}, {59, 44}, {62, 2}, 
            {64, 41}, {84, 21}, {93, 73}, {95, 75}, {98, 63}
        };
}