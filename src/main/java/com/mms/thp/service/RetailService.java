package com.mms.thp.service;

import com.mms.thp.model.Retail;

import java.util.List;

public interface RetailService {
    //TODO implement the paging to fetch all the retail record
    List<Retail> findAllRetailRecord();
}
