// package com.tagalong.service;

// import java.util.UUID;

// import org.springframework.security.crypto.password.PasswordEncoder;
// import org.springframework.stereotype.Service;

// import com.tagalong.dto.LoginRequest;
// import com.tagalong.dto.RegisterRequest;
// import com.tagalong.dto.RegisterResponse;
// import com.tagalong.entity.User;
// import com.tagalong.exception.DuplicateEmailException;
// import com.tagalong.exception.InvalidCredentialsException;
// import com.tagalong.repository.UserRepository;
// import com.tagalong.security.JwtUtil;

// @Service
// public class AuthService {
//     private final UserRepository userRepository;
//     private final PasswordEncoder passwordEncoder;
//     private final JwtUtil jwtUtil;

//     public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil){
//         this.userRepository = userRepository;
//         this.passwordEncoder = passwordEncoder;
//         this.jwtUtil = jwtUtil;
//     }

//     public RegisterResponse register(RegisterRequest request) {
//         if (userRepository.existsByEmail(request.getEmail())) {
//             throw new DuplicateEmailException("Email already registered: " + request.getEmail());
//         }

//         User user = new User(request.getEmail(), passwordEncoder.encode(request.getPassword()), request.getFullName());
//         user.setUserUid(UUID.randomUUID().toString());
//         User saved = userRepository.save(user);

//         String jwtToken = jwtUtil.generateToken(saved.getUserUid(), saved.getEmail());
//         return new RegisterResponse(saved.getUserUid(), jwtToken, saved.getEmail(), saved.getFullName());
//     }

//     public RegisterResponse login(LoginRequest request) {
//         User user = userRepository.findByEmail(request.getEmail())
//                 .orElseThrow(() -> new InvalidCredentialsException("Invalid email or password"));

//         if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
//             throw new InvalidCredentialsException("Invalid email or password");
//         }

//         String jwtToken = jwtUtil.generateToken(user.getUserUid(), user.getEmail());
//         return new RegisterResponse(user.getUserUid(), jwtToken, user.getEmail(), user.getFullName());
//     }
// }
