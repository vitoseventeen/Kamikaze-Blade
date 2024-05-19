package Core.Model;

/**
 * Represents a tile in the game world.
 */
public class Tile {
    private final SurfaceType surfaceType;

    /**
     * Constructs a tile with the specified surface type.
     *
     * @param surfaceType the surface type of the tile
     */
    public Tile(SurfaceType surfaceType) {
        this.surfaceType = surfaceType;
    }

    /**
     * Retrieves the surface type of the tile.
     *
     * @return the surface type of the tile
     */
    public SurfaceType getSurfaceType() {
        return surfaceType;
    }

    /**
     * Checks if the tile has collision.
     *
     * @return true if the tile has collision, false otherwise
     */
    public boolean hasCollision() {
        return switch (surfaceType) {
            case WALL, EMPTY -> true;
            default -> false;
        };
    }
}
