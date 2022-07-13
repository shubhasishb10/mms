package com.mms.thp.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.Objects;

@Entity
@Table(name = "medicine_box")
public class MedicineBoxes {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long medicineBoxId;
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "medicineId", referencedColumnName = "medicineId")
    private Medicine medicine;
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "boxId", referencedColumnName = "boxId")
    private Box box;
    private int medicineCount;

    public long getMedicineBoxId() {
        return medicineBoxId;
    }

    public void setMedicineBoxId(long medicineBoxId) {
        this.medicineBoxId = medicineBoxId;
    }

    public Medicine getMedicine() {
        return medicine;
    }

    public void setMedicine(Medicine medicine) {
        this.medicine = medicine;
    }

    public Box getBox() {
        return box;
    }

    public void setBox(Box box) {
        this.box = box;
    }

    public int getMedicineCount() {
        return medicineCount;
    }

    public void setMedicineCount(int medicineCount) {
        this.medicineCount = medicineCount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MedicineBoxes)) return false;
        MedicineBoxes that = (MedicineBoxes) o;
        return getMedicineBoxId() == that.getMedicineBoxId();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getMedicineBoxId());
    }

    @Override
    public String toString() {
        return "MedicineBoxes{" +
                "medicineBoxId=" + medicineBoxId +
                ", medicine=" + medicine +
                ", box=" + box +
                ", medicineCount=" + medicineCount +
                '}';
    }
}
