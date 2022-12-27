/**
 * Copyright (C) DreamTech Inc - All Rights Reserved Unauthorized copying of this file,
 * via any medium is strictly prohibited Proprietary and confidential For any use
 * of this software please write us to shubhasishb10@gmail.com.
 */

package com.mms.thp.controller.mvc;

import com.mms.thp.model.Medicine;
import com.mms.thp.service.BoxService;
import com.mms.thp.utility.ThpUtility;
import com.mms.thp.utility.WebPages;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/mvc/box")
public class BoxMvcController {

    private static Logger LOGGER = LoggerFactory.getLogger(BoxMvcController.class);

    @Autowired
    private BoxService service;

    @GetMapping("/boxes")
    public String getBoxesList(Model model){
        model.addAttribute("boxes", getAllBoxes());
        model.addAttribute("isSearchResult", false);
        return WebPages.BOX_LIST.toString();
    }
    @GetMapping("/medicines")
    public String getMedicinesInBox(Model model, @RequestParam("selectedBox") String boxNumber, @RequestParam(value = "pageNo", defaultValue = "0") int pageNo) {
        if(pageNo < 0)
            pageNo = 0;
        boxNumber = boxNumber.toLowerCase();
        int totalCount = service.totalMedicineInTheBox(boxNumber);
        List<Medicine> medicines = service.findMedicinesInBox(boxNumber, pageNo, ThpUtility.RECORD_PER_PAGE);
        model.addAttribute("boxes", getAllBoxes());
        model.addAttribute("isSearchResult", true);
        model.addAttribute("medicineList", medicines);
        model.addAttribute("boxNumber", boxNumber.toUpperCase());
        ThpUtility.populateModelForPagination(model, totalCount, pageNo, "/mvc/box/medicines?selectedBox=" + boxNumber + "&", ThpUtility.RECORD_PER_PAGE);
        return WebPages.BOX_LIST.toString();
    }

    private List<String> getAllBoxes(){
        return service.findAllBoxNumbers();
    }
}
