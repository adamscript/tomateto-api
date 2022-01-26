package com.adamscript.tomatetoapi.services;

import com.adamscript.tomatetoapi.models.entities.Post;
import com.adamscript.tomatetoapi.models.repos.PostRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class PostService {

    @Autowired
    private PostRepo postRepo;

    //fetch post content data
    public Post list(Long id){
        return postRepo.findById(id).get();
    };

    //create/edit post
    public Post insert(Post product){
        return postRepo.save(product);
    }

    //liking a post

    //disliking a post

    //deleting a post
    public void delete(Long id){
        postRepo.deleteById(id);
    }

}
