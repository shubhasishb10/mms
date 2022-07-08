package com.example.demo.model;

import java.util.*;

public class Medicine {

    // Unique id for a medicine as id
    private final String medicineId;
    private String name;
    private String company;
    private double price;
    private Date purchaseDate;
    private int volume;
    private List<Box> boxes;

    public Medicine(String name, String company, double price, Date purchaseDate, int volume) {
        this.medicineId = UUID.randomUUID().toString();
        this.name = name;
        this.company = company;
        this.price = price;
        this.purchaseDate = purchaseDate;
        this.volume = volume;
        boxes = new ArrayList<>();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Medicine)) return false;
        Medicine medicine = (Medicine) o;
        return this.medicineId.equals(medicine.medicineId);
    }

    @Override
    public int hashCode() {
        return medicineId.hashCode();
    }

    public String getMedicineId() {
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
}
