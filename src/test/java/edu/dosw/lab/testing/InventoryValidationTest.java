package edu.dosw.lab.testing;

import edu.dosw.lab.agilismo.ManageInventory;
import edu.dosw.lab.agilismo.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.*;

class InventoryValidationShouldTest {

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
}