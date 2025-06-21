package com.code.monks.hrms.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.core.env.Environment;
import java.util.List;
import java.util.Arrays;

@RestController
public class TestController {

    @Autowired
    private Environment env;

    @GetMapping("/nukkad/api/v1/test")
    public ResponseEntity<List<String>> login() {

        return ResponseEntity.ok(List.of(Arrays.toString(env.getActiveProfiles()), env.getProperty("spring.application.name")));
    }
}
