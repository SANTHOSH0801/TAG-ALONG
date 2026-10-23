package com.tagalong.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tagalong.dto.AuthRequest;
import com.tagalong.service.AuthService2;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor

public class AuthController2 {
    private final AuthService2 authService;

    @PostMapping("")
    public ResponseEntity<?> login(@RequestBody AuthRequest req, @RequestHeader HttpHeaders headers, HttpServletRequest request)
        throws Exception{
            return authService.getAuthResponse(req, headers, request);
    }
    
}
