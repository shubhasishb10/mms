package com.mms.thp.controller.mvc;

import com.mms.thp.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author Shubhasish
 */

@Controller
@RequestMapping("/mvc/order")
public class OrderController {

    @Autowired
    private OrderService service;

    @GetMapping
    public String initiateOrderSession(@RequestHeader(value = HttpHeaders.REFERER, required = false) final String referer, @RequestParam(value = "customerName", defaultValue = "") String customerName) {
        service.initiatePurchaseContext(customerName);
        return "redirect:" + referer;
    }

    @GetMapping("/add/{medicineId}")
    public String addItemsToCart(@RequestHeader(value = HttpHeaders.REFERER, required = false) final String referer, @PathVariable("medicineId") String medicineId) {
        service.addToOrder(Long.parseLong(medicineId));
        return referer;
    }

    @GetMapping("/destroy")
    public String destroyPurchaseSession(@RequestHeader(value = HttpHeaders.REFERER, required = false) final String referer) {
        service.destroyPurchaseSession();
        return "redirect:" + referer;
    }
}
