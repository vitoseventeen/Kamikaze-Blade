package Core.Controller;


import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.*;

/**
 * Handles user input from the keyboard.
 */
public class InputHandler implements KeyListener {
    private final Controller controller;
    private boolean leftPressed = false;
    private boolean rightPressed = false;
    private boolean upPressed = false;
    private boolean downPressed = false;

    /**
     * Constructs an InputHandler object.
     * @param controller The controller to handle input for.
     */
    public InputHandler(Controller controller) {
        this.controller = controller;

        // Set up a timer to continuously update player movement based on key presses
        Timer inputTimer = new Timer(8, e -> updatePlayerMovement());
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
                break;
            case KeyEvent.VK_ENTER:
                controller.attack();
                break;
            case KeyEvent.VK_C:
                controller.craftHeal();
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
                    SwingUtilities.invokeLater(controller::togglePause);
                }
                break;
            case KeyEvent.VK_E:
                SwingUtilities.invokeLater(controller::showInventory);
                break;
            default:
                break;
        }
    }

    /**
     * Updates the player's movement based on the current key presses.
     */
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
