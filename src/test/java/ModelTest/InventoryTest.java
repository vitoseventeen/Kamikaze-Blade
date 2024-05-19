package ModelTest;

import Model.GameObject;
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

    private Inventory inventory;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        inventory = new Inventory(5);
        inventory.setCoinBalance(10);
        inventory.addItem(mockItem);
    }

    @Test
    void testSaveAndLoadInventory() throws IOException, ClassNotFoundException {
        // Arrange
        String fileName = "testInventory.dat";
        ObjectOutputStream mockOutputStream = mock(ObjectOutputStream.class);
        ObjectInputStream mockInputStream = mock(ObjectInputStream.class);

        // Stubbing ObjectOutputStream
        doNothing().when(mockOutputStream).writeObject(any());
        // Stubbing ObjectInputStream
        when(mockInputStream.readObject()).thenReturn(inventory);

        // Act
        inventory.saveInventory(fileName);
        Inventory loadedInventory = Inventory.loadInventory(fileName);

        // Assert
        assertNotNull(loadedInventory);
        assertEquals(loadedInventory.getCoinBalance(), inventory.getCoinBalance());
        assertEquals(loadedInventory.getItems().size(), inventory.getItems().size());
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
    void testSetAndGetItems() {
        // Arrange
        List<GameObject> newItems = new ArrayList<>();
        newItems.add(mock(GameObject.class));
        newItems.add(mock(GameObject.class));

        // Act
        inventory.setItems(newItems);

        // Assert
        assertEquals(newItems, inventory.getItems());
    }

    @Test
    void testSetAndGetCoinBalance() {
        // Act
        inventory.setCoinBalance(20);

        // Assert
        assertEquals(20, inventory.getCoinBalance());
    }

}
