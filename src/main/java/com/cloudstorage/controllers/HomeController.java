package com.cloudstorage.controllers;


import com.cloudstorage.config.UserAuthenticationFilter;
import com.cloudstorage.service.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;


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
		return "homePage";
	}

	@GetMapping(value = {"/", "/home"})
	public String welcomePage(){
		if(userAuthenticationFilter.isAuthenticated())
	   		return "redirect:/user";
		else
			return "welcomePage";

	}
}