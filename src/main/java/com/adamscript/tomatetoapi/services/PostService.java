package com.adamscript.tomatetoapi.services;

import com.adamscript.tomatetoapi.helpers.handler.Response;
import com.adamscript.tomatetoapi.helpers.service.ServiceStatus;
import com.adamscript.tomatetoapi.models.entities.Post;
import com.adamscript.tomatetoapi.models.entities.User;
import com.adamscript.tomatetoapi.models.repos.PostRepository;
import com.adamscript.tomatetoapi.models.repos.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    //fetch post content data
    public Response list(long id){
        Optional<Post> post = postRepository.findById(id);

        if(post.isEmpty()){
            return new Response(null, ServiceStatus.POST_NOT_FOUND);
        }
        else if(post.isPresent()){
            return new Response(post, ServiceStatus.SUCCESS);
        }
        else{
            return new Response(null, ServiceStatus.ERROR);
        }
    }

    //create post
    public Response insert(Post post){
        if(post.getUserId() == null){
            return new Response(null, ServiceStatus.POST_USER_EMPTY);
        }
        else if(post.getContent() == null){
            return new Response(null, ServiceStatus.POST_CONTENT_EMPTY);
        }
        else if(post.getUserId() != null && post.getContent() != null){
            Optional<User> user = userRepository.findById(post.getUserId().getId());

            if(user.isEmpty()){
                return new Response(null, ServiceStatus.USER_DOES_NOT_EXIST);
            }
            else if(user.isPresent()){
                return new Response(postRepository.save(post), ServiceStatus.SUCCESS);
            }
            else{
                return new Response(null, ServiceStatus.ERROR);
            }
        }
        else{
            return new Response(null, ServiceStatus.ERROR);
        }
    }

    //edit post
    public Response edit(Post post){
        Optional<Post> insertedPost = postRepository.findById(post.getId());

        if(post.getId() == 0){
            return new Response(null, ServiceStatus.POST_DOES_NOT_EXIST);
        }
        else if(post.getContent() == null){
            return new Response(null, ServiceStatus.POST_CONTENT_EMPTY);
        }
        else if(insertedPost.isEmpty()){
            return new Response(null, ServiceStatus.POST_DOES_NOT_EXIST);
        }
        else if(insertedPost.isPresent()){
            post.setUserId(insertedPost.get().getUserId());
            post.setPicture(insertedPost.get().getPicture());
            post.setDate(insertedPost.get().getDate());
            post.setLikesCount(insertedPost.get().getLikesCount());
            post.setLikes(insertedPost.get().getLikes());
            post.setCommentsCount(insertedPost.get().getCommentsCount());
            post.setComments(insertedPost.get().getComments());
            post.setEdited(true);

            return new Response(postRepository.save(post), ServiceStatus.SUCCESS);
        }
        else{
            return new Response(null, ServiceStatus.ERROR);
        }
    }

    //like a post
    public Response like(long postId, long userId){
        Optional<User> user = userRepository.findById(userId);
        Optional<Post> post = postRepository.findById(postId);

        List<Post> likedPost = postRepository.findLike(postId, user);

        if(post.isEmpty()){
            return new Response(null, ServiceStatus.POST_DOES_NOT_EXIST);
        }
        else if(user.isEmpty()){
            return new Response(null, ServiceStatus.USER_DOES_NOT_EXIST);
        }
        else if(!likedPost.isEmpty()){
            return new Response(null, ServiceStatus.POST_LIKED_ALREADY);
        }
        else if(likedPost.isEmpty()){
            postRepository.likePost(postId, userId);
            return new Response(null, ServiceStatus.SUCCESS);
        }
        else{
            return new Response(null, ServiceStatus.ERROR);
        }
    }

    //unlike a post
    public Response unlike(long postId, long userId){
        Optional<User> user = userRepository.findById(userId);

        List<Post> likedPost = postRepository.findLike(postId, user);

        if(likedPost.isEmpty()){
            return new Response(null, ServiceStatus.POST_NOT_LIKED);
        }
        else if(!likedPost.isEmpty()){
            postRepository.unlikePost(postId, userId);
            return new Response(null, ServiceStatus.SUCCESS);
        }
        else{
            return new Response(null, ServiceStatus.ERROR);
        }
    }

    //deleting a post
    public Response delete(long id){
        Optional<Post> post = postRepository.findById(id);

        if(post.isEmpty()){
            return new Response(null, ServiceStatus.POST_NOT_FOUND);
        }
        else if(post.isPresent()){
            postRepository.deleteById(id);
            return new Response(null, ServiceStatus.SUCCESS);
        }
        else{
            return new Response(null, ServiceStatus.ERROR);
        }
    }
}
