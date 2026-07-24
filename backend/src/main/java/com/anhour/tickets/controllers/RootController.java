package com.anhour.tickets.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class RootController {

    @GetMapping("/")
    public Map<String, String> status() {
        return Map.of(
            "application", "tickets",
            "status", "running"
        );
    }
}
