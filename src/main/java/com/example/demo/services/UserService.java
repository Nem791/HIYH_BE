package com.example.demo.services;

import com.example.demo.dto.request.UserProfileUpdateDto;
import com.example.demo.dto.response.UserResponseDto;
import com.example.demo.exceptions.UserNotFoundException;
import com.example.demo.filter.CustomUserDetails;
import com.example.demo.models.User;
import com.example.demo.repository.UserRepository;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final ObjectMapper objectMapper;

    public UserService(UserRepository userRepository, ObjectMapper objectMapper) {
        this.userRepository = userRepository;
        this.objectMapper = objectMapper;
    }

    @Override
    public CustomUserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + email));

        if (Boolean.FALSE.equals(user.getConsented())) {
            throw new UsernameNotFoundException("User has not consent");
        }
        return new CustomUserDetails(user);
    }

    public void updateUserProfile(String userId, UserProfileUpdateDto dto) throws JsonMappingException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + userId));

        objectMapper.updateValue(user, dto); // May throw JsonMappingException
        userRepository.save(user);
    }

    public UserResponseDto getUserProfile(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found: " + userId));

        return objectMapper.convertValue(user, UserResponseDto.class);
    }

}