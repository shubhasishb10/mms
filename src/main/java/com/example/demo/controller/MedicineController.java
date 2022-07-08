package com.example.demo.controller;

import com.example.demo.model.Medicine;
import com.example.demo.repository.MedicineStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/medicine")
public class MedicineController {

    @Autowired
    private MedicineStore store;

    private static final Logger logger = LoggerFactory.getLogger(MedicineController.class);

    @GetMapping("/list")
    public ResponseEntity<List<Medicine>> listMedicines(int pageNo, int recordPerPage) {
        //TODO get all medicines
        return null;
    }
    @PostMapping("/{boxNo}")
    public ResponseEntity<Medicine> addMedicine(@RequestBody Medicine medicine, @PathVariable String boxNo, BindingResult bindingResult) {
        //TODO call service to add medicine
        return null;
    }
}
