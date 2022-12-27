/**
 * Copyright (C) DreamTech Inc - All Rights Reserved Unauthorized copying of this file,
 * via any medium is strictly prohibited Proprietary and confidential For any use
 * of this software please write us to shubhasishb10@gmail.com.
 */

package com.mms.thp.controller.mvc;

import com.mms.thp.dto.RetailDto;
import com.mms.thp.service.RetailService;
import com.mms.thp.utility.ThpUtility;
import com.mms.thp.utility.WebPages;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/mvc/retail")
public class RetailMvcController {

    @Autowired
    private RetailService service;

    @GetMapping("/sell_report")
    public String loadSellReportPage(Model model, @RequestParam(value = "pageNo", defaultValue = "0") int pageNo) {
        Map<Date, List<RetailDto>> retailMap = service.findRetailMapByDate(pageNo, ThpUtility.RECORD_PER_PAGE_SELL_REPORT);
        model.addAttribute("retailEntry", retailMap);
        model.addAttribute("isSearchResult", false);
        long totalEntries = service.totalRetailEntriesInBook();
        ThpUtility.populateModelForPagination(model, totalEntries, pageNo, "/mvc/retail/sell_report?", ThpUtility.RECORD_PER_PAGE_SELL_REPORT);
        return WebPages.SELL_REPORT.toString();
    }
}
