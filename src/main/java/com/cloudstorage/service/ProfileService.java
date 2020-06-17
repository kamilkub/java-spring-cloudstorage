package com.cloudstorage.service;

import com.cloudstorage.model.StorageUser;
import com.cloudstorage.model.UserProfile;
import com.cloudstorage.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.file.Paths;

@Service
public class ProfileService {

    private UsersRepository usersRepository;
    private StorageService storageService;

    @Autowired
    public ProfileService(UsersRepository usersRepository, StorageService storageService) {
        this.usersRepository = usersRepository;
        this.storageService  = storageService;
    }


    public UserProfile getUsersProfileData(String username) {
        StorageUser authUser = usersRepository.findByUsername(username);
        long takenSpace = storageService.getFolderSize(Paths.get(storageService.getBasePathStorage() + authUser.getUsername()));

        return new UserProfile(authUser.getEmail(), authUser.getUsername(), authUser.getDiskSpace(), takenSpace);
    }


}
