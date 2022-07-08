package com.example.demo.controller;

import com.example.demo.model.Medicine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.OkHttp3ClientHttpRequestFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/medicine")
public class MedicineController {

    private static final Logger logger = LoggerFactory.getLogger(MedicineController.class);

    public ResponseEntity<List<Medicine>> listMedicines(int pageNo, int recordPerPage) {
        return new ResponseEntity<>( null,HttpStatus.OK);
    }
}
