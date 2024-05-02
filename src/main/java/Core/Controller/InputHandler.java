package Core.Controller;

import Core.Model.Enemy;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class InputHandler implements KeyListener {
    private Controller controller;
    private boolean leftPressed = false;
    private boolean rightPressed = false;
    private boolean upPressed = false;
    private boolean downPressed = false;

    public InputHandler(Controller controller) {
        this.controller = controller;
        Thread inputThread = new Thread(this::listenForKeyEvents);
        inputThread.setDaemon(true);
        inputThread.start();
    }

    private void listenForKeyEvents() {
        while (true) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            updatePlayerMovement();
        }
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

        // Check for collision with enemies
        int newX = controller.getPlayer().getX() + deltaX;
        int newY = controller.getPlayer().getY() + deltaY;
        for (Enemy enemy : controller.getEnemies()) {
            if (enemy.checkCollisionWithEnemy(newX, newY, controller.getPlayer().getWidth(), controller.getPlayer().getHeight())) {
                return; // If a collision would occur, don't move the player
            }
        }

        // If no collision, move the player
        controller.movePlayer(deltaX, deltaY);
    }

}