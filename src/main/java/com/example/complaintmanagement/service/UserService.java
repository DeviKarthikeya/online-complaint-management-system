package com.example.complaintmanagement.service;

import com.example.complaintmanagement.model.User;
import com.example.complaintmanagement.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));
    }

    public void deleteUserAccount(Long userId) {
        userRepository.deleteById(userId);
    }

}
