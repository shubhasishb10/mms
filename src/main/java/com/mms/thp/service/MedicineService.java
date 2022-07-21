package com.mms.thp.service;

import com.mms.thp.dto.MedicineOrder;
import com.mms.thp.model.Medicine;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

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

    List<Medicine> searchMedicine(String name, String company, int ml);

    long getTotalMedicineCount();

    void saveFile(MultipartFile file);
    void loadMedicineRecordFromFile(String fileName);

    List<Medicine> populateBoxesFieldAndTotalCountApi(List<Medicine> medicines);
}
