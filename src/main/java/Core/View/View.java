package Core.View;

import Core.Controller.Controller;
import Core.Controller.InputHandler;
import Core.Model.Level;
import Core.Model.Player;
import Core.Model.Enemy;
import javax.swing.*;

import static Core.Util.Constants.SCREEN_SIZE_DIMENSION;

public class View {
    private JFrame frame;
    private Panel panel;
    private final Level level;

    public View(Player player, Level level, Enemy enemy) {
        frame = new JFrame("Ninja Nexus");
        panel = new Panel(player, level, enemy);
        this.level = level;
        Controller controller = new Controller(player, panel, level);
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