package Core.View;

import Core.Controller.GameManager;
import Core.Util.Constants;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * The WinMenu class represents the panel displayed upon the player's death.
 * It allows the player to exit the game.
 */
public class WinMenu extends JPanel implements ActionListener {

    private final JButton exitButton;

    /**
     * Constructs a WinMenu with the specified GameManager.
     *
     * @param gameManager The GameManager controlling the game.
     */
    public WinMenu(GameManager gameManager) {
        setLayout(null);
        setBounds(0, 0, Constants.GAME_WIDTH, Constants.GAME_HEIGHT);
        exitButton = new JButton();
        exitButton.setIcon(new ImageIcon("assets/exitDefault2.png"));
        addButton(exitButton, 453, 580, 430, 120);

        // Add mouse listener to exitButton
        exitButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                // Change button icon and exit the game
                new Thread(() -> {
                    exitButton.setIcon(new ImageIcon("assets/exitHover2.png"));
                    try {
                        Thread.sleep(3);
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                    System.exit(0);
                }).start();
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                // Change button icon when mouse enters
                exitButton.setIcon(new ImageIcon("assets/exitHover2.png"));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                // Change button icon when mouse exits
                exitButton.setIcon(new ImageIcon("assets/exitDefault2.png"));
            }
        });
        exitButton.addActionListener(this); // Add action listener to exitButton
    }

    /**
     * Paints the background image of the WinMenu.
     *
     * @param g The Graphics context.
     */
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Image background = new ImageIcon("assets/WinMenu.png").getImage();
        g.drawImage(background, 0, 0, Constants.GAME_WIDTH, Constants.GAME_HEIGHT, null);
    }

    /**
     * Adds a button to the WinMenu panel.
     *
     * @param button The button to add.
     * @param x      The x-coordinate of the button.
     * @param y      The y-coordinate of the button.
     * @param width  The width of the button.
     * @param height The height of the button.
     */
    public void addButton(JButton button, int x, int y, int width, int height) {
        int newX = (Constants.GAME_WIDTH - width) / 2;

        // Set button properties
        button.setOpaque(false);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setBorder(null);
        button.setFocusPainted(false);
        button.setMargin(new Insets(0, 0, 0, 0));
        button.setBounds(newX, y, width, height);
        add(button);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // Action performed when button is clicked (currently empty)
    }
}
