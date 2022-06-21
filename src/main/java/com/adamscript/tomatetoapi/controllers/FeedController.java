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
    public ResponseEntity listFeedPost(Principal principal){
        return new ResponseEntity(feedService.listFeedPost(principal), HttpStatus.OK);
    }

    @GetMapping("/following")
    public ResponseEntity listFeedPostByFollow(Principal principal){
        return new ResponseEntity(feedService.listFeedPostByFollow(principal), HttpStatus.OK);
    }

    @GetMapping("/top")
    public ResponseEntity listFeedPostTop(Principal principal){
        return new ResponseEntity(feedService.listFeedPostSortTop(principal), HttpStatus.OK);
    }

    @GetMapping("/latest")
    public ResponseEntity listFeedPostLatest(Principal principal){
        return new ResponseEntity(feedService.listFeedPostSortLatest(principal), HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity listByKeyword(@RequestParam String q, Principal principal){
        return new ResponseEntity(feedService.listByKeyword(q, principal), HttpStatus.OK);
    }

}
