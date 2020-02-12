package com.cloudstorage.exceptions;


import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MultipartException;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.nio.file.NoSuchFileException;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class AdviceRestException {

    @ExceptionHandler(value = {FileNotFoundException.class, NoSuchFileException.class, IOException.class})
    public MessageAdvice handleFileNotFound() {
        return new MessageAdvice(HttpStatus.NOT_FOUND, "No such file in your directory");
    }

    @ExceptionHandler({MultipartException.class, SocketTimeoutException.class})
    public MessageAdvice socketTimeoutAndMultipartException() {
        return new MessageAdvice(HttpStatus.NOT_IMPLEMENTED, "We could not process this request. We cannot process folders");
    }


}
