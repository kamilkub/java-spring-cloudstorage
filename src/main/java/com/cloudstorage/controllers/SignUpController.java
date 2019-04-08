package com.cloudstorage.controllers;



import com.cloudstorage.model.Users;
import com.cloudstorage.service.SignUpService;
import com.cloudstorage.service.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.util.Random;

@Controller
public class SignUpController {


	@Autowired
	private SignUpService signUpService;

	@Autowired
	private StorageService storageService;


	@GetMapping("/signup")
	public String signUpPage(Model model){

		model.addAttribute("user", new Users());

		return "authtemps/signupPage";

	}


	@PostMapping("/signup")
	public String signUpUser(@Valid @ModelAttribute("user") Users user, BindingResult result, Model model) throws IOException {

		if (result.hasErrors()) {

		}else {

			if (signUpService.findByEmail(user.getEmail()) != null && signUpService.findByUsername(user.getUsername()) != null) {
				model.addAttribute("exists", true);
				return "authtemps/signupPage";

			} else {

				int pin = generateActivaionPin();
				String stringPin = String.valueOf(pin);

				model.addAttribute("pin", "http://localhost:8080/activate?pin="+stringPin);
				model.addAttribute("userSaved", true);
				model.addAttribute("qrcode", true);

				user.setPin(stringPin);
				signUpService.signUpUser(user);
				storageService.init(user.getUsername());


				return "authtemps/signupPage";

			}

		}
		return "authtemps/signupPage";

	}

	@GetMapping("/activate")
	public String activateUser(@RequestParam("pin") String pin, Model model){


         Users user = signUpService.findByPin(pin);

         if(user == null){
         	model.addAttribute("badPIN", true);
         	return "activatePage";
		 }else{

         	signUpService.activateUser(user);
			 model.addAttribute("activationSuccess", true);
         	return "activatePage";
		 }


	}


	@GetMapping("/signin")
	public String showLoginPage(){

		return "authtemps/signinPage";
	}


	private static int generateActivaionPin(){

		return 10000 + new Random().nextInt(90000);
	}







}
