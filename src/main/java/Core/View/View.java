package Core.View;

import Core.Controller.Controller;
import Core.Controller.GameManager;
import Core.Controller.InputHandler;
import Core.Model.Level;
import Core.Model.Player;
import Core.Model.Enemy;
import javax.swing.*;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;

import static Core.Util.Constants.SCREEN_SIZE_DIMENSION;

public class View {
    private JFrame frame;
    private GamePanel gamePanel;
    private final Level level;
    private final Player player;
    private final List<Enemy> enemies;
    private GameManager gameManager;

    public View(Player player, Level level, List<Enemy> enemies, GameManager gameManager) {
        frame = new JFrame("Kamikaze Blade");
        gamePanel = new GamePanel(player, level);
        this.level = level;
        this.player = player;
        this.enemies = enemies;
        this.gameManager = gameManager;
        Controller controller = new Controller(player, gamePanel, level, enemies, gameManager);
        InputHandler inputHandler = new InputHandler(controller);

        frame.setCursor(Toolkit.getDefaultToolkit().createCustomCursor(
                new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB),
                new Point(0, 0),
                "blank cursor"));
        gamePanel.setFocusable(true);
        gamePanel.requestFocusInWindow();
        gamePanel.addKeyListener(inputHandler);
        frame.setPreferredSize(SCREEN_SIZE_DIMENSION);

        ImageIcon icon = new ImageIcon("assets/icon.png");
        frame.setIconImage(icon.getImage());

        frame.add(gamePanel);


        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public GamePanel getPanel() {
        return gamePanel;
    }

    public JFrame getFrame() {
        return frame;
    }
}
