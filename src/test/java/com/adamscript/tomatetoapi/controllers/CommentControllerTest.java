package com.adamscript.tomatetoapi.controllers;

import com.adamscript.tomatetoapi.helpers.handler.Response;
import com.adamscript.tomatetoapi.helpers.service.ServiceStatus;
import com.adamscript.tomatetoapi.models.entities.Comment;
import com.adamscript.tomatetoapi.models.entities.Post;
import com.adamscript.tomatetoapi.models.entities.User;
import com.adamscript.tomatetoapi.services.CommentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CommentController.class)
public class CommentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CommentService commentService;

    private User user;
    private Post post;

    @BeforeEach
    void initCommentController(){
        user = new User();
        user.setId("3");
        user.setUsername("eyesocketdisc");
        user.setDisplayName("EyeSocketDisc");

        post = new Post();
        post.setId(4);
        post.setUser(user);
        post.setContent("Hi tomates! This is my first tomathought");
    }

    @Test
    void getCommentInformation() throws Exception {
        Comment comment = new Comment();
        comment.setUser(user);
        comment.setPost(post);
        comment.setContent("And this is my first mini-tomathought, aka comment! ;)");

        Response response = new Response(comment, ServiceStatus.SUCCESS);

        when(commentService.list(anyLong())).thenReturn(response);

        MvcResult mvcResult = mockMvc.perform(get("/api/comment/{id}", 1)
                        .contentType("application/json"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items.content").value(comment.getContent()))
                .andReturn();

        assertThat(mvcResult.getResponse().getContentAsString()).isEqualToIgnoringWhitespace(objectMapper.writeValueAsString(response));
    }

    @Test
    void ifCommentNotFound_thenReturns404() throws Exception {
        Response response = new Response(null, ServiceStatus.COMMENT_NOT_FOUND);

        when(commentService.list(anyLong())).thenReturn(response);

        mockMvc.perform(get("/api/comment/{id}", 3)
                        .contentType("application/json"))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void whenGetComment_ifFails_thenReturns400() throws Exception {
        Response response = new Response(null, ServiceStatus.ERROR);

        when(commentService.list(anyLong())).thenReturn(response);

        mockMvc.perform(get("/api/comment/{id}", 3)
                        .contentType("application/json"))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void insertNewComment() throws Exception {
        Comment comment = new Comment();
        comment.setUser(user);
        comment.setPost(post);
        comment.setContent("And this is my first mini-tomathought, aka comment! ;)");

        Response response = new Response(comment, ServiceStatus.SUCCESS);

        when(commentService.insert(any(Comment.class))).thenReturn(response);

        mockMvc.perform(post("/api/comment")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(comment)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items.content").value(comment.getContent()));
    }

    @Test
    void whenInsert_ifFails_thenReturns400() throws Exception {
        Comment comment = new Comment();

        Response response = new Response(null, ServiceStatus.ERROR);

        when(commentService.insert(any(Comment.class))).thenReturn(response);

        mockMvc.perform(post("/api/comment")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(comment)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void likeComment() throws Exception {
        Comment comment = new Comment();

        Response response = new Response(null, ServiceStatus.SUCCESS);

        when(commentService.like(anyLong(), anyString())).thenReturn(response);

        mockMvc.perform(put("/api/comment/{id}/like", comment.getId())
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(user)))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void whenLike_ifCommentNotFound_thenReturns404() throws Exception {
        Comment comment = new Comment();

        Response response = new Response(null, ServiceStatus.COMMENT_NOT_FOUND);

        when(commentService.like(anyLong(), anyString())).thenReturn(response);

        mockMvc.perform(put("/api/comment/{id}/like", comment.getId())
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(user)))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void whenLike_ifFails_thenReturns400() throws Exception {
        Comment comment = new Comment();

        Response response = new Response(null, ServiceStatus.ERROR);

        when(commentService.like(anyLong(), anyString())).thenReturn(response);

        mockMvc.perform(put("/api/comment/{id}/like", comment.getId())
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(user)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void unlikeComment() throws Exception {
        Comment comment = new Comment();

        Response response = new Response(null, ServiceStatus.SUCCESS);

        when(commentService.unlike(anyLong(), anyString())).thenReturn(response);

        mockMvc.perform(put("/api/comment/{id}/unlike", comment.getId())
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(user)))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void whenUnlike_ifFails_thenReturns400() throws Exception {
        Comment comment = new Comment();

        Response response = new Response(null, ServiceStatus.ERROR);

        when(commentService.unlike(anyLong(), anyString())).thenReturn(response);

        mockMvc.perform(put("/api/comment/{id}/unlike", comment.getId())
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(user)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void deleteComment() throws Exception {
        Response response = new Response(null, ServiceStatus.SUCCESS);

        when(commentService.delete(anyLong())).thenReturn(response);

        mockMvc.perform(delete("/api/comment/{id}/delete", 1)
                        .contentType("application/json"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void whenDelete_ifCommentNotFound_thenReturns404() throws Exception {
        Response response = new Response(null, ServiceStatus.COMMENT_NOT_FOUND);

        when(commentService.delete(anyLong())).thenReturn(response);

        mockMvc.perform(delete("/api/comment/{id}/delete", 1)
                        .contentType("application/json"))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void whenDelete_ifFails_thenReturns400() throws Exception {
        Response response = new Response(null, ServiceStatus.ERROR);

        when(commentService.delete(anyLong())).thenReturn(response);

        mockMvc.perform(delete("/api/comment/{id}/delete", 1)
                        .contentType("application/json"))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }
    
}
