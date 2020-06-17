package com.cloudstorage.service;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;

import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest
public class StatisticsServiceTest {

    private String userDirectory = "test";

    @Autowired
    private StatisticsService statisticsService;

    @Autowired
    private StorageService storageService;

    @Test
    public void initLogFile() {
        storageService.init(userDirectory);
        statisticsService.initLogFile(userDirectory);
        assertTrue(Paths.get(storageService.getBasePathStorage() + userDirectory + "/backlog.txt").toFile().exists());
    }

   @Test
    public void lastTimeUploaded() throws IOException {
        //1 # First Case
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        assertNotSame(statisticsService.lastTimeUploaded(userDirectory), LocalDateTime.now().toString());

       // 2 # Second Case
       String dateBeforeModification = statisticsService.lastTimeUploaded(userDirectory);
       File file = Paths.get(storageService.getBasePathStorage() + userDirectory + "/backlog.txt").toFile();

       FileOutputStream outputStream = new FileOutputStream(file);
       outputStream.write("anything".getBytes());
       outputStream.close();

       assertNotSame(dateBeforeModification, statisticsService.lastTimeUploaded(userDirectory));


   }

}