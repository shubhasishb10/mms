/**
 * Copyright (C) DreamTech Inc - All Rights Reserved Unauthorized copying of this file,
 * via any medium is strictly prohibited Proprietary and confidential For any use
 * of this software please write us to shubhasishb10@gmail.com.
 */

package com.mms.thp.controller.mvc;

import com.google.gson.Gson;
import com.mms.thp.dto.MedicineOrder;
import com.mms.thp.model.Medicine;
import com.mms.thp.service.MedicineService;
import com.mms.thp.utility.ThpUtility;
import com.mms.thp.utility.WebPages;
import java.util.List;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.util.StringUtils;

@Controller
@RequestMapping({ "/mvc/medicine", "/" })
public class MedicineMvcController {

    private static final Logger LOGGER = LoggerFactory.getLogger(
        MedicineMvcController.class
    );
    private static final String CLASS_TYPE = "CONTROLLER";

    @Autowired
    private MedicineService service;

    @GetMapping
    public String handleRootPageRequest() {
        return WebPages.REDIRECT_TO_LIST.toString();
    }

    @GetMapping("/list")
    public String getMedicineList(
        Model model,
        @RequestParam(value = "pageNo", defaultValue = "0") int pageNo
    ) {
        LOGGER.info("{}|Start of(getMedicineList)|Params:|", CLASS_TYPE);
        if (pageNo < 0) pageNo = 0;
        model.addAttribute(
            "medicineList",
            service.findAllMedicine(pageNo, ThpUtility.RECORD_PER_PAGE)
        );
        long totalMedicineCount = service.getTotalMedicineCount();
        model.addAttribute("headerText", "ALL MEDICINES");
        model.addAttribute("isSearchResult", false);
        ThpUtility.populateModelForPagination(
            model,
            totalMedicineCount,
            pageNo,
            "/mvc/medicine/list?",
            ThpUtility.RECORD_PER_PAGE
        );
        LOGGER.info("{}|End of(getMedicineList)|", CLASS_TYPE);
        return WebPages.INDEX.toString();
    }

    @GetMapping("/{medicineId}")
    public String loadMedicineDetailPage(
        @RequestHeader(
            value = HttpHeaders.REFERER,
            required = false
        ) final String referer,
        @PathVariable("medicineId") long medicineId,
        Model model
    ) {
        LOGGER.info(
            "{}|Start of(loadMedicineDetailPage)|Params: medicineId={}|",
            CLASS_TYPE,
            medicineId
        );
        model.addAttribute("medicine", service.getMedicineById(medicineId));
        model.addAttribute("medicineOrder", new MedicineOrder());
        model.addAttribute("referer", referer);
        LOGGER.info("{}|End of(loadMedicineDetailPage)|", CLASS_TYPE);
        return WebPages.MEDICINE_DETAIL.toString();
    }

    @GetMapping("/add")
    public String loadAddMedicinePage(Model model) {
        LOGGER.info("{}|Start of(loadAddMedicinePage)|Params:|", CLASS_TYPE);
        Medicine medicine = new Medicine();
        model.addAttribute("medicine", medicine);
        model.addAttribute("isUpdateRequest", false);
        LOGGER.info("{}|End of(loadAddMedicinePage)|", CLASS_TYPE);
        return WebPages.ADD_MEDICINE.toString();
    }

    @PostMapping("/add")
    public String insertMedicine(
        @ModelAttribute @Valid Medicine medicine,
        BindingResult result
    ) {
        LOGGER.info(
            "{}|Start of(insertMedicine)|Params: medicine={}|",
            CLASS_TYPE,
            medicine
        );
        if (result.hasErrors()) {
            return WebPages.ADD_MEDICINE.toString();
        }
        service.addMedicine(medicine);
        LOGGER.info("{}|End of(insertMedicine)|", CLASS_TYPE);
        return WebPages.REDIRECT_TO_LIST.toString();
    }

    @GetMapping("/search")
    public String loadSearchPage() {
        return WebPages.SEARCH_MEDICINE.toString();
    }

    @PostMapping("/search")
    public String loadSearchResult(
        @RequestParam(value = "name") String name,
        @RequestParam(value = "company", required = false) String company,
        @RequestParam(
            value = "ml",
            required = false,
            defaultValue = "0"
        ) String ml,
        Model model,
        @RequestParam(value = "pageNo", defaultValue = "0") int pageNo
    ) {
        if (
            StringUtils.isEmpty(name) &&
            StringUtils.isEmpty(company) &&
            StringUtils.isEmpty(ml)
        ) {
            return WebPages.REDIRECT_TO_LIST.toString();
        }
        LOGGER.info(
            "{}|Start of(loadSearchResult)|Params: name={}, company={}, ml={}|",
            CLASS_TYPE,
            name,
            company,
            ml
        );
        List<Medicine> resultMedicine = service.searchMedicine(
            name,
            company,
            Integer.parseInt(ml)
        );
        model.addAttribute("medicineList", resultMedicine);
        model.addAttribute("headerText", "SEARCH RESULT");
        model.addAttribute("isSearchResult", true);
        ThpUtility.populateModelForPagination(
            model,
            resultMedicine.size(),
            ThpUtility.RECORD_PER_PAGE,
            "/mvc/medicine/list?",
            ThpUtility.RECORD_PER_PAGE
        );
        LOGGER.info("{}|End of(loadSearchPage)|", CLASS_TYPE);
        return WebPages.INDEX.toString();
    }

