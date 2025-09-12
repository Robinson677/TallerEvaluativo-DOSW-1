package edu.dosw.lab.testing;

import edu.dosw.lab.agilismo.ManageInventory;
import edu.dosw.lab.agilismo.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

class InventoryValidationTest {

    private ManageInventory inventory;

    @BeforeEach
    void setUp() {
        inventory = new ManageInventory();
    }


    @Test
    void shouldNotSaveProductWithNullName() {
        Product p = new Product(null, "Disco Duro Externo 1 Tb", 370.0, 1);
        inventory.save(p);
        assertFalse(inventory.existsByName(null));
    }


    @Test
    void shouldNotSaveProductWithBlankName() {
        Product p = new Product("   ", "Sony Psvita", 1699.0, 1);
        inventory.save(p);
        assertFalse(inventory.existsByName("   "));
    }


    @Test
    void shouldTrimNameOnSave() {
        Product p = new Product("  nombreConEspacios  ", "Calculadora Kadio Super", 200.0, 3);
        inventory.save(p);
        assertNotNull(inventory.findByName("nombreConEspacios"));
    }


    @Test
    void shouldRejectDuplicateOnSaveAndKeepOriginal() {
        Product a = new Product("PS5", "Consola", 499.0, 5);
        Product b = new Product("PS5", "Consola", 499.0, 2);
        inventory.save(a);
        inventory.save(b);
        Product found = inventory.findByName("PS5");
        assertNotNull(found, "El producto PS5 debe existir");
        assertEquals(5, found.getQuantity());
    }

    @Test
    void shouldRejectNonPositivePriceOnSave() {
        Product p = new Product("asus", "laptop", 0.0, 5);
        inventory.save(p);
        assertFalse(inventory.existsByName("asus"));
    }

    @Test
    void shouldRejectNegativeQuantityOnSave() {
        Product p = new Product("horizon", "videogame", 250.0, -5);
        inventory.save(p);
        assertFalse(inventory.existsByName("horizon"));
    }

    @Test
    void shouldFindByNameReturnCopy() {
        Product p = new Product("xbox", "console", 300.0, 10);
        inventory.save(p);
        Product returned = inventory.findByName("xbox");
        assertNotNull(returned);
        returned.setQuantity(1);
        assertEquals(10, inventory.findByName("xbox").getQuantity());
    }

    @Test
    void shouldExistsByNameBeCaseInsensitive() {
        Product p = new Product("OPPO", "Tecnology", 200.0, 2);
        inventory.save(p);
        assertTrue(inventory.existsByName("oppo"));
    }

    @Test
    void shouldUpdateNonExistingReturnTrue() {
        Product p = new Product("Powershow", "Camera", 1.0, 1);
        boolean updated = inventory.update(p);
        assertTrue(updated);
    }


    @Test
    void shouldIgnoreNegativeQuantityOnUpdate() {
        Product p = new Product("ram", "comp", 79.0, 10);
        inventory.save(p);
        p.setQuantity(-3);
        boolean result = inventory.update(p);

        assertTrue(result);
        assertEquals(10, inventory.findByName("ram").getQuantity());
    }

    @Test
    void shouldCreateNewProductAndUpdate() {
        Product p = new Product("mouse", "other", 12.0, 7);
        boolean result = inventory.update(p);
        assertTrue(result);
        Product stored = inventory.findByName("mouse");
        assertNotNull(stored);
        assertEquals(7, stored.getQuantity());
    }

    @Test
    void shouldHandleNegativeQuantity() {
        Product p = new Product("samsung", "tablet", 500.0, -4);
        boolean result = inventory.update(p);
        assertTrue(result);
        Product stored = inventory.findByName("samsung");
        assertNotNull(stored);
        assertEquals(0, stored.getQuantity(),
                "Cuando se actualiza con cantidad negativa y no existe, debe crearse con 0");
    }

    @Test
    void shouldEnforceCapacity() {
        inventory.save(new Product("disk", "storage", 50.0, 10));
        inventory.setCapacity("disk", 5);
        Product update = new Product("disk", "storage", 50.0, 20);
        boolean ok = inventory.update(update);
        assertTrue(ok);
        Product stored = inventory.findByName("disk");
        assertNotNull(stored);
        assertEquals(5, stored.getQuantity(), "La cantidad no debe superar la capacidad establecida");
    }

