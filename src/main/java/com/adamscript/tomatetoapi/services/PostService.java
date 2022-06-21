package com.adamscript.tomatetoapi.services;

import com.adamscript.tomatetoapi.helpers.handler.Response;
import com.adamscript.tomatetoapi.helpers.service.ServiceStatus;
import com.adamscript.tomatetoapi.models.dto.FeedCommentDTO;
import com.adamscript.tomatetoapi.models.dto.FeedPostDTO;
import com.adamscript.tomatetoapi.models.dto.PostContentDTO;
import com.adamscript.tomatetoapi.models.entities.Comment;
import com.adamscript.tomatetoapi.models.entities.Post;
import com.adamscript.tomatetoapi.models.entities.User;
import com.adamscript.tomatetoapi.models.repos.CommentRepository;
import com.adamscript.tomatetoapi.models.repos.PostRepository;
import com.adamscript.tomatetoapi.models.repos.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.security.Principal;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
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
    public Response insert(Post post, Principal principal){
        post.setUser(userRepository.getById(principal.getName()));

        if(post.getUser() == null){
            return new Response(null, ServiceStatus.POST_USER_EMPTY);
        }
        else if(post.getContent() == null){
            return new Response(null, ServiceStatus.POST_CONTENT_EMPTY);
        }
        else if(post.getUser() != null && post.getContent() != null){
            Optional<User> user = userRepository.findById(post.getUser().getId());

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
    public Response edit(Post post, Principal principal){
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
        else if(insertedPost.get().getUser() != userRepository.getById(principal.getName()) || userRepository.findById(principal.getName()).isEmpty()){
            return new Response(null, ServiceStatus.UNAUTHORIZED);
        }
        else if(insertedPost.isPresent()){
            post.setUser(insertedPost.get().getUser());
            post.setPhoto(insertedPost.get().getPhoto());
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
    public Response like(long postId, Principal principal){
        Optional<User> user = userRepository.findById(principal.getName());
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
            postRepository.likePost(postId, principal.getName());
            setLikeCount(post.get());

            return new Response(null, ServiceStatus.SUCCESS);
        }
        else{
            return new Response(null, ServiceStatus.ERROR);
        }
    }

    //unlike a post
    public Response unlike(long postId, Principal principal){
        Optional<User> user = userRepository.findById(principal.getName());
        Optional<Post> post = postRepository.findById(postId);

        List<Post> likedPost = postRepository.findLike(postId, user);

        if(likedPost.isEmpty()){
            return new Response(null, ServiceStatus.POST_NOT_LIKED);
        }
        else if(!likedPost.isEmpty()){
            postRepository.unlikePost(postId, principal.getName());
            setLikeCount(post.get());

            return new Response(null, ServiceStatus.SUCCESS);
        }
        else{
            return new Response(null, ServiceStatus.ERROR);
        }
    }

    //deleting a post
    public Response delete(long id, Principal principal){
        Optional<Post> post = postRepository.findById(id);

        if(post.isEmpty()){
            return new Response(null, ServiceStatus.POST_NOT_FOUND);
        }
        else if(post.get().getUser() != userRepository.getById(principal.getName()) || userRepository.findById(principal.getName()).isEmpty()){
            return new Response(null, ServiceStatus.UNAUTHORIZED);
        }
        else if(post.isPresent()){
            postRepository.deleteById(id);
            return new Response(null, ServiceStatus.SUCCESS);
        }
        else{
            return new Response(null, ServiceStatus.ERROR);
        }
    }

    public Response listContent(long id, Principal principal){
        Post post = postRepository.findById(id).get();
        PostContentDTO postContent = postRepository.findContent(post);

        setPrincipalPropertiesOnPost(postContent, principal);

        return new Response(postContent, ServiceStatus.SUCCESS);
    }

    public Response listContentComment(long id, Principal principal){
        Post post = postRepository.findById(id).get();
        List<FeedCommentDTO> feedComment = postRepository.findCommentByPost(post);

        setPrincipalPropertiesOnComment(feedComment, principal);

        return new Response(feedComment, ServiceStatus.SUCCESS);
    }

    private void setLikeCount(Post post){
        post.setLikesCount(postRepository.findLikes(post).size());
        postRepository.save(post);
    }

    private void setPrincipalPropertiesOnPost(PostContentDTO post, Principal principal){
        if(principal != null){
            Optional<User> user = userRepository.findById(principal.getName());
            List<Post> checkLike = postRepository.findLike(post.getId(), user);

            if(post.getUser().get("id").equals(principal.getName())){
                post.setMine(true);
            }
            else{
                post.setMine(false);
            }

            if(!checkLike.isEmpty()){
                post.setLiked(true);
            }
            else{
                post.setLiked(false);
            }
        }
    }

    private void setPrincipalPropertiesOnComment(List<FeedCommentDTO> feedComment, Principal principal){
        if(principal != null){
            Optional<User> user = userRepository.findById(principal.getName());

            //check if post is liked by current user
            for(int i = 0; i < feedComment.size(); i++){
                if(feedComment.get(i).getUser().get("id").equals(principal.getName())){
                    feedComment.get(i).setMine(true);
                }
                else{
                    feedComment.get(i).setMine(false);
                }
            }

            //check if post is liked by current user
            for(int i = 0; i < feedComment.size(); i++){
                List<Comment> checkLike = commentRepository.findLike(feedComment.get(i).getId(), user);

                if(!checkLike.isEmpty()){
                    feedComment.get(i).setLiked(true);
                }
                else{
                    feedComment.get(i).setLiked(false);
                }
            }
        }
    }

}
