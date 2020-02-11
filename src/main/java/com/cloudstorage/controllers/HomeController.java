package com.cloudstorage.controllers;


import com.cloudstorage.config.UserAuthenticationFilter;
import com.cloudstorage.model.BaseFile;
import com.cloudstorage.service.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;


@Controller
public class HomeController {


	private UserAuthenticationFilter checkAuthentication;

	private StorageService storageService;

	@Autowired
	public HomeController(UserAuthenticationFilter checkAuthentication, StorageService storageService) {
		this.checkAuthentication = checkAuthentication;
		this.storageService = storageService;
	}

	@GetMapping(value = {"/user", "/user/files"})
	public String homePage(Model model){

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		ArrayList<BaseFile> fileAndDirectoriesPaths = storageService.getFileAndDirectoriesPaths(authentication.getName());
		model.addAttribute("files", fileAndDirectoriesPaths);

		return "homePage";
	}

	@GetMapping(value = {"/", "/home"})
	public String welcomePage(){

//		if(checkAuthentication.isAuthenticated()){
//	   		return "redirect:/user";
//		}

		return "welcomePage";
	}



}
