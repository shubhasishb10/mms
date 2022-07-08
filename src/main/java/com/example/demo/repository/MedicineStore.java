package com.example.demo.repository;

import com.example.demo.model.Box;
import com.example.demo.model.Medicine;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class MedicineStore {

    private static final List<Medicine> medicineRepo = new ArrayList<>();
    private static final List<Box> boxRepo = new ArrayList<>();

    // add medicine to medicine repo


}
