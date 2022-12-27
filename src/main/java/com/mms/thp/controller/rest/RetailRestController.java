/**
 * Copyright (C) DreamTech Inc - All Rights Reserved Unauthorized copying of this file,
 * via any medium is strictly prohibited Proprietary and confidential For any use
 * of this software please write us to shubhasishb10@gmail.com.
 */

package com.mms.thp.controller.rest;

import com.mms.thp.model.Retail;
import com.mms.thp.service.RetailService;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/retail")
public class RetailRestController {

    @Autowired
    private RetailService service;

    //TODO Implement the pagination later
    @GetMapping("/list")
    public ResponseEntity<List<Retail>> getAllRetailRecord(){
        return new ResponseEntity<>(service.findAllRetailRecord(), HttpStatus.OK);
    }
}
