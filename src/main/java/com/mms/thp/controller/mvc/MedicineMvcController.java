package com.mms.thp.controller.mvc;

import com.mms.thp.dto.MedicineOrder;
import com.mms.thp.utility.WebPages;
import com.mms.thp.model.Medicine;
import com.mms.thp.service.MedicineService;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/mvc/medicine")
public class MedicineMvcController {

    private static final Logger LOGGER = LoggerFactory.getLogger(MedicineMvcController.class);
    private static final String CLASS_TYPE = "CONTROLLER";

    @Autowired
    private MedicineService service;

    @GetMapping("/list")
    public String getMedicineList(Model model){
        LOGGER.info("{}|Start of(getMedicineList)|Params:|", CLASS_TYPE);
        model.addAttribute("medicineList", service.findAllMedicine(0, 10));
        model.addAttribute("headerText", "All Medicines");
        model.addAttribute("isSearchResult", false);
        LOGGER.info("{}|End of(getMedicineList)|", CLASS_TYPE);
        return WebPages.INDEX.toString();
    }
    @GetMapping("/{medicineId}")
    public String loadMedicineDetailPage(@PathVariable("medicineId") long medicineId, Model model) {
        LOGGER.info("{}|Start of(loadMedicineDetailPage)|Params: medicineId={}|", CLASS_TYPE, medicineId);
        model.addAttribute("medicine", service.getMedicineById(medicineId));
        model.addAttribute("medicineOrder", new MedicineOrder());
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
    public String insertMedicine(@ModelAttribute @Valid Medicine medicine, BindingResult result) {
        LOGGER.info("{}|Start of(insertMedicine)|Params: medicine={}|", CLASS_TYPE, medicine);
        if(result.hasErrors()){
            return WebPages.ADD_MEDICINE.toString();
        }
        service.addMedicine(medicine);
        LOGGER.info("{}|End of(insertMedicine)|", CLASS_TYPE);
        return "redirect:/mvc/medicine/list";
    }
    @GetMapping("/search")
    public String loadSearchPage() {
        return WebPages.SEARCH_MEDICINE.toString();
    }
    @PostMapping("/search")
    public String loadSearchResult(@RequestParam(value = "name") String name,
                                   @RequestParam(value = "company", required = false) String company,
                                   @RequestParam(value = "ml", required = false, defaultValue = "0") String ml, Model model){

        LOGGER.info("{}|Start of(loadSearchResult)|Params: name={}, company={}, ml={}|", CLASS_TYPE, name, company, ml);
        List<Medicine> resultMedicine = service.searchMedicine(name, company, Integer.parseInt(ml));
        model.addAttribute("medicineList", resultMedicine);
        model.addAttribute("headerText", "Search Results");
        model.addAttribute("isSearchResult", true);
        LOGGER.info("{}|End of(loadSearchPage)|", CLASS_TYPE);
        return WebPages.INDEX.toString();
    }

    @PostMapping("/sell")
    public String sellMedicine(@ModelAttribute MedicineOrder medicineOrder, BindingResult bindingResult) {
        LOGGER.info("{}|Start of(sellMedicine)|Params: medicineOrder={}|", CLASS_TYPE, medicineOrder);
        if(bindingResult.hasErrors()){
            System.out.println(new Gson().toJson(bindingResult));
        }
        service.sellMedicine(medicineOrder);
        LOGGER.info("End of(sellMedicine)|");
        return "redirect:/mvc/medicine/list";
    }

    @GetMapping("/update/{medicineId}")
    public String updateMedicineCount(@PathVariable long medicineId, Model model) {
        LOGGER.info("{}|Start of(updateMedicineCount)|Params: medicineId={}|", CLASS_TYPE, medicineId);
        model.addAttribute("isUpdateRequest", true);
        Medicine theMedicine = service.getMedicineById(medicineId);
        model.addAttribute("medicine", theMedicine);
        LOGGER.info("{}|End of(updateMedicineCount)|", CLASS_TYPE);
        return WebPages.ADD_MEDICINE.toString();
    }
}
