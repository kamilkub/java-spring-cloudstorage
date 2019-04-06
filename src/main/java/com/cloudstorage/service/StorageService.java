package com.cloudstorage.service;


import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class StorageService {




	public boolean uploadFile(MultipartFile storageFile, String userFolder) throws IOException {

		Path storagePath = Paths.get("/cloudstorage/"+userFolder);

		Files.copy(storageFile.getInputStream(), storagePath.resolve(storageFile.getOriginalFilename()));

        return true;
	}



	public boolean init(String userFolder) throws IOException {
		Files.createDirectory(Paths.get("/cloudstorage/"+userFolder));

		return true;

	}

}
