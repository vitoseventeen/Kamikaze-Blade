package Core.View;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import Core.Controller.GameManager;
import Core.Util.Constants;
import Core.Controller.GameManager;

public class Menu  extends JPanel implements ActionListener {
    private final JButton startButton;
    private final JButton exitButton;
    private GameManager gameManager;
    public Menu() {
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
        exitButton = new JButton();

        startButton.setIcon(new ImageIcon("assets/startDefault.png"));
        addButton(startButton, 453,440,430,120);
        exitButton.setIcon(new ImageIcon("assets/exitDefault.png"));
        addButton(exitButton, 453, 580, 430, 120);
        jFrame.setVisible(true);
        startButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                startButton.setIcon(new ImageIcon("assets/startPressed.png"));
//                gameManager.start();
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
        startButton.addActionListener(this);
        exitButton.addActionListener(this);

    }

    public void paintComponent(Graphics g) {
        // TODO
        super.paintComponent(g);

        Image background = new ImageIcon("assets/background.jpg").getImage();
        g.drawImage(background, 0, 0, Constants.GAME_WIDTH, Constants.GAME_HEIGHT, null);



    }
    public void addButton(JButton button, int x, int y, int width, int height) {

        button.setOpaque(false);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setBorder(null);
        button.setFocusPainted(false);
        button.setMargin(new Insets(0, 0, 0, 0));
        button.setBounds(x,y,width,height);
        add(button);

    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }
}
