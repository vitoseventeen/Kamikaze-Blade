package Core.View;

import Core.Controller.Controller;
import Core.Controller.InputHandler;
import Core.Model.Level;
import Core.Model.Player;
import Core.Model.Enemy;
import javax.swing.*;

import java.util.List;

import static Core.Util.Constants.SCREEN_SIZE_DIMENSION;

public class View {
    private JFrame frame;
    private Panel panel;
    private final Level level;
    private final Player player;
    private final List<Enemy> enemies;

    public View(Player player, Level level, List<Enemy> enemies) {
        frame = new JFrame("Kamikaze Blade");
        panel = new Panel(player, level);
        this.level = level;
        this.player = player;
        this.enemies = enemies;
        Controller controller = new Controller(player, panel, level, enemies);
        InputHandler inputHandler = new InputHandler(controller);
        panel.setFocusable(true);
        panel.requestFocusInWindow();
        panel.addKeyListener(inputHandler);
        frame.setPreferredSize(SCREEN_SIZE_DIMENSION);

        ImageIcon icon = new ImageIcon("assets/icon.png");
        frame.setIconImage(icon.getImage());

        frame.add(panel);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public Panel getPanel() {
        return panel;
    }

}
