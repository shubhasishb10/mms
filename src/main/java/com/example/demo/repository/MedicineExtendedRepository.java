package com.example.demo.repository;

import com.example.demo.dto.SearchCriteria;
import com.example.demo.model.Medicine;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface MedicineExtendedRepository {
    List<Medicine> searchMedicineByCriteria(SearchCriteria criteria);
}
