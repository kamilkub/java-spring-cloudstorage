package com.cloudstorage.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class BaseFile {

    private int id;
    private String fileName;
    private String fullPath;

    public BaseFile(int id, String fileName, String fullPath) {
        this.id = id;
        this.fileName = fileName;
        this.fullPath = fullPath;
    }
}
