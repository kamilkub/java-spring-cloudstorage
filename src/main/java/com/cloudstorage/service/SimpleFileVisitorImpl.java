package com.cloudstorage.service;

import com.cloudstorage.model.BaseFile;
import org.springframework.beans.factory.annotation.Value;
import org.thymeleaf.util.StringUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;


public class SimpleFileVisitorImpl extends SimpleFileVisitor<Path> {

    private static int counter = 0;

    @Value("${BASE_PATH}")
    private String stablePath = "";

    private static String realPath = "";

    private static String fullPath = "";

    private ArrayList<BaseFile> dirsAndFiles = new ArrayList<BaseFile>();



    SimpleFileVisitorImpl(String userDirectory) {
        this.stablePath += userDirectory;
    }

    ArrayList<BaseFile> getDirsAndFiles() {
        return dirsAndFiles;
    }


    @Override
    public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) {
        if(dir.getParent().toString().toLowerCase().contains(stablePath) && attrs.isDirectory()) {
            realPath = dir.getParent().toString() + File.separator + dir.getFileName().toString();
            fullPath = StringUtils.replace(realPath, stablePath, "").replace("\\", "/");
            BaseFile baseFile = new BaseFile(counter, dir.getFileName().toString(), dir.getParent().toString());
            dirsAndFiles.add(baseFile);
            counter++;
        }
        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult visitFile(Path dir, BasicFileAttributes attrs) {
            realPath = dir.getParent().toString() + File.separator + dir.getFileName().toString();
            fullPath = StringUtils.replace(realPath, stablePath, "").replace("\\", "/");
            BaseFile baseFile = new BaseFile(counter, dir.getFileName().toString() , dir.getParent().toString());
            dirsAndFiles.add(baseFile);
            counter++;

        return FileVisitResult.CONTINUE;
    }


    @Override
    public FileVisitResult visitFileFailed(Path dir, IOException e)  {
        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult postVisitDirectory(Path dir, IOException e) {
        return FileVisitResult.CONTINUE;
    }
}
