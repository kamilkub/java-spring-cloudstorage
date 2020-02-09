package com.cloudstorage.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
public class Directory {


    private String directoryName;
    private Set<BaseFile> containsFiles;


}
