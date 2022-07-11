package com.example.demo.service.impl;

import com.example.demo.model.Box;
import com.example.demo.repository.BoxRepository;
import com.example.demo.service.BoxService;
import org.springframework.beans.factory.annotation.Autowired;

public class BoxServiceImpl implements BoxService {

    @Autowired
    private BoxRepository boxRepository;

    @Override
    public Box findBoxByNumber(int number) {
        return boxRepository.findByNumber(number);
    }
}
