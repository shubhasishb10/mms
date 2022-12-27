/**
 * Copyright (C) DreamTech Inc - All Rights Reserved Unauthorized copying of this file,
 * via any medium is strictly prohibited Proprietary and confidential For any use
 * of this software please write us to shubhasishb10@gmail.com.
 */

package com.mms.thp.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "retail")
public class Retail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long retailId;
    @OneToMany(targetEntity = RetailMedicine.class, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "retailId", referencedColumnName = "retailId")
    private List<RetailMedicine> retailMedicines = new ArrayList<>();
    @Temporal(TemporalType.DATE)
    private Date retailDate;
    private String customerName;
    private String customerAddress;

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

    public long getRetailId() {
        return retailId;
    }

    public void setRetailId(long retailId) {
        this.retailId = retailId;
    }

    public List<RetailMedicine> getRetailMedicines() {
        return retailMedicines;
    }

    public String getCustomerAddress() {
        return customerAddress;
    }

    public void setCustomerAddress(String customerAddress) {
        this.customerAddress = customerAddress;
    }
}
