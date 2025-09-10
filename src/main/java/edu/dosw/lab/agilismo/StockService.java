package edu.dosw.lab.agilismo;

import java.util.List;
import java.util.Objects;

/**
 * Clase encargada de la gestion de los productos  y se asegura que la pagina funcione bien
 */
public class StockService {

    private final Inventory inventory;
    private final List<StockObserver> observers;

    public StockService(Inventory inventory, List<StockObserver> observers) {
        this.inventory = Objects.requireNonNull(inventory);
        this.observers = Objects.requireNonNull(observers);
    }

    /**
     * Añade un producto al inventario y si ya existe sobreescribe el stock
     * @param name del producto
     * @param category del producto
     * @param price valor del producto
     * @param quantity cantidad del producto
     * @return el producto añadido
     */
    public Product addProduct(String name, String category, double price, int quantity) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("El nombre es invalido si es nulo o esta en blanco");
        }
        if (price <= 0) {
            throw new IllegalArgumentException("El precio debe de ser positivo");
        }
        if (quantity < 0) {
            throw new IllegalArgumentException("La cantidad no debe ser negativa");
        }

        Product p = new Product(name, category, price, quantity);
        inventory.save(p);
        notifyObservers(p);
        return p;
    }

    /**
     * Actualiza la cantidad de un producto identificadolo por su nombre
     * @param name nombre del producto
     * @param newQuantity nueva cantidad
     * @return true si exitia y actualizo si no es false
     */
    public boolean updateStock(String name, int newQuantity) {
        if (name == null || name.isBlank()) {
            return false;
        }
        if (newQuantity < 0) {
            return false;
        }

        Product existing = inventory.findByName(name);
        if (existing == null) {
            return false;
        }

        existing.setQuantity(newQuantity);
        inventory.update(existing);

        notifyObservers(existing);
        return true;
    }

    /**
     * Los agentes estan al tanto de los movimientos de un cliente y
     * como modifica al stock del inventario
     * @param product
     */
    private void notifyObservers(Product product) {
        for (StockObserver obs : observers) {
            try {
                obs.onStockChanged(product);
            } catch (Exception ex) {
                System.err.println("Observer failed: " + ex.getMessage());
            }
        }
    }
}
