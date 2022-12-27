/**
 * Copyright (C) DreamTech Inc - All Rights Reserved Unauthorized copying of this file,
 * via any medium is strictly prohibited Proprietary and confidential For any use
 * of this software please write us to shubhasishb10@gmail.com.
 */

package com.mms.thp.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "retail_medicine")
public class RetailMedicine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long retailMedicineId;
    private int retailCount;
    @OneToOne
    @JoinColumn(name = "medicineId", referencedColumnName = "medicineId")
    private Medicine medicine;

    @OneToOne
    @JoinColumn(name = "boxId", referencedColumnName = "boxId")
    private Box box;

    public long getRetailMedicineId() {
        return retailMedicineId;
    }

    public void setRetailMedicineId(long retailMedicineId) {
        this.retailMedicineId = retailMedicineId;
    }

    public int getRetailCount() {
        return retailCount;
    }

    public void setRetailCount(int retailCount) {
        this.retailCount = retailCount;
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
}
