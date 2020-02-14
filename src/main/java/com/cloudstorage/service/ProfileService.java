package com.cloudstorage.service;

import com.cloudstorage.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProfileService {

    private UsersRepository usersRepository;

    @Autowired
    public ProfileService(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }


//    public Users getUsersProfileData(String username) {
//        Users authUser = usersRepository.findByUsername(username);
//
//    }


}
