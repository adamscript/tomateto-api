package com.adamscript.tomatetoapi.controllers;

import com.adamscript.tomatetoapi.helpers.handler.Response;
import com.adamscript.tomatetoapi.models.entities.Comment;
import com.adamscript.tomatetoapi.models.entities.User;
import com.adamscript.tomatetoapi.services.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/comment")
public class CommentController {

    @Autowired
    private CommentService commentService;

    //fetch comment content data
    @GetMapping("/{id}")
    public ResponseEntity listById(@PathVariable("id") Long id){
        Response response = commentService.list(id);

        if(response.getCode() == 0){
            return new ResponseEntity(response, HttpStatus.OK);
        }
        else if(response.getCode() == 300){
            return new ResponseEntity(response, HttpStatus.NOT_FOUND);
        }
        else{
            return new ResponseEntity(response, HttpStatus.BAD_REQUEST);
        }
    }

    //create/edit comment
    @PostMapping
    public ResponseEntity insert(@RequestBody Comment comment, Principal principal){
        Response response = commentService.insert(comment, principal);

        if(response.getCode() == 0){
            return new ResponseEntity(response, HttpStatus.OK);
        }
        else{
            return new ResponseEntity(response, HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/{id}/like")
    public ResponseEntity like(@PathVariable("id") long commentId, Principal principal){
        Response response = commentService.like(commentId, principal);

        if(response.getCode() == 0){
            return new ResponseEntity(response, HttpStatus.OK);
        }
        else if(response.getCode() == 300){
            return new ResponseEntity(response, HttpStatus.NOT_FOUND);
        }
        else{
            return new ResponseEntity(response, HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/{id}/unlike")
    public ResponseEntity unlike(@PathVariable("id") long commentId, Principal principal){
        Response response = commentService.unlike(commentId, principal);

        if(response.getCode() == 0){
            return new ResponseEntity(response, HttpStatus.OK);
        }
        else{
            return new ResponseEntity(response, HttpStatus.BAD_REQUEST);
        }
    }

    //deleting a comment
    @DeleteMapping("/{commentId}/delete")
    public ResponseEntity delete(@PathVariable("commentId") Long commentId, Principal principal){
        Response response = commentService.delete(commentId, principal);

        if(response.getCode() == 0){
            return new ResponseEntity(response, HttpStatus.OK);
        }
        else if(response.getCode() == 300){
            return new ResponseEntity(response, HttpStatus.NOT_FOUND);
        }
        else if(response.getCode() == 401){
            return new ResponseEntity(response, HttpStatus.UNAUTHORIZED);
        }
        else{
            return new ResponseEntity(response, HttpStatus.BAD_REQUEST);
        }
    }
    
}
