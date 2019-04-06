package com.cloudstorage.controllers;


import com.cloudstorage.config.CheckAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.IOException;

@Controller
public class HomeController {


       @Autowired
	   private CheckAuthentication checkAuthentication;


	   @GetMapping(value = {"/user"})
	   public String homePage(){
	   	   return "homePage";
	   }

	   @GetMapping(value = {"/", "/home"})
	   public String welcomePage(){

	   	if(checkAuthentication.isAuthenticated()){
	   		return "redirect:/user";
		}

	   	   return "welcomePage";
	   }



}
