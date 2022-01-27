package com.adamscript.tomatetoapi.controllers;

import com.adamscript.tomatetoapi.models.entities.Post;
import com.adamscript.tomatetoapi.models.entities.User;
import com.adamscript.tomatetoapi.services.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/post")
public class PostController {

    @Autowired
    private PostService postService;

    //fetch post content data
    @GetMapping("/{id}")
    public Post listById(@PathVariable("id") Long id){
        return postService.list(id);
    }

    //create/edit post
    @PostMapping
    public Post insert(@RequestBody Post post){
        return postService.insert(post);
    }

    //liking a post
    @PutMapping("/{id}/like")
    public Post like(@RequestBody User userId, @PathVariable("id") Long postId){
        return postService.like(postId, userId);
    }

    //disliking a post
    @PutMapping("/{id}/dislike")
    public void dislike(@RequestBody User userId, @PathVariable("id") Long postId){
        postService.dislike(postId, userId);
    }

    //deleting a post

}
