package ModelTest;

import Model.NPC;
import Model.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.awt.*;
import java.io.IOException;
import javax.imageio.ImageIO;
import static org.junit.jupiter.api.Assertions.*;

public class NpcTest {

    private NPC npc;
    private Player player;

    @BeforeEach
    void setUp() {
        npc = new NPC(100, 100);
        player = new Player("TestPlayer", 0, 0, 50, 50, null);
    }

    @Test
    void testInteract() {
        assertFalse(npc.isTalking());
        assertTrue(npc.interact(player));
        assertTrue(npc.isTalking());
        assertFalse(npc.interact(player)); // Should not interact if already talking
    }

    @Test
    void testTask1Complete() {
        assertFalse(npc.isTask1Complete());
        npc.setTask1Complete(true);
        assertTrue(npc.isTask1Complete());
    }

    @Test
    void testLoadImages() {
        try {
            Image npcImage = ImageIO.read(getClass().getResource("/npcStand.png"));
            Image npcQuest = ImageIO.read(getClass().getResource("/npcQuest.png"));
            Image task1 = ImageIO.read(getClass().getResource("/Task1.png"));
            Image taskCompleted = ImageIO.read(getClass().getResource("/TaskFinish.png"));

            assertNotNull(npcImage);
            assertNotNull(npcQuest);
            assertNotNull(task1);
            assertNotNull(taskCompleted);
        } catch (IOException e) {
            fail("Failed to load images");
        }
    }
}
