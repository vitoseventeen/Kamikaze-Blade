package Core.Controller;

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

        if (deltaX != 0 || deltaY != 0) {
            controller.movePlayer(deltaX, deltaY);
        }
    }
}