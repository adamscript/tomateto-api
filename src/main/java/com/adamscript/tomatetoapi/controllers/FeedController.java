package com.adamscript.tomatetoapi.controllers;

import com.adamscript.tomatetoapi.services.FeedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/feed")
public class FeedController {

    @Autowired
    private FeedService feedService;

    @GetMapping
    public ResponseEntity listFeedPost(){
        return new ResponseEntity(feedService.listFeedPost(), HttpStatus.OK);
    }

    @GetMapping("/following")
    public ResponseEntity listFeedPostByFollow(Principal principal){
        return new ResponseEntity(feedService.listFeedPostByFollow("1l", principal), HttpStatus.OK);
    }

    @GetMapping("/top")
    public ResponseEntity listFeedPostTop(){
        return new ResponseEntity(feedService.listFeedPostSortTop(), HttpStatus.OK);
    }

    @GetMapping("/latest")
    public ResponseEntity listFeedPostLatest(){
        return new ResponseEntity(feedService.listFeedPostSortLatest(), HttpStatus.OK);
    }

}
