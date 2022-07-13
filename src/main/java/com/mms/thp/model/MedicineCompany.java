package com.mms.thp.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * This is not used in the implementation
 */

/*@Entity
@Table(name = "medicine_company")*/
public class MedicineCompany {
    /*@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String medicineCompanyId;
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "companyId", referencedColumnName = "companyId")
    private Company company;
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "medicineId", referencedColumnName = "medicineId")
    private Medicine medicine;

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public String getMedicineCompanyId() {
        return medicineCompanyId;
    }

    public void setMedicineCompanyId(String medicineCompanyId) {
        this.medicineCompanyId = medicineCompanyId;
    }

    public Medicine getMedicine() {
        return medicine;
    }

    public void setMedicine(Medicine medicine) {
        this.medicine = medicine;
    }

    @Override
    public String toString() {
        return "MedicineCompany{" +
                "medicineCompanyId='" + medicineCompanyId + '\'' +
                ", company=" + company +
                ", medicine=" + medicine +
                '}';
    }*/
}
