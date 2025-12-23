package com.cinestream.auth_service.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthServiceController {
    @GetMapping("/oauth2/callback")
    public ResponseEntity<String> handleCode(
            @RequestParam(name = "code") String code,
            @RequestParam(name = "state", required = false) String state) {

        return ResponseEntity.ok("Received code: " + code + " | state: " + state);
    }
}

