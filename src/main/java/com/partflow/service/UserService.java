package com.partflow.service;

import com.partflow.model.User;
import com.partflow.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public Optional<User> findByUserName(String username){
        return userRepository.findByUsername(username);
    }
    public User saveUser(User user){
        return userRepository.save(user);
    }

    public boolean validateLogin(String username, String password){
        return userRepository.findByUsername(username)
                .map(u -> u.getPassword().equals(password))
                .orElse(false);
    }
}
