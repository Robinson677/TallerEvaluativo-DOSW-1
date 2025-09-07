package edu.dosw.lab.agilismo;

/**
 * Productos del inventario
 */
public class Product {
    private String name;
    private String category;
    private double price;
    private int quantity;

    /**
     * Contructor de Product
     * @param name
     * @param category
     * @param price
     * @param quantity
     */
    public Product(String name, String category, double price, int quantity) {
        this.name = name;
        this.category = category;
        this.price = price;
        this.quantity = quantity;
    }

    /**
     * Obtiene el nombre
     * @return nombre en string
     */
    public String getName() { return name; }

    /**
     * Establece el nombre del producto
     * @param name del producto
     */
    public void setName(String name) { this.name = name; }

    /**
     * Obtiene la categoria
     * @return la categoria en String
     */
    public String getCategory() { return category; }

    /**
     * Establece la categoria
     * @param category del producto
     */
    public void setCategory(String category) { this.category = category; }

    /**
     * Obtiene el precio del producto
     * @return el valor
     */
    public double getPrice() { return price; }

    /**
     * Establece el precio del producto
     * @param price del producto
     */
    public void setPrice(double price) { this.price = price; }

    /**
     * Obtiene la cantidad de stock de un producto
     * @return cantidad de stock
     */
    public int getQuantity() { return quantity; }

    /**
     * Establece la cantidad de stock del producto
     * @param quantity del producto
     */
    public void setQuantity(int quantity) { this.quantity = quantity; }
}
