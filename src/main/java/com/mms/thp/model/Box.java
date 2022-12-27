/**
 * Copyright (C) DreamTech Inc - All Rights Reserved Unauthorized copying of this file,
 * via any medium is strictly prohibited Proprietary and confidential For any use
 * of this software please write us to shubhasishb10@gmail.com.
 */

package com.mms.thp.model;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "box")
public class Box {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String boxId;
    private String number;
    private String location;
    private int capacity;
    private String name;
    @OneToMany(mappedBy = "box", fetch = FetchType.LAZY)
    private List<MedicineBoxes> medicinesBoxes;

    public String getBoxId(){
        return boxId;
    }
    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<MedicineBoxes> getMedicinesInBox() {
        return this.medicinesBoxes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Box)) return false;
        Box box = (Box) o;
        return name.equalsIgnoreCase(box.name) && number == box.number;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, number);
    }

    @Override
    public String toString() {
        return "Box{" +
                "boxId='" + boxId + '\'' +
                ", number='" + number + '\'' +
                ", location='" + location + '\'' +
                ", capacity=" + capacity +
                ", name='" + name + '\'' +
                '}';
    }
}
