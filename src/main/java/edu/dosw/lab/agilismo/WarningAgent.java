package edu.dosw.lab.agilismo;

import org.springframework.stereotype.Component;

@Component
public class WarningAgent implements StockObserver {

    private static final int UNIT_LIMIT = 5;

    @Override
    public void onStockChanged(Product product) {
        if (product.getQuantity() < UNIT_LIMIT) {
            System.out.println("ALERTA!!! El stock del Prodcto: " + product.getName()
                    + " es muy bajo, solo quedan " + product.getQuantity() + " unidades.");
        }
    }
}
