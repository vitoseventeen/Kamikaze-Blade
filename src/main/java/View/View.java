package View;

import Controller.Controller;
import Controller.GameManager;
import Controller.InputHandler;
import Model.Level;
import Model.Player;
import Model.Enemy;

import javax.swing.*;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;

import static Util.Constants.SCREEN_SIZE_DIMENSION;

public class View {
    private final JFrame frame;
    private final GamePanel gamePanel;

    /**
     * Constructor for the View class
     * @param player Player object
     * @param level Level object
     * @param enemies List of Enemy objects
     * @param gameManager GameManager object
     */

    public View(Player player, Level level, List<Enemy> enemies, GameManager gameManager) {
        frame = new JFrame("Kamikaze Blade");
        gamePanel = new GamePanel(player, level);
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

    /**
     * Getter for the GamePanel object
     * @return GamePanel object
     */

    public GamePanel getPanel() {
        return gamePanel;
    }

    /**
     * Getter for the JFrame object
     * @return JFrame object
     */

    public JFrame getFrame() {
        return frame;
    }
}
