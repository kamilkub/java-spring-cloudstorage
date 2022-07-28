package com.cloudstorage.service;

import com.cloudstorage.model.FileObject;
import org.thymeleaf.util.StringUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;

public class SimpleFileVisitorImpl extends SimpleFileVisitor<Path> {

    private static int counter = 0;

    private static String realPath;

    private static String fullPath;

    private ArrayList<FileObject> dirsAndFiles = new ArrayList<FileObject>();

    private Path stablePath;

    public SimpleFileVisitorImpl(String stablePath) {
        this.stablePath = Paths.get(stablePath);
    }
    public ArrayList<FileObject> getDirsAndFiles() {
        return dirsAndFiles;
    }
    @Override
    public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) {
        if(dir.getParent().toString().contains(stablePath.toString()) && attrs.isDirectory()) {
            realPath = dir.getParent().toString() + File.separator + dir.getFileName().toString();
            fullPath = StringUtils.replace(realPath, stablePath.toString(), "").replace("\\", "/");
            FileObject fileObject = new FileObject(counter, dir.getFileName().toString(), dir.getParent().toString(), true);
            dirsAndFiles.add(fileObject);
            counter++;
        }
        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult visitFile(Path dir, BasicFileAttributes attrs) {
            if(!dir.toFile().getName().equals("backlog.txt")){
                realPath = dir.getParent().toString() + File.separator + dir.getFileName().toString();
                fullPath = StringUtils.replace(realPath, stablePath.toString(), "").replace("\\", "/");
                FileObject fileObject = new FileObject(counter, dir.getFileName().toString() , dir.getParent().toString(), false);
                dirsAndFiles.add(fileObject);
                counter++;
            }

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
