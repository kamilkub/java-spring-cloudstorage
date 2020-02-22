package com.cloudstorage.controllers;

import com.cloudstorage.config.UserAuthenticationFilter;
import com.cloudstorage.model.UserProfile;
import com.cloudstorage.service.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/user")
public class ProfileController {

    private UserAuthenticationFilter authenticationFilter;
    private ProfileService profileService;

    @Autowired
    public ProfileController(UserAuthenticationFilter authenticationFilter, ProfileService profileService) {
        this.authenticationFilter = authenticationFilter;
        this.profileService = profileService;
    }


    @GetMapping("/profile")
    public String showUserProfile() {
        return "profilePage";
    }

    @GetMapping("/profile-data")
    @ResponseBody
    public UserProfile getUserProfileData() {
        return profileService.getUsersProfileData(authenticationFilter.getAuthenticationUsername());
    }

}
