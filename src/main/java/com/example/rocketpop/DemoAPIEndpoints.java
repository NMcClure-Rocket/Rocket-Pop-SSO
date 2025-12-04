package com.example.rocketpop;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DemoAPIEndpoints {
    @GetMapping("/")
    public String home() {
        return "Hello World!";
    }
}
