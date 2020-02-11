package com.cloudstorage.controllers;


import com.cloudstorage.config.UserAuthenticationFilter;
import com.cloudstorage.model.BaseFile;
import com.cloudstorage.service.StorageService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;


@RestController
@RequestMapping("/user")
public class StorageController {

	private UserAuthenticationFilter userAuthenticationFilter;

	private StorageService storageService;

	@Autowired
	public StorageController(StorageService storageService, UserAuthenticationFilter userAuthenticationFilter) {
		this.storageService = storageService;
		this.userAuthenticationFilter = userAuthenticationFilter;
	}

	@PostMapping("/storage/upload")
	public String storageUpload(@RequestParam("files") MultipartFile[] storageFile) {

		Authentication authenticated = userAuthenticationFilter.isAuthenticated();

		Arrays.stream(storageFile)
				.forEach((multipartFile) -> {
						if(!multipartFile.isEmpty() && authenticated != null)
							storageService.uploadFile(multipartFile, authenticated.getName());
		});


		return "redirect:/user";
	}

	@PostMapping("/delete-file")
	public String deleteFile(@RequestBody String fileName){

		Authentication authenticated = userAuthenticationFilter.isAuthenticated();

		boolean isRemoved = (!fileName.isEmpty() && authenticated != null) && storageService.removeFileByName(authenticated.getName(), fileName);

		 	if(isRemoved){
				return "redirect:/user";
			} else {
				System.out.println(">>>>>>>>> 404");
				return "redirect:/user";
			}
	}

	@PostMapping("/create-dir")
	public String createDir(@RequestParam("dirName") String dirName) {

		Authentication authenticated = userAuthenticationFilter.isAuthenticated();

		if(dirName.length() > 0 && authenticated != null){
			storageService.createDirectory(authenticated.getName(), dirName);
			return "redirect:/user";
		} else {
			return "redirect:/user";
		}
	}


	@GetMapping("/all-files")
	public ArrayList<BaseFile> getAllFiles() {
		Authentication authenticated = userAuthenticationFilter.isAuthenticated();
		return authenticated != null ? storageService.getFileAndDirectoriesPaths(authenticated.getName()) : null;
	}

	@SneakyThrows
	@GetMapping("/download-file")
	public ResponseEntity<Resource> shareFileUnderUrl(@RequestParam("fileName") String fileName) {
		Authentication authenticated = userAuthenticationFilter.isAuthenticated();
		if(storageService.findFileByName(authenticated.getName(), fileName) && fileName.contains(".")) {
			HttpHeaders header = new HttpHeaders();
			header.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename="+fileName.substring(fileName.lastIndexOf("/")+1));
			header.add("Cache-Control", "no-cache, no-store, must-revalidate");
			header.add("Pragma", "no-cache");
			header.add("Expires", "0");

			ByteArrayResource resource =
					new ByteArrayResource(Files.readAllBytes(Paths.get(storageService.getBasePath() + authenticated.getName() + fileName)));

			return ResponseEntity.ok()
					.headers(header)
					.contentLength(resource.contentLength())
					.contentType(MediaType.parseMediaType("application/octet-stream"))
					.body(resource);
		} else {

			return null;
		}


	}


}
