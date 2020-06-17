package com.cloudstorage;

import com.cloudstorage.model.FileObject;
import com.cloudstorage.service.StorageService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.nio.file.Path;
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
        assertEquals(0, storageService.getFolderSize(Paths.get(storageService.getBasePathStorage() + "kamkk")));
    }

    @Test
    public void testGetAllFilesAndDirsMethod() {
        ArrayList<FileObject> fileAndDirectoriesPaths = storageService.getFileAndDirectoriesPaths("test");
        fileAndDirectoriesPaths.forEach(fileObject -> {
            System.out.println(fileObject.getFileName() + " " + fileObject.getFullPath());
        });
    }


    @Test
    public void doesPathEquality() {
        Path path1 = Paths.get(storageService.getBasePathStorage() + "test");
        Path path2 = Paths.get(storageService.getBasePathStorage() + "test" + File.separator + "asd");
        assertTrue("How?", path2.toString().contains(path1.toString()));
    }




}
