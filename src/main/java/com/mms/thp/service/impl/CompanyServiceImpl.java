package com.mms.thp.service.impl;

import com.mms.thp.model.Company;
import com.mms.thp.repository.CompanyRepository;
import com.mms.thp.service.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CompanyServiceImpl implements CompanyService {

    private final CompanyRepository companyRepository;

    @Autowired
    CompanyServiceImpl(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    @Override
    public Company findCompanyByName(String name) {
        return companyRepository.findByName(name);
    }

    @Override
    public List<Company> findCompanies(int page, int size) {
        Pageable pageRequest = PageRequest.of(page, size);
        return companyRepository.findAll(pageRequest).getContent();
    }
}
