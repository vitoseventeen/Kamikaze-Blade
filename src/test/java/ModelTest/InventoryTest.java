package ModelTest;

import Model.GameObject;
import Model.GameObjectType;
import Model.Inventory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class InventoryTest {

    @Mock
    private Logger mockLogger;

    @Mock
    private GameObject mockItem;

    @Mock
    private GameObject mockKeyItem;

    private Inventory inventory;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        inventory = new Inventory(5);
        inventory.setCoinBalance(10);
        when(mockKeyItem.getType()).thenReturn(GameObjectType.KEY);
        inventory.addItem(mockItem);
    }

    @Test
    void testSaveAndLoadInventory() throws IOException, ClassNotFoundException {
        // Arrange
        String fileName = "testInventory.dat";

        // Act
        inventory.saveInventory(fileName);
        Inventory loadedInventory = Inventory.loadInventory(fileName);

        // Assert
        assertNotNull(loadedInventory);
        assertEquals(loadedInventory.getCoinBalance(), inventory.getCoinBalance());
        assertEquals(loadedInventory.getItems().size(), inventory.getItems().size());

        // Clean up
        new File(fileName).delete();
    }

    @Test
    void testAddItemToFullInventory() {
        // Arrange
        Inventory fullInventory = new Inventory(1);
        GameObject anotherMockItem = mock(GameObject.class);
        fullInventory.addItem(mockItem);

        // Act
        fullInventory.addItem(anotherMockItem);

        // Assert
        assertEquals(1, fullInventory.getItems().size());
        assertTrue(fullInventory.getItems().contains(mockItem));
        assertFalse(fullInventory.getItems().contains(anotherMockItem));
    }

    @Test
    void testRemoveItems() {
        // Arrange
        GameObject anotherMockItem = mock(GameObject.class);
        inventory.addItem(anotherMockItem);

        // Act
        inventory.removeItems(mockItem, 1);

        // Assert
        assertFalse(inventory.getItems().contains(mockItem));
        assertTrue(inventory.getItems().contains(anotherMockItem));
    }


    @Test
    void testIsQuestFinished() {
        // Act
        boolean isFinished = inventory.isQuestFinished();
        // Assert
        assertTrue(isFinished);
    }

    @Test
    void testAddAndRemoveCoins() {
        // Act
        inventory.addCoinToBalance(10);
        inventory.removeCoinFromBalance(5);

        // Assert
        assertEquals(15, inventory.getCoinBalance());
    }

    @Test
    void testIsFull() {
        // Arrange
        Inventory smallInventory = new Inventory(1);
        smallInventory.addItem(mockItem);

        // Act
        boolean isFull = smallInventory.isFull();

        // Assert
        assertTrue(isFull);
    }



    @Test
    void testIntegration_AddMultipleItemsAndCheckSize() {
        // Arrange
        Inventory newInventory = new Inventory(3);
        GameObject item1 = mock(GameObject.class);
        GameObject item2 = mock(GameObject.class);

        // Act
        newInventory.addItem(item1);
        newInventory.addItem(item2);

        // Assert
        assertEquals(2, newInventory.getInventorySize());
        assertTrue(newInventory.getItems().contains(item1));
        assertTrue(newInventory.getItems().contains(item2));
    }

    @Test
    void testProcess_FullInventory() {
        // Arrange
        Inventory processInventory = new Inventory(2);
        GameObject item1 = mock(GameObject.class);
        GameObject item2 = mock(GameObject.class);
        GameObject item3 = mock(GameObject.class);

        // Act
        processInventory.addItem(item1);
        processInventory.addItem(item2);
        processInventory.addItem(item3); // Should not be added

        // Assert
        assertEquals(2, processInventory.getInventorySize());
        assertTrue(processInventory.getItems().contains(item1));
        assertTrue(processInventory.getItems().contains(item2));
        assertFalse(processInventory.getItems().contains(item3));
    }
}
