package com.mms.thp.service.impl;

import com.mms.thp.dto.MedicineOrder;
import com.mms.thp.dto.OrderedBox;
import com.mms.thp.dto.SearchCriteria;
import com.mms.thp.model.Box;
import com.mms.thp.model.Company;
import com.mms.thp.model.Medicine;
import com.mms.thp.model.MedicineBoxes;
import com.mms.thp.model.Retail;
import com.mms.thp.model.RetailMedicine;
import com.mms.thp.repository.BoxRepository;
import com.mms.thp.repository.CompanyRepository;
import com.mms.thp.repository.MedicineBoxRepository;
import com.mms.thp.repository.MedicineRepository;
import com.mms.thp.repository.RetailRepository;
import com.mms.thp.service.MedicineService;
import com.mms.thp.utility.StringUtility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.Transient;
import javax.security.auth.login.LoginException;
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

    private static final Logger LOGGER = LoggerFactory.getLogger(MedicineServiceImpl.class);
    private static final String CLASS_TYPE = "SERVICE";

    private static final String DUMMY_CUSTOMER_NAME = "DUMMY_CUSTOMER_NAME";
    private static final String DUMMY_CUSTOMER_ADDRESS = "DUMMY_CUSTOMER_ADDRESS";

    private final MedicineRepository medicineRepository;
    private BoxRepository boxRepository;

    private MedicineBoxRepository medicineBoxRepository;

    private CompanyRepository companyRepository;

    private RetailRepository retailRepository;

    @Autowired
    MedicineServiceImpl(MedicineRepository medicineRepository) {
        this.medicineRepository = medicineRepository;
    }

    @Override
    public Medicine getMedicineById(Long id) {
        LOGGER.info("{}|Start of(getMedicineById)|Params: id={}", CLASS_TYPE, id);
        Medicine medicine = medicineRepository.findById(id).orElse(null);
        LOGGER.info("Fetched the medicine from repository, medicine={}", medicine);
        if(medicine != null && medicine.getMedicineBoxes().size() > 0) {
            LOGGER.info("Got the medicine. medicine in stock, medicineBoxes={}", medicine.getMedicineBoxes());
            medicine.setTotalMedicinePresent(medicine.getMedicineBoxes().stream().map(MedicineBoxes::getMedicineCount).reduce(Integer::sum).get());
        }
        LOGGER.info("{}|End of(getMedicineById)|", CLASS_TYPE);
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
        LOGGER.info("{}|Start of(addMedicine)|Params: medicine={}", CLASS_TYPE, medicine);
        try{
            String boxNumber = medicine.getBoxNumber();
            Box theBox = boxRepository.findByNumber(boxNumber);
            if (theBox == null) {
                LOGGER.info("Box not present creating the box");
                theBox = createBoxWithNumber(boxNumber);
            }else {
                LOGGER.info("Box is present in the db, box={}", theBox);
            }
            LOGGER.info("Got the box, box={}", theBox);
            Medicine theMedicine = medicineRepository.findMedicineByNameAndCompanyStringNameAndVolume(medicine.getName(), medicine.getCompanyStringName(), medicine.getVolume());
            if (theMedicine == null) {
                LOGGER.info("Medicine not in the db, creating the medicine");
                theMedicine = createMedicine(medicine);
            }else {
                // Refreshing the last updated date
                LOGGER.info("Medicine is present in the box, refreshing the purchase date");
                theMedicine.setPurchaseDate(Date.from(Instant.now()));
            }
            LOGGER.info("Got the medicine, medicine={}", theMedicine);
            // Create or update the company with the medicine
            createOrSaveCompanyWithMedicine(medicine.getCompanyStringName(), theMedicine);
            createMedicineBoxEntry(theMedicine, theBox, medicine.getCount());
            LOGGER.info("{}|End of(addMedicine)|", CLASS_TYPE);
            return theMedicine;
        }catch(Exception e) {
            LOGGER.error("{}|Exception|{}", CLASS_TYPE, e);
        }
        return null;
    }

    private void createMedicineBoxEntry(Medicine theMedicine, Box theBox, int addedCount) {
        LOGGER.info("{}|Start of(createMedicineBoxEntry)|Params: theMedicine={}, theBox={}, addedCount={}|", CLASS_TYPE, theMedicine, theBox, addedCount);
        MedicineBoxes existingBox = medicineBoxRepository.findMedicineBoxesByMedicineAndBox(theMedicine, theBox);
        if(existingBox == null) {
            LOGGER.info("MedicineBox entry is not present creating a medicine box entry");
            existingBox = new MedicineBoxes();
            existingBox.setMedicine(theMedicine);
            existingBox.setBox(theBox);
            existingBox.setMedicineCount(addedCount);
        }else {
            LOGGER.info("MedicineBox entry is present, re-calculating the medicine count to=" + existingBox.getMedicineCount()+addedCount);
            existingBox.setMedicineCount(existingBox.getMedicineCount() + addedCount);
        }
        medicineBoxRepository.save(existingBox);
        LOGGER.info("Saved the MedicineBox entry");
        LOGGER.info("{}|End of(createMedicineBoxEntry)|", CLASS_TYPE);
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
        LOGGER.info("{}|Start of(createOrSaveCompanyWithMedicine)|Params: companyName={}, medicine={}", CLASS_TYPE, companyName, medicine);
        Company savedCompany = companyRepository.findByName(companyName);
        if( savedCompany == null) {
            LOGGER.info("Company info not found creating a new company entry");
            savedCompany = new Company();
            savedCompany.setName(companyName);
        } else {
            LOGGER.info("Company is already present in the db");
        }
        LOGGER.info("Found the company={}", savedCompany);
        savedCompany.getMedicines().add(medicine);
        companyRepository.save(savedCompany);
        LOGGER.info("Created the entry for CompanyDetails for the medicine");
        LOGGER.info("{}|End of(createOrSaveCompanyWithMedicine)|", CLASS_TYPE);
    }

    @Override
    public void removeMedicine(Medicine medicine) {
        // Not used
        // Medicine entry is not deletable
        medicineRepository.delete(medicine);
    }

    @Override
    public Medicine sellMedicine(MedicineOrder medicineOrder) {

        LOGGER.info("{}|Start of(sellMedicine)|Params: medicineOrder={}", CLASS_TYPE, medicineOrder);
        long medicineId = medicineOrder.getMedicineId();
        Optional<Medicine> retailMedicineReturn = Optional.empty();
        try{
            Optional<Medicine> retailMedicine = medicineRepository.findById(medicineId);
            if(retailMedicine.isPresent()) {
                retailMedicineReturn = retailMedicine;
                LOGGER.info("Medicine present in the db medicine=" + retailMedicine.get());
                medicineOrder.getBoxes().stream().filter(OrderedBox.MEDICINE_COUNT_NOT_EMPTY).forEach( b -> {
                    LOGGER.info("Selling medicineCount {} from the box {}", b.getMedicineCount(), b.getBoxNumber());
                    Box retailBox = boxRepository.findByNumber(b.getBoxNumber());
                    if(null != retailBox) {
                        LOGGER.info("Box present in the db, theBox={}", retailBox);
                        MedicineBoxes medicineBoxes = medicineBoxRepository.findMedicineBoxesByMedicineAndBox(retailMedicine.get(), retailBox);
                        if(null != medicineBoxes) {
                            LOGGER.info("MedicineBoxes entry is present in the db, medicineBoxes={}", medicineBoxes);
                            if(b.getMedicineCount() > medicineBoxes.getMedicineCount()){
                                LOGGER.error("Requested medicine count is greater than available medicine count");
                            }
                            else if(b.getMedicineCount() == medicineBoxes.getMedicineCount()) {
                                LOGGER.info("Requested medicine count is equal to the medicine present in the box, deleting the medicineBox entry");
                                populateRetailEntity(retailMedicine.get(), b);
                                medicineBoxRepository.delete(medicineBoxes);
                            }else {
                                LOGGER.info("Adjusting the medicine count in the box, new MedicineCount={}", medicineBoxes.getMedicineCount() - b.getMedicineCount());
                                medicineBoxes.setMedicineCount(medicineBoxes.getMedicineCount() - b.getMedicineCount());
                                populateRetailEntity(retailMedicine.get(), b);
                                medicineBoxRepository.save(medicineBoxes);
                            }
                        }else {
                            LOGGER.error("MedicineBoxes entry not present in the db");
                        }
                    }else {
                        LOGGER.error("Box not present with the box number:{}" + b.getBoxNumber());
                    }
                });

            }else {
                LOGGER.error("error in sell medicine, medicine not present with id={}", medicineId);
            }
        }catch(Exception e) {
            LOGGER.error("{}|Exception|{}", CLASS_TYPE, e);
        }
        LOGGER.info("{}|End of(sellMedicine)|", CLASS_TYPE);
        return retailMedicineReturn.get();
    }

    private void populateRetailEntity(Medicine medicine, OrderedBox orderedBox) {
        LOGGER.info("{}|Start of(populateRetailEntity)|Params: medicine={}, orderedBox={}", CLASS_TYPE, medicine, orderedBox);
        try{
            LOGGER.info("Creating retail information");
            Retail retail = new Retail();
            RetailMedicine retailMedicine = new RetailMedicine();
            retailMedicine.setMedicine(medicine);
            retailMedicine.setRetailCount(orderedBox.getMedicineCount());
            retail.getRetailMedicines().add(retailMedicine);
            retail.setCustomerAddress(DUMMY_CUSTOMER_ADDRESS);
            retail.setCustomerName(DUMMY_CUSTOMER_NAME);
            retail.setRetailDate(Date.from(Instant.now()));
            retailRepository.save(retail);
            LOGGER.info("Retail information saved to db");
        }catch(Exception e) {
            LOGGER.error("error in populateRetailEntity: {} {}, exception={}", medicine, orderedBox, e);
        }
        LOGGER.info("{}|End of(populateRetailEntity)|", CLASS_TYPE);
    }

    @Override
    public List<Medicine> findAllMedicine(int pageNumber, int recordPerPage) {
        LOGGER.info("{}|Start of(findAllMedicine)|Params: pageNumber={}, recordPerPage={}", CLASS_TYPE, pageNumber, recordPerPage);
        try{
            PageRequest request = PageRequest.of(pageNumber, recordPerPage);
            List<Medicine> medicines = medicineRepository.findAll(request).getContent();
            LOGGER.info("Got the medicine lis and populating the transient field in the medicine");
            return populateBoxesFieldAndTotalCount(medicines);
        }catch(Exception e) {
            LOGGER.error("{}|Exception|{}", CLASS_TYPE, e);
        }
        LOGGER.info("{}|End of(findAllMedicine)|", CLASS_TYPE);
        return null;
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

        LOGGER.info("{}|Start of(searchMedicine)|Params: name={}, company={}, ml={}", CLASS_TYPE, name, company, ml);
        try{
            LOGGER.info("Creating search criteria");
            SearchCriteria.SearchCriteriaBuilder builder = SearchCriteria.builder();
            if(!"".equalsIgnoreCase(name))
                builder.withMedicineName(StringUtility.normalizeString(name));
            if(!"".equalsIgnoreCase(company))
                builder.withCompanyName(StringUtility.normalizeString(company));
            if(0 != ml)
                builder.withMedicineVolume(ml);
            LOGGER.info("Searching medicine db with search criteria={}", builder.buildDto());
            return populateBoxesFieldAndTotalCount(medicineRepository.searchMedicineByCriteria(builder.buildDto()));
        }catch(Exception e) {
            LOGGER.error("{}|Exception|{}", CLASS_TYPE, e);
        }
        LOGGER.info("{}|End of(searchMedicine)|", CLASS_TYPE);
        return null;
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

    @Autowired
    public void setRetailRepository(RetailRepository retailRepository) {
        this.retailRepository = retailRepository;
    }
}
