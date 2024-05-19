package View;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import Controller.GameManager;
import Util.Constants;

public class PauseMenu extends JPanel implements ActionListener{
    private final JButton resumeButton;
    private final JButton exitButton;
    private final JButton saveButton;

    /**
     * Constructor for the PauseMenu class
     * @param gameManager GameManager object
     */

    public PauseMenu(GameManager gameManager) {

        setLayout(null);

        resumeButton = new JButton();
        saveButton = new JButton();

        exitButton = new JButton();

        resumeButton.setIcon(new ImageIcon("assets/resumeDefault.png"));
        addButton(resumeButton, 453,410,400,80);
        saveButton.setIcon(new ImageIcon("assets/saveDefault.png"));
        addButton(saveButton, 453, 500, 400, 80);
        exitButton.setIcon(new ImageIcon("assets/exitMenuDefault.png"));
        addButton(exitButton, 453, 590, 400, 80);

        resumeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                resumeButton.setIcon(new ImageIcon("assets/resumePressed.png"));
                Timer timer = new Timer(2, e1 -> {
                    gameManager.togglePause();
                });
                timer.setRepeats(false);
                timer.start();
            }});

        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveButton.setIcon(new ImageIcon("assets/savePressed.png"));
                Timer timer = new Timer(2, e1 -> {
                    gameManager.saveInventory();
                });
                timer.setRepeats(false);
                timer.start();
            }});

        saveButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                saveButton.setIcon(new ImageIcon("assets/saveHover.png"));
            }
            @Override
            public void mouseExited(MouseEvent e) {
                saveButton.setIcon(new ImageIcon("assets/saveDefault.png"));
            }
        });

        resumeButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                resumeButton.setIcon(new ImageIcon("assets/resumeHover.png"));
            }
            @Override
            public void mouseExited(MouseEvent e) {
                resumeButton.setIcon(new ImageIcon("assets/resumeDefault.png"));
            }
        });

        exitButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                new Thread(() -> {
                    exitButton.setIcon(new ImageIcon("assets/exitMenuPressed.png"));
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
                exitButton.setIcon(new ImageIcon("assets/exitMenuHover.png"));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                exitButton.setIcon(new ImageIcon("assets/exitMenuDefault.png"));
            }
        });
        saveButton.addActionListener(this);
        resumeButton.addActionListener(this);
        exitButton.addActionListener(this);
    }

    /**
     * Paints the pause menu
     * @param g Graphics object
     */

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Image pauseMenuImage = new ImageIcon("assets/pauseMenu.png").getImage();
        g.drawImage(pauseMenuImage, 0, 0, Constants.GAME_WIDTH, Constants.GAME_HEIGHT, this);
    }

    /**
     * Adds a button to the pause menu
     * @param button JButton object
     * @param x x-coordinate
     * @param y y-coordinate
     * @param width width of the button
     * @param height height of the button
     */

    public void addButton(JButton button, int x, int y, int width, int height) {
        int newX = (Constants.GAME_WIDTH - width) / 2;

        button.setOpaque(false);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setBorder(null);
        button.setFocusPainted(false);
        button.setMargin(new Insets(0, 0, 0, 0));
        button.setBounds(newX, y, width, height);
        add(button);
    }

    /**
     * Action performed method
     * @param e ActionEvent object
     */

    @Override
    public void actionPerformed(ActionEvent e) {

    }

}
