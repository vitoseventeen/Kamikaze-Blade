package Core.View;

import Core.Model.Player;

import javax.swing.*;

public class View {
    private JFrame frame;
    private Panel panel;

    public View(Player player) {
        frame = new JFrame("Ninja Nexus");
        panel = new Panel(player);
        ImageIcon icon = new ImageIcon("assets/icon.png");
        frame.setIconImage(icon.getImage());

        frame.add(panel);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false); // Změna velikosti obrazovky pro správné zobrazení animace je zakázána
        frame.pack();
        frame.setVisible(true);
        panel.requestFocusInWindow();


    }


    public Panel getPanel() {
        return panel;
    }
}
