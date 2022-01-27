package com.adamscript.tomatetoapi.controllers;

import com.adamscript.tomatetoapi.models.entities.User;
import com.adamscript.tomatetoapi.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/{id}")
    public User listById(@PathVariable("id") Long id){
        return userService.list(id);
    }


}
