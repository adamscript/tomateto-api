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

    @PutMapping
    public Post edit(@RequestBody Post post) {
        return postService.edit(post);
    }

    //deleting a post
    @DeleteMapping("/{postId}/delete")
    public void delete(@PathVariable("postId") Long postId){
        postService.delete(postId);
    }

    @PutMapping("/{id}/like")
    public void like(@PathVariable("id") long postId, @RequestBody User user){
        long userId = user.getId();

        postService.like(postId, userId);
    }

    @PutMapping("/{id}/unlike")
    public void unlike(@PathVariable("id") long postId, @RequestBody User user){
        long userId = user.getId();

        postService.unlike(postId, userId);
    }
}
