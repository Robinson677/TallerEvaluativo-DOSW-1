package edu.dosw.lab.agilismo;

public class LogAgent implements StockObserver{
    @Override
    public void onStockChanged(Product product) {
        System.out.println("Producto: " + product.getName() + " ->" + product.getQuantity() + " unidades disponibles");
    }
}
