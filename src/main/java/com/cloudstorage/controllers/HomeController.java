package com.cloudstorage.controllers;


import com.cloudstorage.config.UserAuthenticationFilter;
import com.cloudstorage.model.BaseFile;
import com.cloudstorage.service.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;


@Controller
public class HomeController {


	private UserAuthenticationFilter userAuthenticationFilter;

	private StorageService storageService;

	@Autowired
	public HomeController(UserAuthenticationFilter userAuthenticationFilter, StorageService storageService) {
		this.userAuthenticationFilter = userAuthenticationFilter;
		this.storageService = storageService;
	}

	@GetMapping(value = {"/user", "/user/files"})
	public String homePage(Model model){

		Authentication authentication = userAuthenticationFilter.isAuthenticated();

		ArrayList<BaseFile> fileAndDirectoriesPaths = storageService.getFileAndDirectoriesPaths(authentication.getName());
		model.addAttribute("files", fileAndDirectoriesPaths);


		return "homePage";
	}

	@GetMapping(value = {"/", "/home"})
	public String welcomePage(){
		if(userAuthenticationFilter.isAuthenticatedBool())
	   		return "redirect:/user";
		else
			return "welcomePage";

	}



}
