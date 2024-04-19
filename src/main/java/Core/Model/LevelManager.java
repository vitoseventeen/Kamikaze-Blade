package Core.Model;

import java.util.ArrayList;
import java.util.List;

public class LevelManager {
    private List<Level> levels;
    private int currentLevelIndex;

    public LevelManager() {
        levels = new ArrayList<>();
        addLevel();
        currentLevelIndex = 0;
    }

    private void addLevel() {
        Level level = LevelFactory.createLevel();
        levels.add(level);
    }

    public Level getLevel(int index) {
        if (index >= 0 && index < levels.size()) {
            return levels.get(index);
        }
        return null;
    }

    public Level getCurrentLevel() {
        return getLevel(currentLevelIndex);
    }

    public void nextLevel() {
        if (currentLevelIndex < levels.size() - 1) {
            currentLevelIndex++;
        }
    }

    public void previousLevel() {
        if (currentLevelIndex > 0) {
            currentLevelIndex--;
        }
    }

    public int getNumLevels() {
        return levels.size();
    }
}
