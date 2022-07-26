package com.adamscript.tomatetoapi.controllers;

import com.adamscript.tomatetoapi.helpers.handler.Response;
import com.adamscript.tomatetoapi.models.entities.User;
import com.adamscript.tomatetoapi.services.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

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
    public ResponseEntity insert(@RequestBody User user, Principal principal){
        Response response = userService.insert(user, principal);

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
    public ResponseEntity edit(@RequestBody User user, Principal principal){
        Response response = userService.edit(user, principal);

        if(response.getCode() == 0){
            return new ResponseEntity(response, HttpStatus.OK);
        }
        else if(response.getCode() == 101){
            return new ResponseEntity(response, HttpStatus.CONFLICT);
        }
        else if(response.getCode() == 102){
            return new ResponseEntity(response, HttpStatus.NOT_FOUND);
        }
        else{
            return new ResponseEntity(response, HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/{id}/follow")
    public ResponseEntity follow(@PathVariable("id") String userFollowedId, Principal principal){
        Response response = userService.follow(userFollowedId, principal);

        if(response.getCode() == 0){
            return new ResponseEntity(response, HttpStatus.OK);
        }
        else{
            return new ResponseEntity(response, HttpStatus.BAD_REQUEST);
        }

    };

    @PutMapping("/{id}/unfollow")
    public ResponseEntity unfollow(@PathVariable("id") String userFollowedId, Principal principal){
        Response response = userService.unfollow(userFollowedId, principal);

        if(response.getCode() == 0){
            return new ResponseEntity(response, HttpStatus.OK);
        }
        else{
            return new ResponseEntity(response, HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity delete(Principal principal){
        Response response = userService.delete(principal);

        if(response.getCode() == 0){
            return new ResponseEntity(response, HttpStatus.OK);
        }
        else{
            return new ResponseEntity(response, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{username}/follows")
    public ResponseEntity listFollows(@PathVariable("username") String username, Principal principal){
        return new ResponseEntity(userService.listFollows(username, principal), HttpStatus.OK);
    }

    @GetMapping("/{username}/followers")
    public ResponseEntity listFollowers(@PathVariable("username") String username, Principal principal){
        return new ResponseEntity(userService.listFollowers(username, principal), HttpStatus.OK);
    }

    @GetMapping("/explore")
    public ResponseEntity listAll(Principal principal){
        return new ResponseEntity(userService.listAll(principal), HttpStatus.OK);
    }

    @GetMapping("/nonfollows")
    public ResponseEntity listNonFollows(Principal principal){
        return new ResponseEntity(userService.listNonFollows(principal), HttpStatus.OK);
    }

    @GetMapping("/profile/{username}")
    public ResponseEntity listProfile(@PathVariable("username") String username, Principal principal){
        Response response = userService.listProfile(username, principal);

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

    @GetMapping("/profile/{id}/posts")
    public ResponseEntity listProfilePost(@PathVariable("id") String id, Principal principal){
        return new ResponseEntity(userService.listProfilePost(id, principal), HttpStatus.OK);
    }

    @GetMapping("/profile/{id}/comments")
    public ResponseEntity listProfileComment(@PathVariable("id") String id, Principal principal){
        return new ResponseEntity(userService.listProfileComment(id, principal), HttpStatus.OK);
    }

    @GetMapping("/profile/{id}/liked")
    public ResponseEntity listProfileLiked(@PathVariable("id") String id, Principal principal){
        return new ResponseEntity(userService.listProfileLiked(id, principal), HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity listByKeyword(@RequestParam String q, Principal principal){
        return new ResponseEntity(userService.listByKeyword(q, principal), HttpStatus.OK);
    }

}
