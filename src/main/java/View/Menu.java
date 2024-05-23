package View;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import Controller.GameManager;
import Util.Constants;

public class Menu  extends JPanel implements ActionListener {
    private final JButton startButton;
    private final JButton exitButton;
    private final JButton loadButton;

    /**
     * Constructor for the Menu class
     * @param gameManager GameManager object
     */

    public Menu(GameManager gameManager) {

        JFrame jFrame = new JFrame("Kamikaze Blade");
        jFrame.setIconImage(new ImageIcon("assets/icon.png").getImage());
        jFrame.getContentPane().add(this);

        jFrame.setSize(Constants.GAME_WIDTH,Constants.GAME_HEIGHT);
        jFrame.setLocationRelativeTo(null);
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jFrame.setResizable(false);
        setBounds(0, 0, Constants.GAME_WIDTH,Constants.GAME_HEIGHT);
        setLayout(null);

        requestFocus();

        startButton = new JButton();
        loadButton = new JButton();
        exitButton = new JButton();

        startButton.setIcon(new ImageIcon("assets/startDefault.png"));
        addButton(startButton, 453,490,430,120);
        loadButton.setIcon(new ImageIcon("assets/loadDefault.png"));
        addButton(loadButton, 453, 590, 430, 120);
        exitButton.setIcon(new ImageIcon("assets/exitDefault.png"));
        addButton(exitButton, 453, 690, 430, 120);
        jFrame.setVisible(true);
        startButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                startButton.setIcon(new ImageIcon("assets/startPressed.png"));
                Timer timer = new Timer(1, e1 -> {
                    try {
                        gameManager.start();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }

                    jFrame.dispose();
                });
                timer.setRepeats(false);
                timer.start();
            }


            @Override
            public void mouseEntered(MouseEvent e) {
                startButton.setIcon(new ImageIcon("assets/startHover.png"));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                startButton.setIcon(new ImageIcon("assets/startDefault.png"));
            }
        });

        loadButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                loadButton.setIcon(new ImageIcon("assets/loadPressed.png"));
                Timer timer = new Timer(1, e1 -> {
                    try {
                        gameManager.loadInventory();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }

                    jFrame.dispose();
                });
                timer.setRepeats(false);
                timer.start();
            }


            @Override
            public void mouseEntered(MouseEvent e) {
                loadButton.setIcon(new ImageIcon("assets/loadHover.png"));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                loadButton.setIcon(new ImageIcon("assets/loadDefault.png"));
            }
        });

        exitButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                new Thread(() -> {
                    exitButton.setIcon(new ImageIcon("assets/exitPressed.png"));
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
                exitButton.setIcon(new ImageIcon("assets/exitHover.png"));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                exitButton.setIcon(new ImageIcon("assets/exitDefault.png"));
            }
        });
        loadButton.addActionListener(this);
        startButton.addActionListener(this);
        exitButton.addActionListener(this);

    }

    /**
     * Paints the background image
     * @param g Graphics object
     */

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Image background = new ImageIcon("assets/background.png").getImage();
        g.drawImage(background, 0, 0, Constants.GAME_WIDTH, Constants.GAME_HEIGHT, null);

    }

    /**
     * Adds a button to the menu
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
