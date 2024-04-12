package Core.View;

import Core.Model.Player;

import javax.swing.*;

public class View {
    private JFrame frame;
    private Panel panel;

    public View(Player player) {
        frame = new JFrame("Ninja Nexus");
        panel = new Panel(player);

        frame.add(panel);
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        panel.requestFocusInWindow();
    }

    public Panel getPanel() {
        return panel;
    }
}
