package com.mms.thp.repository;

import com.mms.thp.dto.SearchCriteria;
import com.mms.thp.model.Medicine;

import java.util.List;

public interface MedicineExtendedRepository {
    List<Medicine> searchMedicineByCriteria(SearchCriteria criteria);
}
