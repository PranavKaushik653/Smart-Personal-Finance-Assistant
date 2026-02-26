package com.financeapp.personal.service;


import com.financeapp.personal.entity.User;
import com.financeapp.personal.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    //CREATE A NEW USER
    public User createUser(User user) {
        if(userRepository.existsByEmail(user.getEmail())) {
            throw new IllegalArgumentException("User with email " + user.getEmail() + " already exists");
        }
        return userRepository.save(user);
    }

    //finding user via email
    @Transactional(readOnly = true)
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    //getting all users
    @Transactional(readOnly = true)
    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    //UPDATE USER INFORMATION
    public User updateUser(User user) {
        if(user.getId() == null) {
            throw new IllegalArgumentException("Cannot update user without id");
        }

        Optional<User> existingUser = userRepository.findById(user.getId());
        if (existingUser.isEmpty()) {
            throw new IllegalArgumentException("User not found with ID: " + user.getId());
        }

        // Check if email is being changed and if new email already exists
        if (!existingUser.get().getEmail().equals(user.getEmail()) &&
                userRepository.existsByEmail(user.getEmail())) {
            throw new IllegalArgumentException("Email " + user.getEmail() + " is already in use");
        }

        return userRepository.save(user);
    }

    //DELETE USER
    @Transactional(readOnly = true)
    public void deleteUser(Long id) {
        if(!userRepository.existsById(id)) {
            throw new IllegalArgumentException("Cannot delete user without id");
        }
        userRepository.deleteById(id);
    }
    /**
     * Check if email is available for registration
     */
    @Transactional(readOnly = true)
    public boolean isEmailAvailable(String email) {
        return !userRepository.existsByEmail(email);
    }
}

