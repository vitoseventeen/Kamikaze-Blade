package VIewTest;

import static org.mockito.Mockito.*;

import Controller.GameManager;
import Model.GameObject;
import Model.GameObjectType;
import Model.Inventory;
import Model.Player;
import View.InventoryMenu;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.List;

public class InventoryMenuTest {

    @Mock
    GameManager mockGameManager;

    @Mock
    Player mockPlayer;

    @Mock
    Inventory mockInventory;

    @BeforeEach
    void setUp() {
        mockPlayer = mock(Player.class);
        mockInventory = mock(Inventory.class);
        when(mockPlayer.getInventory()).thenReturn(mockInventory);
    }

    @Test
    void testUsePotion() {
        InventoryMenu inventoryMenu = new InventoryMenu(mockGameManager, mockPlayer);
        when(mockPlayer.getHealth()).thenReturn(10);

        GameObject mockPotion = mock(GameObject.class);
        when(mockPotion.getType()).thenReturn(GameObjectType.POTION);

        List<GameObject> itemsList = new ArrayList<>();
        itemsList.add(mockPotion);
        when(mockInventory.getItems()).thenReturn(itemsList);

        inventoryMenu.useSelectedItem();

        verify(mockPlayer).setHealth(12);
        verify(mockInventory).removeItem(mockPotion);
    }

    @Test
    void testUseHeal() {
        InventoryMenu inventoryMenu = new InventoryMenu(mockGameManager, mockPlayer);
        when(mockPlayer.getHealth()).thenReturn(10);

        GameObject mockHeal = mock(GameObject.class);
        when(mockHeal.getType()).thenReturn(GameObjectType.HEAL);

        List<GameObject> itemsList = new ArrayList<>();
        itemsList.add(mockHeal);
        when(mockInventory.getItems()).thenReturn(itemsList);

        inventoryMenu.useSelectedItem();

        verify(mockPlayer).setHealth(15);
        verify(mockInventory).removeItem(mockHeal);
    }

    @Test
    void testUseNoItem() {
        InventoryMenu inventoryMenu = new InventoryMenu(mockGameManager, mockPlayer);
        when(mockPlayer.getHealth()).thenReturn(10);

        List<GameObject> itemsList = new ArrayList<>();
        when(mockInventory.getItems()).thenReturn(itemsList);

        inventoryMenu.useSelectedItem();

        verify(mockPlayer, never()).setHealth(anyInt());
        verify(mockInventory, never()).removeItem(any(GameObject.class));
    }


}
