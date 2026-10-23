// package com.tagalong.controller;

// import org.springframework.http.HttpStatus;
// import org.springframework.http.ResponseEntity;
// import org.springframework.web.bind.annotation.PostMapping;
// import org.springframework.web.bind.annotation.RequestBody;
// import org.springframework.web.bind.annotation.RequestMapping;
// import org.springframework.web.bind.annotation.RestController;

// import com.tagalong.dto.LoginRequest;
// import com.tagalong.dto.RegisterRequest;
// import com.tagalong.dto.RegisterResponse;
// import com.tagalong.service.AuthService;

// import jakarta.validation.Valid;
// import lombok.AllArgsConstructor;

// @RestController
// @RequestMapping("/api/auth")
// @AllArgsConstructor
// public class AuthController {

//     private final AuthService authService;

//     @PostMapping("/register")
//     public ResponseEntity<RegisterResponse> register(@Valid @RequestBody RegisterRequest request) {
//         RegisterResponse response = authService.register(request);
//         return ResponseEntity.status(HttpStatus.CREATED).body(response);
//     }

//     @PostMapping("/login")
//     public ResponseEntity<RegisterResponse> login(@Valid @RequestBody LoginRequest request) {
//         RegisterResponse response = authService.login(request);
//         return ResponseEntity.ok(response);
//     }
// }
