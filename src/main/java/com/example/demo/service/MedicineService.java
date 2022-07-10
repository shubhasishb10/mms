package com.example.demo.service;

import com.example.demo.model.Medicine;

import java.util.List;

public interface MedicineService {

    //TODO medicine service logic

    //TODO Get medicine by id
    Medicine getMedicineById(Long id);

    //TODO Get medicine by name
    List<Medicine> getMedicineByName(String name);

    //TODO Get medicine by company
    List<Medicine> getMedicinesByCompany(String companyName);

    //TODO Get medicine in the box
    List<Medicine> getMedicineInBox(int boxNumber);

    //TODO Get medicine across boxes

    //TODO Add medicine
    Medicine addMedicine(Medicine medicine);

    //TODO Remove medicine
    void removeMedicine(Medicine medicine);

    //TODO Sell medicine
    Medicine sellMedicine(long medicineId);

    List<Medicine> findAllMedicine(int pageNumber, int recordPerPage);
}
