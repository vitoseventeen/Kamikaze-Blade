package Core.View;

import Core.Controller.GameManager;
import Core.Util.Constants;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class DeathMenu extends JPanel implements ActionListener {

    private GameManager gameManager;
    private final JButton menuButton;
    private final JButton exitButton;
    private Menu menu;


    public DeathMenu(GameManager gameManager) {
        this.gameManager = gameManager;
        setLayout(null);
        setBounds(0, 0, Constants.GAME_WIDTH, Constants.GAME_HEIGHT);
        menuButton = new JButton();
        exitButton = new JButton();
        menuButton.setIcon(new ImageIcon("assets/menuDefault.png"));
        addButton(menuButton, 453, 440, 430, 120);
        exitButton.setIcon(new ImageIcon("assets/exitDefault2.png"));
        addButton(exitButton, 453, 580, 430, 120);

        menuButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                menuButton.setIcon(new ImageIcon("assets/menuPressed.png"));
                Timer timer = new Timer(2, e1 -> {
                    new Menu();
                    //TODO: CLOSE THIS WINDOW

                });

                timer.setRepeats(false);
                timer.start();
            }});

        menuButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                menuButton.setIcon(new ImageIcon("assets/menuHover.png"));
            }
            @Override
            public void mouseExited(MouseEvent e) {
                menuButton.setIcon(new ImageIcon("assets/menuDefault.png"));
            }
        });
        exitButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                new Thread(() -> {
                    exitButton.setIcon(new ImageIcon("assets/exitPressed2.png"));
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
                exitButton.setIcon(new ImageIcon("assets/exitHover2.png"));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                exitButton.setIcon(new ImageIcon("assets/exitDefault2.png"));
            }
        });
        menuButton.addActionListener(this);
        exitButton.addActionListener(this);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Image background = new ImageIcon("assets/deathMenu.png").getImage();
        g.drawImage(background, 0, 0, Constants.GAME_WIDTH, Constants.GAME_HEIGHT, null);
    }
    public void addButton(JButton button, int x, int y, int width, int height) {
        // Calculate new x coordinate to center horizontally
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

    @Override
    public void actionPerformed(ActionEvent e) {

    }
}




