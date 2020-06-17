package com.cloudstorage.controllers;


import com.cloudstorage.config.UserAuthenticationFilter;
import com.cloudstorage.model.StorageUser;
import com.cloudstorage.service.SignUpService;
import com.cloudstorage.service.StatisticsService;
import com.cloudstorage.service.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.util.stream.Collectors;

@Controller
public class SignUpController {

	private SignUpService signUpService;

	private StorageService storageService;

	private UserAuthenticationFilter userAuthenticationFilter;

	private StatisticsService statisticsService;

	@Autowired
	public SignUpController(SignUpService signUpService, StorageService storageService, UserAuthenticationFilter userAuthenticationFilter, StatisticsService statisticsService) {
		this.signUpService = signUpService;
		this.storageService = storageService;
		this.userAuthenticationFilter = userAuthenticationFilter;
		this.statisticsService = statisticsService;
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
			model.addAttribute("user", new StorageUser());
			return "auth_templates/sign-up";
	}


	@PostMapping("/sign-up")
	public String signUpUser(@Valid @ModelAttribute("user") StorageUser user, BindingResult result, Model model) {

		if (result.hasErrors()) {
			model.addAttribute("listOfErrors", result.getAllErrors()
														.stream()
														.map(DefaultMessageSourceResolvable::getDefaultMessage)
														.collect(Collectors.toList()));
		} else {

			if (signUpService.isEmailTaken(user.getEmail()) || signUpService.isUsernameTaken(user.getUsername())) {
				model.addAttribute("exists", true);
				return "auth_templates/sign-up";

			} else {
				String pinFromService = signUpService.signUpUser(user);
				storageService.init(user.getUsername());
				statisticsService.initLogFile(user.getUsername());

				model.addAttribute("pin", "http://localhost:8080/activate?pin=" + pinFromService);
				model.addAttribute("userSaved", true);
				model.addAttribute("qrcode", true);

				return "auth_templates/sign-up";

			}
		}

		return "auth_templates/sign-up";

	}


	@GetMapping("/activate")
	public String activateUser(@RequestParam("pin") String pin, Model model){

         if(signUpService.activateUser(pin)){
             model.addAttribute("activationSuccess", true);
			 return "activatePage";
         } else {
             model.addAttribute("wrongPing", true);
             return "activatePage";
         }


	}


}
