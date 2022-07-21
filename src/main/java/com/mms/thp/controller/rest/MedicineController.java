package com.mms.thp.controller.rest;

import com.mms.thp.model.Medicine;
import com.mms.thp.service.MedicineService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/medicine")
public class MedicineController {

    @Autowired
    private MedicineService service;

    private static final Logger logger = LoggerFactory.getLogger(MedicineController.class);

    @GetMapping("/list")
    public ResponseEntity<List<Medicine>> listMedicines(@RequestParam(value = "pageNo", defaultValue = "0") int pageNo, @RequestParam(value = "recordPerPage", defaultValue = "10") int recordPerPage) {
        if(pageNo < 0)
            pageNo = 0;
        if(recordPerPage <= 0)
            recordPerPage = 10;
        return new ResponseEntity<>(service.findAllMedicine(pageNo, recordPerPage), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Medicine> addMedicine(@RequestBody Medicine medicine, BindingResult bindingResult) {
        Medicine addedMedicine = service.addMedicine(medicine);
        return new ResponseEntity<>(addedMedicine, HttpStatus.OK);
    }

    @GetMapping("/{medicineName}")
    public ResponseEntity<List<Medicine>> getMedicinesByName(@PathVariable("medicineName") String medicineName) {
        return new ResponseEntity<>(service.getMedicineByName(medicineName), HttpStatus.OK);
    }
    @GetMapping("/box/{boxNumber}")
    public ResponseEntity<List<Medicine>> getMedicineForBox(@PathVariable("boxNumber") int boxNumber) {
        return new ResponseEntity<>(service.getMedicineInBox(boxNumber), HttpStatus.OK);
    }
}
