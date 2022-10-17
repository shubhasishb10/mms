package com.mms.thp.model;

import net.bytebuddy.asm.Advice;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Shubhasish
 */
public class Order {

    private static boolean isPurchaseActive = false;
    private static Order currentInstance;

    private final String customerName;
    private final LocalDate purchaseDate;

    private final List<Medicine> medicines;

    private Order(String customerName, LocalDate purchaseDate) {
        this.customerName = customerName;
        this.purchaseDate = purchaseDate;
        medicines = new LinkedList<>();
    }

    public static Order prepareOrderBucket(String customerName) {
        if (!isPurchaseActive) {
            isPurchaseActive = true;
            currentInstance = new Order(customerName, LocalDate.now());
        }
        return currentInstance;
    }

    public static boolean destroyPurchaseSession() {
        if(isPurchaseActive) {
            currentInstance = null;
            isPurchaseActive = false;
            return true;
        }
        return false;
    }

    public String getCustomerName() {
        return customerName;
    }

    public LocalDate getPurchaseDate() {
        return this.purchaseDate;
    }

    public List<Medicine> getMedicines() {
        return medicines;
    }

    public static boolean isPurchaseActive() {
        return isPurchaseActive;
    }
}
