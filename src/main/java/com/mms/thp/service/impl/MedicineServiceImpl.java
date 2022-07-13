package com.mms.thp.service.impl;

import com.mms.thp.dto.MedicineOrder;
import com.mms.thp.dto.OrderedBox;
import com.mms.thp.dto.SearchCriteria;
import com.mms.thp.model.Box;
import com.mms.thp.model.Company;
import com.mms.thp.model.Medicine;
import com.mms.thp.model.MedicineBoxes;
import com.mms.thp.repository.BoxRepository;
import com.mms.thp.repository.CompanyRepository;
import com.mms.thp.repository.MedicineBoxRepository;
import com.mms.thp.repository.MedicineRepository;
import com.mms.thp.service.MedicineService;
import com.mms.thp.utility.StringUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toMap;

@Service
@Transactional
public class MedicineServiceImpl implements MedicineService {

    private final MedicineRepository medicineRepository;
    private BoxRepository boxRepository;

    private MedicineBoxRepository medicineBoxRepository;

    private CompanyRepository companyRepository;

    @Autowired
    MedicineServiceImpl(MedicineRepository medicineRepository) {
        this.medicineRepository = medicineRepository;
    }

    @Override
    public Medicine getMedicineById(Long id) {
        Medicine medicine = medicineRepository.findById(id).orElse(null);
        assert medicine != null;
        if(medicine.getMedicineBoxes().size() > 0)
            medicine.setTotalMedicinePresent(medicine.getMedicineBoxes().stream().map(MedicineBoxes::getMedicineCount).reduce(Integer::sum).get());
        return medicine;
    }

    @Override
    public List<Medicine> getMedicineByName(String name) {
        return medicineRepository.findMedicineByNameStartingWith(name);
    }

    @Override
    public List<Medicine> getMedicinesByCompanyStringName(String companyName) {
        return medicineRepository.findMedicineByCompanyStringName(companyName);
    }

    @Override
    public List<Medicine> getMedicineInBox(int boxNumber) {
        //return medicineRepository.findMedicineByBoxNumber(boxNumber);
        return null;
    }

    @Override
    public Medicine addMedicine(Medicine medicine) {
        String boxNumber = medicine.getBoxNumber();
        Box theBox = boxRepository.findByNumber(boxNumber);
        if (theBox == null) {
            theBox = createBoxWithNumber(boxNumber);
        }
        Medicine theMedicine = medicineRepository.findMedicineByNameAndCompanyStringNameAndVolume(medicine.getName(), medicine.getCompanyStringName(), medicine.getVolume());
        if (theMedicine == null) {
            theMedicine = createMedicine(medicine);
        }else {
            // Refreshing the last updated date
            theMedicine.setPurchaseDate(Date.from(Instant.now()));
        }
        // Create or update the company with the medicine
        createOrSaveCompanyWithMedicine(medicine.getCompanyStringName(), theMedicine);
        createMedicineBoxEntry(theMedicine, theBox, medicine.getCount());
        return theMedicine;
    }

    private void createMedicineBoxEntry(Medicine theMedicine, Box theBox, int addedCount) {
        MedicineBoxes existingBox = medicineBoxRepository.findMedicineBoxesByMedicineAndBox(theMedicine, theBox);
        if(existingBox == null) {
            existingBox = new MedicineBoxes();
            existingBox.setMedicine(theMedicine);
            existingBox.setBox(theBox);
            existingBox.setMedicineCount(addedCount);
        }else {
            existingBox.setMedicineCount(existingBox.getMedicineCount() + addedCount);
        }
        medicineBoxRepository.save(existingBox);
    }

    private Box createBoxWithNumber(String boxNumber) {
        Box createdBox = new Box();
        createdBox.setNumber(boxNumber);
        return boxRepository.save(createdBox);
    }

    private Medicine createMedicine(Medicine medicine) {
        medicine.setPurchaseDate(Date.from(Instant.now()));
        return medicineRepository.save(medicine);
    }

