package com.adamscript.tomatetoapi.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class HomeController {

    @GetMapping
    public ResponseEntity getMessage(){
        String message = "Hello Tomates!\n" +
                "\n" +
                "Uhh... you're not supposed to be here. So if you somehow able to see this message, please let me know through github.com/adamscript or adam@adamscript.com" +
                "\n" +
                "P.S. I may or may not get notified, better watch your back ;)" +
                "\n" +
                "v1.1.1-beta";

        return new ResponseEntity(message, HttpStatus.OK);
    }

}
