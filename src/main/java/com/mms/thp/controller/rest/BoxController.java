/**
 * Copyright (C) DreamTech Inc - All Rights Reserved Unauthorized copying of this file,
 * via any medium is strictly prohibited Proprietary and confidential For any use
 * of this software please write us to shubhasishb10@gmail.com.
 */

package com.mms.thp.controller.rest;

import com.mms.thp.model.Box;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/box")
public class BoxController {
    //TODO Implement it for later

    private static final Logger logger = LoggerFactory.getLogger(BoxController.class);

    public ResponseEntity<List<Box>> listBoxes(int pageNo, int recordPerPage) {
        return new ResponseEntity<>( null,HttpStatus.OK);
    }
}
