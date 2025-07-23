package com.example.demo.services;

import com.example.demo.dto.request.UserProfileUpdateDto;
import com.example.demo.dto.response.UserResponseDto;
import com.example.demo.exceptions.UserNotFoundException;
import com.example.demo.filter.CustomUserDetails;
import com.example.demo.models.User;
import com.example.demo.repository.UserRepository;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final ObjectMapper objectMapper;
    private final ModelMapper modelMapper;

    public UserService(UserRepository userRepository, ObjectMapper objectMapper, ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.objectMapper = objectMapper;
        this.modelMapper = modelMapper;
    }

    @Override
    public CustomUserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + email));
        return new CustomUserDetails(user);
    }

    public UserResponseDto updateUserProfile(String userId, UserProfileUpdateDto dto) throws JsonMappingException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + userId));
        objectMapper.updateValue(user, dto);
        User saved = userRepository.save(user);
        return modelMapper.map(saved, UserResponseDto.class);
    }

    public UserResponseDto getUserProfile(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found: " + userId));

        return objectMapper.convertValue(user, UserResponseDto.class);
    }

}