    @PostMapping("/sell")
    public String sellMedicine(
        @ModelAttribute MedicineOrder medicineOrder,
        BindingResult bindingResult
    ) {
        LOGGER.info(
            "{}|Start of(sellMedicine)|Params: medicineOrder={}|",
            CLASS_TYPE,
            medicineOrder
        );
        if (bindingResult.hasErrors()) {
            System.out.println(new Gson().toJson(bindingResult));
        }
        service.sellMedicine(medicineOrder);
        LOGGER.info("End of(sellMedicine)|");
        return WebPages.REDIRECT_TO_LIST.toString();
    }

    @GetMapping("/update/{medicineId}")
    public String updateMedicineCount(
        @PathVariable long medicineId,
        Model model
    ) {
        LOGGER.info(
            "{}|Start of(updateMedicineCount)|Params: medicineId={}|",
            CLASS_TYPE,
            medicineId
        );
        model.addAttribute("isUpdateRequest", true);
        Medicine theMedicine = service.getMedicineById(medicineId);
        model.addAttribute("medicine", theMedicine);
        LOGGER.info("{}|End of(updateMedicineCount)|", CLASS_TYPE);
        return WebPages.ADD_MEDICINE.toString();
    }

    @GetMapping("/upload_excel")
    public String loadUploadExcelFile() {
        return WebPages.UPLOAD_FILE.toString();
    }

    @PostMapping("/upload_excel")
    public String saveExcelFileRecord(
        @RequestParam("file") MultipartFile file,
        @RequestParam("adminKey") String adminKey
    ) {
        if (adminKey.equals(ThpUtility.ADMIN_KEY)) {
            service.saveFile(file);
        }
        return WebPages.REDIRECT_TO_LIST.toString();
    }

    @GetMapping("/order/census")
    public String loadPageForMedicineCensus(
        Model model,
        @RequestParam(
            value = "inputLetter",
            required = false
        ) String inputLetter
    ) {
        List<String> allLetters = service.getAllMedicineFirstLetter();
        if (!allLetters.isEmpty() && inputLetter == null) inputLetter =
            allLetters.get(0);
        List<Medicine> medicineEntries = service.getMedicineByFirstLetter(
            inputLetter
        );
        model.addAttribute("medicineList", medicineEntries);
        model.addAttribute("headerText", "MEDICINE CENSUS");
        model.addAttribute("isSearchResult", false);
        model.addAttribute("selectedLetter", inputLetter);
        model.addAttribute("allLetters", allLetters);
        return WebPages.MEDICINE_CENSUS.toString();
    }

    @PostMapping("/name/change/{medicineId}")
    public String modifyMedicineName(
        @PathVariable("medicineId") long medicineId,
        @RequestParam("medicineNewName") String medicineName
    ) {
        LOGGER.info(
            "{}|Start of(modifyMedicineName)|Params: oldMedicineId={}, newMedicineName={}|",
            CLASS_TYPE,
            medicineId,
            medicineName
        );
        service.changeMedicineName(medicineId, medicineName);
        LOGGER.info("{}|End of(modifyMedicineName)|", CLASS_TYPE);
        return WebPages.REDIRECT_TO_LIST.toString();
    }

    @GetMapping("/mls")
    public String findMls(Model model) {
        model.addAttribute("volumes", findAvailableVolumes());
        model.addAttribute("isSearchResult", false);
        return WebPages.ML_LIST.toString();
    }

    @GetMapping("/mls/medicines")
    public String findMedicinesOfVolumes(
        Model model,
        @RequestParam("selectedVolume") String selectedVolume,
        @RequestParam(value = "pageNo", defaultValue = "0") int pageNo
    ) {
        if (pageNo < 0) pageNo = 0;
        selectedVolume = selectedVolume.toLowerCase();
        int medicineVolume;
        try {
            medicineVolume = Integer.parseInt(selectedVolume);
        } catch (NumberFormatException e) {
            return WebPages.REDIRECT_TO_LIST.toString();
        }

        int totalCount = service.totalMedicineCountOfVolume(medicineVolume);
        List<Medicine> medicines = service.findAllMedicineOfVolume(
            medicineVolume,
            pageNo,
            ThpUtility.RECORD_PER_PAGE
        );
        model.addAttribute("volumes", findAvailableVolumes());
        model.addAttribute("isSearchResult", true);
        model.addAttribute("medicineList", medicines);
        model.addAttribute("selectedVolume", selectedVolume.toUpperCase());
        ThpUtility.populateModelForPagination(
            model,
            totalCount,
            pageNo,
            "/mvc/medicine/mls/medicines?selectedVolume=" +
            selectedVolume +
            "&",
            ThpUtility.RECORD_PER_PAGE
        );
        return WebPages.ML_LIST.toString();
    }

    @GetMapping("/mother")
    public String loadMotherMedicines(
        Model model,
        @RequestParam(
            value = "inputLetter",
            defaultValue = "A"
        ) String inputLetter
    ) {
        List<Medicine> medicineEntries =
            service.findAllMotherMedicineByFirstLetter(inputLetter);
        List<String> allLetters = service.getAllMotherMedicineFirstLetter();
        model.addAttribute("medicineList", medicineEntries);
        model.addAttribute("headerText", "MOTHER");
        model.addAttribute("isSearchResult", false);
        model.addAttribute("selectedLetter", inputLetter);
        model.addAttribute("allLetters", allLetters);
        return WebPages.MOTHER_LIST.toString();
    }

    private List<String> findAvailableVolumes() {
        return service.findAvailableMedicinesVolumes();
    }
}
