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
public class CommentIntegrationTest {

    @Autowired
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

    @BeforeEach
    void initCommentIntegration(){
        user = new User();
        user.setUsername("eyesocketdisc");
        user.setDisplayName("EyeSocketDisc");

        post = new Post();
        post.setUserId(user);
        post.setContent("Hi tomates! This is my first tomathought");

        userRepository.saveAndFlush(user);
        postRepository.saveAndFlush(post);
    }

    @Test
    void getCommentInformation() throws Exception{
        Comment comment = new Comment();
        comment.setUserId(user);
        comment.setPostId(post);
        comment.setContent("And this is my first mini-tomathought, aka comment! ;)");

        commentRepository.saveAndFlush(comment);

        mockMvc.perform(get("/api/comment/{id}", comment.getId())
                        .contentType("application/json"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items.content").value(comment.getContent()));
    }

    @Test
    void insertNewComment() throws Exception{
        Comment comment = new Comment();
        comment.setUserId(user);
        comment.setPostId(post);
        comment.setContent("And this is my first mini-tomathought, aka comment! ;)");

        mockMvc.perform(post("/api/comment")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(comment)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items.content").value(comment.getContent()));
    }

    @Test
    void likeComment() throws Exception{
        Comment comment = new Comment();
        User user = new User();

        commentRepository.save(comment);
        userRepository.save(user);

        mockMvc.perform(put("/api/comment/{id}/like", comment.getId())
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(user)))
                .andDo(print())
                .andExpect(status().isOk());

        Comment likedComment = commentRepository.findById(comment.getId()).get();

        //check if comment successfully liked
        assertThat(commentRepository.findLike(comment.getId(), Optional.of(user))).isNotNull().isEqualTo(List.of(likedComment));
    }

    @Test
    void unlikeComment() throws Exception{
        Comment comment = new Comment();
        User user = new User();

        commentRepository.save(comment);
        userRepository.save(user);

        //like newly created comment first
        commentRepository.likeComment(comment.getId(), user.getId());

        //then unlike it
        mockMvc.perform(put("/api/comment/{id}/unlike", comment.getId())
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(user)))
                .andDo(print())
                .andExpect(status().isOk());

        //check if comment successfully unliked
        assertThat(commentRepository.findLike(comment.getId(), Optional.of(user))).isEqualTo(List.of());
    }

    @Test
    void deleteComment() throws Exception{
        Comment comment = new Comment();
        commentRepository.saveAndFlush(comment);

        mockMvc.perform(delete("/api/comment/{id}/delete", comment.getId())
                        .contentType("application/json"))
                .andDo(print())
                .andExpect(status().isOk());

        assertThat(commentRepository.findById(comment.getId())).isEqualTo(Optional.empty());
    }
    
}
