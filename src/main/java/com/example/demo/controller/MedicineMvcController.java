package com.example.demo.controller;

import com.example.demo.WebPages;
import com.example.demo.model.Medicine;
import com.example.demo.service.MedicineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/mvc/medicine")
public class MedicineMvcController {

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
        return WebPages.MEDICINE_DETAIL.toString();
    }
    @GetMapping("/add")
    public String loadAddMedicinePage(Model model) {
        Medicine medicine = new Medicine();
        model.addAttribute("medicine", medicine);
        return WebPages.ADD_MEDICINE.toString();
    }
    @PostMapping("/add")
    public String insertMedicine(@ModelAttribute Medicine medicine) {
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

        List<Medicine> resultMedicine = service.searchMedicine(name.toLowerCase(), company.toLowerCase(), Integer.parseInt(ml));
        model.addAttribute("medicineList", resultMedicine);
        model.addAttribute("headerText", "Search Results");
        model.addAttribute("isSearchResult", true);
        return WebPages.INDEX.toString();
    }
}
