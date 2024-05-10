package Core.Util;

import java.awt.*;

public class Constants {
    public static final int TILE_SIZE = 128;

    public static final int GAME_WIDTH = 1280;
    public static final int GAME_HEIGHT = 1024;

    public static final int TARGET_FPS = 60;
    public static final Dimension SCREEN_SIZE_DIMENSION = new Dimension(GAME_WIDTH, GAME_HEIGHT);


    public static final int PLAYER_WIDTH = 16;
    public static final int PLAYER_HEIGHT = 16;
    public static final int NUMBER_OF_ENEMIES = 5;
    public static final double ZOOM_FACTOR = 2.5; // 2.5 skoro idealni
    public static final int ATTACK_RADIUS = 30;
    public static final long ATTACK_COOLDOWN = 800; // Время в миллисекундах между атаками

}
