package com.mms.thp.service;

import com.mms.thp.model.Company;

import java.util.List;

public interface CompanyService {
    Company findCompanyByName(String name);
    List<Company> findCompanies(int page, int size);
}
