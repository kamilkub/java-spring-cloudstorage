package com.cloudstorage.controllers;



import com.cloudstorage.service.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller
public class StorageController {

	@Autowired
	private StorageService storageService;

	@PostMapping("/user/storage/upload")
	public String storageUpload(@RequestParam("file") MultipartFile storageFile, Model model) throws IOException {

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();


		storageService.uploadFile(storageFile, authentication.getName());


		return "homePage";


	}






}
