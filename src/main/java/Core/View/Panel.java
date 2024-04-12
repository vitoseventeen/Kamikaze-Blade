package Core.View;

import javax.swing.*;
import Core.Model.Player;

import java.awt.*;

public class Panel extends JPanel {
    private Player player;

    public Panel(Player player) {
        this.player = player;
        setPreferredSize(new Dimension(800, 600));
    }
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.DARK_GRAY);
        g.fillRect(player.getX(), player.getY(), 50, 50);
    }
}
