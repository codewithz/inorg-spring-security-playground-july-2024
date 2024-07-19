package com.inorg.controller;

import com.inorg.model.Notice;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class NoticeController {
    @GetMapping("/notices")
    public Notice getNotices(){
        return new Notice("Imp Notice","Demo Notice");
    }
}
