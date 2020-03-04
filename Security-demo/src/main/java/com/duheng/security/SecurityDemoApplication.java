package com.duheng.security;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/*************************
 Author: 杜衡
 Date: 2020/3/1
 Describe:
 *************************/
@SpringBootApplication
@RestController
public class SecurityDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(SecurityDemoApplication.class, args);
    }

    @GetMapping("/hello")
    public String hello(){
        return "Welcome to Security";
    }
}
