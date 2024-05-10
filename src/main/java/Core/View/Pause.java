package Core.View;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import Core.Controller.GameManager;
import Core.Util.Constants;

public class Pause extends JPanel {
    private JButton resumeButton;
    private JButton exitButton;
    private GameManager gameManager;

    public Pause(GameManager gameManager) {
        this.gameManager = gameManager;

        setLayout(null);
        resumeButton = new JButton("Resume");
        exitButton = new JButton("Exit");

        int buttonWidth = 200;
        int buttonHeight = 50;
        int buttonX = (Constants.GAME_WIDTH - buttonWidth) / 2;
        int resumeButtonY = Constants.GAME_HEIGHT / 2 - buttonHeight;
        int exitButtonY = Constants.GAME_HEIGHT / 2;

        resumeButton.setBounds(buttonX, resumeButtonY, buttonWidth, buttonHeight);
        exitButton.setBounds(buttonX, exitButtonY, buttonWidth, buttonHeight);

        // Добавляем кнопки на панель
        add(resumeButton);
        add(exitButton);

        resumeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gameManager.togglePause();
            }
        });

        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Image pauseMenuImage = new ImageIcon("assets/pauseMenu.png").getImage();
        g.drawImage(pauseMenuImage, 0, 0, Constants.GAME_WIDTH, Constants.GAME_HEIGHT, this);
    }

}
