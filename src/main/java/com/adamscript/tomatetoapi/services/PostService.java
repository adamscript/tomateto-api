package com.adamscript.tomatetoapi.services;

import com.adamscript.tomatetoapi.models.entities.Post;
import com.adamscript.tomatetoapi.models.entities.User;
import com.adamscript.tomatetoapi.models.repos.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class PostService {

    @Autowired
    private PostRepository postRepository;

    //fetch post content data
    public Post list(long id){
        return postRepository.findById(id).get();
    };

    //create/edit post
    public Post insert(Post post){
        return postRepository.save(post);
    }

    //liking a post
    public Post like(Long postId, User userId){

        Post post = postRepository.findById(postId).get();
        post.getUserLiked().add(userId);

        return postRepository.save(post);
    }

    //disliking a post
    public void dislike(Long postId, User userId){

        Post post = postRepository.findById(postId).get();
        post.getUserLiked().remove(userId);

    }

    //deleting a post
    public void delete(long id){
        postRepository.deleteById(id);
    }

}
