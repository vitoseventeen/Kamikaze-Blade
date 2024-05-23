package Util;

import java.awt.*;

/**
 * Contains constant values used throughout the game.
 */
public class Constants {
    /** The size of each tile in pixels. */
    public static final int TILE_SIZE = 64;

    /** The width of the game window. */
    public static final int GAME_WIDTH = 1280;

    /** The height of the game window. */
    public static final int GAME_HEIGHT = 1024;

    /** The target frames per second (FPS) for the game. */
    public static final int TARGET_FPS = 60;

    /** The screen size dimension. */
    public static final Dimension SCREEN_SIZE_DIMENSION = new Dimension(GAME_WIDTH, GAME_HEIGHT);

    /** The number of columns in the inventory. */
    public static final int INVENTORY_COLUMNS = 7;

    /** The number of rows in the inventory. */
    public static final int INVENTORY_ROWS = 2;

    /** The width of each cell in the inventory. */
    public static final int INVENTORY_CELL_WIDTH = 808 / INVENTORY_COLUMNS;

    /** The height of each cell in the inventory. */
    public static final int INVENTORY_CELL_HEIGHT = 320 / INVENTORY_ROWS;

    /** The x-coordinate of the inventory. */
    public static final int INVENTORY_X = 240;

    /** The y-coordinate of the inventory. */
    public static final int INVENTORY_Y = 320;

    /** The width of the player character. */
    public static final int PLAYER_WIDTH = 16;

    /** The height of the player character. */
    public static final int PLAYER_HEIGHT = 16;

    /** The number of enemies in the game. */
    public static final int NUMBER_OF_ENEMIES = 5;

    /** The zoom factor for the game. */
    public static final double ZOOM_FACTOR = 3.0;

    /** The radius for player attack. */
    public static final int ATTACK_RADIUS = 55;

    /** The radius for player interaction. */
    public static final int INTERACTION_RADIUS = 25;

    /** The delay between player movements in milliseconds. */
    public static final long MOVEMENT_DELAY = 30;

    /** The cooldown duration for player attacks in milliseconds. */
    public static final long ATTACK_COOLDOWN = 800;


    public static final int OBJECT_WIDTH = 16;
    public static final int OBJECT_HEIGHT = 16;
    public static final int DOOR_WIDTH = 64;
    public static final int DOOR_HEIGHT = 64;
    public static final int NPC_WIDTH = 17;
    public static final int NPC_HEIGHT = 17;
}
