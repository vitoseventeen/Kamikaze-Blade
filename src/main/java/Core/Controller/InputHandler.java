package Core.Controller;

import Core.Model.Enemy;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.sql.SQLOutput;

import javax.swing.*;

public class InputHandler implements KeyListener {
    private Controller controller;
    private boolean leftPressed = false;
    private boolean rightPressed = false;
    private boolean upPressed = false;
    private boolean downPressed = false;

    private Timer inputTimer;

    public InputHandler(Controller controller) {
        this.controller = controller;

        inputTimer = new Timer(8, e -> updatePlayerMovement());
        inputTimer.start();
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
                leftPressed = true;
                break;
            case KeyEvent.VK_RIGHT:
            case KeyEvent.VK_D:
                rightPressed = true;
                break;
            case KeyEvent.VK_UP:
            case KeyEvent.VK_W:
                upPressed = true;
                break;
            case KeyEvent.VK_DOWN:
            case KeyEvent.VK_S:
                downPressed = true;
                break;
            case KeyEvent.VK_F:
                    controller.interact();
                System.out.println("Interact");
                break;
            case KeyEvent.VK_ENTER:
                controller.attack();
                break;
            default:
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int keyCode = e.getKeyCode();
        switch (keyCode) {
            case KeyEvent.VK_LEFT:
            case KeyEvent.VK_A:
                leftPressed = false;
                break;
            case KeyEvent.VK_RIGHT:
            case KeyEvent.VK_D:
                rightPressed = false;
                break;
            case KeyEvent.VK_UP:
            case KeyEvent.VK_W:
                upPressed = false;
                break;
            case KeyEvent.VK_DOWN:
            case KeyEvent.VK_S:
                downPressed = false;
                break;
            case KeyEvent.VK_ESCAPE:
                if(controller.showingInventory()) {
                    controller.hideInventory();
                } else {
                    SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            controller.togglePause();
                        }
                    });
                }
                break;
            case KeyEvent.VK_E:
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        controller.showInventory();
                    }
                });
                break;
            default:
                break;
        }
    }

    private void updatePlayerMovement() {

        int deltaX = 0;
        int deltaY = 0;

        if (leftPressed) {
            deltaX -= controller.getPlayer().getSpeed();
        }
        if (rightPressed) {
            deltaX += controller.getPlayer().getSpeed();
        }
        if (upPressed) {
            deltaY -= controller.getPlayer().getSpeed();
        }
        if (downPressed) {
            deltaY += controller.getPlayer().getSpeed();
        }

        controller.updatePlayerMovement(deltaX, deltaY);
    }
}
