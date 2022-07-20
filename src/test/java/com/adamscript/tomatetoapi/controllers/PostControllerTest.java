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
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.security.Principal;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
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
    private Post post;

    @BeforeEach
    void initPostController(){
        user = new User();
        user.setId("1");
        user.setUsername("eyesocketdisc");
        user.setDisplayName("EyeSocketDisc");

        post = new Post();
        post.setContent("Hi tomates! This is my first tomathought");
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
    @WithMockUser
    void insertNewPost() throws Exception {
        Response response = new Response(post, ServiceStatus.SUCCESS);

        when(postService.insert(any(Post.class), any(Principal.class))).thenReturn(response);

        mockMvc.perform(post("/api/post")
                        .with(csrf())
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(post)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items.content").value(post.getContent()));
    }

    @Test
    @WithMockUser
    void whenInsert_ifFails_thenReturns400() throws Exception {
        Response response = new Response(null, ServiceStatus.ERROR);

        when(postService.insert(any(Post.class), any(Principal.class))).thenReturn(response);

        mockMvc.perform(post("/api/post")
                        .with(csrf())
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(post)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser
    void editPost() throws Exception {
        Response response = new Response(post, ServiceStatus.SUCCESS);

        when(postService.edit(any(Post.class), any(Principal.class))).thenReturn(response);

        mockMvc.perform(put("/api/post")
                        .with(csrf())
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(post)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items.content").value(post.getContent()));
    }

    @Test
    @WithMockUser
    void whenEdit_ifFails_thenReturns400() throws Exception {
        Response response = new Response(null, ServiceStatus.ERROR);

        when(postService.edit(any(Post.class), any(Principal.class))).thenReturn(response);

        mockMvc.perform(put("/api/post")
                        .with(csrf())
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(post)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser
    void likePost() throws Exception {
        Response response = new Response(null, ServiceStatus.SUCCESS);

        when(postService.like(anyLong(), any(Principal.class))).thenReturn(response);

        mockMvc.perform(put("/api/post/{id}/like", post.getId())
                        .with(csrf())
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(user)))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    void whenLike_ifPostNotFound_thenReturns404() throws Exception {
        Response response = new Response(null, ServiceStatus.POST_NOT_FOUND);

        when(postService.like(anyLong(), any(Principal.class))).thenReturn(response);

        mockMvc.perform(put("/api/post/{id}/like", post.getId())
                        .with(csrf())
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(user)))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser
    void whenLike_ifFails_thenReturns400() throws Exception {
        Response response = new Response(null, ServiceStatus.ERROR);

        when(postService.like(anyLong(), any(Principal.class))).thenReturn(response);

        mockMvc.perform(put("/api/post/{id}/like", post.getId())
                        .with(csrf())
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(user)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser
    void unlikePost() throws Exception {
        Response response = new Response(null, ServiceStatus.SUCCESS);

        when(postService.unlike(anyLong(), any(Principal.class))).thenReturn(response);

        mockMvc.perform(put("/api/post/{id}/unlike", post.getId())
                        .with(csrf())
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(user)))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    void whenUnlike_ifFails_thenReturns400() throws Exception {
        Response response = new Response(null, ServiceStatus.ERROR);

        when(postService.unlike(anyLong(), any(Principal.class))).thenReturn(response);

        mockMvc.perform(put("/api/post/{id}/unlike", post.getId())
                        .with(csrf())
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(user)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser
    void deletePost() throws Exception {
        Response response = new Response(null, ServiceStatus.SUCCESS);

        when(postService.delete(anyLong(), any(Principal.class))).thenReturn(response);

        mockMvc.perform(delete("/api/post/{id}/delete", 1)
                        .with(csrf())
                        .contentType("application/json"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    void whenDelete_ifPostNotFound_thenReturns404() throws Exception {
        Response response = new Response(null, ServiceStatus.POST_NOT_FOUND);

        when(postService.delete(anyLong(), any(Principal.class))).thenReturn(response);

        mockMvc.perform(delete("/api/post/{id}/delete", 1)
                        .with(csrf())
                        .contentType("application/json"))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser
    void whenDelete_ifFails_thenReturns400() throws Exception {
        Response response = new Response(null, ServiceStatus.ERROR);

        when(postService.delete(anyLong(), any(Principal.class))).thenReturn(response);

        mockMvc.perform(delete("/api/post/{id}/delete", 1)
                        .with(csrf())
                        .contentType("application/json"))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }
}
