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
import org.springframework.security.task.DelegatingSecurityContextAsyncTaskExecutor;
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
import java.util.function.Predicate;


@RestController
@RequestMapping("/user/storage")
public class StorageController {

	private UserAuthenticationFilter userAuthenticationFilter;

	private StorageService storageService;

	@Autowired
	public StorageController(StorageService storageService, UserAuthenticationFilter userAuthenticationFilter) {
		this.storageService = storageService;
		this.userAuthenticationFilter = userAuthenticationFilter;
	}



	@PostMapping("/upload")
	public void storageUpload(@RequestParam("files") MultipartFile[] storageFile, Model model, HttpServletResponse response) {

		AtomicBoolean isAvailableSpace = new AtomicBoolean(true);

		Arrays.stream(storageFile)
				.forEach((multipartFile) -> {
						if(!multipartFile.isEmpty()
								&& userAuthenticationFilter.isAuthenticatedBool()
								&& storageService.isEnoughSpace(userAuthenticationFilter.getAuthenticationUsername(), userAuthenticationFilter.getAuthenticationUsername(), multipartFile.getSize()))
							storageService.uploadFile(multipartFile, userAuthenticationFilter.getAuthenticationUsername());
						else
							isAvailableSpace.set(false);
		});

		if(!isAvailableSpace.get()) {
			response.setStatus(406);
		}

	}



	@PostMapping("/delete-file")
	public String deleteFile(@RequestBody String fileName) throws IOException {

		if (userAuthenticationFilter.isAuthenticatedBool() && !fileName.isEmpty())
			if (storageService.removeFileByName(userAuthenticationFilter.getAuthenticationUsername(), fileName))
				return "redirect:/user";

		return "redirect:/user";
	}

	@PostMapping("/delete-dir")
	public String deleteDir(@RequestBody String dirName) {

		if (userAuthenticationFilter.isAuthenticatedBool())
			if (storageService.removeDirectory(userAuthenticationFilter.getAuthenticationUsername(), dirName))
				return "redirect:/user";

		return "redirect:/user";
	}




	@PostMapping("/create-dir")
	public String createDir(@RequestParam("dirName") String dirName) {

		if(dirName.length() > 0 && userAuthenticationFilter.isAuthenticatedBool()){
			storageService.createDirectory(userAuthenticationFilter.getAuthenticationUsername(), dirName);
			return "redirect:/user";
		} else {
			return "redirect:/user";
		}
	}



	@GetMapping("/all-files")
	public ArrayList<BaseFile> getAllFiles() {
		System.out.println("Authenticated directory >>> : " + userAuthenticationFilter.getAuthenticatedDirectory());
		return userAuthenticationFilter.isAuthenticatedBool() ?
				storageService.getFileAndDirectoriesPaths(userAuthenticationFilter.getAuthenticationUsername()) : null;
	}




	@GetMapping("/download-file")
	public ResponseEntity shareFileUnderUrl(@RequestParam("fileName") String fileName) throws IOException {

		if(storageService.findFileByName(userAuthenticationFilter.getAuthenticationUsername(), fileName) && fileName.contains(".")) {
			HttpHeaders header = new HttpHeaders();
			header.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName.substring(fileName.lastIndexOf("/")+1));
			header.add("Cache-Control", "no-cache, no-store, must-revalidate");
			header.add("Pragma", "no-cache");
			header.add("Expires", "0");

			ByteArrayResource resource =
					new ByteArrayResource
							(Files.readAllBytes(Paths.get(storageService.getBasePath() + userAuthenticationFilter.getAuthenticationUsername() + fileName)));

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
