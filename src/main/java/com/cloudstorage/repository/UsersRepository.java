package com.cloudstorage.repository;


import com.cloudstorage.model.StorageUser;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsersRepository extends MongoRepository<StorageUser, String> {

	StorageUser findByUsername(String username);
    StorageUser findByEmail(String email);
    StorageUser findByPin(String pin);

}
