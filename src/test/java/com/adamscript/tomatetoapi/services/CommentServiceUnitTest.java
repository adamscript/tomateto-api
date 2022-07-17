package com.adamscript.tomatetoapi.services;

import com.adamscript.tomatetoapi.models.entities.Comment;
import com.adamscript.tomatetoapi.models.entities.Post;
import com.adamscript.tomatetoapi.models.entities.User;
import com.adamscript.tomatetoapi.models.repos.CommentRepository;
import com.adamscript.tomatetoapi.models.repos.PostRepository;
import com.adamscript.tomatetoapi.models.repos.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

public class CommentServiceUnitTest {

    private CommentRepository commentRepository = Mockito.mock(CommentRepository.class);
    private PostRepository postRepository = Mockito.mock(PostRepository.class);
    private UserRepository userRepository = Mockito.mock(UserRepository.class);

    private Principal principal = Mockito.mock(Principal.class);

    private CommentService commentService;

    private User user;
    private Post post;
    private Comment comment;

    @BeforeEach
    void initCommentService(){
        commentService = new CommentService(commentRepository, postRepository, userRepository);

        user = new User();
        user.setId("1");
        user.setUsername("eyesocketdisc");
        user.setDisplayName("EyeSocketDisc");

        post = new Post();
        post.setUser(user);
        post.setContent("Hi tomates! This is my first tomathought");

        comment = new Comment();
        comment.setPost(post);
        comment.setContent("And this is my first mini-tomathought, aka comment! ;)");
    }

    @Test
    void getCommentInformation(){
        when(commentRepository.findById(anyLong())).thenReturn(Optional.of(comment));

        assertThat(commentService.list(anyLong()).getCode()).isEqualTo(0);
    }

    @Test
    void ifcommentNotFound_thenReturnsError(){
        when(commentRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThat(commentService.list(anyLong()).getCode()).isEqualTo(300);
    }

    @Test
    void insertNewComment(){
        when(principal.getName()).thenReturn(user.getId());
        when(userRepository.findById(anyString())).thenReturn(Optional.of(user));
        when(postRepository.findById(anyLong())).thenReturn(Optional.of(post));

        assertThat(commentService.insert(comment, principal).getCode()).isEqualTo(0);
    }

    @Test
    void whenInsert_ifPostNull_thenReturnsError(){
        comment.setPost(null);

        when(principal.getName()).thenReturn(user.getId());
        when(userRepository.findById(anyString())).thenReturn(Optional.of(user));

        assertThat(commentService.insert(comment, principal).getCode()).isEqualTo(303);
    }

    @Test
    void whenInsert_ifContentNull_thenReturnsError(){
        comment.setContent(null);

        when(principal.getName()).thenReturn(user.getId());
        when(userRepository.findById(anyString())).thenReturn(Optional.of(user));
        when(postRepository.findById(anyLong())).thenReturn(Optional.of(post));

        assertThat(commentService.insert(comment, principal).getCode()).isEqualTo(304);
    }

    @Test
    void whenInsert_ifUserDoesNotExist_thenReturnsError(){
        when(userRepository.getById(anyString())).thenReturn(null);
        when(userRepository.findById(anyString())).thenReturn(Optional.empty());

        assertThat(commentService.insert(comment, principal).getCode()).isEqualTo(102);
    }

    @Test
    void whenInsert_ifPostDoesNotExist_thenReturnsError(){
        when(principal.getName()).thenReturn(user.getId());
        when(userRepository.findById(anyString())).thenReturn(Optional.of(user));
        when(postRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThat(commentService.insert(comment, principal).getCode()).isEqualTo(201);
    }

    @Test
    void likeComment(){
        when(principal.getName()).thenReturn(user.getId());
        when(commentRepository.findById(anyLong())).thenReturn(Optional.of(comment));
        when(userRepository.findById(anyString())).thenReturn(Optional.of(user));

        assertThat(commentService.like(comment.getId(), principal).getCode()).isEqualTo(0);
    }

    @Test
    void ifAlreadyLiked_thenReturnsError(){
        when(principal.getName()).thenReturn(user.getId());
        when(commentRepository.findById(comment.getId())).thenReturn(Optional.of(comment));
        when(userRepository.findById(anyString())).thenReturn(Optional.of(user));
        when(commentRepository.findLike(comment.getId(), Optional.of(user))).thenReturn(List.of(comment));

        assertThat(commentService.like(comment.getId(), principal).getCode()).isEqualTo(305);
    }

    @Test
    void whenLike_ifCommentDoesNotExist_thenReturnsError(){
        when(commentRepository.findById(comment.getId())).thenReturn(Optional.empty());

        assertThat(commentService.like(comment.getId(), principal).getCode()).isEqualTo(301);
    }

    @Test
    void whenLike_ifUserDoesNotExist_thenReturnsError(){
        when(commentRepository.findById(comment.getId())).thenReturn(Optional.of(comment));
        when(userRepository.findById(anyString())).thenReturn(Optional.empty());

        assertThat(commentService.like(comment.getId(), principal).getCode()).isEqualTo(102);
    }

    @Test
    void unlikeComment(){
        when(principal.getName()).thenReturn(user.getId());
        when(userRepository.findById(anyString())).thenReturn(Optional.of(user));
        when(commentRepository.findById(anyLong())).thenReturn(Optional.of(comment));
        when(commentRepository.findLike(anyLong(), any())).thenReturn(List.of(comment));

        assertThat(commentService.unlike(comment.getId(), principal).getCode()).isEqualTo(0);
    }

    @Test
    void ifAlreadyUnliked_thenReturnsError(){
        when(commentRepository.findLike(anyLong(), any())).thenReturn(List.of());

        assertThat(commentService.unlike(comment.getId(), principal).getCode()).isEqualTo(306);
    }

    @Test
    void deleteComment(){
        comment.setUser(user);

        when(principal.getName()).thenReturn(user.getId());
        when(userRepository.findById(anyString())).thenReturn(Optional.of(user));
        when(postRepository.findById(anyLong())).thenReturn(Optional.of(post));
        when(commentRepository.findById(anyLong())).thenReturn(Optional.of(comment));

        assertThat(commentService.delete(anyLong(), principal).getCode()).isEqualTo(0);
    }

    @Test
    void whenDelete_ifCommentNotFound_thenReturnsError(){
        when(commentRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThat(commentService.delete(anyLong(), principal).getCode()).isEqualTo(300);
    }
}

