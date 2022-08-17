package com.cloudstorage.controllers;


import com.cloudstorage.config.UserAuthenticationFilter;
import com.cloudstorage.model.FileObject;
import com.cloudstorage.service.StatisticsService;
import com.cloudstorage.service.StorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicBoolean;


@RestController
@RequestMapping("/user/storage")
@RequiredArgsConstructor
public class StorageController {
    private final UserAuthenticationFilter userAuthenticationFilter;
    private final StorageService storageService;
    private final StatisticsService statisticsService;

    @PostMapping("/upload")
    public void storageUpload(
            @RequestParam("files") MultipartFile[] storageFile,
            HttpServletResponse response
	) {
        AtomicBoolean isAvailableSpace = new AtomicBoolean(true);

        Arrays.stream(storageFile)
                .forEach((multipartFile) -> {
                    if (!multipartFile.isEmpty() && userAuthenticationFilter.isAuthenticated()
                            && storageService.isEnoughSpace(userAuthenticationFilter.getAuthenticationUsername(),
                            userAuthenticationFilter.getAuthenticationUsername(), multipartFile.getSize())) {

                        storageService.uploadFile(multipartFile, userAuthenticationFilter.getAuthenticationUsername());
                        statisticsService.logUsersUpload(userAuthenticationFilter.getAuthenticationUsername(), multipartFile.getOriginalFilename());
                    } else {
                        isAvailableSpace.set(false);

                    }
                });

        if (!isAvailableSpace.get()) {
            response.setStatus(HttpStatus.NOT_ACCEPTABLE.value());
        }

    }

    @PostMapping("/delete-file")
    public String deleteFile(@RequestBody String fileName) {

        if (userAuthenticationFilter.isAuthenticated() && !fileName.isEmpty())
            if (storageService.removeFileByName(userAuthenticationFilter.getAuthenticationUsername(), fileName))
                return "redirect:/user";

        return "redirect:/user";
    }

    @PostMapping("/delete-dir")
    public String deleteDir(@RequestBody String dirName) {

        if (userAuthenticationFilter.isAuthenticated())
            if (storageService.removeDirectory(userAuthenticationFilter.getAuthenticationUsername(), dirName))
                return "redirect:/user";

        return "redirect:/user";
    }

    @PostMapping("/create-dir")
    public String createDir(@RequestParam("dirName") String dirName) {

        if (dirName.length() > 0 && userAuthenticationFilter.isAuthenticated()) {
            if (storageService.createDirectory(userAuthenticationFilter.getAuthenticationUsername(), dirName)) {
                return "Folder created";
            } else {
                return "File already exists";
            }

        } else {
            return "redirect:/user";
        }
    }

    @GetMapping("/all-files")
    public ArrayList<FileObject> getAllFiles() {
        return userAuthenticationFilter.isAuthenticated() ?
                storageService.getFileAndDirectoriesPaths(userAuthenticationFilter.getAuthenticationUsername()) : null;
    }

    @ResponseBody
    @GetMapping(value = "/download-file", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public Object shareFileUnderUrl(@RequestParam("fileName") String fileName) {

        String userName = userAuthenticationFilter.getAuthenticationUsername();

        if (storageService.fileExists(userName, fileName) && fileName.contains(".")) {
            File file = new File(storageService.getBasePathStorage() + userName + File.separator + fileName);

            HttpHeaders header = new HttpHeaders();
            header.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName.substring(fileName.lastIndexOf("/") + 1));
            header.add("Cache-Control", "no-cache, no-store, must-revalidate");
            header.add("Pragma", "no-cache");
            header.add("Expires", "0");

            header.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            header.setContentLength(file.length());

            return new ResponseEntity<>(new FileSystemResource(file), header, HttpStatus.OK);

        } else {

            return new RedirectView("/user");
        }

    }


}
