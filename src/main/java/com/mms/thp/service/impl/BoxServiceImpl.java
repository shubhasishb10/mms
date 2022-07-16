package com.mms.thp.service.impl;

import com.mms.thp.model.Box;
import com.mms.thp.repository.BoxRepository;
import com.mms.thp.service.BoxService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class BoxServiceImpl implements BoxService {

    @Autowired
    private BoxRepository boxRepository;

    @Override
    public Box findBoxByNumber(String number) {
        return boxRepository.findByNumber(number);
    }
}
