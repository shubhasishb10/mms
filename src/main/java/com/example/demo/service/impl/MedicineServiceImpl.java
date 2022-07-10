package com.example.demo.service.impl;

import com.example.demo.model.Medicine;
import com.example.demo.repository.MedicineRepository;
import com.example.demo.service.MedicineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class MedicineServiceImpl implements MedicineService {

    private final MedicineRepository medicineRepository;

    @Autowired
    MedicineServiceImpl(MedicineRepository medicineRepository){
        this.medicineRepository = medicineRepository;
    }

    @Override
    public Medicine getMedicineById(Long id) {
        return medicineRepository.findById(id).orElse(null);
    }

    @Override
    public List<Medicine> getMedicineByName(String name) {
        return medicineRepository.findMedicineByNameStartingWith(name);
    }

    @Override
    public List<Medicine> getMedicinesByCompany(String companyName) {
        return medicineRepository.findMedicineByCompany(companyName);
    }

    @Override
    public List<Medicine> getMedicineInBox(int boxNumber) {
        return medicineRepository.findMedicineByBoxNumber(boxNumber);
    }

    @Override
    public Medicine addMedicine(Medicine medicine) {
        medicine.setPurchaseDate(Date.from(Instant.now()));
        return medicineRepository.save(medicine);
    }

    @Override
    public void removeMedicine(Medicine medicine) {
        medicineRepository.delete(medicine);
    }

    @Override
    public Medicine sellMedicine(long medicineId) {
        medicineRepository.sellMedicineOnDate(Date.from(Instant.now()), medicineId);
        return getMedicineById(medicineId);
    }

    @Override
    public List<Medicine> findAllMedicine(int pageNumber, int recordPerPage) {
        PageRequest request = PageRequest.of(pageNumber, recordPerPage);
        List<Medicine> medicines = medicineRepository.findAll(request).getContent();
       /* medicines.stream()
                .forEach(m-> );*/
        return null;
    }
}
