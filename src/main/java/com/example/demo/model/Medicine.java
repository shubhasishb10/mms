package com.example.demo.model;

import javax.persistence.*;
import java.util.*;

@Entity
@Table(name = "medicine")
public class Medicine {

    // Unique id for a medicine as id
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long medicineId;
    private String name;
    private String company;
    private double price;

    @Temporal(TemporalType.DATE)
    private Date purchaseDate;
    private int volume;
    private int boxNumber;
    @Temporal(TemporalType.DATE)
    private Date sellDate;

    @Transient
    List<Integer> containingBoxes;
    @Transient
    String boxes;
    /*@ManyToMany()
    private List<Box> boxes;*/

/*    public Medicine(String name, String company, double price, Date purchaseDate, int volume) {
        this.medicineId = UUID.randomUUID().toString();
        this.name = name;
        this.company = company;
        this.price = price;
        this.purchaseDate = purchaseDate;
        this.volume = volume;
        boxes = new ArrayList<>();
    }*/

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Medicine)) return false;
        Medicine medicine = (Medicine) o;
        return this.medicineId == medicine.medicineId;
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

    public void setName(String name) {
        this.name = name;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
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
    /*public List<Box> getBoxes(){
        return boxes;
    }*/
    public int getBoxNumber(){
        return boxNumber;
    }
    public void setBoxNumber(int boxNumber) {
        this.boxNumber = boxNumber;
    }

    public Date getSellDate() {
        return sellDate;
    }

    public void setSellDate(Date sellDate) {
        this.sellDate = sellDate;
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
        this.boxes = boxes;
    }
}
