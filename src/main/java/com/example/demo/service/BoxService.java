package com.example.demo.service;

import com.example.demo.model.Box;
import com.example.demo.repository.BoxRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public interface BoxService {

    Box findBoxByNumber(int number);
}
