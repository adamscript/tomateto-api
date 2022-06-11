package com.adamscript.tomatetoapi.services;

import com.adamscript.tomatetoapi.models.entities.Post;
import com.adamscript.tomatetoapi.models.entities.User;
import com.adamscript.tomatetoapi.models.repos.PostRepository;
import com.adamscript.tomatetoapi.models.repos.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

class PostServiceUnitTest {

    private PostRepository postRepository = Mockito.mock(PostRepository.class);
    private UserRepository userRepository = Mockito.mock(UserRepository.class);

    private PostService postService;

    private User user;

    @BeforeEach
    void initPostService(){
        postService = new PostService(postRepository, userRepository);

        user = new User();
        user.setUsername("eyesocketdisc");
        user.setDisplayName("EyeSocketDisc");
    }

    @Test
    void getPostInformation(){
        Post post = new Post();
        post.setUser(user);
        post.setContent("Hi tomates! This is my first tomathought");

        when(postRepository.findById(anyLong())).thenReturn(Optional.of(post));

        assertThat(postService.list(anyLong()).getCode()).isEqualTo(0);
    }

    @Test
    void ifPostNotFound_thenReturnsError(){
        when(postRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThat(postService.list(anyLong()).getCode()).isEqualTo(200);
    }

    @Test
    void insertNewPost(){
        Post post = new Post();
        post.setUser(user);
        post.setContent("Hi tomates! This is my first tomathought");

        when(userRepository.findById(anyString())).thenReturn(Optional.of(user));
        when(postRepository.save(post)).thenReturn(post);

        assertThat(postService.insert(post).getCode()).isEqualTo(0);
    }

    @Test
    void whenInsert_ifUserNull_thenReturnsError(){
        Post post = new Post();

        assertThat(postService.insert(post).getCode()).isEqualTo(202);
    }

    @Test
    void whenInsert_ifContentNull_thenReturnsError(){
        Post post = new Post();
        post.setUser(user);

        assertThat(postService.insert(post).getCode()).isEqualTo(203);
    }

    @Test
    void whenInsert_ifUserDoesNotExist_thenReturnsError(){
        Post post = new Post();
        post.setUser(user);
        post.setContent("Hi tomates! This is my first tomathought");

        when(userRepository.findById(anyString())).thenReturn(Optional.empty());

        assertThat(postService.insert(post).getCode()).isEqualTo(102);
    }

    @Test
    void editPost(){
        Post post = new Post();
        post.setId(1);
        post.setUser(user);
        post.setContent("Hi tomates! This is my first tomathought");

        when(postRepository.findById(anyLong())).thenReturn(Optional.of(post));

        assertThat(postService.edit(post).getCode()).isEqualTo(0);
    }

    @Test
    void ifPostDoesNotExist_thenReturnsError(){
        Post post = new Post();
        post.setUser(user);
        post.setContent("Hi tomates! This is my first tomathought");

        when(postRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThat(postService.edit(post).getCode()).isEqualTo(201);
    }

    @Test
    void whenEdit_ifPostNull_thenReturnsError(){
        Post post = new Post();

        assertThat(postService.edit(post).getCode()).isEqualTo(201);
    }

    @Test
    void whenEdit_ifContentNull_thenReturnsError(){
        Post post = new Post();
        post.setId(1);
        post.setUser(user);

        assertThat(postService.edit(post).getCode()).isEqualTo(203);
    }

    @Test
    void likePost(){
        Post post = new Post();

        when(postRepository.findById(post.getId())).thenReturn(Optional.of(post));
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        assertThat(postService.like(post.getId(), user.getId()).getCode()).isEqualTo(0);
    }

    @Test
    void ifAlreadyLiked_thenReturnsError(){
        Post post = new Post();

        when(postRepository.findById(post.getId())).thenReturn(Optional.of(post));
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(postRepository.findLike(post.getId(), Optional.of(user))).thenReturn(List.of(post));

        assertThat(postService.like(post.getId(), user.getId()).getCode()).isEqualTo(204);
    }

    @Test
    void whenLike_ifPostDoesNotExist_thenReturnsError(){
        Post post = new Post();

        when(postRepository.findById(post.getId())).thenReturn(Optional.empty());

        assertThat(postService.like(post.getId(), user.getId()).getCode()).isEqualTo(201);
    }

    @Test
    void whenLike_ifUserDoesNotExist_thenReturnsError(){
        Post post = new Post();

        when(postRepository.findById(post.getId())).thenReturn(Optional.of(post));
        when(userRepository.findById(user.getId())).thenReturn(Optional.empty());

        assertThat(postService.like(post.getId(), user.getId()).getCode()).isEqualTo(102);
    }

    @Test
    void unlikePost(){
        Post post = new Post();

        when(userRepository.findById(anyString())).thenReturn(Optional.of(user));
        when(postRepository.findLike(anyLong(), any())).thenReturn(List.of(post));

        assertThat(postService.unlike(post.getId(), user.getId()).getCode()).isEqualTo(0);
    }

    @Test
    void ifAlreadyUnliked_thenReturnsError(){
        Post post = new Post();
        when(postRepository.findLike(anyLong(), any())).thenReturn(List.of());

        assertThat(postService.unlike(post.getId(), user.getId()).getCode()).isEqualTo(205);
    }

    @Test
    void deletePost(){
        Post post = new Post();

        when(postRepository.findById(anyLong())).thenReturn(Optional.of(post));

        assertThat(postService.delete(anyLong()).getCode()).isEqualTo(0);
    }

    @Test
    void whenDelete_ifPostNotFound_thenReturnsError(){
        when(postRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThat(postService.delete(anyLong()).getCode()).isEqualTo(200);
    }
}
