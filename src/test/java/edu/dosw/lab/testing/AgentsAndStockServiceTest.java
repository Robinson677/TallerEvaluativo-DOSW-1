package edu.dosw.lab.testing;

import edu.dosw.lab.agilismo.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AgentsAndStockServiceTest {

    private final PrintStream originalOut = System.out;
    private ByteArrayOutputStream outContent;

    @BeforeEach
    void setUp() {
        outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
    }

    @Test
    void shouldLogAgentPrintMessage() {
        LogAgent log = new LogAgent();
        Product p = new Product("xbox one s", "Consola", 299.0, 10);
        log.onStockChanged(p);
        String output = outContent.toString().trim();
        assertEquals("Prodcto: xbox one s ->10 unidades disponibles", output);
    }

    @Test
    void shouldNotWarnWhenAboveThreshold() {
        WarningAgent warn = new WarningAgent();
        Product p = new Product("xbox one s", "Consola", 299.0, 10);
        warn.onStockChanged(p);
        String output = outContent.toString().trim();
        assertTrue(output.isEmpty());
    }

    @Test
    void shouldWarnWhenBelowThreshold() {
        WarningAgent warn = new WarningAgent();
        Product p = new Product("xbox one s", "Consola", 299.0, 3);
        warn.onStockChanged(p);
        String output = outContent.toString().trim();
        assertTrue(output.contains("ALERTA!!! El stock del Prodcto: xbox one s es muy bajo"));
        assertTrue(output.contains("3 unidades"));
    }


    @Test
    void shouldNotNotifyWhenUpdateFails() {
        ManageInventory inv = new ManageInventory();
        LogAgent log = spy(new LogAgent());
        WarningAgent warn = spy(new WarningAgent());
        StockService service = new StockService(inv, List.of(log, warn));
        boolean result = service.updateStock("nonexistent", 5);
        assertFalse(result);
        verify(log, never()).onStockChanged(any());
        verify(warn, never()).onStockChanged(any());
    }
    @Test
    void shouldFailWhenNameIsNullOrBlank() {
        ManageInventory inv = new ManageInventory();
        StockService service = new StockService(inv, List.of());

        assertThrows(IllegalArgumentException.class,
                () -> service.addProduct(null, "Other", 10.0, 5));

        assertThrows(IllegalArgumentException.class,
                () -> service.addProduct("   ", "Other", 10.0, 5));
    }

    @Test
    void shouldFailWhenPriceIsNotPositive() {
        ManageInventory inv = new ManageInventory();
        StockService service = new StockService(inv, List.of());

        assertThrows(IllegalArgumentException.class,
                () -> service.addProduct("ps5", "Other", 0.0, 5));

        assertThrows(IllegalArgumentException.class,
                () -> service.addProduct("ps5", "Other", -15.0, 5));
    }

    @Test
    void shouldFailWhenQuantityIsNegative() {
        ManageInventory inv = new ManageInventory();
        StockService service = new StockService(inv, List.of());

        assertThrows(IllegalArgumentException.class,
                () -> service.addProduct("switch", "Other", 300.0, -1));
    }


}
