package com.mms.thp.controller.rest;

import com.mms.thp.model.Company;
import com.mms.thp.service.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/company")
public class CompanyController {

    @Autowired
    private CompanyService service;

    @RequestMapping("/{companyName}/medicines")
    public ResponseEntity<Company> getCompanyByName(@PathVariable String companyName) {
        return new ResponseEntity<>(service.findCompanyByName(companyName), HttpStatus.OK);
    }

    @RequestMapping("/list")
    public ResponseEntity<List<Company>> listCompanies(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        return new ResponseEntity<>(service.findCompanies(page, size), HttpStatus.OK);
    }
}
