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
    @OneToMany(mappedBy = "medicine", fetch = FetchType.LAZY)
    private List<MedicineBoxes> medicineBoxes;
    @Transient
    private List<Integer> containingBoxes = new ArrayList<>();
    @Transient
    private String boxes;
    @Transient
    private int boxNumber;

    @Transient
    private int count;

    @Transient
    private int totalMedicinePresent;

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
        return this.name.equalsIgnoreCase(medicine.name) && this.company.equalsIgnoreCase(medicine.company) &&
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

    public void setName(String name) {
        this.name = name.toLowerCase();
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company.toLowerCase();
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
        this.boxes = boxes;
    }

    public Map<Integer, Integer> getMedicineCountByBoxNumber() {
        return medicineCountByBoxNumber;
    }

    public void setMedicineCountByBoxNumber(Map<Integer, Integer> medicineCountByBoxNumber) {
        this.medicineCountByBoxNumber = medicineCountByBoxNumber;
    }

    public void setBoxNumber(int boxNumber) {
        this.boxNumber = boxNumber;
    }
    public int getBoxNumber(){
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

    @Override
    public String toString() {
        return "Medicine{" +
                "medicineId=" + medicineId +
                ", name='" + name + '\'' +
                ", company='" + company + '\'' +
                ", price=" + price +
                ", purchaseDate=" + purchaseDate +
                ", volume=" + volume +
                ", box=" + medicineBoxes +
                '}';
    }
}
