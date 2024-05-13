package Core.View;

import Core.Controller.GameManager;
import Core.Model.Inventory;
import Core.Model.Player;
import Core.Util.Constants;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class InventoryMenu extends JPanel implements ActionListener {
    private Inventory inventory;
    private Player player;
    private GameManager gameManager;
    public InventoryMenu(GameManager gameManager, Player player) {
        this.gameManager = gameManager;
        this.player = player;
        setLayout(null);



        this.inventory = player.getInventory();

    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Image inventoryMenuImage = new ImageIcon("assets/inventoryMenuFull.png").getImage();
        g.drawImage(inventoryMenuImage, 0, 0, Constants.GAME_WIDTH, Constants.GAME_HEIGHT, this);
    }


    @Override
    public void actionPerformed(ActionEvent e) {

    }
}
