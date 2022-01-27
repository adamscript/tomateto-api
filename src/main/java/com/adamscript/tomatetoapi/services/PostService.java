package com.adamscript.tomatetoapi.services;

import com.adamscript.tomatetoapi.models.entities.Post;
import com.adamscript.tomatetoapi.models.entities.PostLike;
import com.adamscript.tomatetoapi.models.repos.PostLikeRepo;
import com.adamscript.tomatetoapi.models.repos.PostRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class PostService {

    @Autowired
    private PostRepo postRepo;

    @Autowired
    private PostLikeRepo postLikeRepo;

    //fetch post content data
    public Post list(long id){
        return postRepo.findById(id).get();
    };

    //create/edit post
    public Post insert(Post post){
        return postRepo.save(post);
    }

    //liking a post
    public PostLike like(PostLike postLike){
        return postLikeRepo.save(postLike);
    }

    //disliking a post
    public void dislike(long id){
        postLikeRepo.deleteById(id);
    }

    //deleting a post
    public void delete(long id){
        postRepo.deleteById(id);
    }

}
