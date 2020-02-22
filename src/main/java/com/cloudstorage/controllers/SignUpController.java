package com.cloudstorage.controllers;


import com.cloudstorage.config.UserAuthenticationFilter;
import com.cloudstorage.model.Users;
import com.cloudstorage.service.SignUpService;
import com.cloudstorage.service.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Controller
public class SignUpController {

	private SignUpService signUpService;

	private StorageService storageService;

	private UserAuthenticationFilter userAuthenticationFilter;


	@Autowired
	public SignUpController(SignUpService signUpService, StorageService storageService, UserAuthenticationFilter userAuthenticationFilter) {
		this.signUpService = signUpService;
		this.storageService = storageService;
		this.userAuthenticationFilter = userAuthenticationFilter;
	}

	@GetMapping("/sign-in")
	public String showLoginPage(){
		if(userAuthenticationFilter.isAuthenticatedBool())
			return "redirect:/user";
		else
			return "auth_templates/sign-in";
	}


	@GetMapping("/sign-up")
	public String signUpPage(Model model){
		if(userAuthenticationFilter.isAuthenticatedBool())
			return "redirect:/user";
		else
			model.addAttribute("user", new Users());
			return "auth_templates/sign-up";
	}


	@PostMapping("/sign-up")
	public String signUpUser(@Valid @ModelAttribute("user") Users user, BindingResult result, Model model) throws IOException {

		if (result.hasErrors()) {
			List<String> errorMessages = new ArrayList<>();

			result.getAllErrors().forEach((objectError -> errorMessages.add(objectError.getDefaultMessage())));

			model.addAttribute("listOfErrors", errorMessages);
		}else {

			if (signUpService.findByEmail(user.getEmail()) != null && signUpService.findByUsername(user.getUsername()) != null) {
				model.addAttribute("exists", true);
				return "auth_templates/sign-up";

			} else {

				String stringPin = String.valueOf(signUpService.generateActivationPin());

				model.addAttribute("pin", "http://localhost:8080/activate?pin=" + stringPin);
				model.addAttribute("userSaved", true);
				model.addAttribute("qrcode", true);

				user.setDirectoryName(user.getUsername());
				user.setPin(stringPin);
				user.setPersisted(true);
				signUpService.signUpUser(user);
				storageService.init(user.getUsername());

				return "auth_templates/sign-up";


			}
		}

		return "auth_templates/sign-up";

	}


	@GetMapping("/activate")
	public String activateUser(@RequestParam("pin") String pin, Model model){
         Users user = signUpService.findByPin(pin);

         if(user == null){
			model.addAttribute("wrongPing", true);
         	return "activatePage";

		 } else {
			signUpService.activateUser(user);
			model.addAttribute("activationSuccess", true);
         	return "activatePage";
		 }


	}


}
