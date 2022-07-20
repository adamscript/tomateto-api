package com.adamscript.tomatetoapi.integration;

import com.adamscript.tomatetoapi.models.entities.Post;
import com.adamscript.tomatetoapi.models.entities.User;
import com.adamscript.tomatetoapi.models.repos.PostRepository;
import com.adamscript.tomatetoapi.models.repos.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.event.annotation.BeforeTestClass;
import org.springframework.test.context.event.annotation.BeforeTestMethod;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.transaction.Transactional;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
public class PostIntegrationTest {

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    private User user;
    private Post post;

    @BeforeEach
    void initPostIntegration(){
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();

        user = new User();
        user.setId("1");
        user.setUsername("eyesocketdisc");
        user.setDisplayName("EyeSocketDisc");

        userRepository.saveAndFlush(user);

        post = new Post();
        post.setUser(user);
        post.setContent("Hi tomates! This is my first tomathought");

        postRepository.saveAndFlush(post);
    }

    @Test
    void getPostInformation() throws Exception{
        mockMvc.perform(get("/api/post/{id}", post.getId())
                        .contentType("application/json"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items.content").value(post.getContent()));
    }

    @Test
    @WithMockUser("1")
    void insertNewPost() throws Exception{
        mockMvc.perform(post("/api/post")
                        .with(csrf())
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(post)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items.content").value(post.getContent()));
    }

    @Test
    @WithMockUser("1")
    void editPost() throws Exception{
        Post editedPost = new Post();
        editedPost.setId(post.getId());
        editedPost.setContent("Hello tomates! This is my first tomathought");

        mockMvc.perform(put("/api/post")
                        .with(csrf())
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(editedPost)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items.content").value(editedPost.getContent()));
    }

    @Test
    @WithMockUser("1")
    void likePost() throws Exception{
        mockMvc.perform(put("/api/post/{id}/like", post.getId())
                        .with(csrf())
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(user)))
                .andDo(print())
                .andExpect(status().isOk());

        Post likedPost = postRepository.findById(post.getId()).get();

        //check if post successfully liked
        assertThat(postRepository.findLike(post.getId(), Optional.of(user))).isNotNull().isEqualTo(List.of(likedPost));
    }

    @Test
    @WithMockUser("1")
    void unlikePost() throws Exception{
        //like newly created post first
        postRepository.likePost(post.getId(), user.getId());

        //then unlike it
        mockMvc.perform(put("/api/post/{id}/unlike", post.getId())
                        .with(csrf())
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(user)))
                .andDo(print())
                .andExpect(status().isOk());

        //check if post successfully unliked
        assertThat(postRepository.findLike(post.getId(), Optional.of(user))).isEqualTo(List.of());
    }

    @Test
    @WithMockUser("1")
    void deletePost() throws Exception{
        mockMvc.perform(delete("/api/post/{id}/delete", post.getId())
                        .with(csrf())
                        .contentType("application/json"))
                .andDo(print())
                .andExpect(status().isOk());

        assertThat(postRepository.findById(post.getId())).isEqualTo(Optional.empty());
    }

}
