package com.cloudstorage;

import com.cloudstorage.service.StorageService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.nio.file.Paths;

import static org.junit.Assert.assertTrue;


@RunWith(SpringRunner.class)
@SpringBootTest
public class StorageServiceTest {

    @Autowired
    private StorageService storageService;


    @Test
    public void testIsEnoughSpaceForUpload() {
        assertTrue("You cannot download this file cause it's bigger than available space", storageService.isEnoughSpace("kamkk", "kamkk", 50000));
    }

    @Test
    public void testRecursiveFolderDelete() throws IOException {
        assertTrue("Folder has not been deleted", storageService.removeDirectory("kamkk", "pies"));

    }

    @Test
    public void testMethodGetFolderSize() {
        System.out.println(storageService.getFolderSize(Paths.get(storageService.getBasePath() + "kamkk")));
    }




}
