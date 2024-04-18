package Core.Controller;

import Core.Model.Player;
import Core.View.Panel;
import Core.View.View;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class InputController implements KeyListener {
    private Player player;
    private Panel panel;

    public InputController(Player player, View view) {
        this.player = player;
        this.panel = view.getPanel();
        this.panel.addKeyListener(this);
        this.panel.setFocusable(true);
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        switch (keyCode) {
            case KeyEvent.VK_LEFT:
            case KeyEvent.VK_A:
                player.moveLeft();
                // System.out.println("left");
                break;
            case KeyEvent.VK_RIGHT:
            case KeyEvent.VK_D:
                player.moveRight();
                // System.out.println("right");
                break;
            case KeyEvent.VK_UP:
            case KeyEvent.VK_W:
                player.moveUp();
                // System.out.println("up");
                break;
            case KeyEvent.VK_DOWN:
            case KeyEvent.VK_S:
                player.moveDown();
                // System.out.println("down");
                break;
            default:
                break;
        }

        panel.repaint();
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }


}
