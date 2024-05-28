package ControllerTest;

import Controller.Controller;
import Controller.InputHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.awt.event.KeyEvent;

import static org.mockito.Mockito.*;

public class InputHandlerTest {

    private InputHandler inputHandler;
    private Controller mockController;

    @BeforeEach
    void setUp() {
        mockController = Mockito.mock(Controller.class);
        inputHandler = new InputHandler(mockController);
    }

    @Test
    void testKeyPressedF() {
        KeyEvent mockKeyEvent = mock(KeyEvent.class);
        when(mockKeyEvent.getKeyCode()).thenReturn(KeyEvent.VK_F);

        inputHandler.keyPressed(mockKeyEvent);

        verify(mockController, times(1)).interact();
        verify(mockController, never()).attack();
        verify(mockController, never()).craftHeal();
        verify(mockController, never()).updatePlayerMovement(anyInt(), anyInt());
    }

    @Test
    void testKeyPressedC() {
        KeyEvent mockKeyEvent = mock(KeyEvent.class);
        when(mockKeyEvent.getKeyCode()).thenReturn(KeyEvent.VK_C);

        inputHandler.keyPressed(mockKeyEvent);

        verify(mockController, never()).interact();
        verify(mockController, never()).attack();
        verify(mockController, times(1)).craftHeal();
    }

    @Test
    void testKeyPressedEnter() {
        KeyEvent mockKeyEvent = mock(KeyEvent.class);
        when(mockKeyEvent.getKeyCode()).thenReturn(KeyEvent.VK_ENTER);

        inputHandler.keyPressed(mockKeyEvent);

        verify(mockController, never()).interact();
        verify(mockController, times(1)).attack();
        verify(mockController, never()).craftHeal();
    }



    @Test
    void testEscape_HideInventory() {
        KeyEvent mockKeyEvent = mock(KeyEvent.class);
        when(mockKeyEvent.getKeyCode()).thenReturn(KeyEvent.VK_ESCAPE);

        when(mockController.showingInventory()).thenReturn(true);

        inputHandler.keyReleased(mockKeyEvent);

        verify(mockController, times(1)).hideInventory();
        verify(mockController, never()).togglePause();
    }


}