    private void createOrSaveCompanyWithMedicine(String companyName, Medicine medicine) {
        Company savedCompany = companyRepository.findByName(companyName);
        if( savedCompany == null) {
            savedCompany = new Company();
            savedCompany.setName(companyName);
        }
        savedCompany.getMedicines().add(medicine);
        companyRepository.save(savedCompany);
    }

    @Override
    public void removeMedicine(Medicine medicine) {
        medicineRepository.delete(medicine);
    }

    @Override
    public Medicine sellMedicine(MedicineOrder medicineOrder) {
        //medicineRepository.sellMedicineOnDate(Date.from(Instant.now()), medicineId);
        long medicineId = medicineOrder.getMedicineId();
        try{
            Optional<Medicine> retailMedicine = medicineRepository.findById(medicineId);
            //List<String> retailFromBoxes = medicineOrder.getBoxes().stream().map(OrderedBox::getBoxNumber).collect(Collectors.toList());
            if(retailMedicine.isPresent()) {
                medicineOrder.getBoxes().stream().filter(OrderedBox.MEDICINE_COUNT_NOT_EMPTY).forEach( b -> {
                    Box retailBox = boxRepository.findByNumber(b.getBoxNumber());
                    MedicineBoxes medicineBoxes = medicineBoxRepository.findMedicineBoxesByMedicineAndBox(retailMedicine.get(), retailBox);
                    if(b.getMedicineCount() > medicineBoxes.getMedicineCount()){
                        throw new RuntimeException("Medicine count is not sufficient");
                    }
                    else if(b.getMedicineCount() == medicineBoxes.getMedicineCount()) {
                        medicineBoxRepository.delete(medicineBoxes);
                    }else {
                        medicineBoxes.setMedicineCount(medicineBoxes.getMedicineCount() - b.getMedicineCount());
                        medicineBoxRepository.save(medicineBoxes);
                    }
                });

            }else {
                // Medicine not present in the db can not sell it
            }
        }catch(RuntimeException e) {

        }
        return new Medicine();
    }

    @Override
    public List<Medicine> findAllMedicine(int pageNumber, int recordPerPage) {
        PageRequest request = PageRequest.of(pageNumber, recordPerPage);
        List<Medicine> medicines = medicineRepository.findAll(request).getContent();
        return populateBoxesFieldAndTotalCount(medicines);
        //return medicines;
    }

    private List<Medicine> populateBoxesFieldAndTotalCount(List<Medicine> medicines) {
        Map<Medicine, List<String>> medicineOnBoxes = medicines.stream().filter(m->m.getMedicineBoxes().size() > 0)
                .collect(toMap(Function.identity(),  e -> e.getMedicineBoxes().stream().map(m->m.getBox().getNumber()).collect(Collectors.toList())));
        Map<Medicine, Integer> totalMedicineCountMap = medicines.stream().filter(m->m.getMedicineBoxes().size() > 0)
                .collect(toMap(Function.identity(),  e -> e.getMedicineBoxes().stream().map(MedicineBoxes::getMedicineCount).reduce(Integer::sum).get()));
        medicines.stream().filter(m->m.getMedicineBoxes().size() > 0).forEach(m->m.setBoxes(medicineOnBoxes.get(m).stream().map(String::valueOf).collect(Collectors.joining(","))));
        medicines.stream().filter(m->m.getMedicineBoxes().size() > 0).forEach(m->m.setTotalMedicinePresent(totalMedicineCountMap.get(m)));
        return medicines;
    }

    @Override
    public List<Medicine> searchMedicine(String name, String company, int ml) {
        SearchCriteria.SearchCriteriaBuilder builder = SearchCriteria.builder();
        if(!"".equalsIgnoreCase(name))
            builder.withMedicineName(StringUtility.normalizeString(name));
        if(!"".equalsIgnoreCase(company))
            builder.withCompanyName(StringUtility.normalizeString(company));
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

    @Autowired
    public void setCompanyRepository(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }
}
