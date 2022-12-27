/**
 * Copyright (C) DreamTech Inc - All Rights Reserved Unauthorized copying of this file,
 * via any medium is strictly prohibited Proprietary and confidential For any use
 * of this software please write us to shubhasishb10@gmail.com.
 */

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
import com.mms.thp.repository.RetailMedicineRepository;
import com.mms.thp.repository.RetailRepository;
import com.mms.thp.service.MedicineService;
import com.mms.thp.utility.ThpUtility;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.util.StringUtils;

import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toMap;

@Service
@Transactional
public class MedicineServiceImpl implements MedicineService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MedicineServiceImpl.class);
    private static final String CLASS_TYPE = "SERVICE";

    private static final String DUMMY_CUSTOMER_NAME = "N/A";
    private static final String DUMMY_CUSTOMER_ADDRESS = "N/A";

    private final MedicineRepository medicineRepository;
    private BoxRepository boxRepository;

    private MedicineBoxRepository medicineBoxRepository;

    private CompanyRepository companyRepository;

    private RetailRepository retailRepository;
    private RetailMedicineRepository retailMedicineRepository;

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
    public long getTotalMedicineCount(){
        return medicineRepository.count();
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
                medicineRepository.save(theMedicine);
            }
            LOGGER.info("Got the medicine, medicine={}", theMedicine);
            // Create or update the company with the medicine
            createOrSaveCompanyWithMedicine(medicine.getCompanyStringName(), theMedicine);
            if (medicine.getCount() > 0) {
                createMedicineBoxEntry(theMedicine, theBox, medicine.getCount());
            }else {
                LOGGER.info("Medicine count is 0 not inserting in the medicineBox entity");
            }
            LOGGER.info("{}|End of(addMedicine)|", CLASS_TYPE);
            // For updating the
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
                String customerName = StringUtils.isEmpty(medicineOrder.getCustomerName()) ? DUMMY_CUSTOMER_NAME : medicineOrder.getCustomerName();
                String customerAddress = StringUtils.isEmpty(medicineOrder.getCustomerAddress()) ? DUMMY_CUSTOMER_ADDRESS : medicineOrder.getCustomerAddress();
                medicineOrder.getBoxes().stream().filter(OrderedBox.MEDICINE_COUNT_NOT_EMPTY).forEach( b -> {
                    LOGGER.info("Selling medicineCount {} from the box {}", b.getMedicineCount(), b.getBoxNumber());
                    Box retailBox = boxRepository.findByNumber(b.getBoxNumber().toLowerCase());
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
                                if(!medicineOrder.getIsDropRequest())
                                    populateRetailEntity(retailMedicine.get(), b, customerName, customerAddress);
                                medicineBoxRepository.delete(medicineBoxes);
                            }else {
                                LOGGER.info("Adjusting the medicine count in the box, new MedicineCount={}", medicineBoxes.getMedicineCount() - b.getMedicineCount());
                                medicineBoxes.setMedicineCount(medicineBoxes.getMedicineCount() - b.getMedicineCount());
                                if(!medicineOrder.getIsDropRequest())
                                    populateRetailEntity(retailMedicine.get(), b, customerName, customerAddress);
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

    private void populateRetailEntity(Medicine medicine, OrderedBox orderedBox, String customerName, String customerAddress) {
        LOGGER.info("{}|Start of(populateRetailEntity)|Params: medicine={}, orderedBox={}", CLASS_TYPE, medicine, orderedBox);
        try{
            Box retailFromBox = boxRepository.findByNumber(orderedBox.getBoxNumber());
            if(retailFromBox != null) {
                LOGGER.info("Creating retail information");
                Retail retail = new Retail();
                RetailMedicine retailMedicine = new RetailMedicine();
                retailMedicine.setMedicine(medicine);
                retailMedicine.setRetailCount(orderedBox.getMedicineCount());
                retailMedicine.setBox(retailFromBox);
                retail.getRetailMedicines().add(retailMedicine);
                retail.setCustomerAddress(customerAddress);
                retail.setCustomerName(customerName);
                retail.setRetailDate(Date.from(Instant.now()));
                retailRepository.save(retail);
                LOGGER.info("Retail information saved to db");
            }
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
            List<Medicine> medicines = medicineRepository.findAllByOrderByNameAsc(request).getContent();
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
        medicines.stream().filter(m->m.getMedicineBoxes().size() > 0).forEach(m->medicineOnBoxes.get(m).forEach(b->m.getBoxes().add(Medicine.BoxWrapperForHTML.generateWrapper(b))));
        medicines.stream().filter(m->m.getMedicineBoxes().size() > 0).forEach(m->m.setTotalMedicinePresent(totalMedicineCountMap.get(m)));
        return medicines;
    }

    public List<Medicine> populateBoxesFieldAndTotalCountApi(List<Medicine> medicines) {
        return populateBoxesFieldAndTotalCount(medicines);
    }

    @Override
    public void changeMedicineName(final long oldMedicineId, final String newMedicineName) {
        LOGGER.info("{}|Start of(changeMedicineName)|Params: oldMedicineId={}, newMedicineName={}", CLASS_TYPE, oldMedicineId, newMedicineName);
        Optional<Medicine> oldMedicine = medicineRepository.findById(oldMedicineId);
        List<Medicine> medicineForChangeRetailId = new ArrayList<>(1);
        if(oldMedicine.isPresent()) {
            Medicine oldMedicineTemp = oldMedicine.get();
            LOGGER.info("Got the medicine with the id = {}, medicine={}", oldMedicineTemp.getMedicineId(), oldMedicineTemp.getDisplayName());
            oldMedicineTemp.getMedicineBoxes().forEach(medicineBox -> {
                Medicine newMedicine = new Medicine();
                newMedicine.setDisplayName(newMedicineName);
                newMedicine.setCompanyDisplayName(oldMedicineTemp.getCompanyDisplayName());
                newMedicine.setBoxNumber(medicineBox.getBox().getNumber());
                newMedicine.setVolume(oldMedicineTemp.getVolume());
                newMedicine.setPrice(oldMedicineTemp.getPrice());
                newMedicine.setCount(medicineBox.getMedicineCount());
                medicineBoxRepository.deleteMedicineBoxesById(medicineBox.getMedicineBoxId());
                Medicine tempMedicine = addMedicine(newMedicine);
                if(medicineForChangeRetailId.size() <= 0) {
                    medicineForChangeRetailId.add(tempMedicine);
                }
            });
            LOGGER.info("Changed the medicineName of {} with id={} to new medicine name={} of id={}", oldMedicineTemp.getDisplayName(),
                    oldMedicineId,  medicineForChangeRetailId.isEmpty() ? null : medicineForChangeRetailId.get(0).getDisplayName(),
                    medicineForChangeRetailId.isEmpty() ? null : medicineForChangeRetailId.get(0).getMedicineId());
            medicineRepository.deleteMedicineById(oldMedicineId);
            LOGGER.info("Updating the retail_medicine table to update the deleted medicine id from id={} to new id={}",
                    oldMedicineId, medicineForChangeRetailId.isEmpty() ? null : medicineForChangeRetailId.get(0).getMedicineId());
            retailMedicineRepository.updateMedicineIdOfDeletedMedicine(medicineForChangeRetailId.get(0).getMedicineId(), oldMedicineId);
        }
        LOGGER.info("{}|End of(changeMedicineName)|", CLASS_TYPE);
    }

    @Override
    public List<Medicine> searchMedicine(String name, String company, int ml) {

        LOGGER.info("{}|Start of(searchMedicine)|Params: name={}, company={}, ml={}", CLASS_TYPE, name, company, ml);
        try{
            LOGGER.info("Creating search criteria");
            SearchCriteria.SearchCriteriaBuilder builder = SearchCriteria.builder();
            if(!"".equalsIgnoreCase(name))
                builder.withMedicineName(name);
            if(!"".equalsIgnoreCase(company))
                builder.withCompanyName(company);
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

    @Override
    public void saveFile(MultipartFile file) {
        // save the file in a temp location
        // call loadFile with the filepath parameter
        XSSFWorkbook workbook = null;
        try {
            workbook = new XSSFWorkbook(file.getInputStream());
            XSSFSheet sheet = workbook.getSheetAt(0);
            if(sheet != null) {
                Iterator<Row> rowIterator = sheet.rowIterator();
                // Skipping the header row
                rowIterator.next();
                while (rowIterator.hasNext()) {
                    Row row = rowIterator.next();
                    addMedicine(prepareMedicineObject(row));
                }
            }
        } catch (Exception e) {
            System.out.println("Error: " + e);
        }finally {
            if(workbook != null) {
                try {
                    workbook.close();
                } catch (IOException ignore) {
                    ignore.printStackTrace();
                }
            }
        }
        System.out.println("Testing");
    }

    private Medicine prepareMedicineObject(Row row) {

        Cell nameCell = row.getCell(ThpUtility.MedicineColumnIndex.NAME.ordinal());
        Cell companyCell = row.getCell(ThpUtility.MedicineColumnIndex.COMPANY.ordinal());
        Cell priceCell = row.getCell(ThpUtility.MedicineColumnIndex.PRICE.ordinal());
        Cell countCell = row.getCell(ThpUtility.MedicineColumnIndex.COUNT.ordinal());
        Cell mlCell = row.getCell(ThpUtility.MedicineColumnIndex.ML.ordinal());
        Cell boxNumberCell = row.getCell(ThpUtility.MedicineColumnIndex.BOX_NUMBER.ordinal());
        nameCell.setCellType(CellType.STRING);
        companyCell.setCellType(CellType.STRING);
        priceCell.setCellType(CellType.STRING);
        countCell.setCellType(CellType.STRING);
        mlCell.setCellType(CellType.STRING);
        boxNumberCell.setCellType(CellType.STRING);
        String name = nameCell.getStringCellValue();
        String company = companyCell.getStringCellValue();
        double price = Double.parseDouble(priceCell.getStringCellValue());
        int count = Integer.parseInt(countCell.getStringCellValue());
        int ml = Integer.parseInt(mlCell.getStringCellValue());
        String boxNumber = boxNumberCell.getStringCellValue();

        Medicine theMedicine = new Medicine();
        theMedicine.setDisplayName(name);
        theMedicine.setCompanyDisplayName(company);
        theMedicine.setPrice(price);
        theMedicine.setCount(count);
        theMedicine.setVolume(ml);
        theMedicine.setBoxNumber(boxNumber);

        return theMedicine;
    }

    @Override
    public Map<String, List<Medicine>> getMedicineListForCensus(){
        Map<String, List<Medicine>> medicineMapLetterWise = new HashMap<>();
        try {
            List<Medicine> medicines = medicineRepository.findAllByOrderByNameAsc();
            populateBoxesFieldAndTotalCount(medicines);
            medicineMapLetterWise = medicines.stream().collect(groupingBy(v->String.valueOf(v.getName().charAt(0)).toUpperCase()));

        }catch(Exception e) {
            LOGGER.error("{}|Exception|{}", CLASS_TYPE, e);
        }
        return medicineMapLetterWise;
    }

    /*public long getTotalMotherMedicineCount(){
        return medicineRepository.findMedicineByNameEndsWithOrderByNameAsc("mother").size();
    }*/

    @Override
    public List<Medicine> findAllMotherMedicineByFirstLetter(String firstLetter) {
        List<Medicine> medicineList = medicineRepository.findMedicineByNameEndsWithAndNameStartsWithOrderByNameAsc("mother", firstLetter);
        return populateBoxesFieldAndTotalCount(medicineList);
    }

    @Override
    public List<String> getAllMotherMedicineFirstLetter(){
        return medicineRepository.findMedicineByNameEndsWithOrderByNameAsc("mother").stream().map(m->String.valueOf(m.getDisplayName().charAt(0))).map(String::toUpperCase).distinct().collect(Collectors.toList());
    }

    @Override
    public List<String> getAllMedicineFirstLetter(){
        return medicineRepository.findAllByOrderByNameAsc().stream().map( m-> String.valueOf(m.getName().charAt(0))).distinct().map(String::toUpperCase).collect(Collectors.toList());
    }

    @Override
    public List<Medicine> getMedicineByFirstLetter(String letter) {
        return populateBoxesFieldAndTotalCount(medicineRepository.findMedicinesByFirstLetter(ThpUtility.normalizeString(letter)));
    }

    @Override
    public List<String> findAvailableMedicinesVolumes(){
        return medicineRepository.findAvailableMedicinesVolumes();
    }

    @Override
    public int totalMedicineCountOfVolume(int volume) {
        return medicineRepository.findMedicineCountOfVolume(volume);
    }
    @Override
    public List<Medicine> findAllMedicineOfVolume(int volume, int pageNo, int recordPerPage) {
        Pageable pageRequest = PageRequest.of(pageNo, recordPerPage);
        return populateBoxesFieldAndTotalCount(medicineRepository.findAllMedicineOfVolume(volume, pageRequest).getContent());
    }

    @Override
    public void loadMedicineRecordFromFile(String fileName) {
        // Load the file
        // read the rows and insert in db
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
    @Autowired
    public void setRetailMedicineRepository(RetailMedicineRepository retailMedicineRepository) {
        this.retailMedicineRepository = retailMedicineRepository;
    }
}
