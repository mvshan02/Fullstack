package com.example.ElectroMart.Service;

import com.example.ElectroMart.Model.User;
import com.example.ElectroMart.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MongoTemplate mongoTemplate;


    // Add a new user using MongoTemplate (if not already present)
    public void registerUser(User user) {
        // Check if the user already exists
        Query query = new Query();
        query.addCriteria(Criteria.where("email").is(user.getEmail()));

        if (mongoTemplate.exists(query, User.class)) {
            throw new RuntimeException("Email is already registered");
        }

        // Save the user
        mongoTemplate.save(user);
    }

    // Add a new user using UserRepository
    public User addUser(User user) {
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new RuntimeException("User with email " + user.getEmail() + " already exists");
        }
        return userRepository.save(user);
    }

    public User saveUser(User user) {
        Optional<User> existingUser = userRepository.findByEmail(user.getEmail());
        if (existingUser.isPresent()) {
            throw new RuntimeException("User with email " + user.getEmail() + " already exists");
        }
        return userRepository.save(user);
    }
    // Get all users
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // Get a user by ID
    public User getUserById(String id) {
        Optional<User> user = userRepository.findById(id);
        return user.orElseThrow(() -> new RuntimeException("User not found with id: " + id));
    }

    // Delete a user by ID
    public void deleteUserById(String id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
        } else {
            throw new RuntimeException("User not found with id: " + id);
        }
    }
}
