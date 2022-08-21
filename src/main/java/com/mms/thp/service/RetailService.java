package com.mms.thp.service;

import com.mms.thp.dto.RetailDto;
import com.mms.thp.model.Retail;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface RetailService {
    //TODO implement the paging to fetch all the retail record
    List<Retail> findAllRetailRecord();
    Map<Date, List<RetailDto>> findRetailMapByDate(int pageNo, int recordPerPage);

    long totalRetailEntriesInBook();
}
