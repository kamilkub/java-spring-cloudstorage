package com.cloudstorage.controllers;

import com.cloudstorage.config.UserAuthenticationFilter;
import com.cloudstorage.model.UpdateProfileObject;
import com.cloudstorage.model.UserProfile;
import com.cloudstorage.service.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/user")
@RequiredArgsConstructor
public class ProfileController {
    private final UserAuthenticationFilter authenticationFilter;
    private final ProfileService profileService;

    @GetMapping("/profile")
    public String showUserProfile(Model model) {
        model.addAttribute("updateObject", new UpdateProfileObject());
        model.addAttribute("passwordChanged");
        return "profilePage";
    }

    @GetMapping("/profile-data")
    @ResponseBody
    public UserProfile getUserProfileData() {
        return profileService.getUsersProfileData(authenticationFilter.getAuthenticationUsername());
    }
    @GetMapping("/statistics")
    public String show() {
        return "statisticsPage";
    }
    @PostMapping(value = "/update-profile-data")
    @ResponseBody
    public HttpStatus updateUserProfileData(@RequestBody UpdateProfileObject updateObject) {
        if(profileService.updateUserProfile(updateObject)) {
            return HttpStatus.OK;
        } else {
            return HttpStatus.BAD_REQUEST;
        }

    }



}
