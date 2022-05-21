package com.adamscript.tomatetoapi.integration;

import com.adamscript.tomatetoapi.models.entities.Post;
import com.adamscript.tomatetoapi.models.entities.User;
import com.adamscript.tomatetoapi.models.repos.PostRepository;
import com.adamscript.tomatetoapi.models.repos.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import javax.transaction.Transactional;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
public class PostIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    private User user;

    @BeforeEach
    void initPostController(){
        user = new User();
        user.setId(1);
        user.setUsername("eyesocketdisc");
        user.setDisplayName("EyeSocketDisc");
    }

    @Test
    void getPostInformation() throws Exception{
        Post post = new Post();
        post.setUserId(user);
        post.setContent("Hi tomates! This is my first tomathought");

        postRepository.saveAndFlush(post);

        mockMvc.perform(get("/api/post/{id}", post.getId())
                        .contentType("application/json"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items.content").value(post.getContent()));
    }

    @Test
    void insertNewPost() throws Exception{
        Post post = new Post();
        post.setUserId(user);
        post.setContent("Hi tomates! This is my first tomathought");

        mockMvc.perform(post("/api/post")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(post)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items.content").value(post.getContent()));
    }

    @Test
    void editPost() throws Exception{
        Post post = new Post();
        post.setUserId(user);
        post.setContent("Hi tomates! This is my first tomathought");

        postRepository.saveAndFlush(post);

        Post editedPost = new Post();
        editedPost.setId(post.getId());
        editedPost.setContent("Hello tomates! This is my first tomathought");

        mockMvc.perform(put("/api/post")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(editedPost)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items.content").value(editedPost.getContent()));
    }

    @Test
    void likePost() throws Exception{
        Post post = new Post();
        User user = new User();

        postRepository.save(post);
        userRepository.save(user);

        mockMvc.perform(put("/api/post/{id}/like", post.getId())
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(user)))
                .andDo(print())
                .andExpect(status().isOk());

        Post likedPost = postRepository.findById(post.getId()).get();

        //check if post successfully liked
        assertThat(postRepository.findLike(post.getId(), Optional.of(user))).isNotNull().isEqualTo(List.of(likedPost));
    }

    @Test
    void unlikePost() throws Exception{
        Post post = new Post();
        User user = new User();

        postRepository.save(post);
        userRepository.save(user);

        //like newly created post first
        postRepository.likePost(post.getId(), user.getId());

        //then unlike it
        mockMvc.perform(put("/api/post/{id}/unlike", post.getId())
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(user)))
                .andDo(print())
                .andExpect(status().isOk());

        //check if post successfully unliked
        assertThat(postRepository.findLike(post.getId(), Optional.of(user))).isEqualTo(List.of());
    }

    @Test
    void deletePost() throws Exception{
        Post post = new Post();
        postRepository.saveAndFlush(post);

        mockMvc.perform(delete("/api/post/{id}/delete", post.getId())
                        .contentType("application/json"))
                .andDo(print())
                .andExpect(status().isOk());

        assertThat(postRepository.findById(post.getId())).isEqualTo(Optional.empty());
    }

}
