/**
 * Copyright (C) DreamTech Inc - All Rights Reserved Unauthorized copying of this file,
 * via any medium is strictly prohibited Proprietary and confidential For any use
 * of this software please write us to shubhasishb10@gmail.com.
 */

package com.mms.thp.model;

import com.mms.thp.utility.ThpUtility;
import com.mms.thp.validation.annotation.CheckValue;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.*;

@Entity
@Table(name = "medicine")
public class Medicine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long medicineId;
    private String name;
    @NotEmpty(message = "Name is required")
    private String displayName;
    private String companyStringName;
    @NotEmpty(message = "Company name is required")
    private String companyDisplayName;
    private double price;

    @Temporal(TemporalType.DATE)
    private Date purchaseDate;
    private int volume;
    @OneToMany(mappedBy = "medicine", fetch = FetchType.LAZY)
    private List<MedicineBoxes> medicineBoxes;
    @Transient
    private List<Integer> containingBoxes = new ArrayList<>();
    @Transient
    private String boxes;
    @Transient
    private String boxNumber;
    @Transient
    private int count;

    @Transient
    private int totalMedicinePresent;

    @Transient
    private int medicineInTheBox;

    @Transient
    private Map<Integer, Integer> medicineCountByBoxNumber = new HashMap<>();

    public List<MedicineBoxes> getMedicineBoxes() {
        return medicineBoxes;
    }

    public void setMedicineBoxes(List<MedicineBoxes> medicineBoxes) {
        this.medicineBoxes = medicineBoxes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Medicine)) return false;
        Medicine medicine = (Medicine) o;
        return this.name.equalsIgnoreCase(medicine.name) && this.companyStringName.equalsIgnoreCase(medicine.companyStringName) &&
                this.volume == medicine.volume;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(medicineId);
    }

    public void setMedicineId(long medicineId) {
        this.medicineId = medicineId;
    }

    public long getMedicineId() {
        return medicineId;
    }

    public String getName() {
        return name;
    }

    private void setName(String name) {
        this.name = name.toLowerCase();
    }

    public String getCompanyStringName() {
        return companyStringName;
    }

    private void setCompanyStringName(String companyStringName) {
        this.companyStringName = companyStringName;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Date getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(Date purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public int getVolume() {
        return volume;
    }

    public void setVolume(int volume) {
        this.volume = volume;
    }

    public List<Integer> getContainingBoxes() {
        return containingBoxes;
    }

    public void setContainingBoxes(List<Integer> containingBoxes) {
        this.containingBoxes = containingBoxes;
    }

    public String getBoxes() {
        return boxes;
    }

    public void setBoxes(String boxes) {
        this.boxes = boxes.toUpperCase();
    }

    public Map<Integer, Integer> getMedicineCountByBoxNumber() {
        return medicineCountByBoxNumber;
    }

    public void setMedicineCountByBoxNumber(Map<Integer, Integer> medicineCountByBoxNumber) {
        this.medicineCountByBoxNumber = medicineCountByBoxNumber;
    }

    public void setBoxNumber(String boxNumber) {
        this.boxNumber = boxNumber.toLowerCase();
    }
    public String getBoxNumber(){
        return boxNumber;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getTotalMedicinePresent() {
        return totalMedicinePresent;
    }

    public void setTotalMedicinePresent(int totalMedicinePresent) {
        this.totalMedicinePresent = totalMedicinePresent;
    }

    public String getDisplayName() {
        return displayName;
    }
    public void setDisplayName(String displayName) {
        this.displayName = ThpUtility.normalizeStringForDisplaying(displayName);
        setName(ThpUtility.normalizeString(displayName));
    }

    public String getCompanyDisplayName() {
        return companyDisplayName;
    }

    public void setCompanyDisplayName(String companyDisplayName) {
        this.companyDisplayName = ThpUtility.normalizeStringForDisplaying(companyDisplayName);
        setCompanyStringName(ThpUtility.normalizeString(companyDisplayName));
    }

    public int getMedicineInTheBox() {
        return medicineInTheBox;
    }

    public void setMedicineInTheBox(int medicineInTheBox) {
        this.medicineInTheBox = medicineInTheBox;
    }

    @Override
    public String toString() {
        return "Medicine{" +
                "medicineId=" + medicineId +
                ", name='" + name + '\'' +
                ", company='" + companyStringName + '\'' +
                ", price=" + price +
                ", purchaseDate=" + purchaseDate +
                ", volume=" + volume +
                ", box=" + medicineBoxes +
                '}';
    }
}
