package edu.dosw.lab.testing;

import edu.dosw.lab.agilismo.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class StockMonitoringSystemTest {

    private final PrintStream originalOut = System.out;
    private ByteArrayOutputStream outContent;

    @BeforeEach
    void setUpStreams() {
        outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent, true, StandardCharsets.UTF_8));
    }

    @AfterEach
    void restoreStreams() {
        System.setOut(originalOut);
    }

    @Test
    void shouldAddProductAndNotifyOnce() {
        String input =
                "añadir\n" +
                        "xbox one s\n" +
                        "100.0\n" +
                        "5\n" +
                        "10\n" +
                        "finalizar\n";
        Scanner sc = new Scanner(input);

        ManageInventory inventory = new ManageInventory();
        StockObserver obs = mock(StockObserver.class);
        StockService service = new StockService(inventory, List.of(obs));

        ExecutableStockMonitoringSystem exe = new ExecutableStockMonitoringSystem(service, inventory);
        exe.runInteractive(sc);

        var prod = inventory.findByName("xbox one s");
        assertNotNull(prod);
        assertEquals(10, prod.getQuantity());

        ArgumentCaptor<Product> cap = ArgumentCaptor.forClass(Product.class);
        verify(obs, times(1)).onStockChanged(cap.capture());
        Product notified = cap.getValue();
        assertEquals("xbox one s", notified.getName());
        assertEquals(10, notified.getQuantity());
    }

    @Test
    void shouldDecreaseStockAndNotify() {
        ManageInventory inventory = new ManageInventory();
        inventory.save(new Product("xbox one s", "Other", 100.0, 8));
        inventory.setCapacity("xbox one s", 8);

        String input =
                "modificar\n" +
                        "xbox one s\n" +
                        "a\n" +
                        "3\n" +
                        "finalizar\n";
        Scanner sc = new Scanner(input);

        StockObserver obs = mock(StockObserver.class);
        StockService service = new StockService(inventory, List.of(obs));
        ExecutableStockMonitoringSystem exe = new ExecutableStockMonitoringSystem(service, inventory);
        exe.runInteractive(sc);

        Product p = inventory.findByName("xbox one s");
        assertNotNull(p);
        assertEquals(5, p.getQuantity());

        ArgumentCaptor<Product> cap = ArgumentCaptor.forClass(Product.class);
        verify(obs, times(1)).onStockChanged(cap.capture());
        assertEquals(5, cap.getValue().getQuantity());
    }

    @Test
    void shouldNotExceedCapacityWhenReplenishing() {
        ManageInventory inv = new ManageInventory();
        inv.save(new Product("xbox one s", "Other", 50.0, 20));
        inv.setCapacity("xbox one s", 20);

        String input =
                "modificar\n" +
                        "xbox one s\n" +
                        "r\n" +
                        "10\n" +
                        "finalizar\n";
        Scanner sc = new Scanner(input);

        StockObserver obs = mock(StockObserver.class);
        StockService svc = new StockService(inv, List.of(obs));
        ExecutableStockMonitoringSystem exe = new ExecutableStockMonitoringSystem(svc, inv);
        exe.runInteractive(sc);
        Product p = inv.findByName("xbox one s");
        assertNotNull(p);
        assertEquals(20, p.getQuantity());
        verify(obs, times(0)).onStockChanged(any());
    }

    @Test
    void shouldNotPrintNotaMessage() {
        String input =
                "añadir\n" +
                        "xbox one s\n" +
                        "100.0\n" +
                        "5\n" +
                        "1\n" +
                        "finalizar\n";
        Scanner sc = new Scanner(input);

        ManageInventory inventory = new ManageInventory();
        StockObserver obs = mock(StockObserver.class);
        StockService service = new StockService(inventory, List.of(obs));
        ExecutableStockMonitoringSystem exe = new ExecutableStockMonitoringSystem(service, inventory);
        exe.runInteractive(sc);
        String output = outContent.toString();
        assertFalse(output.contains("Nota:"));
    }

    @Test
    void shouldListProductsInInventory() {
        ManageInventory inventory = new ManageInventory();
        inventory.save(new Product("xbox one s", "Other", 200.0, 5));
        inventory.setCapacity("xbox one s", 10);

        String input =
                "listar\n" +
                        "finalizar\n";
        Scanner sc = new Scanner(input);

        StockObserver obs = mock(StockObserver.class);
        StockService service = new StockService(inventory, List.of(obs));
        ExecutableStockMonitoringSystem exe = new ExecutableStockMonitoringSystem(service, inventory);
        exe.runInteractive(sc);

        String output = outContent.toString();
        assertTrue(output.contains("xbox one s"), "Debe listar el producto guardado");
        assertTrue(output.contains("quantity: 5"), "Debe mostrar la cantidad actual del producto");
    }

    @Test
    void shouldHandleInvalidCommand() {
        String input =
                "invalido\n" +
                        "finalizar\n";
        Scanner sc = new Scanner(input);

        ManageInventory inv = new ManageInventory();
        StockService svc = new StockService(inv, List.of());
        ExecutableStockMonitoringSystem exe = new ExecutableStockMonitoringSystem(svc, inv);
        exe.runInteractive(sc);

        String output = outContent.toString();
        assertTrue(output.contains("Comando invalido"));
    }

    @Test
    void shouldHandleInvalidNumberOnPrice() {
        String input =
                "añadir\n" +
                        "ps5\n" +
                        "abc\n" +
                        "5\n" +
                        "10\n" +
                        "finalizar\n";
        Scanner sc = new Scanner(input);

        ManageInventory inv = new ManageInventory();
        StockService svc = new StockService(inv, List.of());
        ExecutableStockMonitoringSystem exe = new ExecutableStockMonitoringSystem(svc, inv);
        exe.runInteractive(sc);

        String output = outContent.toString();
        assertTrue(output.contains("Entrada invalida en precio o cantidad."));
    }

    @Test
    void shouldNotAllowRemoveMoreThanStock() {
        ManageInventory inv = new ManageInventory();
        inv.save(new Product("xbox", "Other", 100.0, 5));
        inv.setCapacity("xbox", 5);

        String input =
                "modificar\n" +
                        "xbox\n" +
                        "a\n" +
                        "10\n" +
                        "finalizar\n";
        Scanner sc = new Scanner(input);

        StockService svc = new StockService(inv, List.of());
        ExecutableStockMonitoringSystem exe = new ExecutableStockMonitoringSystem(svc, inv);
        exe.runInteractive(sc);

        String output = outContent.toString();
        assertTrue(output.contains("Cantidad inválida. Stock disponible: 5"));
    }

    @Test
    void shouldHandleInvalidOptionInModify() {
        ManageInventory inv = new ManageInventory();
        inv.save(new Product("mouse", "Other", 10.0, 5));

        String input =
                "modificar\n" +
                        "mouse\n" +
                        "x\n" +
                        "finalizar\n";
        Scanner sc = new Scanner(input);

        StockService svc = new StockService(inv, List.of());
        ExecutableStockMonitoringSystem exe = new ExecutableStockMonitoringSystem(svc, inv);
        exe.runInteractive(sc);

        String output = outContent.toString();
        assertTrue(output.contains("Opción inválida. Use 'a' o 'r'."));
    }

    @Test
    void shouldHandleInvalidNumberOnQuantity() {
        ManageInventory inv = new ManageInventory();
        inv.save(new Product("tablet", "Other", 200.0, 5));

        String input =
                "modificar\n" +
                        "tablet\n" +
                        "r\n" +
                        "abc\n" +
                        "finalizar\n";
        Scanner sc = new Scanner(input);

        StockService svc = new StockService(inv, List.of());
        ExecutableStockMonitoringSystem exe = new ExecutableStockMonitoringSystem(svc, inv);
        exe.runInteractive(sc);

        String output = outContent.toString();
        assertTrue(output.contains("Cantidad inválida. Ingresa un número entero."));
    }


    @Test
    void shouldRejectInvalidCategoryWhenAdding() {
        String input =
                "añadir\n" +
                        "ps5\n" +
                        "350.0\n" +
                        "99\n" +
                        "finalizar\n";
        Scanner sc = new Scanner(input);

        ManageInventory inventory = new ManageInventory();
        StockService service = new StockService(inventory, java.util.List.of());
        ExecutableStockMonitoringSystem exe = new ExecutableStockMonitoringSystem(service, inventory);
        exe.runInteractive(sc);

        String output = outContent.toString();
        assertTrue(output.contains("Categoría invalida."), "Debe notificar categoría inválida");
        assertNull(inventory.findByName("ps5"), "No debe añadirse el producto cuando la categoría es inválida");
    }

    @Test
    void shouldPrintNoProductsWhenInventoryIsEmpty() {
        ManageInventory inventory = new ManageInventory();
        StockService service = new StockService(inventory, java.util.List.of());
        ExecutableStockMonitoringSystem exe = new ExecutableStockMonitoringSystem(service, inventory);
        String input = "listar\nfinalizar\n";
        Scanner sc = new Scanner(input);
        exe.runInteractive(sc);

        String output = outContent.toString();
        assertTrue(output.contains("No hay productos en el inventario"),
                "Si no hay productos debe mostrar: No hay productos en el inventario");
    }

}


