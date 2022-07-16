package com.mms.thp.service.impl;

import com.mms.thp.model.Retail;
import com.mms.thp.repository.RetailRepository;
import com.mms.thp.service.RetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

import static org.springframework.transaction.annotation.Propagation.REQUIRED;

@Service
@Transactional
public class RetailServiceImpl implements RetailService {

    private final RetailRepository retailRepository;

    @Autowired
    RetailServiceImpl(RetailRepository retailRepository){this.retailRepository = retailRepository;}

    @Override
    public List<Retail> findAllRetailRecord() {
        return retailRepository.findAll();
    }
}
