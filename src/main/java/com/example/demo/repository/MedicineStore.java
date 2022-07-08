package com.example.demo.repository;

import com.example.demo.model.Box;
import com.example.demo.model.Medicine;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class MedicineStore {

    private static final List<Medicine> medicineRepo = new ArrayList<>();
    private static final List<Box> boxRepo = new ArrayList<>();

    // add medicine to medicine repo
    public Medicine addMedicine(Medicine medicine) {
        medicineRepo.add(medicine);
        return medicine;
    }
    public Medicine removeMedicine(Medicine medicine) {
        if(isMedicineExists(medicine)) {
            medicineRepo.remove(medicine);
        }
        return medicine;
    }
    public Box addBox(Box box) {
        boxRepo.add(box);
        return box;
    }
    public Box removeBox(Box box) {
        if(isBoxExists(box)){
            boxRepo.remove(box);
        }
        return box;
    }

    public List<Medicine> getMedicineInBox(Box box){
        return medicineRepo.stream().filter(m->m.getBoxes().stream()
                .allMatch(b->b.getBoxId().equals(box.getBoxId())))
                .collect(Collectors.toList());
    }
    public List<Box> getBoxesForMedicine(Medicine medicine) {
        return boxRepo.stream().filter(b->b.getMedicinesInBox().stream()
                .allMatch(m->m.getMedicineId().equals(medicine.getMedicineId())))
                .collect(Collectors.toList());
    }
    public Box getBoxByNumber(String number) {
        return boxRepo.stream().filter(b->b.getNumber().equalsIgnoreCase(number)).findFirst().get();
    }
    public List<Medicine> getMedicineByName(String name) {
        return medicineRepo.stream().filter(m->m.getName().equalsIgnoreCase(name)).collect(Collectors.toList());
    }

    //Check for entity existence
    private boolean isMedicineExists(Medicine medicine){
        return medicineRepo.contains(medicine);
    }
    private boolean isBoxExists(Box box) {
        return boxRepo.contains(box);
    }
}
