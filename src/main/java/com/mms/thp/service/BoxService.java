package com.mms.thp.service;

import com.mms.thp.model.Box;
import com.mms.thp.model.Medicine;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface BoxService {

    Box findBoxByNumber(String number);

    List<String> findAllBoxNumbers();
    List<Medicine> findMedicinesInBox(String boxNumber, int pageNo, int recordPerPage);

    int totalMedicineInTheBox(String boxNumber);
}
