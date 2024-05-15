package Core.View;

import Core.Controller.GameManager;
import Core.Model.GameObject;
import Core.Model.Inventory;
import Core.Model.Player;
import Core.Util.Constants;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.HashMap;
import java.util.Map;

import static Core.Util.Constants.*;

public class InventoryMenu extends JPanel {
    private Inventory inventory;
    private Player player;
    private GameManager gameManager;

    private Image defaultCell;
    private Image selectedCell;
    private int selectedSlotX = -1;
    private int selectedSlotY = -1;
    private Map<String, Image> itemImages; // Map to store item images

    public InventoryMenu(GameManager gameManager, Player player) {
        this.gameManager = gameManager;
        this.player = player;
        setLayout(null);
        this.inventory = player.getInventory();

        defaultCell = new ImageIcon("assets/defaultCell.png").getImage();
        selectedCell = new ImageIcon("assets/activeCell.png").getImage();

        selectedSlotX = 0;
        selectedSlotY = 0;

        itemImages = new HashMap<>();
        loadItemImages(); // Load item images into the map

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
        this.addMouseWheelListener(new MouseWheelListener() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                int notches = e.getWheelRotation();
                if (notches < 0) {
                    // Mouse wheel moved UP
                    selectedSlotX = (selectedSlotX + 1) % Constants.INVENTORY_COLUMNS;
                    if (selectedSlotX == 0) { // If we've reached the end of a row
                        selectedSlotY = (selectedSlotY + 1) % Constants.INVENTORY_ROWS; // Move to the next row
                    }
                } else {
                    // Mouse wheel moved DOWN
                    selectedSlotX = (selectedSlotX - 1 + Constants.INVENTORY_COLUMNS) % Constants.INVENTORY_COLUMNS;
                    if (selectedSlotX == Constants.INVENTORY_COLUMNS - 1) { // If we've reached the start of a row
                        selectedSlotY = (selectedSlotY - 1 + Constants.INVENTORY_ROWS) % Constants.INVENTORY_ROWS; // Move to the previous row
                    }
                }
                repaint();
            }
        });

        InputMap inputMap = this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap actionMap = this.getActionMap();

        inputMap.put(KeyStroke.getKeyStroke("UP"), "up");
        actionMap.put("up", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectedSlotY = (selectedSlotY - 1 + Constants.INVENTORY_ROWS) % Constants.INVENTORY_ROWS;
                repaint();
            }
        });

        inputMap.put(KeyStroke.getKeyStroke("DOWN"), "down");
        actionMap.put("down", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectedSlotY = (selectedSlotY + 1) % Constants.INVENTORY_ROWS;
                repaint();
            }
        });

        inputMap.put(KeyStroke.getKeyStroke("LEFT"), "left");
        actionMap.put("left", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectedSlotX = (selectedSlotX - 1 + Constants.INVENTORY_COLUMNS) % Constants.INVENTORY_COLUMNS;
                repaint();
            }
        });

        inputMap.put(KeyStroke.getKeyStroke("RIGHT"), "right");
        actionMap.put("right", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectedSlotX = (selectedSlotX + 1) % Constants.INVENTORY_COLUMNS;
                repaint();
            }
        });

    }

    private void loadItemImages() {
        itemImages.put("KEY", new ImageIcon("assets/key.png").getImage());
        itemImages.put("POTION", new ImageIcon("assets/potion.png").getImage());
        itemImages.put("HEAL", new ImageIcon("assets/heal.png").getImage());
        itemImages.put("COIN", new ImageIcon("assets/coin.png").getImage());
        itemImages.put("QUEST_KEY", new ImageIcon("assets/QuestKey.png").getImage());
        // Add other items similarly
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
                drawItemInCell(g, cellX, cellY, x, y);
            }
        }
    }

    private void drawItemInCell(Graphics g, int cellX, int cellY, int cellIndexX, int cellIndexY) {
        int index = cellIndexY * Constants.INVENTORY_COLUMNS + cellIndexX;
        if (index < inventory.getItems().size()) {
            GameObject item = inventory.getItems().get(index);
            Image itemImage = itemImages.get(item.getType().toString());
            if (itemImage != null) {
                // Calculate the aspect ratio
                double aspectRatio = (double) itemImage.getWidth(this) / itemImage.getHeight(this);

                // Calculate the new dimensions to fit within the cell while maintaining aspect ratio
                int itemWidth = Math.min(Constants.INVENTORY_CELL_WIDTH - 10, (int) (Constants.INVENTORY_CELL_HEIGHT * aspectRatio) - 10);
                int itemHeight = Math.min(Constants.INVENTORY_CELL_HEIGHT - 10, (int) (Constants.INVENTORY_CELL_WIDTH / aspectRatio) - 10);

                // Center the item within the cell
                int itemX = cellX + (Constants.INVENTORY_CELL_WIDTH - itemWidth) / 2 - 11;
                int itemY = cellY + (Constants.INVENTORY_CELL_HEIGHT - itemHeight) / 2 - 30;

                g.drawImage(itemImage, itemX, itemY, itemWidth, itemHeight, this);
            }
        }
    }
}
