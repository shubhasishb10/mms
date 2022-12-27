/**
 * Copyright (C) DreamTech Inc - All Rights Reserved Unauthorized copying of this file,
 * via any medium is strictly prohibited Proprietary and confidential For any use
 * of this software please write us to shubhasishb10@gmail.com.
 */

package com.mms.thp.service.impl;

import com.mms.thp.model.Box;
import com.mms.thp.model.Medicine;
import com.mms.thp.model.MedicineBoxes;
import com.mms.thp.repository.BoxRepository;
import com.mms.thp.repository.MedicineBoxRepository;
import com.mms.thp.service.BoxService;
import com.mms.thp.service.MedicineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toMap;

@Service
@Transactional
public class BoxServiceImpl implements BoxService {

    @Autowired
    private BoxRepository boxRepository;
    @Autowired
    private MedicineBoxRepository medicineBoxRepository;

    @Autowired
    private MedicineService medicineService;

    @Override
    public Box findBoxByNumber(String number) {
        return boxRepository.findByNumber(number);
    }

    @Override
    public List<String> findAllBoxNumbers(){
        List<Box> allBoxes = boxRepository.findAll();
        return allBoxes.stream().map(Box::getNumber).map(String::toUpperCase).collect(Collectors.toList());
    }

    @Override
    public List<Medicine> findMedicinesInBox(String boxNumber, int pageNo, int recordPerPage) {
        Box box = boxRepository.findByNumber(boxNumber);
        PageRequest pageRequest = PageRequest.of(pageNo, recordPerPage);
        List<MedicineBoxes> medicineBoxes = medicineBoxRepository.findByBox(box, pageRequest);
        return populateMedicineTotalCountInTheBox(medicineBoxes.stream().map(MedicineBoxes::getMedicine).collect(Collectors.toList()), boxNumber);
    }

    private List<Medicine> populateMedicineTotalCountInTheBox(List<Medicine> medicines, String boxNumber) {
        Map<Medicine, List<String>> medicineOnBoxes = medicines.stream().filter(m->m.getMedicineBoxes().size() > 0)
                .collect(toMap(Function.identity(), e -> e.getMedicineBoxes().stream().map(m->m.getBox().getNumber()).collect(Collectors.toList())));
        Map<Medicine, Integer> totalMedicineCountMap = medicines.stream().filter(m->m.getMedicineBoxes().size() > 0)
                .collect(toMap(Function.identity(),  e -> e.getMedicineBoxes().stream().filter(el->el.getBox().getNumber().equalsIgnoreCase(boxNumber)).map(MedicineBoxes::getMedicineCount).reduce(Integer::sum).get()));
        medicines.stream().filter(m->m.getMedicineBoxes().size() > 0).forEach(m->m.setBoxes(medicineOnBoxes.get(m).stream().map(String::valueOf).collect(Collectors.joining(","))));
        medicines.stream().filter(m->m.getMedicineBoxes().size() > 0).forEach(m->m.setMedicineInTheBox(totalMedicineCountMap.get(m)));
        return medicineService.populateBoxesFieldAndTotalCountApi(medicines);
    }

    @Override
    public int totalMedicineInTheBox(String boxNumber) {
        Box box = boxRepository.findByNumber(boxNumber);
        List<MedicineBoxes> entries = medicineBoxRepository.findByBox(box);
        return entries.size();
    }
}
