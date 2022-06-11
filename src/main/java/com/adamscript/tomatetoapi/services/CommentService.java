package com.adamscript.tomatetoapi.services;

import com.adamscript.tomatetoapi.helpers.handler.Response;
import com.adamscript.tomatetoapi.helpers.service.ServiceStatus;
import com.adamscript.tomatetoapi.models.entities.Comment;
import com.adamscript.tomatetoapi.models.entities.Post;
import com.adamscript.tomatetoapi.models.entities.User;
import com.adamscript.tomatetoapi.models.repos.CommentRepository;
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
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    //fetch comment content data
    public Response list(long id){
        Optional<Comment> comment = commentRepository.findById(id);

        if(comment.isEmpty()){
            return new Response(null, ServiceStatus.COMMENT_NOT_FOUND);
        }
        else if(comment.isPresent()){
            return new Response(comment, ServiceStatus.SUCCESS);
        }
        else{
            return new Response(null, ServiceStatus.ERROR);
        }
    }

    //create comment
    public Response insert(Comment comment){
        if(comment.getUser() == null){
            return new Response(null, ServiceStatus.COMMENT_USER_EMPTY);
        }
        else if(comment.getPost() == null){
            return new Response(null, ServiceStatus.COMMENT_POST_EMPTY);
        }
        else if(comment.getContent() == null){
            return new Response(null, ServiceStatus.COMMENT_CONTENT_EMPTY);
        }
        else if(comment.getUser() != null && comment.getPost() != null && comment.getContent() != null){
            Optional<User> user = userRepository.findById(comment.getUser().getId());
            Optional<Post> post = postRepository.findById(comment.getPost().getId());

            if(user.isEmpty()){
                return new Response(null, ServiceStatus.USER_DOES_NOT_EXIST);
            }
            else if(post.isEmpty()){
                return new Response(null, ServiceStatus.POST_DOES_NOT_EXIST);
            }
            else if(user.isPresent() && post.isPresent()){
                return new Response(commentRepository.save(comment), ServiceStatus.SUCCESS);
            }
            else{
                return new Response(null, ServiceStatus.ERROR);
            }
        }
        else{
            return new Response(null, ServiceStatus.ERROR);
        }
    }

    //like a comment
    public Response like(long commentId, String userId){
        Optional<User> user = userRepository.findById(userId);
        Optional<Comment> comment = commentRepository.findById(commentId);

        List<Comment> likedComment = commentRepository.findLike(commentId, user);

        if(comment.isEmpty()){
            return new Response(null, ServiceStatus.COMMENT_DOES_NOT_EXIST);
        }
        else if(user.isEmpty()){
            return new Response(null, ServiceStatus.USER_DOES_NOT_EXIST);
        }
        else if(!likedComment.isEmpty()){
            return new Response(null, ServiceStatus.COMMENT_LIKED_ALREADY);
        }
        else if(likedComment.isEmpty()){
            commentRepository.likeComment(commentId, userId);
            return new Response(null, ServiceStatus.SUCCESS);
        }
        else{
            return new Response(null, ServiceStatus.ERROR);
        }
    }

    //unlike a comment
    public Response unlike(long commentId, String userId){
        Optional<User> user = userRepository.findById(userId);

        List<Comment> likedComment = commentRepository.findLike(commentId, user);

        if(likedComment.isEmpty()){
            return new Response(null, ServiceStatus.COMMENT_NOT_LIKED);
        }
        else if(!likedComment.isEmpty()){
            commentRepository.unlikeComment(commentId, userId);
            return new Response(null, ServiceStatus.SUCCESS);
        }
        else{
            return new Response(null, ServiceStatus.ERROR);
        }
    }

    //deleting a comment
    public Response delete(long id){
        Optional<Comment> comment = commentRepository.findById(id);

        if(comment.isEmpty()){
            return new Response(null, ServiceStatus.COMMENT_NOT_FOUND);
        }
        else if(comment.isPresent()){
            commentRepository.deleteById(id);
            return new Response(null, ServiceStatus.SUCCESS);
        }
        else{
            return new Response(null, ServiceStatus.ERROR);
        }
    }
}
