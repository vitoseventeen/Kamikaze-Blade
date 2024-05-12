package Core.Model;

public class Tile {
    private final SurfaceType surfaceType;

    public Tile(SurfaceType surfaceType) {
        this.surfaceType = surfaceType;
    }

    public SurfaceType getSurfaceType() {
        return surfaceType;
    }

    public boolean hasCollision() {
        return switch (surfaceType) {
            case WALL, EMPTY, DOOR_CLOSED -> true;
            default -> false;
        };
    }
}
