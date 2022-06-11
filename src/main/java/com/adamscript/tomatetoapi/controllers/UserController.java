package com.adamscript.tomatetoapi.controllers;

import com.adamscript.tomatetoapi.helpers.handler.Response;
import com.adamscript.tomatetoapi.models.entities.User;
import com.adamscript.tomatetoapi.services.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    private UserService userService;

    @GetMapping("/{id}")
    public ResponseEntity listById(@PathVariable("id") String id){
        Response response = userService.list(id);

        if(response.getCode() == 0){
            return new ResponseEntity(response, HttpStatus.OK);
        }
        else if(response.getCode() == 100){
            return new ResponseEntity(response, HttpStatus.NOT_FOUND);
        }
        else{
            return new ResponseEntity(response, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping
    public ResponseEntity insert(@RequestBody User user){
        Response response = userService.insert(user);

        if(response.getCode() == 0){
            return new ResponseEntity(response, HttpStatus.OK);
        }
        else if(response.getCode() == 101){
            return new ResponseEntity(response, HttpStatus.CONFLICT);
        }
        else{
            return new ResponseEntity(response, HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping
    public ResponseEntity edit(@RequestBody User user){
        Response response = userService.edit(user);

        if(response.getCode() == 0){
            return new ResponseEntity(response, HttpStatus.OK);
        }
        else if(response.getCode() == 102){
            return new ResponseEntity(response, HttpStatus.NOT_FOUND);
        }
        else{
            return new ResponseEntity(response, HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/{id}/follow")
    public ResponseEntity follow(@RequestBody User userFollowing, @PathVariable("id") String userFollowedId){
        String userFollowingId = userFollowing.getId();

        Response response = userService.follow(userFollowingId, userFollowedId);

        if(response.getCode() == 0){
            return new ResponseEntity(response, HttpStatus.OK);
        }
        else{
            return new ResponseEntity(response, HttpStatus.BAD_REQUEST);
        }

    };

    @PutMapping("/{id}/unfollow")
    public ResponseEntity unfollow(@RequestBody User userFollowing, @PathVariable("id") String userFollowedId){
        String userFollowingId = userFollowing.getId();

        Response response = userService.unfollow(userFollowingId, userFollowedId);

        if(response.getCode() == 0){
            return new ResponseEntity(response, HttpStatus.OK);
        }
        else{
            return new ResponseEntity(response, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/follows")
    public ResponseEntity listFollows(){
        return new ResponseEntity(userService.listFollows(), HttpStatus.OK);
    }

    @GetMapping("/followers")
    public ResponseEntity listFollowers(){
        return new ResponseEntity(userService.listFollowers(), HttpStatus.OK);
    }

    @GetMapping("/explore")
    public ResponseEntity listAll(){
        return new ResponseEntity(userService.listAll(), HttpStatus.OK);
    }

    @GetMapping("/nonfollows")
    public ResponseEntity listNonFollows(){
        return new ResponseEntity(userService.listNonFollows(), HttpStatus.OK);
    }

    @GetMapping("/profile/{username}")
    public ResponseEntity listProfile(@PathVariable("username") String username){
        return new ResponseEntity(userService.listProfile(username), HttpStatus.OK);
    }

    @GetMapping("/profile/{id}/posts")
    public ResponseEntity listProfilePost(@PathVariable("id") String id){
        return new ResponseEntity(userService.listProfilePost(id), HttpStatus.OK);
    }

    @GetMapping("/profile/{id}/comments")
    public ResponseEntity listProfileComment(@PathVariable("id") String id){
        return new ResponseEntity(userService.listProfileComment(id), HttpStatus.OK);
    }

    @GetMapping("/profile/{id}/liked")
    public ResponseEntity listProfileLiked(@PathVariable("id") String id){
        return new ResponseEntity(userService.listProfileLiked(id), HttpStatus.OK);
    }

}
