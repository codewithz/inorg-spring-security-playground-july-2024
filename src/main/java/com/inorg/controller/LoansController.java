package com.inorg.controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoansController {
    @GetMapping("/loans")
    public String getLoanDetails(){
        return "Loan Details from DB";
    }
}

