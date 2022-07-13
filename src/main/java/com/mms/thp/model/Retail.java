package com.mms.thp.model;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "retail")
public class Retail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private long medicineId;
    @Temporal(TemporalType.DATE)
    private Date retailDate;
    private String customerName;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getMedicineId() {
        return medicineId;
    }

    public void setMedicineId(long medicineId) {
        this.medicineId = medicineId;
    }

    public Date getRetailDate() {
        return retailDate;
    }

    public void setRetailDate(Date retailDate) {
        this.retailDate = retailDate;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }
}
