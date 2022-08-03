package com.cloudstorage.exceptions;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Setter
@Getter
@NoArgsConstructor
public class ResponseDto {

    private HttpStatus httpStatus;
    private String message;

    public ResponseDto(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }

}
