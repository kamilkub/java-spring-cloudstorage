package com.cloudstorage.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FileObject {

    private int id;

    private String fileName;

    @JsonIgnore
    private String fullPath;
    private boolean isDirectory;

    public FileObject(int id, String fileName, String fullPath, boolean isDirectory) {
        this.id = id;
        this.fileName = fileName;
        this.fullPath = fullPath;
        this.isDirectory = isDirectory;

    }
}
