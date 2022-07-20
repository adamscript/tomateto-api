package com.adamscript.tomatetoapi.integration;

import com.adamscript.tomatetoapi.models.entities.Comment;
import com.adamscript.tomatetoapi.models.entities.Post;
import com.adamscript.tomatetoapi.models.entities.User;
import com.adamscript.tomatetoapi.models.repos.CommentRepository;
import com.adamscript.tomatetoapi.models.repos.PostRepository;
import com.adamscript.tomatetoapi.models.repos.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
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
public class CommentIntegrationTest {

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;
    
    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostRepository postRepository;

    private User user;
    private Post post;
    private Comment comment;

    @BeforeEach
    void initCommentIntegration(){
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();

        user = new User();
        user.setId("1");
        user.setUsername("eyesocketdisc");
        user.setDisplayName("EyeSocketDisc");

        post = new Post();
        post.setUser(user);
        post.setContent("Hi tomates! This is my first tomathought");

        comment = new Comment();
        comment.setUser(user);
        comment.setPost(post);
        comment.setContent("And this is my first mini-tomathought, aka comment! ;)");

        userRepository.saveAndFlush(user);
        postRepository.saveAndFlush(post);
        commentRepository.saveAndFlush(comment);
    }

    @Test
    void getCommentInformation() throws Exception{
        mockMvc.perform(get("/api/comment/{id}", comment.getId())
                        .contentType("application/json"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items.content").value(comment.getContent()));
    }

    @Test
    @WithMockUser("1")
    void insertNewComment() throws Exception{
        mockMvc.perform(post("/api/comment")
                        .with(csrf())
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(comment)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items.content").value(comment.getContent()));
    }

    @Test
    @WithMockUser("1")
    void likeComment() throws Exception{
        mockMvc.perform(put("/api/comment/{id}/like", comment.getId())
                        .with(csrf())
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(user)))
                .andDo(print())
                .andExpect(status().isOk());

        Comment likedComment = commentRepository.findById(comment.getId()).get();

        //check if comment successfully liked
        assertThat(commentRepository.findLike(comment.getId(), Optional.of(user))).isNotNull().isEqualTo(List.of(likedComment));
    }

    @Test
    @WithMockUser("1")
    void unlikeComment() throws Exception{
        //like newly created comment first
        commentRepository.likeComment(comment.getId(), user.getId());

        //then unlike it
        mockMvc.perform(put("/api/comment/{id}/unlike", comment.getId())
                        .with(csrf())
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(user)))
                .andDo(print())
                .andExpect(status().isOk());

        //check if comment successfully unliked
        assertThat(commentRepository.findLike(comment.getId(), Optional.of(user))).isEqualTo(List.of());
    }

    @Test
    @WithMockUser("1")
    void deleteComment() throws Exception{
        mockMvc.perform(delete("/api/comment/{id}/delete", comment.getId())
                        .with(csrf())
                        .contentType("application/json"))
                .andDo(print())
                .andExpect(status().isOk());

        assertThat(commentRepository.findById(comment.getId())).isEqualTo(Optional.empty());
    }
    
}
