package com.inorg.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CardsController {
    @GetMapping("/cards")
    public String getCardDetails(){
        return "Card Details from DB";
    }
}
