package com.mms.thp.service;

/**
 * @author Shubhasish
 */
public interface OrderService {

    void initiatePurchaseContext(String customerName);
    boolean isPurchaseActive();
    void destroyPurchaseSession();
    void addToOrder(long medicineId);
}
