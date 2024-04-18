package Core.Model;

import java.util.ArrayList;
import java.util.List;

public class LevelManager {
    private List<Level> levels;

    public LevelManager() {
        levels = new ArrayList<>();
        addLevel("/level1.png");
        addLevel("/level2.png");
        addLevel("/level3.png");
        addLevel("/level4.png");
    }

    private void addLevel(String imagePath) {
        Level level = new Level(imagePath);
        levels.add(level);
    }

    public Level getLevel(int index) {
        if (index >= 0 && index < levels.size()) {
            return levels.get(index);
        }
        return null;
    }

    public int getNumLevels() {
        return levels.size();
    }
}
