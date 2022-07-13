package com.mms.thp.dto;

import java.util.ArrayList;
import java.util.List;

public class MedicineOrder {

    private long medicineId;
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

    @Override
    public String toString() {
        return "MedicineOrder{" +
                "medicineId=" + medicineId +
                ", boxes=" + boxes +
                '}';
    }
}
