package com.cloudstorage.service;


import com.cloudstorage.model.BaseFile;
import com.cloudstorage.repository.UsersRepository;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.util.StringUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Objects;

@Service
public class StorageService {

	@Value("${BASE_PATH}")
	private String basePathStorage;

	private UsersRepository usersRepository;


	@Autowired
	public StorageService(UsersRepository usersRepository) {
		this.usersRepository = usersRepository;
	}

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
			Files.walkFileTree(Paths.get(getBasePath() + userDirectory).normalize(), new FileVisitor<Path>() {

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

	public boolean removeFileByName(String userDirectory, String fileName) throws IOException {

		if(!fileName.isEmpty()) {
			Files.delete(Paths.get(getBasePath() + userDirectory + File.separator + fileName).normalize());
			return true;
		} else {
			return false;
		}

	}

	public boolean removeDirectory(String userDirectory, String folderName) {

		File userDir = new File(getBasePath() + userDirectory + File.separator + folderName);

		if(userDir.isDirectory() && userDir.exists()){
			try {
				FileUtils.deleteDirectory(userDir);
			} catch (IOException e) {
				e.printStackTrace();
			}
			return true;
		}else {
			return false;
		}

 	}




	private boolean hasAvailableSpace(String userDirectory, long userDiskSpace) {
		Path path = Paths.get(getBasePath() + userDirectory);

		long actualUserFolder = getFolderSize(path);

		return actualUserFolder < userDiskSpace;

	}


	private long getAvailableSpace(String userDirectory, long userDiskSpace) {
		Path path = Paths.get(getBasePath() + userDirectory);
		long actualUserFolder = getFolderSize(path);


		if(hasAvailableSpace(userDirectory, userDiskSpace)) {
			return userDiskSpace - actualUserFolder;
		} else {
			return 0;
		}

	}

	public boolean isEnoughSpace(String userDirectory, String username, long fileSize) {

		long userDiskSpace = usersRepository.findByUsername(username).getDiskSpace();

		long availableSpace = getAvailableSpace(userDirectory, userDiskSpace);

		return availableSpace >= fileSize;
	}

	public long getFolderSize(Path path) {
		long size = 0;

	 	if(path.toFile().exists())
			for(File file : Objects.requireNonNull(path.toFile().listFiles())){
				if(file.isFile()) {
					size += file.length();
				} else {
					size += getFolderSize(file.toPath());
				}
			}

		return size;
	}


	public boolean findFileByName(String authName, String fileName) {
		ArrayList<BaseFile> fileAndDirectoriesPaths = getFileAndDirectoriesPaths(authName);
		LinkedHashSet<String> fileNames = new LinkedHashSet<>();
		fileAndDirectoriesPaths.forEach((file -> fileNames.add(file.getFileName())));
		return fileNames.contains(fileName);
	}

	public String getBasePath() {
		return basePathStorage;
	}





}
