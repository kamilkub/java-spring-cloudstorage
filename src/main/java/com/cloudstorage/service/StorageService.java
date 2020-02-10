package com.cloudstorage.service;


import com.cloudstorage.model.BaseFile;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.util.StringUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Objects;

@Service
public class StorageService {

	@Value("${BASE_PATH}")
	private String basePath;

	public void uploadFile(MultipartFile storageFile, String userFolder) {

		Path storagePath = Paths.get(getBasePath() + userFolder);

			try {
				Files.copy(storageFile.getInputStream(), storagePath.resolve(Objects.requireNonNull(storageFile.getOriginalFilename())));

			} catch (IOException e) {
				e.printStackTrace();
			}

	}

	/*
	  $ init() method - creates new folder for signed up user
	 */

	public void init(String userDirectory) {

		try {
			Files.createDirectory(Paths.get(getBasePath() + userDirectory));
		} catch (IOException e) {
			e.printStackTrace();
		}

	}


	public void createDirectory(String userDirectory, String directoryName) {
		try {
			Files.createDirectory(Paths.get(getBasePath() + userDirectory + File.separator + directoryName));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	public ArrayList<BaseFile> getFileAndDirectoriesPaths(String userDirectory)  {

		ArrayList<BaseFile> userDirectoriesAndFiles = new ArrayList<>();


		try {
			Files.walkFileTree(Paths.get(getBasePath() + userDirectory), new FileVisitor<Path>() {

				int counter = 0;
				String stablePath = "\\cloudstorage\\" + userDirectory;
				String realPath = "";
				String fullPath = "";

				@Override
				public FileVisitResult preVisitDirectory(Path path, BasicFileAttributes basicFileAttributes) throws IOException {
					if(path.getParent().toString().toLowerCase().contains(stablePath) && basicFileAttributes.isDirectory()) {
						realPath = path.getParent().toString() + File.separator + path.getFileName().toString();
						fullPath = StringUtils.replace(realPath, stablePath, "").replace("\\", "/");
						BaseFile baseFile = new BaseFile(counter, fullPath, path.getParent().toString());
						userDirectoriesAndFiles.add(baseFile);
						counter++;
					}
					return FileVisitResult.CONTINUE;
				}

				@Override
				public FileVisitResult visitFile(Path path, BasicFileAttributes basicFileAttributes) throws IOException {
					if(path.getParent().toString().toLowerCase().contains(stablePath) && basicFileAttributes.isRegularFile()) {
						realPath = path.getParent().toString() + File.separator + path.getFileName().toString();
						fullPath = StringUtils.replace(realPath, stablePath, "").replace("\\", "/");
						BaseFile baseFile = new BaseFile(counter, fullPath, path.getParent().toString());
						userDirectoriesAndFiles.add(baseFile);
						counter++;
					}

					return FileVisitResult.CONTINUE;
				}

				@Override
				public FileVisitResult visitFileFailed(Path path, IOException e) throws IOException {
					return FileVisitResult.CONTINUE;
				}

				@Override
				public FileVisitResult postVisitDirectory(Path path, IOException e) throws IOException {
					return FileVisitResult.CONTINUE;
				}
			});
		} catch (IOException e) {
			e.printStackTrace();
		}

		return userDirectoriesAndFiles;

	}

	public boolean removeFileByName(String userDirectory, String fileName) {
		try {
			Files.delete(Paths.get(getBasePath() + userDirectory + "/" + fileName));
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}

	}


	public String getBasePath() {
		return basePath;
	}


}
