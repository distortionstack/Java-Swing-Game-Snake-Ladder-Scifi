import java.awt.Dimension;
import java.awt.Point;

public class ConstantConfig {
    private ConstantConfig(){};
}
class DisplayUI{
    public static final Dimension WINDOW_SIZE = new Dimension(1360,640);

    private DisplayUI(){};
}
class GameUI{
        public static final Point DICE_BUTTON_POINT = new Point(1100, 500);
        public static final Dimension DICE_BUTTON_DIMENSION = new Dimension(84, 87);
        public static final Point START_POINT_ONE_PLAYER = new Point(300,570);
        public static final Point INDEX_ONE_ONE_PLAYER = new Point(420, 618);
        public static final Dimension PLAYER_SIZE = new Dimension(15, 25);
        public static final Dimension LADDER_AND_SNAKES_CELL_SIZE = new Dimension(40,42);
        private GameUI() {}
        
    }
class GameLogical {
        static String [] SKINCODE_ARRAY = {"red","yellow","green","blue","purple","white"};
        static int SKINCODE_ARRAY_LENGTH = SKINCODE_ARRAY.length;
        static int INDEX_AMOUNT = 101;
        public static final int[][] LADDERS_UP = {
            {1, 38}, {4, 14}, {9, 31}, {21, 42}, {28, 84}, 
            {36, 44}, {51, 67}, {61, 81}, {71, 91}
        };

        public static final int[][] SNAKES_DOWN = {
            {16, 6}, {47, 26}, {49, 11}, {59, 44}, {62, 2}, 
            {64, 41}, {84, 21}, {93, 73}, {95, 75}, {98, 63}
        };
}

