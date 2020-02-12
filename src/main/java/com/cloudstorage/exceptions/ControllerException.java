package com.cloudstorage.exceptions;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ControllerException {

    @GetMapping("/error")
    public String defaultErrorPage() {
        return "error";
    }


}
