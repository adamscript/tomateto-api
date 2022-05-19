package com.adamscript.tomatetoapi.services;

import com.adamscript.tomatetoapi.models.entities.Post;
import com.adamscript.tomatetoapi.models.entities.User;
import com.adamscript.tomatetoapi.models.repos.PostRepository;
import com.adamscript.tomatetoapi.models.repos.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
@Transactional
public class PostService {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    //fetch post content data
    public Post list(long id){
        return postRepository.findById(id).get();
    };

    //create post
    public Post insert(Post post){
        return postRepository.save(post);
    }

    //edit post
    public Post edit(Post post){
        Optional<Post> insertedPost = postRepository.findById(post.getId());

        post.setUserId(insertedPost.get().getUserId());
        post.setDate(insertedPost.get().getDate());
        post.setLikesCount(insertedPost.get().getLikesCount());
        post.setLikes(insertedPost.get().getLikes());
        post.setCommentsCount(insertedPost.get().getCommentsCount());
        post.setComments(insertedPost.get().getComments());

        post.setEdited(true);
        return postRepository.save(post);
    }

    //deleting a post
    public void delete(long id){
        postRepository.deleteById(id);
    }

    //like a post
    public void like(long postId, long userId){
        postRepository.likePost(postId, userId);
    }

    //unlike a post
    public void unlike(long postId, long userId){
        postRepository.unlikePost(postId, userId);
    }

}
