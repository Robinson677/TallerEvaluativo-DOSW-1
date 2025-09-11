package edu.dosw.lab.agilismo;

import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Scanner;
import java.util.stream.Collectors;

/***
 * Clase encargada de ejecutar el monitor de stock para los usarios y
 * funciones correctamente
 */

@Component
public class ExecutableStockMonitoringSystem {

    private final StockService stockService;
    private final Inventory inventory;

    /**
     * Constructor de ExecutableStockMonitoringSystem
     * @param stockService
     * @param inventory
     */
    public ExecutableStockMonitoringSystem(StockService stockService, Inventory inventory) {
        this.stockService = stockService;
        this.inventory = inventory;
    }

    /**
     * Permite al usario elegir enre 4 opciones
     * @param sc para las lineas
     */
    public void runInteractive(Scanner sc) {
        System.out.println("Sistema de Monitoreo de Stock: añadir producto | modificar producto | listar productos | finalizar compra");
        boolean running = true;

        while (running) {
            System.out.print("\nAcción a realizar: añadir | modificar | listar | finalizar\nElija qué hacer: ");
            String cmd = sc.nextLine().trim().toLowerCase();
            if (cmd.isEmpty()) {continue;}

            switch (cmd) {
                case "finalizar" -> {
                    running = false;
                    System.out.println("Finalizando...");
                }
                case "añadir" -> handleAdd(sc);
                case "modificar" -> handleModify(sc);
                case "listar" -> handleList();
                default -> System.out.println("Comando invalido: use añadir, modificar, listar o finalizar");
            }
        }
    }

    /**
     * Categorias que se van a manejar
     */
    private enum Category {
        CONSOLE("Console"),
        ACCESSORY("Accessory"),
        GAME("Game"),
        COMPONENT("Component"),
        OTHER("Other");

        private final String display;

        /**
         * Contructor de Category
         * @param display
         */
        Category(String display) { this.display = display; }

        @Override
        public String toString() { return display; }

        /**
         * Opciones que el usuario puede elegir para la categoria de producto
         * @return las opciones
         */
        static String options() {
            return Arrays.stream(values())
                    .map(c -> (c.ordinal() + 1) + ") " + c)
                    .collect(Collectors.joining(", "));
        }

        /**
         * Permite elegir un valor para una opcion de categoria
         * @param idx el numero de la categoria
         * @return la categoria elegida
         */
        static Category fromIndex(int idx) {
            return (idx >= 1 && idx <= values().length) ? values()[idx - 1] : null;
        }
    }

    /**
     * Permite que el usario pueda añadir productos
     * @param sc
     */
    private void handleAdd(Scanner sc) {
        try {
            String name = prompt(sc, "Nombre del producto");
            double price = Double.parseDouble(prompt(sc, "Precio"));
            if (price <= 0) { System.out.println("El precio debe ser mayor que cero."); return; }

            System.out.println("Categorias disponibles: " + Category.options());
            int idx = Integer.parseInt(prompt(sc, "Numero de categoria"));
            Category chosen = Category.fromIndex(idx);
            if (chosen == null) { System.out.println("Categoría invalida."); return; }

            int quantity = Integer.parseInt(promptDefault(sc, "Cantidad de stock", "1"));
            if (quantity < 0) { System.out.println("La cantidad no puede ser negativa."); return; }

            Product p = stockService.addProduct(name, chosen.toString(), price, quantity);
            inventory.setCapacity(p.getName(), quantity);
            System.out.println("Añadido: " + p.getName() + " | categoría: " + chosen + " | quantity: " + p.getQuantity());

        } catch (NumberFormatException e) {
            System.out.println("Entrada invalida en precio o cantidad.");
        }
    }

    /**
     * Permite que el usuario pueda modificar sus productos elegidos
     * tiene la opcion de añadir o reponer el producto
     * @param sc
     */
    private void handleModify(Scanner sc) {
        try {
            String name = prompt(sc, "Nombre del producto a modificar");
            Product existing = inventory.findByName(name);
            if (existing == null) { System.out.println("Producto no encontrado."); return; }

            System.out.println("Producto: " + existing.getName() + " -> " + existing.getQuantity() + " unidades disponibles");
            String option = prompt(sc, "¿Desea (a)ñadir al carrito o (r)eponer stock?").toLowerCase();

            if (option.startsWith("a")) {
                int removeQty = Integer.parseInt(promptDefault(sc, "Cantidad a añadir al carrito", "1"));
                if (removeQty <= 0 || removeQty > existing.getQuantity()) {
                    System.out.println("Cantidad inválida. Stock disponible: " + existing.getQuantity());
                    return;
                }
                updateStock(existing, existing.getQuantity() - removeQty,
                        "Usuario añadió " + removeQty + " unidades al carrito de " + existing.getName() + ".");
            } else if (option.startsWith("r")) {
                int addQty = Integer.parseInt(promptDefault(sc, "Cantidad a reponer", "1"));
                if (addQty <= 0) { System.out.println("La cantidad debe ser positiva."); return; }

                int target = existing.getQuantity() + addQty;
                Integer capacity = inventory.getCapacity(existing.getName());
                if (capacity != null && target > capacity) {
                    System.out.println("La cantidad solicitada excede la capacidad máxima (" + capacity + ").");
                    return;
                }
                updateStock(existing, target, "Usuario repuso " + addQty + " unidades a " + existing.getName() + ".");
            } else {
                System.out.println("Opción inválida. Use 'a' o 'r'.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Cantidad inválida. Ingresa un número entero.");
        }
    }

    /**
     * Maneja la lista de productos
     */
    private void handleList() {
        var all = inventory.findAll();
        if (all.isEmpty()) {
            System.out.println("No hay productos en el inventario");
            return;
        }
        System.out.println("Productos en inventario:");
        all.forEach(p -> System.out.println(
                "- " + p.getName()
                        + " | categoría: " + p.getCategory()
                        + " | precio: " + p.getPrice()
                        + " | quantity: " + p.getQuantity()));
    }


    /**
     * Ayuda a los mensajes no se dupliquen
     * @param sc
     * @param msg
     * @return
     */
    private String prompt(Scanner sc, String msg) {
        System.out.print(msg + ": ");
        return sc.nextLine().trim();
    }

    /**
     * No deja que hayan inputs invalidos
     * @param sc
     * @param msg
     * @param def
     * @return
     */
    private String promptDefault(Scanner sc, String msg, String def) {
        System.out.print(msg + ": ");
        String input = sc.nextLine().trim();
        return input.isEmpty() ? def : input;
    }

    /**
     * Verifica si se puede actualizar el stock
     * @param existing
     * @param newQty
     * @param successMsg
     */
    private void updateStock(Product existing, int newQty, String successMsg) {
        boolean ok = stockService.updateStock(existing.getName(), newQty);
        System.out.println(ok ? successMsg : "No se pudo actualizar el stock.");
    }
}
