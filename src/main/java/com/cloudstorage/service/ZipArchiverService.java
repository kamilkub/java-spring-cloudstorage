package com.cloudstorage.service;

import com.cloudstorage.annotation.UnderImplementation;
import org.springframework.stereotype.Service;

/*
    Future implementation

 */

// TODO: Service for uploading entire folder
@UnderImplementation
@Service
public class ZipArchiverService implements Runnable {

    public ZipArchiverService() { }

    @Override
    public void run() { }

    public String getNameForArchive() { return null; }

}
