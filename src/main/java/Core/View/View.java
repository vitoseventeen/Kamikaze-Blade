package Core.View;

import Core.Controller.Controller;
import Core.Controller.InputHandler;
import Core.Model.Player;

import javax.swing.*;
import java.awt.*;

public class View {
    private JFrame frame;
    private Panel panel;

    public View(Player player) {
        frame = new JFrame("Ninja Nexus");
        panel = new Panel(player);

        Controller controller = new Controller(player, panel,panel.getLevelManager());
        InputHandler inputHandler = new InputHandler(controller);
        panel.setFocusable(true);
        panel.requestFocusInWindow();
        panel.addKeyListener(inputHandler);
        frame.setPreferredSize(new Dimension(1280, 1024));

        ImageIcon icon = new ImageIcon("assets/icon.png");
        frame.setIconImage(icon.getImage());

        frame.add(panel);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.pack();
        frame.setVisible(true);
    }

    public Panel getPanel() {
        return panel;
    }

}
