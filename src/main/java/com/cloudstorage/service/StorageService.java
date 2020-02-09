package com.cloudstorage.service;


import com.cloudstorage.model.BaseFile;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Objects;

@Service
public class StorageService {

	@Value("${BASE_PATH}")
	private String basePath;

	public boolean uploadFile(MultipartFile storageFile, String userFolder) {

		Path storagePath = Paths.get(getBasePath()+userFolder);

			try {
				Files.copy(storageFile.getInputStream(), storagePath.resolve(Objects.requireNonNull(storageFile.getOriginalFilename())));
				return true;

			} catch (IOException e) {
				e.printStackTrace();
			}

		return false;
	}

	/*
	  $ init() method - creates new folder for signed up user
	 */

	public boolean init(String userFolder) {

		try {
			Files.createDirectory(Paths.get(getBasePath()+userFolder));
			return true;

		} catch (IOException e) {
			e.printStackTrace();
		}

		return false;
	}


	public ArrayList<BaseFile> getFileAndDirectoriesPaths(String path)  {

		ArrayList<BaseFile> userFilePaths = new ArrayList<>();



		try {
			Files.walkFileTree(Paths.get(getBasePath() + "/" + path), new FileVisitor<Path>() {

				int counter = 0;

				@Override
				public FileVisitResult preVisitDirectory(Path path, BasicFileAttributes basicFileAttributes) throws IOException {
					return FileVisitResult.CONTINUE;
				}

				@Override
				public FileVisitResult visitFile(Path path, BasicFileAttributes basicFileAttributes) throws IOException {
					System.out.println(basicFileAttributes.size());
					if(basicFileAttributes.isRegularFile() || basicFileAttributes.isDirectory()) {
						userFilePaths.add(new BaseFile(counter, path.getFileName().toString(), path.getRoot().toString()));
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

		return userFilePaths;

	}

	public boolean removeFileByName(String name) {
		try {
			Files.delete(Paths.get(getBasePath()+name));
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
