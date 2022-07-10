package com.example.demo.controller;

import com.example.demo.WebPages;
import com.example.demo.service.MedicineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/mvc/medicine")
public class MedicineMvcController {

    @Autowired
    private MedicineService service;

    @GetMapping("/list")
    public String getMedicineList(Model model){
        model.addAttribute("medicineList", service.findAllMedicine(0, 10));
        return WebPages.INDEX.toString();
    }
    @GetMapping("/{medicineId}")
    public String loadMedicinePage(@PathVariable("medicineId") long medicineId, Model model) {
        model.addAttribute("medicine", service.getMedicineById(medicineId));
        return WebPages.MEDICINE.toString();
    }
}
