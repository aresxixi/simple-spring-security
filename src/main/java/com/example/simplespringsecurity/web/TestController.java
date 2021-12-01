package com.example.simplespringsecurity.web;

import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Collections;

@RestController
@RequestMapping({"/"})
public class TestController {

    @RequestMapping("test")
    public Object test(){
        return "This is a test";
    }

    @RequestMapping("loginSuccess")
    public Object loginSuccess(){
        return Collections.singletonMap("result", true);
    }

    @RequestMapping("loginFail")
    public Object loginFail(){
        return Collections.singletonMap("result", false);
    }
}
