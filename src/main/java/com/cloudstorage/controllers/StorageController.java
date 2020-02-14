package com.cloudstorage.controllers;


import com.cloudstorage.config.UserAuthenticationFilter;
import com.cloudstorage.exceptions.MessageAdvice;
import com.cloudstorage.model.BaseFile;
import com.cloudstorage.service.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicBoolean;


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
	public void storageUpload(@RequestParam("files") MultipartFile[] storageFile, Model model, HttpServletResponse response) {

		Authentication authenticated = userAuthenticationFilter.isAuthenticated();
		AtomicBoolean isAvailableSpace = new AtomicBoolean(true);

		Arrays.stream(storageFile)
				.forEach((multipartFile) -> {
						if(!multipartFile.isEmpty()
								&& authenticated != null
								&& storageService.isEnoughSpace(authenticated.getName(), authenticated.getName(), multipartFile.getSize()))
							storageService.uploadFile(multipartFile, authenticated.getName());
						else
							isAvailableSpace.set(false);
		});

		if(!isAvailableSpace.get()) {
			response.setStatus(406);
		}

	}



	@PostMapping("/delete-file")
	public String deleteFile(@RequestBody String fileName) throws IOException {

		Authentication authenticated = userAuthenticationFilter.isAuthenticated();

		if (authenticated != null)
			if (storageService.removeFileByName(authenticated.getName(), fileName))
				return "redirect:/user";

		return "redirect:/user";
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




	@GetMapping("/download-file")
	public ResponseEntity shareFileUnderUrl(@RequestParam("fileName") String fileName) throws IOException {
		Authentication authenticated = userAuthenticationFilter.isAuthenticated();

		if(storageService.findFileByName(authenticated.getName(), fileName) && fileName.contains(".")) {
			HttpHeaders header = new HttpHeaders();
			header.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName.substring(fileName.lastIndexOf("/")+1));
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

			return ResponseEntity.badRequest().body(new MessageAdvice(HttpStatus.NOT_FOUND, "Selected file is a directory or has not been found"));
		}


	}


}
