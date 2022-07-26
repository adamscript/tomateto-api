package com.adamscript.tomatetoapi.controllers;

import com.adamscript.tomatetoapi.helpers.handler.Response;
import com.adamscript.tomatetoapi.models.entities.Post;
import com.adamscript.tomatetoapi.models.entities.User;
import com.adamscript.tomatetoapi.services.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/post")
public class PostController {

    @Autowired
    private PostService postService;

    //fetch post content data
    @GetMapping("/{id}")
    public ResponseEntity listById(@PathVariable("id") Long id){
        Response response = postService.list(id);

        if(response.getCode() == 0){
            return new ResponseEntity(response, HttpStatus.OK);
        }
        else if(response.getCode() == 200){
            return new ResponseEntity(response, HttpStatus.NOT_FOUND);
        }
        else{
            return new ResponseEntity(response, HttpStatus.BAD_REQUEST);
        }
    }

    //create/edit post
    @PostMapping
    public ResponseEntity insert(@RequestBody Post post, Principal principal){
        Response response = postService.insert(post, principal);

        if(response.getCode() == 0){
            return new ResponseEntity(response, HttpStatus.OK);
        }
        else{
            return new ResponseEntity(response, HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping
    public ResponseEntity edit(@RequestBody Post post, Principal principal) {
        Response response = postService.edit(post, principal);

        if(response.getCode() == 0) {
            return new ResponseEntity(response, HttpStatus.OK);
        }
        else if(response.getCode() == 401){
            return new ResponseEntity(response, HttpStatus.UNAUTHORIZED);
        }
        else{
            return new ResponseEntity(response, HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/{id}/like")
    public ResponseEntity like(@PathVariable("id") long postId, Principal principal){
        Response response = postService.like(postId, principal);

        if(response.getCode() == 0){
            return new ResponseEntity(response, HttpStatus.OK);
        }
        else if(response.getCode() == 200){
            return new ResponseEntity(response, HttpStatus.NOT_FOUND);
        }
        else{
            return new ResponseEntity(response, HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/{id}/unlike")
    public ResponseEntity unlike(@PathVariable("id") long postId, Principal principal){
        Response response = postService.unlike(postId, principal);

        if(response.getCode() == 0){
            return new ResponseEntity(response, HttpStatus.OK);
        }
        else{
            return new ResponseEntity(response, HttpStatus.BAD_REQUEST);
        }
    }

    //deleting a post
    @DeleteMapping("/{postId}/delete")
    public ResponseEntity delete(@PathVariable("postId") Long postId, Principal principal){
        Response response = postService.delete(postId, principal);

        if(response.getCode() == 0){
            return new ResponseEntity(response, HttpStatus.OK);
        }
        else if(response.getCode() == 200){
            return new ResponseEntity(response, HttpStatus.NOT_FOUND);
        }
        else if(response.getCode() == 401){
            return new ResponseEntity(response, HttpStatus.UNAUTHORIZED);
        }
        else{
            return new ResponseEntity(response, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/content/{id}")
    public ResponseEntity listContent(@PathVariable("id") Long id, Principal principal){
        Response response = postService.listContent(id, principal);

        if(response.getCode() == 0){
            return new ResponseEntity(response, HttpStatus.OK);
        }
        else if(response.getCode() == 200){
            return new ResponseEntity(response, HttpStatus.NOT_FOUND);
        }
        else{
            return new ResponseEntity(response, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/content/{id}/comments")
    public ResponseEntity listContentComment(@PathVariable("id") Long id, Principal principal){
        return new ResponseEntity(postService.listContentComment(id, principal), HttpStatus.OK);
    }

    @GetMapping("/content/{id}/likes")
    public ResponseEntity listContentLikes(@PathVariable("id") Long id, Principal principal){
        Response response = postService.listContentLikes(id, principal);

        if(response.getCode() == 0){
            return new ResponseEntity(response, HttpStatus.OK);
        }
        else if(response.getCode() == 200){
            return new ResponseEntity(response, HttpStatus.NOT_FOUND);
        }
        else{
            return new ResponseEntity(response, HttpStatus.BAD_REQUEST);
        }
    }

}