    @Test
    void shouldReturnSafeCopies() {
        inventory.save(new Product("aretes", "accesorios", 100.0, 2));
        inventory.save(new Product("collar", "accesorios", 200.0, 3));
        Collection<Product> all = inventory.findAll();
        assertEquals(2, all.size());
        Product first = all.iterator().next();
        int originalQty = inventory.findByName(first.getName()).getQuantity();
        first.setQuantity(999);
        assertEquals(originalQty, inventory.findByName(first.getName()).getQuantity());
    }

    @Test
    void shouldNotSaveNull() {
        ManageInventory inv = new ManageInventory();
        inv.save(null);
        Collection<Product> all = inv.findAll();
        assertTrue(all.isEmpty(), "No debe haber productos cuando se guarda null");
    }

    @Test
    void shouldIgnoreInvalidPriceOrQuantity() {
        ManageInventory inv = new ManageInventory();
        inv.save(new Product("tablet", "Electronics", 0.0, 1));
        inv.save(new Product("legos", "Toys", 10.0, -1));
        assertNull(inv.findByName("tablet"));
        assertNull(inv.findByName("legos"));
    }

    @Test
    void shouldNormalizeNameOnSave() {
        ManageInventory inv = new ManageInventory();
        inv.save(new Product("  Audifonos Bluetooth  ", "Accessory", 100.0, 5));
        assertNotNull(inv.findByName("audifonos bluetooth"));
        assertNotNull(inv.findByName("  AUDIFONOS bluetooth "));
        assertTrue(inv.existsByName("AUDIFONOS BLUETOOTH"));
    }

    @Test
    void shouldReturnNullForNullName() {
        ManageInventory inv = new ManageInventory();
        assertNull(inv.findByName(null));
    }

    @Test
    void shouldReturnCopiesFromFindAll() {
        ManageInventory inv = new ManageInventory();
        inv.save(new Product("legos", "Toys", 10.0, 2));
        Collection<Product> all = inv.findAll();
        assertEquals(1, all.size());
        Product first = all.iterator().next();
        first.setQuantity(999);
        Product original = inv.findByName("legos");
        assertNotEquals(999, original.getQuantity());
    }

    @Test
    void shouldReturnFalseForInvalidUpdate() {
        ManageInventory inv = new ManageInventory();
        assertFalse(inv.update(null));
        assertFalse(inv.update(new Product(null, "Other", 10.0, 1)));
        assertFalse(inv.update(new Product("   ", "Other", 10.0, 1)));
    }

    @Test
    void shouldCreateWithZeroWhenNegativeQty() {
        ManageInventory inv = new ManageInventory();
        Product p = new Product("tablet", "Electronics", 0.5, -5);
        boolean ok = inv.update(p);
        assertTrue(ok);
        Product stored = inv.findByName("tablet");
        assertNotNull(stored);
        assertEquals(0, stored.getQuantity(), "Cuando se pasa cantidad negativa y no existía, debe crearse con 0");
        assertTrue(stored.getPrice() >= 1.0, "Precio mínimo aplicado con Math.max(product.getPrice(), 1.0)");
        Integer cap = inv.getCapacity("tablet");
        assertNotNull(cap);
        assertEquals(0, cap.intValue(), "La capacidad debe haberse puesto a 0 para este caso");
    }

    @Test
    void shouldRespectCapacityOnUpdate() {
        ManageInventory inv = new ManageInventory();
        inv.save(new Product("audifonos", "Accessory", 20.0, 5));
        inv.setCapacity("audifonos", 3);
        Product update = new Product("audifonos", "AccessoryNew", 15.0, 10);
        boolean ok = inv.update(update);
        assertTrue(ok);
        Product stored = inv.findByName("audifonos");
        assertNotNull(stored);
        assertEquals("AccessoryNew", stored.getCategory(), "Debió actualizar la categoría");
        assertEquals(15.0, stored.getPrice(), 0.0001, "Debió actualizar el precio");
        assertEquals(3, stored.getQuantity(), "Cantidad debe haberse limitado a la capacidad (3)");
    }


}