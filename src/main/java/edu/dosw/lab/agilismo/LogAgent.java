package edu.dosw.lab.agilismo;

import org.springframework.stereotype.Component;

@Component
public class LogAgent implements StockObserver{
    @Override
    public void onStockChanged(Product product) {
        System.out.println("Prodcto: " + product.getName() + " ->" + product.getQuantity() + " unidades disponibles");
    }
}
