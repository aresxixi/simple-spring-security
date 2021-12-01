package com.example.simplespringsecurity.web;

import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Collections;

@RestController
@RequestMapping("user")
public class UserController {

    @GetMapping
    public Object getUser(){
        return Collections.singletonMap("success", true);
    }

    @GetMapping("list")
    public Object listUser(){
        return Arrays.asList("success", "success", "success", "success");
    }

    @PostMapping
    public Object addUser(){
        return null;
    }

    @PutMapping
    public Object updateUser(){
        return null;
    }

    @DeleteMapping
    public Object deleteUser(){
        return null;
    }
}
