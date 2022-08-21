package com.mms.thp.service;

import com.mms.thp.dto.MedicineOrder;
import com.mms.thp.model.Medicine;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

public interface MedicineService {

    //TODO medicine service logic

    //TODO Get medicine by id
    Medicine getMedicineById(Long id);

    //TODO Get medicine by name
    List<Medicine> getMedicineByName(String name);

    //TODO Get medicine by company
    List<Medicine> getMedicinesByCompanyStringName(String companyName);

    //TODO Get medicine in the box
    List<Medicine> getMedicineInBox(int boxNumber);

    //TODO Get medicine across boxes

    //TODO Add medicine
    Medicine addMedicine(Medicine medicine);

    //TODO Remove medicine
    void removeMedicine(Medicine medicine);

    //TODO Sell medicine
    Medicine sellMedicine(MedicineOrder medicineOrder);

    List<Medicine> findAllMedicine(int pageNumber, int recordPerPage);

    void changeMedicineName(long oldMedicineId, String newMedicineName);

    List<Medicine> searchMedicine(String name, String company, int ml);

    long getTotalMedicineCount();

    void saveFile(MultipartFile file);

    Map<String, List<Medicine>> getMedicineListForCensus();

    List<Medicine> findAllMotherMedicineByFirstLetter(String firstLetter);

    List<String> getAllMotherMedicineFirstLetter();

    List<String> getAllMedicineFirstLetter();

    List<Medicine> getMedicineByFirstLetter(String letter);

    List<String> findAvailableMedicinesVolumes();

    int totalMedicineCountOfVolume(int volume);

    List<Medicine> findAllMedicineOfVolume(int volume, int pageNo, int recordPerPage);

    void loadMedicineRecordFromFile(String fileName);

    List<Medicine> populateBoxesFieldAndTotalCountApi(List<Medicine> medicines);
}
