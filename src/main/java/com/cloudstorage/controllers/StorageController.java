package com.cloudstorage.controllers;


import com.cloudstorage.model.BaseFile;
import com.cloudstorage.service.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;


@RestController
@RequestMapping("/user")
public class StorageController {


	private StorageService storageService;

	@Autowired
	public StorageController(StorageService storageService) {
		this.storageService = storageService;
	}

	@PostMapping("/storage/upload")
	public String storageUpload(@RequestParam("files") MultipartFile[] storageFile) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		if(storageFile.length != 0){
			Arrays.stream(storageFile).forEach((multipartFile -> storageService.uploadFile(multipartFile, authentication.getName())));
			System.out.println("Zapisane");
		}
		return "redirect:/user";
	}

	@PostMapping("/delete-file")
	public String deleteFile(@RequestBody String fileName){
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		boolean isRemoved = !fileName.isEmpty() && storageService.removeFileByName(authentication.getName(), fileName);

		 	if(isRemoved){
				return "redirect:/user";
			} else {
				System.out.println(">>>>>>>>> 404");
				return "redirect:/user";
			}
	}

	@PostMapping("/create-dir/{name}")
	public String createDir(@PathVariable("name") String name) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		if(name.length() > 0){
			storageService.createDirectory(authentication.getName(), name);
			return "redirect:/user";
		} else {
			return "redirect:/user";
		}
	}


	@GetMapping("/all-files")
	public ArrayList<BaseFile> getAllFiles() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		return storageService.getFileAndDirectoriesPaths(authentication.getName());
	}


}
