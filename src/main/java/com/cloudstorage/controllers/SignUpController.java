package com.cloudstorage.controllers;


import com.cloudstorage.config.UserAuthenticationFilter;
import com.cloudstorage.model.StorageUser;
import com.cloudstorage.service.SignUpService;
import com.cloudstorage.service.StatisticsService;
import com.cloudstorage.service.StorageService;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class SignUpController {
	private SignUpService signUpService;
	private StorageService storageService;
	private UserAuthenticationFilter userAuthenticationFilter;
	private StatisticsService statisticsService;

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
			boolean isEmailTaken = signUpService.isEmailTaken(user.getEmail());
			boolean isUsernameTaken = signUpService.isUsernameTaken(user.getUsername());

			if (!isEmailTaken && !isUsernameTaken) {
				String pinFromService = signUpService.signUpUser(user);
				storageService.init(user.getUsername());
				statisticsService.initLogFile(user.getUsername());

				model.addAttribute("pin", "http://localhost:8080/activate?pin=" + pinFromService);
				model.addAttribute("userSaved", true);
				model.addAttribute("qrcode", true);
			}
			model.addAttribute("exists", isEmailTaken || isUsernameTaken);
		}

		return "auth_templates/sign-up";

	}

	@GetMapping("/activate")
	public String activateUser(@RequestParam("pin") String pin, Model model){
		String attribute = signUpService.activateUser(pin) ? "activationSuccess" : "wrongPing";
		model.addAttribute(attribute, true);
		return "activatePage";
	}
}