package edu.dosw.lab.agilismo;

import java.util.*;

public class ManageInventory implements Inventory{
    private final Map<String, Product> stock = new LinkedHashMap<>();

    /**
     * Guarda el producto
     * @param product
     */
    @Override
    public void save(Product product) {
        stock.put(product.getName(), product);
    }

    /**
     * Encuentra el nombre del producto
     */
    @Override
    public Product findByName(String name) {
        return stock.get(name);
    }

    /**
     * Busaca todos lo productos disponibles
     * @return la coleccion de los productos
     */
    @Override
    public Collection<Product> findAll() {
        return Collections.unmodifiableCollection(stock.values());
    }

    /**
     * Actualiza el producto
     * @param product
     * @return si existe o no el producto
     */
    @Override
    public boolean update(Product product) {
        String name = product.getName();
        if (!stock.containsKey(name)) {
            return false;
        }
        stock.put(name, product);
        return true;
    }

    /**
     * devuelve true si existe un producto con ese nombre
     */
    @Override
    public boolean existsByName(String name) {
        return stock.containsKey(name);
    }
}
