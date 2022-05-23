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

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

public class CommentServiceUnitTest {

    private CommentRepository commentRepository = Mockito.mock(CommentRepository.class);
    private PostRepository postRepository = Mockito.mock(PostRepository.class);
    private UserRepository userRepository = Mockito.mock(UserRepository.class);

    private CommentService commentService;

    private User user;
    private Post post;

    @BeforeEach
    void initCommentService(){
        commentService = new CommentService(commentRepository, postRepository, userRepository);

        user = new User();
        user.setUsername("eyesocketdisc");
        user.setDisplayName("EyeSocketDisc");

        post = new Post();
        post.setUserId(user);
        post.setContent("Hi tomates! This is my first tomathought");
    }

    @Test
    void getCommentInformation(){
        Comment comment = new Comment();
        comment.setUserId(user);
        comment.setPostId(post);
        comment.setContent("And this is my first mini-tomathought, aka comment! ;)");

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
        Comment comment = new Comment();
        comment.setUserId(user);
        comment.setPostId(post);
        comment.setContent("And this is my first mini-tomathought, aka comment! ;)");

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(postRepository.findById(anyLong())).thenReturn(Optional.of(post));
        when(commentRepository.save(comment)).thenReturn(comment);

        assertThat(commentService.insert(comment).getCode()).isEqualTo(0);
    }

    @Test
    void whenInsert_ifUserNull_thenReturnsError(){
        Comment comment = new Comment();

        assertThat(commentService.insert(comment).getCode()).isEqualTo(302);
    }

    @Test
    void whenInsert_ifPostNull_thenReturnsError(){
        Comment comment = new Comment();
        comment.setUserId(user);

        assertThat(commentService.insert(comment).getCode()).isEqualTo(303);
    }

    @Test
    void whenInsert_ifContentNull_thenReturnsError(){
        Comment comment = new Comment();
        comment.setUserId(user);
        comment.setPostId(post);

        assertThat(commentService.insert(comment).getCode()).isEqualTo(304);
    }

    @Test
    void whenInsert_ifUserDoesNotExist_thenReturnsError(){
        Comment comment = new Comment();
        comment.setUserId(user);
        comment.setPostId(post);
        comment.setContent("And this is my first mini-tomathought, aka comment! ;)");

        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThat(commentService.insert(comment).getCode()).isEqualTo(102);
    }

    @Test
    void whenInsert_ifPostDoesNotExist_thenReturnsError(){
        Comment comment = new Comment();
        comment.setUserId(user);
        comment.setPostId(post);
        comment.setContent("And this is my first mini-tomathought, aka comment! ;)");

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(postRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThat(commentService.insert(comment).getCode()).isEqualTo(201);
    }

    @Test
    void likeComment(){
        Comment comment = new Comment();

        when(commentRepository.findById(comment.getId())).thenReturn(Optional.of(comment));
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        assertThat(commentService.like(comment.getId(), user.getId()).getCode()).isEqualTo(0);
    }

    @Test
    void ifAlreadyLiked_thenReturnsError(){
        Comment comment = new Comment();

        when(commentRepository.findById(comment.getId())).thenReturn(Optional.of(comment));
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(commentRepository.findLike(comment.getId(), Optional.of(user))).thenReturn(List.of(comment));

        assertThat(commentService.like(comment.getId(), user.getId()).getCode()).isEqualTo(305);
    }

    @Test
    void whenLike_ifCommentDoesNotExist_thenReturnsError(){
        Comment comment = new Comment();

        when(commentRepository.findById(comment.getId())).thenReturn(Optional.empty());

        assertThat(commentService.like(comment.getId(), user.getId()).getCode()).isEqualTo(301);
    }

    @Test
    void whenLike_ifUserDoesNotExist_thenReturnsError(){
        Comment comment = new Comment();

        when(commentRepository.findById(comment.getId())).thenReturn(Optional.of(comment));
        when(userRepository.findById(user.getId())).thenReturn(Optional.empty());

        assertThat(commentService.like(comment.getId(), user.getId()).getCode()).isEqualTo(102);
    }

    @Test
    void unlikeComment(){
        Comment comment = new Comment();

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(commentRepository.findLike(anyLong(), any())).thenReturn(List.of(comment));

        assertThat(commentService.unlike(comment.getId(), user.getId()).getCode()).isEqualTo(0);
    }

    @Test
    void ifAlreadyUnliked_thenReturnsError(){
        Comment comment = new Comment();
        when(commentRepository.findLike(anyLong(), any())).thenReturn(List.of());

        assertThat(commentService.unlike(comment.getId(), user.getId()).getCode()).isEqualTo(306);
    }

    @Test
    void deleteComment(){
        Comment comment = new Comment();

        when(commentRepository.findById(anyLong())).thenReturn(Optional.of(comment));

        assertThat(commentService.delete(anyLong()).getCode()).isEqualTo(0);
    }

    @Test
    void whenDelete_ifCommentNotFound_thenReturnsError(){
        when(commentRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThat(commentService.delete(anyLong()).getCode()).isEqualTo(300);
    }
}

