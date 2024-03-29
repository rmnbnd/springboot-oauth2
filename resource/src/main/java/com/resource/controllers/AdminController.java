package com.resource.controllers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AdminController {

    @RequestMapping("/admin")
    @PreAuthorize("hasPermission(returnObject, 'WRITE')")
    public String resource() {
        return "Hi, Admin!";
    }

}