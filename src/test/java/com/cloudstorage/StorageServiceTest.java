package com.cloudstorage;

import com.cloudstorage.model.BaseFile;
import com.cloudstorage.service.StorageService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.nio.file.Paths;
import java.util.ArrayList;

import static org.junit.Assert.*;


@RunWith(SpringRunner.class)
@SpringBootTest
public class StorageServiceTest {

    @Autowired
    private StorageService storageService;


    @Test
    public void testIsEnoughSpaceForUpload() {
        assertTrue("You cannot download this file cause it's bigger than available space", storageService.isEnoughSpace("kamkk", "kamkk", 50000));
        assertFalse(storageService.isEnoughSpace("kamkk", "kamkk", 1080000001));
        assertFalse(storageService.isEnoughSpace("kamkk", "kamkk", 1080000003));
        assertFalse(storageService.isEnoughSpace("kamkk", "kamkk", 1080052431));
    }

    @Test
    public void testRecursiveFolderDelete() {
        storageService.createDirectory("kamkk", "test-dir");
        storageService.createDirectory("kamkk", "test-dir/test-exp");
        assertTrue("Folder has not been deleted", storageService.removeDirectory("kamkk", "test-dir"));

    }

    @Test
    public void testMethodGetFolderSize() {
        assertEquals(0,storageService.getFolderSize(Paths.get(storageService.getBasePath() + "kamkk")));
    }

    @Test
    public void testGetAllFilesAndDirsMethod() {
        ArrayList<BaseFile> fileAndDirectoriesPaths = storageService.getFileAndDirectoriesPaths("kamkk");

//        fileAndDirectoriesPaths.forEach(baseFile -> System.out.println(baseFile.getFileName()));
    }




}
