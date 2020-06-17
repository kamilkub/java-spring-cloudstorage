package com.cloudstorage.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller("/user")
public class StatisticController {

    @ResponseBody
    @GetMapping("/statistics")
    public String getAllStatistics() {
        return "Hello World";
    }
}
