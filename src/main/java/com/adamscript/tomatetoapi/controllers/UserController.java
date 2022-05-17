package com.adamscript.tomatetoapi.controllers;

import com.adamscript.tomatetoapi.models.entities.User;
import com.adamscript.tomatetoapi.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/{id}")
    public ResponseEntity listById(@PathVariable("id") Long id){
        Optional<User> user = userService.list(id);

        if(user != null){
            return new ResponseEntity(user, HttpStatus.OK);
        }
        else{
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping
    public ResponseEntity insert(@RequestBody User user){
        Optional<User> insertedUser = userService.insert(user);

        if(insertedUser != null){
            return new ResponseEntity(insertedUser, HttpStatus.OK);
        }
        else{
            return new ResponseEntity(HttpStatus.CONFLICT);
        }
    }

    @PutMapping("/{id}/follow")
    public ResponseEntity follow(@RequestBody User userFollowing, @PathVariable("id") Long userFollowedId){
        Long userFollowingId = userFollowing.getId();

        Optional<User> user = userService.follow(userFollowingId, userFollowedId);

        if(user != null){
            return new ResponseEntity(user, HttpStatus.OK);
        }
        else{
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }

    };

}
