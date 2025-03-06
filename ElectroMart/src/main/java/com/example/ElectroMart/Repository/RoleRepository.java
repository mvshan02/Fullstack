package com.example.ElectroMart.Repository;

import com.example.ElectroMart.Model.Role;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface RoleRepository extends MongoRepository<Role, String> {
    Optional<Role> findByRole(String role); // Change from "findByName" to "findByRole"
}


