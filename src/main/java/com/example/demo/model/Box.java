package com.example.demo.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class Box {

    private String boxId;
    private String number;
    private String location;
    private int capacity;
    private String name;
    private List<Medicine> medicines;

    public Box(String boxId, String number, String location, int capacity, String name) {
        this.boxId = UUID.randomUUID().toString();
        this.number = number;
        this.location = location;
        this.capacity = capacity;
        this.name = name;
        medicines = new ArrayList<>();
    }

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

    public int getSize() {
        return capacity;
    }

    public void setSize(int capacity) {
        this.capacity = capacity;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Medicine> getMedicinesInBox() {
        return this.medicines;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Box)) return false;
        Box box = (Box) o;
        return boxId.equals(box.boxId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(boxId);
    }
}
