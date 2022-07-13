package com.mms.thp.service;

import com.mms.thp.model.Box;
import org.springframework.stereotype.Service;

@Service
public interface BoxService {

    Box findBoxByNumber(String number);
}
