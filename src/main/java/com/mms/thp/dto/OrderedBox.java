package com.mms.thp.dto;

import java.util.function.Predicate;

public class OrderedBox {

    public static final Predicate<OrderedBox> MEDICINE_COUNT_NOT_EMPTY = b -> b.getMedicineCount() > 0;

    private String boxNumber;
    private int medicineCount;

    public String getBoxNumber() {
        return boxNumber;
    }

    public void setBoxNumber(String boxNumber) {
        this.boxNumber = boxNumber.toLowerCase();
    }

    public int getMedicineCount() {
        return medicineCount;
    }

    public void setMedicineCount(int medicineCount) {
        this.medicineCount = medicineCount;
    }



    @Override
    public String toString() {
        return "OrderedBox{" +
                "boxNumber='" + boxNumber + '\'' +
                ", medicineCount=" + medicineCount +
                '}';
    }
}
