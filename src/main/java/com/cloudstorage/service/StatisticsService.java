package com.cloudstorage.service;


import com.cloudstorage.service.helper.SideTaskHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Service
public class StatisticsService {

    @Value("${BASE_PATH}")
    private String basePathStorage;

    private static final Logger logger = LoggerFactory.getLogger(StatisticsService.class);

    public void initLogFile(String userDirectory) {
        try{
            Files.createFile( Paths.get(basePathStorage + File.separator + userDirectory + File.separator + "backlog.txt"));
        }catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

    public String lastTimeUploaded(String userDirectory) {
        long lastModified = Paths.get(basePathStorage + File.separator + userDirectory + File.separator + "backlog.txt").toFile().lastModified();
        return SideTaskHelper.longToDateString(lastModified);
    }

    public void logUsersUpload(String userDirectory, String fileName) {
        File backLogFile = Paths.get(basePathStorage + File.separator + userDirectory + File.separator + "backlog.txt").toFile();
        String currentTime = SideTaskHelper.currentDateString();
        try {
            FileWriter writer = new FileWriter(backLogFile, true);
            writer.write("At [" + currentTime + "]: you have uploaded a file named ## " + fileName);
            writer.write(System.lineSeparator());
            writer.close();
        } catch (IOException e) {
            logger.error(e.getMessage());
        }

    }


}
