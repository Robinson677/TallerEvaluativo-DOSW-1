package edu.dosw.lab.agilismo;

import java.util.*;

public class ManageInventory implements Inventory{
    private final Map<String, Product> stock = new LinkedHashMap<>();


    /**
     * Normalizamos el nombre de un producto
     * @param name
     * @return
     */
    private String normalize(String name) {
        if (name == null) {
            return null;
        }
        String trimmed = name.trim();
        if (trimmed.isEmpty()) {
            return null;
        }
        return trimmed.toLowerCase(Locale.ROOT);
    }

    /**
     * Verifica si un nombre es null o esta en blanco
     * @param name
     * @return
     */
    private String trimmedName(String name) {
        if (name == null) {
            return null;
        }
        return name.trim();
    }

    /**
     * Guarda el producto
     * @param product
     */
    @Override
    public void save(Product product) {
        if (product == null) {
            return;
        }

        String name = product.getName();
        if (name == null) {
            return;
        }

        String trimmed = name.trim();
        if (trimmed.isEmpty()) {
            return;
        }

        if (product.getPrice() <= 0.0) {
            return;
        }

        if (product.getQuantity() < 0) {
            return;
        }

        String key = trimmed.toLowerCase(Locale.ROOT);

        if (stock.containsKey(key)) {
            return;
        }

        Product toStore = new Product(trimmed, product.getCategory(), product.getPrice(), product.getQuantity());
        stock.put(key, toStore);
    }
    /**
     * Encuentra el nombre del producto
     */
    @Override
    public Product findByName(String name) {
        if (name == null) {
            return null;
        }

        String key = name.trim().toLowerCase(Locale.ROOT);
        if (key.isEmpty()) {
            return null;
        }

        Product stored = stock.get(key);
        if (stored == null) {
            return null;
        }

        return new Product(stored.getName(), stored.getCategory(), stored.getPrice(), stored.getQuantity());
    }

    /**
     * Busaca todos lo productos disponibles
     * @return la coleccion de los productos
     */
    public Collection<Product> findAll() {
        List<Product> copies = new ArrayList<>();
        for (Product p : stock.values()) {
            copies.add(new Product(p.getName(), p.getCategory(), p.getPrice(), p.getQuantity()));
        }
        return Collections.unmodifiableCollection(copies);
    }
    /**
     * Actualiza el producto
     * @param product
     * @return si existe o no el producto
     */
    @Override
    public boolean update(Product product) {
        if (product == null) {
            return false;
        }

        String name = product.getName();
        if (name == null) {
            return false;
        }

        String trimmed = name.trim();
        if (trimmed.isEmpty()) {
            return false;
        }

        String key = trimmed.toLowerCase(Locale.ROOT);
        Product existing = stock.get(key);
        if (product.getQuantity() < 0) {
            if (existing == null) {
                Product toStore = new Product(trimmed, product.getCategory(), product.getPrice(), 0);
                stock.put(key, toStore);
                return true;
            } else {
                return true;
            }
        }

        if (existing == null) {
            Product toStore = new Product(trimmed, product.getCategory(), product.getPrice(), product.getQuantity());
            stock.put(key, toStore);
            return true;
        } else {
            existing.setCategory(product.getCategory());
            existing.setPrice(product.getPrice());
            existing.setQuantity(product.getQuantity());
            return true;
        }
    }


    /**
     * devuelve true si existe un producto con ese nombre
     */
    @Override
    public boolean existsByName(String name) {
        if (name == null) {
            return false;
        }
        String key = name.trim().toLowerCase(Locale.ROOT);
        if (key.isEmpty()) {
            return false;
        }
        return stock.containsKey(key);
    }
}
