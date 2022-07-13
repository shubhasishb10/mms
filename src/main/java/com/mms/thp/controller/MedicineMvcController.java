package com.mms.thp.controller;

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

    @Autowired
    private MedicineService service;

    @GetMapping("/list")
    public String getMedicineList(Model model){
        model.addAttribute("medicineList", service.findAllMedicine(0, 10));
        model.addAttribute("headerText", "All Medicines");
        model.addAttribute("isSearchResult", false);
        return WebPages.INDEX.toString();
    }
    @GetMapping("/{medicineId}")
    public String loadMedicineDetailPage(@PathVariable("medicineId") long medicineId, Model model) {
        model.addAttribute("medicine", service.getMedicineById(medicineId));
        model.addAttribute("medicineOrder", new MedicineOrder());
        return WebPages.MEDICINE_DETAIL.toString();
    }
    @GetMapping("/add")
    public String loadAddMedicinePage(Model model) {
        Medicine medicine = new Medicine();
        model.addAttribute("medicine", medicine);
        model.addAttribute("isUpdateRequest", false);
        return WebPages.ADD_MEDICINE.toString();
    }
    @PostMapping("/add")
    public String insertMedicine(@ModelAttribute @Valid Medicine medicine, BindingResult result) {
        if(result.hasErrors()){
            return WebPages.ADD_MEDICINE.toString();
        }
        service.addMedicine(medicine);
        System.out.println(medicine);
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

        List<Medicine> resultMedicine = service.searchMedicine(name, company, Integer.parseInt(ml));
        model.addAttribute("medicineList", resultMedicine);
        model.addAttribute("headerText", "Search Results");
        model.addAttribute("isSearchResult", true);
        return WebPages.INDEX.toString();
    }

    @PostMapping("/sell")
    public String sellMedicine(@ModelAttribute MedicineOrder medicineOrder, BindingResult bindingResult) {
        LOGGER.info("In sellMedicine: medicineOrder={}", medicineOrder);
        if(bindingResult.hasErrors()){
            System.out.println(new Gson().toJson(bindingResult));
        }
        service.sellMedicine(medicineOrder);
        return "redirect:/mvc/medicine/list";
    }

    @GetMapping("/update/{medicineId}")
    public String updateMedicineCount(@PathVariable long medicineId, Model model) {
        model.addAttribute("isUpdateRequest", true);
        Medicine theMedicine = service.getMedicineById(medicineId);
        model.addAttribute("medicine", theMedicine);
        return WebPages.ADD_MEDICINE.toString();
    }
}
