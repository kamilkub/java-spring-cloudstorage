package com.cloudstorage.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.File;

@Getter
@Setter
public class BaseFile  {

    private int id;
    private String fileName;

    @JsonIgnore
    private String fullPath;
    private String fileType;

    public BaseFile(int id, String fileName, String fullPath) {
        this.id = id;
        this.fileName = fileName;
        this.fullPath = fullPath;
        if(!fileName.contains(".")) {
            this.fileType = "dir";
            this.fileName += "/";
        } else {
            this.fileType = "file";
        }
    }

}
