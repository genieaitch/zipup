package com.teamzipup.zipup.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;



@Controller
public class PageController {

    /* ********* product 관련  ********* product 관련 */
    @GetMapping("/product/add")
    public String productAdd() {
        return "productAdd"; // productAdd.html로 이동
    }

    @GetMapping("/product/detail")
    public String productDetail() {
        return "productDetail"; // productDetail.html로 이동
    }

    @GetMapping("/purchase/completed")
    public String purchaseCompleted() {
        return "purchaseCompleted"; // purchaseCompleted.html로 이동
    }
}
