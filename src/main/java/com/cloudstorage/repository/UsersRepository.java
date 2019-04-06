package com.cloudstorage.repository;


import com.cloudstorage.model.Users;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsersRepository extends MongoRepository<Users, String> {

	Users findByUsername(String username);
    Users findByEmail(String email);
    Users findByPin(String pin);



}
