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
        switch (surfaceType) {
            case WALL:
            case EMPTY:
                return true;
            case GRASS:
                return false;
            default:
                return false;
        }
    }
}
