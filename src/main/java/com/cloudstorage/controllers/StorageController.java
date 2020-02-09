package com.cloudstorage.controllers;


import com.cloudstorage.model.BaseFile;
import com.cloudstorage.model.Users;
import com.cloudstorage.service.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Stream;


@RestController
public class StorageController {


	private StorageService storageService;

	@Autowired
	public StorageController(StorageService storageService) {
		this.storageService = storageService;
	}

	@PostMapping("/user/storage/upload")
	public String storageUpload(@RequestParam("files") MultipartFile[] storageFile) throws IOException {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		if(storageFile.length != 0){
			Arrays.stream(storageFile).forEach((multipartFile -> storageService.uploadFile(multipartFile, authentication.getName())));
			System.out.println("Zapisane");
		}
		return "redirect:/user";
	}

	@PostMapping("/user/delete-file/{name}")
	public String deleteFile(@PathVariable("name") String name){
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String path = authentication.getName() + "/" + name;
		boolean isRemoved = storageService.removeFileByName(path);

		 	if(isRemoved){
				return "redirect:/user";
			} else {
				System.out.println(">>>>>>>>> 404");
				return "redirect:/user";
			}
	}

	@GetMapping("/user/all-files")
	public ArrayList<BaseFile> getAllFiles() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		return storageService.getFileAndDirectoriesPaths(authentication.getName());
	}


}
