package com.example.demo.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Box {

    private String number;
    private String location;
    private BoxSize size;
    private String name;
    private List<Medicine> medicines;

    public Box(String number, String location, BoxSize size, String name){
        medicines = new ArrayList<>();
        this.number = number;
        this.location = location;
        this.size = size;

    }

    public List<Medicine> addMedicine(Medicine medicine){
        Optional<Medicine> addMedicine = Optional.ofNullable(medicine);
        addMedicine.ifPresent(value -> this.medicines.add(value));
        return medicines;
    }


    public enum BoxSize {
        SMALL,
        MEDIUM,
        LARGE
    }
}

class ParamTest {

    public static void main(String[] args) {
        new ParamTest().test("Hello");
    }
    public void test(String str, String... str1) {

    }
}
