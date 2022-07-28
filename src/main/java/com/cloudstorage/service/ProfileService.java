package com.cloudstorage.service;

import com.cloudstorage.model.StorageUser;
import com.cloudstorage.model.UpdateProfileObject;
import com.cloudstorage.model.UserProfile;
import com.cloudstorage.repository.UsersRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.nio.file.Paths;
import java.util.Optional;

@Service
public class ProfileService {
    private UsersRepository usersRepository;
    private StorageService storageService;
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    public ProfileService(UsersRepository usersRepository, StorageService storageService, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.usersRepository = usersRepository;
        this.storageService = storageService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }
    public UserProfile getUsersProfileData(String username) {
        StorageUser authUser = usersRepository.findByUsername(username);
        long takenSpace = storageService.getFolderSize(Paths.get(storageService.getBasePathStorage() + authUser.getUsername()));

        return new UserProfile(authUser.getEmail(), authUser.getUsername(), authUser.getDiskSpace(), takenSpace);
    }

    public boolean updateUserProfile(UpdateProfileObject updateProfileObject) {
        Optional<StorageUser> byEmail = Optional.ofNullable(usersRepository.findByEmail(updateProfileObject.getEmail()));

        if(byEmail.isEmpty()) return false;
        if(updateProfileObject.getNewPassword().trim().length() < 6) return false;

        StorageUser user = byEmail.get();

        if(bCryptPasswordEncoder.matches(updateProfileObject.getOldPassword(), user.getPassword())){
            user.setPassword(bCryptPasswordEncoder.encode(updateProfileObject.getNewPassword()));
            usersRepository.save(user);
            return true;
        }

        return false;
    }
}