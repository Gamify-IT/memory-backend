package de.unistuttgart.memorybackend.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/configurations")
public class ConfigController {

    @GetMapping("")
    public void test(){
        System.out.println("Hello World");
    }
}
