package edu.dosw.lab.agilismo;

import java.util.Collection;

public interface Inventory {

    /**
     * Guarda el producto
     * @param product
     */
    void save(Product product);

    /**
     * Encuentra el nombre del producto
     */
    Product findByName(String name);

    /**
     * Busaca todos lo productos disponibles
     * @return la coleccion de los productos
     */
    Collection<Product> findAll();

    /**
     * Actualiza el producto
     * @param product
     * @return si existe o no el producto
     */
    boolean update(Product product);
    /**
     * devuelve true si existe un producto con ese nombre
     */
    boolean existsByName(String name);
}
