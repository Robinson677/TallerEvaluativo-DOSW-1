package edu.dosw.lab.agilismo;

import java.util.*;
import java.util.stream.Collectors;

public class ManageInventory implements Inventory{
    private final Map<String, Product> stock = new LinkedHashMap<>();


    /**
     * Normalizamos el nombre de un producto
     * @param name
     * @return
     */
    private String normalize(String name) {
        return Optional.ofNullable(name)
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .map(s -> s.toLowerCase(Locale.ROOT))
                .orElse(null);
    }

    /**
     * Hace una copia del producto
     * @param p representa el producto
     * @return el producto con sus atributos
     */
    private Product copyOfProduct(Product p) {
        return new Product(p.getName(), p.getCategory(), p.getPrice(), p.getQuantity());
    }


    /**
     * Guarda el producto
     * @param product
     */
    @Override
    public void save(Product product) {
        if (product == null) {return;}
        String key = normalize(product.getName());
        if (key == null || product.getPrice() <= 0.0 || product.getQuantity() < 0) {return;}

        stock.putIfAbsent(key,
                new Product(product.getName().trim(), product.getCategory(),
                        product.getPrice(), product.getQuantity()));
    }
    /**
     * Encuentra el nombre del producto
     */
    @Override
    public Product findByName(String name) {
        String key = normalize(name);
        return key == null ? null :
                Optional.ofNullable(stock.get(key)).map(this::copyOfProduct).orElse(null);
    }

    /**
     * Busaca todos lo productos disponibles
     * @return la coleccion de los productos
     */
    public Collection<Product> findAll() {
        return stock.values().stream()
                .map(this::copyOfProduct)
                .collect(Collectors.toUnmodifiableList());
    }

    /**
     * Actualiza el producto
     * @param product
     * @return si existe o no el producto
     */
    @Override
    public boolean update(Product product) {
        if (product == null) {return false;}
        String key = normalize(product.getName());
        if (key == null) {return false;}

        if (product.getQuantity() < 0) {
            Product existing = stock.get(key);
            if (existing == null) {
                Product toStore = new Product(product.getName().trim(),
                        product.getCategory(),
                        Math.max(product.getPrice(), 1.0),
                        0);
                stock.putIfAbsent(key, toStore);
                return true;
            } else {
                return true;
            }
        }

        Product existing = stock.get(key);

        Optional.ofNullable(existing).ifPresentOrElse(
                ex -> {
                    ex.setCategory(product.getCategory());
                    ex.setPrice(product.getPrice());
                    ex.setQuantity(product.getQuantity());
                },
                () -> {
                    Product toStore = new Product(product.getName().trim(),
                            product.getCategory(),
                            product.getPrice(),
                            product.getQuantity());
                    stock.put(key, toStore);
                }
        );

        return true;
    }


    /**
     * devuelve true si existe un producto con ese nombre
     */
    @Override
    public boolean existsByName(String name) {
        String key = normalize(name);
        return key != null && stock.containsKey(key);
    }
}
