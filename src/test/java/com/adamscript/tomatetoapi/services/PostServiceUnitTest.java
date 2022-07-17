package com.adamscript.tomatetoapi.services;

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

class PostServiceUnitTest {

    private PostRepository postRepository = Mockito.mock(PostRepository.class);
    private CommentRepository commentRepository = Mockito.mock(CommentRepository.class);
    private UserRepository userRepository = Mockito.mock(UserRepository.class);

    private Principal principal = Mockito.mock(Principal.class);

    private PostService postService;

    private User user;
    private Post post;

    @BeforeEach
    void initPostService(){
        postService = new PostService(postRepository, commentRepository, userRepository);

        user = new User();
        user.setId("1");
        user.setUsername("eyesocketdisc");
        user.setDisplayName("EyeSocketDisc");

        post = new Post();
        post.setId(1);
        post.setContent("Hi tomates! This is my first tomathought");
    }

    @Test
    void getPostInformation(){
        post.setUser(user);

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
        when(principal.getName()).thenReturn(user.getId());
        when(userRepository.findById(anyString())).thenReturn(Optional.of(user));

        assertThat(postService.insert(post, principal).getCode()).isEqualTo(0);
    }

    @Test
    void whenInsert_ifContentNull_thenReturnsError(){
        post.setContent(null);

        assertThat(postService.insert(post, principal).getCode()).isEqualTo(203);
    }

    @Test
    void whenInsert_ifUserDoesNotExist_thenReturnsError(){
        when(userRepository.findById(anyString())).thenReturn(Optional.empty());

        assertThat(postService.insert(post, principal).getCode()).isEqualTo(102);
    }

    @Test
    void editPost(){
        post.setUser(user);

        when(principal.getName()).thenReturn(user.getId());
        when(userRepository.findById(anyString())).thenReturn(Optional.of(user));
        when(postRepository.findById(anyLong())).thenReturn(Optional.of(post));

        assertThat(postService.edit(post, principal).getCode()).isEqualTo(0);
    }

    @Test
    void ifPostDoesNotExist_thenReturnsError(){
        when(postRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThat(postService.edit(post, principal).getCode()).isEqualTo(201);
    }

    @Test
    void whenEdit_ifPostNull_thenReturnsError(){
        post.setId(2);
        assertThat(postService.edit(post, principal).getCode()).isEqualTo(201);
    }

    @Test
    void whenEdit_ifContentNull_thenReturnsError(){
        post.setContent(null);

        assertThat(postService.edit(post, principal).getCode()).isEqualTo(203);
    }

    @Test
    void likePost(){
        when(principal.getName()).thenReturn(user.getId());
        when(userRepository.findById(anyString())).thenReturn(Optional.of(user));
        when(postRepository.findById(post.getId())).thenReturn(Optional.of(post));

        assertThat(postService.like(post.getId(), principal).getCode()).isEqualTo(0);
    }

    @Test
    void ifAlreadyLiked_thenReturnsError(){
        when(principal.getName()).thenReturn(user.getId());
        when(postRepository.findById(post.getId())).thenReturn(Optional.of(post));
        when(userRepository.findById(anyString())).thenReturn(Optional.of(user));
        when(postRepository.findLike(post.getId(), Optional.of(user))).thenReturn(List.of(post));

        assertThat(postService.like(post.getId(), principal).getCode()).isEqualTo(204);
    }

    @Test
    void whenLike_ifPostDoesNotExist_thenReturnsError(){
        when(postRepository.findById(post.getId())).thenReturn(Optional.empty());

        assertThat(postService.like(post.getId(), principal).getCode()).isEqualTo(201);
    }

    @Test
    void whenLike_ifUserDoesNotExist_thenReturnsError(){
        when(postRepository.findById(anyLong())).thenReturn(Optional.of(post));
        when(userRepository.findById(anyString())).thenReturn(Optional.empty());

        assertThat(postService.like(post.getId(), principal).getCode()).isEqualTo(102);
    }

    @Test
    void unlikePost(){
        when(principal.getName()).thenReturn(user.getId());
        when(userRepository.findById(anyString())).thenReturn(Optional.of(user));
        when(postRepository.findById(anyLong())).thenReturn(Optional.of(post));
        when(postRepository.findLike(anyLong(), any())).thenReturn(List.of(post));

        assertThat(postService.unlike(post.getId(), principal).getCode()).isEqualTo(0);
    }

    @Test
    void ifAlreadyUnliked_thenReturnsError(){
        when(postRepository.findLike(anyLong(), any())).thenReturn(List.of());

        assertThat(postService.unlike(post.getId(), principal).getCode()).isEqualTo(205);
    }

    @Test
    void deletePost(){
        post.setUser(user);

        when(principal.getName()).thenReturn(user.getId());
        when(userRepository.findById(anyString())).thenReturn(Optional.of(user));
        when(postRepository.findById(anyLong())).thenReturn(Optional.of(post));

        assertThat(postService.delete(anyLong(), principal).getCode()).isEqualTo(0);
    }

    @Test
    void whenDelete_ifPostNotFound_thenReturnsError(){
        when(postRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThat(postService.delete(anyLong(), principal).getCode()).isEqualTo(200);
    }
}
