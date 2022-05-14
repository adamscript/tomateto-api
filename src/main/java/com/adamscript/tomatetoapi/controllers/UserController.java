package com.adamscript.tomatetoapi.controllers;

import com.adamscript.tomatetoapi.models.entities.User;
import com.adamscript.tomatetoapi.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/{id}")
    public Optional<User> listById(@PathVariable("id") Long id){
        Optional<User> user = userService.list(id);

        if(user != null){
            return user;
        }
        else{
            return null;
        }
    }


}
