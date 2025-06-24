//package com.example.demo.services;
//
//import com.example.demo.dto.AuthRequest;
//import com.example.demo.exceptions.InvalidCredentialsException;
//import com.example.demo.exceptions.UserNotFoundException;
//import com.example.demo.models.User;
//import com.example.demo.repository.UserRepository;
//import com.example.demo.utils.JwtUtil;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Service;
//
//@Service
//public class AuthService {
//    @Autowired
//    private UserRepository userRepository;
//    @Autowired private PasswordEncoder passwordEncoder;
//    @Autowired private JwtUtil jwtUtil;
//
//    public String login(AuthRequest request) {
//        User user = userRepository.findByUsername(request.getUsername())
//                .orElseThrow(() -> new UserNotFoundException("User not found"));
//
//        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
//            throw new InvalidCredentialsException("Invalid credentials");
//        }
//
//        return jwtUtil.generateToken(user.getUsername());
//    }
//
//}