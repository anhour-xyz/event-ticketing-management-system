package com.anhour.tickets.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class FrontendController {

    @GetMapping({
        "/",
        "/login",
        "/callback",
        "/organizers",
        "/events/{path:[^\\.]*}",
        "/events/{path:[^\\.]*}/**",
        "/dashboard",
        "/dashboard/**"
    })
    public String frontend() {
        return "forward:/index.html";
    }
}