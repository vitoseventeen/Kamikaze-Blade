package Core.Util;

import java.awt.*;

public class Constants {
    public static final int TILE_SIZE = 64;

    public static final int GAME_WIDTH = 1280;
    public static final int GAME_HEIGHT = 1024;

    public static final int TARGET_FPS = 60;
    public static final Dimension SCREEN_SIZE_DIMENSION = new Dimension(GAME_WIDTH, GAME_HEIGHT);

    public static final int INVENTORY_COLUMNS = 7;
    public static final int INVENTORY_ROWS = 2;

    public static final int INVENTORY_CELL_WIDTH = 808 / INVENTORY_COLUMNS;

    public static final int INVENTORY_CELL_HEIGHT = 320 / INVENTORY_ROWS;

    public static final int INVENTORY_X = 240;

    public static final int INVENTORY_Y = 320;

    public static final int PLAYER_WIDTH = 16;
    public static final int PLAYER_HEIGHT = 16;
    public static final int NUMBER_OF_ENEMIES = 5;
    public static final double ZOOM_FACTOR = 3.0; // 2.5 skoro idealni
    public static final int ATTACK_RADIUS = 45;
    public static final int INTERACTION_RADIUS = 25;

    public static final long ATTACK_COOLDOWN = 800; // Время в миллисекундах между атаками

}
