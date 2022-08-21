package com.mms.thp.dto;

import java.util.ArrayList;
import java.util.List;

public class MedicineOrder {

    private long medicineId;
    private String customerName;
    private String customerAddress;
    private List<OrderedBox> boxes = new ArrayList<>();

    public long getMedicineId() {
        return medicineId;
    }

    public void setMedicineId(long medicineId) {
        this.medicineId = medicineId;
    }

    public List<OrderedBox> getBoxes() {
        return boxes;
    }

    public void setBoxes(List<OrderedBox> boxes) {
        this.boxes = boxes;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerAddress() {
        return customerAddress;
    }

    public void setCustomerAddress(String customerAddress) {
        this.customerAddress = customerAddress;
    }

    @Override
    public String toString() {
        return "MedicineOrder{" +
                "medicineId=" + medicineId +
                ", boxes=" + boxes +
                '}';
    }
}
