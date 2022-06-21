package com.adamscript.tomatetoapi.controllers;

import com.adamscript.tomatetoapi.helpers.handler.Response;
import com.adamscript.tomatetoapi.helpers.service.ServiceStatus;
import com.adamscript.tomatetoapi.models.entities.Post;
import com.adamscript.tomatetoapi.models.entities.User;
import com.adamscript.tomatetoapi.services.PostService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.security.Principal;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PostController.class)
public class PostControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private PostService postService;

    @MockBean
    private Principal principal;

    private User user;

    @BeforeEach
    void initPostController(){
        user = new User();
        user.setId("1");
        user.setUsername("eyesocketdisc");
        user.setDisplayName("EyeSocketDisc");
    }

    @Test
    void getPostInformation() throws Exception {
        Post post = new Post();
        post.setUser(user);
        post.setContent("Hi tomates! This is my first tomathought");

        Response response = new Response(post, ServiceStatus.SUCCESS);

        when(postService.list(anyLong())).thenReturn(response);

        MvcResult mvcResult = mockMvc.perform(get("/api/post/{id}", 1)
                        .contentType("application/json"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items.content").value(post.getContent()))
                .andReturn();

        assertThat(mvcResult.getResponse().getContentAsString()).isEqualToIgnoringWhitespace(objectMapper.writeValueAsString(response));
    }

    @Test
    void ifPostNotFound_thenReturns404() throws Exception {
        Response response = new Response(null, ServiceStatus.POST_NOT_FOUND);

        when(postService.list(anyLong())).thenReturn(response);

        mockMvc.perform(get("/api/post/{id}", 3)
                        .contentType("application/json"))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void whenGetPost_ifFails_thenReturns400() throws Exception {
        Response response = new Response(null, ServiceStatus.ERROR);

        when(postService.list(anyLong())).thenReturn(response);

        mockMvc.perform(get("/api/post/{id}", 3)
                        .contentType("application/json"))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void insertNewPost() throws Exception {
        Post post = new Post();
        post.setUser(user);
        post.setContent("Hi tomates! This is my first tomathought");

        Response response = new Response(post, ServiceStatus.SUCCESS);

        when(postService.insert(any(Post.class), any(Principal.class))).thenReturn(response);

        mockMvc.perform(post("/api/post")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(post)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items.content").value(post.getContent()));
    }

    @Test
    void whenInsert_ifFails_thenReturns400() throws Exception {
        Post post = new Post();

        Response response = new Response(null, ServiceStatus.ERROR);

        when(postService.insert(any(Post.class), any(Principal.class))).thenReturn(response);

        mockMvc.perform(post("/api/post")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(post)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void editPost() throws Exception {
        Post post = new Post();
        post.setUser(user);
        post.setContent("Hi tomates! This is my first tomathought");

        Response response = new Response(post, ServiceStatus.SUCCESS);

        when(postService.edit(any(Post.class), any(Principal.class))).thenReturn(response);

        mockMvc.perform(put("/api/post")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(post)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items.content").value(post.getContent()));
    }

    @Test
    void whenEdit_ifFails_thenReturns400() throws Exception {
        Post post = new Post();

        Response response = new Response(null, ServiceStatus.ERROR);

        when(postService.edit(any(Post.class), any(Principal.class))).thenReturn(response);

        mockMvc.perform(put("/api/post")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(post)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void likePost() throws Exception {
        Post post = new Post();

        Response response = new Response(null, ServiceStatus.SUCCESS);

        when(postService.like(anyLong(), any(Principal.class))).thenReturn(response);

        mockMvc.perform(put("/api/post/{id}/like", post.getId())
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(user)))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void whenLike_ifPostNotFound_thenReturns404() throws Exception {
        Post post = new Post();

        Response response = new Response(null, ServiceStatus.POST_NOT_FOUND);

        when(postService.like(anyLong(), any(Principal.class))).thenReturn(response);

        mockMvc.perform(put("/api/post/{id}/like", post.getId())
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(user)))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void whenLike_ifFails_thenReturns400() throws Exception {
        Post post = new Post();

        Response response = new Response(null, ServiceStatus.ERROR);

        when(postService.like(anyLong(), any(Principal.class))).thenReturn(response);

        mockMvc.perform(put("/api/post/{id}/like", post.getId())
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(user)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void unlikePost() throws Exception {
        Post post = new Post();

        Response response = new Response(null, ServiceStatus.SUCCESS);

        when(postService.unlike(anyLong(), any(Principal.class))).thenReturn(response);

        mockMvc.perform(put("/api/post/{id}/unlike", post.getId())
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(user)))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void whenUnlike_ifFails_thenReturns400() throws Exception {
        Post post = new Post();

        Response response = new Response(null, ServiceStatus.ERROR);

        when(postService.unlike(anyLong(), any(Principal.class))).thenReturn(response);

        mockMvc.perform(put("/api/post/{id}/unlike", post.getId())
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(user)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void deletePost() throws Exception {
        Response response = new Response(null, ServiceStatus.SUCCESS);

        when(postService.delete(anyLong(), any(Principal.class))).thenReturn(response);

        mockMvc.perform(delete("/api/post/{id}/delete", 1)
                        .contentType("application/json"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void whenDelete_ifPostNotFound_thenReturns404() throws Exception {
        Response response = new Response(null, ServiceStatus.POST_NOT_FOUND);

        when(postService.delete(anyLong(), any(Principal.class))).thenReturn(response);

        mockMvc.perform(delete("/api/post/{id}/delete", 1)
                        .contentType("application/json"))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void whenDelete_ifFails_thenReturns400() throws Exception {
        Response response = new Response(null, ServiceStatus.ERROR);

        when(postService.delete(anyLong(), any(Principal.class))).thenReturn(response);

        mockMvc.perform(delete("/api/post/{id}/delete", 1)
                        .contentType("application/json"))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }
}
