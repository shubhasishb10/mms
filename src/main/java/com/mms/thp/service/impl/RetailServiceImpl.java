/**
 * Copyright (C) DreamTech Inc - All Rights Reserved Unauthorized copying of this file,
 * via any medium is strictly prohibited Proprietary and confidential For any use
 * of this software please write us to shubhasishb10@gmail.com.
 */

package com.mms.thp.service.impl;

import com.mms.thp.dto.RetailDto;
import com.mms.thp.model.Box;
import com.mms.thp.model.Medicine;
import com.mms.thp.model.Retail;
import com.mms.thp.repository.MedicineRepository;
import com.mms.thp.repository.RetailRepository;
import com.mms.thp.service.RetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
public class RetailServiceImpl implements RetailService {

    private final RetailRepository retailRepository;

    @Autowired
    private MedicineRepository medicineRepository;

    @Autowired
    RetailServiceImpl(RetailRepository retailRepository){this.retailRepository = retailRepository;}

    @Override
    public List<Retail> findAllRetailRecord() {
        return retailRepository.findAll();
    }

    @Override
    public Map<Date, List<RetailDto>> findRetailMapByDate(int pageNo, int recordPerPage){
        Pageable pageRequest = PageRequest.of(pageNo, recordPerPage);
        List<Retail> allRetails = new ArrayList<>();
        List<Retail> retails = retailRepository.findAllByOrderByRetailDateDesc(pageRequest).getContent();
        retails.forEach(r->allRetails.addAll(retailRepository.findRetailByRetailDate(r.getRetailDate())));
        return allRetails
                .stream()
                .collect(Collectors.toMap(Retail::getRetailDate, r-> r.getRetailMedicines()
                                        .stream().map(rm -> {
                                            RetailDto retailDto = new RetailDto();
                                            Medicine medicine = rm.getMedicine();
                                            retailDto.setMedicineName(medicine.getDisplayName());
                                            retailDto.setMedicineCompany(medicine.getCompanyDisplayName());
                                            retailDto.setMedicineVolume(medicine.getVolume());
                                            retailDto.setCustomerName(r.getCustomerName());
                                            retailDto.setCustomerAddress(r.getCustomerAddress());
                                            retailDto.setMedicineCount(rm.getRetailCount());
                                            Box theBox = rm.getBox();
                                            String boxNumber = "N/A";
                                            if(theBox != null){
                                                boxNumber = theBox.getNumber();
                                            }
                                            retailDto.setBoxNumber(boxNumber);
                                            return retailDto;
                                            }).collect(Collectors.toList()), (a, b) -> {a.addAll(b); return a;},
                                            LinkedHashMap::new
                                        )
                        );
    }

    @Override
    public long totalRetailEntriesInBook(){
        return retailRepository.findAllRecordGroupByDate();
    }
}
