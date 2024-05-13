package Core.View;

import Core.Controller.GameManager;
import Core.Model.Inventory;
import Core.Model.Player;
import Core.Util.Constants;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import static Core.Util.Constants.*;

public class InventoryMenu extends JPanel implements ActionListener {
    private Inventory inventory;
    private Player player;
    private GameManager gameManager;

    private Image defaultCell;
    private Image selectedCell;
    private int selectedSlotX = -1;
    private int selectedSlotY = -1;


    public InventoryMenu(GameManager gameManager, Player player) {
        this.gameManager = gameManager;
        this.player = player;
        setLayout(null);
        this.inventory = player.getInventory();

        defaultCell = new ImageIcon("assets/defaultCell.png").getImage();
        selectedCell = new ImageIcon("assets/activeCell.png").getImage();


        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                selectedSlotX = (e.getX() - Constants.INVENTORY_X) / Constants.INVENTORY_CELL_WIDTH;
                selectedSlotY = (e.getY() - Constants.INVENTORY_Y) / Constants.INVENTORY_CELL_HEIGHT;

                if (selectedSlotX >= 0 && selectedSlotX < Constants.INVENTORY_COLUMNS &&
                        selectedSlotY >= 0 && selectedSlotY < Constants.INVENTORY_ROWS) {
                    repaint();
                } else {
                    selectedSlotX = -1;
                    selectedSlotY = -1;
                }
            }
        });
    }



    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Image inventoryMenuImage = new ImageIcon("assets/inventoryMenu.png").getImage();
        g.drawImage(inventoryMenuImage, 0, 0, Constants.GAME_WIDTH, Constants.GAME_HEIGHT, this);

        for (int y = 0; y < Constants.INVENTORY_ROWS; y++) {

            for (int x = 0; x < Constants.INVENTORY_COLUMNS; x++) {
                int cellX = Constants.INVENTORY_X + x * Constants.INVENTORY_CELL_WIDTH;
                int cellY = Constants.INVENTORY_Y + y * Constants.INVENTORY_CELL_HEIGHT;
                if (x == selectedSlotX && y == selectedSlotY) {
                    g.drawImage(selectedCell, cellX, cellY, this);
                } else {
                    g.drawImage(defaultCell, cellX, cellY, this);
                }



            }

        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }
}
