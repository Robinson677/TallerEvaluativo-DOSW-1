package edu.dosw.lab.agilismo;

public interface StockObserver {
    /**
     * Mira si el stock de algun producto gue modificado
     * @param product que se modifico su stock
     */
    void onStockChanged(Product product);
}
