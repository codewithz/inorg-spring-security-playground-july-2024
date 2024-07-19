package com.inorg.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AccountController {
    @GetMapping("/accounts")
    public String getAccountDetails(){
        return "Account Details from DB";
    }
}
