/**
 * Copyright (C) DreamTech Inc - All Rights Reserved Unauthorized copying of this file,
 * via any medium is strictly prohibited Proprietary and confidential For any use
 * of this software please write us to shubhasishb10@gmail.com.
 */

package com.mms.thp.service.impl;

import com.mms.thp.model.Company;
import com.mms.thp.repository.CompanyRepository;
import com.mms.thp.service.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
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
