package Core.Model.level;

import java.util.ArrayList;
import java.util.List;

public class LevelManager {
    // Добавьте методы и данные, связанные с управлением уровнями
    private List<Level> levels;

    public LevelManager() {
        levels = new ArrayList<>();
        addLevel("/level1.png");

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
