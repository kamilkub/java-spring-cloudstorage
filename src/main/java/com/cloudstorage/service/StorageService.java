package com.cloudstorage.service;


import com.cloudstorage.model.BaseFile;
import com.cloudstorage.repository.UsersRepository;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Optional;

@Service
public class StorageService {

	private static final Logger logger = LoggerFactory.getLogger(StorageService.class);

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
				logger.error("Method uploadFile() threw an exception : " + e.getMessage());
			}

	}


	public void init(String userDirectory) {

		try {
			Files.createDirectory(Paths.get(getBasePath() + userDirectory));
		} catch (IOException e) {
			logger.error("Method init() threw an exception : " + e.getLocalizedMessage());
		}

	}


	@EventListener(ApplicationReadyEvent.class)
    public void initializeHomeDirectory() {
	    try{
	    	if(!Paths.get(getBasePath()).toFile().exists()){
				Files.createDirectory(Paths.get(getBasePath()));
				logger.info("!< Home directory initialized successfully >!");
			}
        }catch (IOException e) {
			logger.error(e.getMessage());
        }

    }


	public boolean createDirectory(String userDirectory, String directoryName) {
		Path path = Paths.get(getBasePath() + userDirectory + File.separator + directoryName);
		try {
			if(!path.toFile().exists()){
				Files.createDirectory(path);
				return true;
			}
		} catch (IOException e) {
			logger.error(e.getMessage());
			logger.warn(e.getMessage());
		}
		return false;
	}


	public ArrayList<BaseFile> getFileAndDirectoriesPaths(String userDirectory)  {

		SimpleFileVisitorImpl fileVisitor = new SimpleFileVisitorImpl(userDirectory);

		try {
			Files.walkFileTree(Paths.get(getBasePath() + userDirectory).normalize(), fileVisitor);
		} catch (IOException e) {
			logger.error("Method getFileAndDirectoriesPaths() threw an exception the cause is " + e.getMessage());
		}

		return fileVisitor.getDirsAndFiles();

	}

	public boolean removeFileByName(String userDirectory, String fileName) {

		if (!fileName.isEmpty()) {
			try {
				Files.delete(Paths.get(getBasePath() + userDirectory + File.separator + fileName).normalize());
				return true;
			} catch (IOException e) {
				logger.error(e.getMessage());
				return false;
			}
		}

		return false;
	}

	public boolean removeDirectory(String userDirectory, String folderName) {

		File userDir = new File(getBasePath() + userDirectory + File.separator + folderName);

		if(userDir.isDirectory() && userDir.exists()){
			try {
				FileUtils.deleteDirectory(userDir);
			} catch (IOException e) {
				logger.error("Method removeDirectory() threw an exception : " + e.getLocalizedMessage());
				return false;
			}
			return true;
		}

		return false;

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
		Optional<BaseFile> fileExistsOrNot =
				getFileAndDirectoriesPaths(authName).stream().filter(baseFile -> baseFile.getFileName().equals(fileName)).findAny();

		return fileExistsOrNot.isPresent();
	}


	public String getBasePath() {
		return basePathStorage;
	}



}



//Files.walkFileTree(Paths.get(getBasePath() + userDirectory).normalize(), new SimpleFileVisitor<>() {
//		int counter = 0;
//		String stablePath = "\\cloudstorage\\" + userDirectory;
//		String realPath = "";
//		String fullPath = "";
//
//@Override
//public FileVisitResult preVisitDirectory(Path path, BasicFileAttributes basicFileAttributes) throws IOException {
//		if(path.getParent().toString().toLowerCase().contains(stablePath) && basicFileAttributes.isDirectory()) {
//		realPath = path.getParent().toString() + File.separator + path.getFileName().toString();
//		fullPath = StringUtils.replace(realPath, stablePath, "").replace("\\", "/");
//		BaseFile baseFile = new BaseFile(counter, fullPath, path.getParent().toString());
//		userDirectoriesAndFiles.add(baseFile);
//		counter++;
//		}
//		return FileVisitResult.CONTINUE;
//		}
//
//@Override
//public FileVisitResult visitFile(Path path, BasicFileAttributes basicFileAttributes) throws IOException {
//		if(path.getParent().toString().toLowerCase().contains(stablePath) && basicFileAttributes.isRegularFile()) {
//		realPath = path.getParent().toString() + File.separator + path.getFileName().toString();
//		fullPath = StringUtils.replace(realPath, stablePath, "").replace("\\", "/");
//		BaseFile baseFile = new BaseFile(counter, fullPath, path.getParent().toString());
//		userDirectoriesAndFiles.add(baseFile);
//		counter++;
//		}
//
//		return FileVisitResult.CONTINUE;
//		}
//
//@Override
//public FileVisitResult visitFileFailed(Path path, IOException e) throws IOException {
//		return FileVisitResult.CONTINUE;
//		}
//
//@Override
//public FileVisitResult postVisitDirectory(Path path, IOException e) throws IOException {
//		return FileVisitResult.CONTINUE;
//		}
//		});
