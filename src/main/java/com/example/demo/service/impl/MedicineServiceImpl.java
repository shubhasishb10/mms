package com.example.demo.service.impl;

import com.example.demo.dto.SearchCriteria;
import com.example.demo.model.Box;
import com.example.demo.model.Medicine;
import com.example.demo.model.MedicineBoxes;
import com.example.demo.repository.BoxRepository;
import com.example.demo.repository.MedicineBoxRepository;
import com.example.demo.repository.MedicineRepository;
import com.example.demo.service.MedicineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toMap;

@Service
@Transactional
public class MedicineServiceImpl implements MedicineService {

    private final MedicineRepository medicineRepository;
    private BoxRepository boxRepository;

    private MedicineBoxRepository medicineBoxRepository;

    @Autowired
    MedicineServiceImpl(MedicineRepository medicineRepository) {
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
        //return medicineRepository.findMedicineByBoxNumber(boxNumber);
        return null;
    }

    @Override
    public Medicine addMedicine(Medicine medicine) {
        int boxNumber = medicine.getBoxNumber();
        Box theBox = boxRepository.findByNumber(boxNumber);
        if (theBox == null) {
            theBox = createBoxWithNumber(boxNumber);
        }
        Medicine theMedicine = medicineRepository.findMedicineByNameAndCompanyAndVolume(medicine.getName(), medicine.getCompany(), medicine.getVolume());
        if (theMedicine == null) {
            theMedicine = createMedicine(medicine);
        }
        createMedicineBoxEntry(theMedicine, theBox, medicine.getCount());
        return theMedicine;
    }

    private void createMedicineBoxEntry(Medicine theMedicine, Box theBox, int count) {
        MedicineBoxes existingBox = medicineBoxRepository.findMedicineBoxesByMedicineAndBox(theMedicine, theBox);
        if(existingBox == null) {
            existingBox = new MedicineBoxes();
            existingBox.setMedicine(theMedicine);
            existingBox.setBox(theBox);
            existingBox.setMedicineCount(count);
        }else {
            existingBox.setMedicineCount(existingBox.getMedicineCount() + count);
        }
        medicineBoxRepository.save(existingBox);
    }

    private Box createBoxWithNumber(int boxNumber) {
        Box createdBox = new Box();
        createdBox.setNumber(boxNumber);
        return boxRepository.save(createdBox);
    }

    private Medicine createMedicine(Medicine medicine) {
        medicine.setPurchaseDate(Date.from(Instant.now()));
        return medicineRepository.save(medicine);
    }

    @Override
    public void removeMedicine(Medicine medicine) {
        medicineRepository.delete(medicine);
    }

    @Override
    public Medicine sellMedicine(long medicineId) {
        //medicineRepository.sellMedicineOnDate(Date.from(Instant.now()), medicineId);
        return getMedicineById(medicineId);
    }

    @Override
    public List<Medicine> findAllMedicine(int pageNumber, int recordPerPage) {
        PageRequest request = PageRequest.of(pageNumber, recordPerPage);
        List<Medicine> medicines = medicineRepository.findAll(request).getContent();
        return populateBoxesFieldAndTotalCount(medicines);
        //return medicines;
    }

    private List<Medicine> populateBoxesFieldAndTotalCount(List<Medicine> medicines) {
        Map<Medicine, List<Integer>> medicineOnBoxes = medicines.stream()
                .collect(toMap(Function.identity(),  e -> e.getMedicineBoxes().stream().map(m->m.getBox().getNumber()).collect(Collectors.toList())));
        Map<Medicine, Integer> totalMedicineCountMap = medicines.stream()
                .collect(toMap(Function.identity(),  e -> e.getMedicineBoxes().stream().map(MedicineBoxes::getMedicineCount).reduce(Integer::sum).get()));
        medicines.forEach(m->m.setBoxes(medicineOnBoxes.get(m).stream().map(String::valueOf).collect(Collectors.joining(","))));
        medicines.forEach(m->m.setTotalMedicinePresent(totalMedicineCountMap.get(m)));
        return medicines;
    }

    @Override
    public List<Medicine> searchMedicine(String name, String company, int ml) {
        SearchCriteria.SearchCriteriaBuilder builder = SearchCriteria.builder();
        if(!"".equalsIgnoreCase(name))
            builder.withMedicineName(name);
        if(!"".equalsIgnoreCase(company))
            builder.withCompanyName(company);
        if(0 != ml)
            builder.withMedicineVolume(ml);
        return populateBoxesFieldAndTotalCount(medicineRepository.searchMedicineByCriteria(builder.buildDto()));
    }

    @Autowired
    public void setBoxRepository(BoxRepository boxRepository) {
        this.boxRepository = boxRepository;
    }

    @Autowired
    public void setMedicineBoxRepository(MedicineBoxRepository medicineBoxRepository) {
        this.medicineBoxRepository = medicineBoxRepository;
    }
}
