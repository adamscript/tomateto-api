package com.adamscript.tomatetoapi.integration;

import com.adamscript.tomatetoapi.models.entities.Post;
import com.adamscript.tomatetoapi.models.entities.User;
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
public class UserIntegrationTest {

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    private User user;
    private User user2;

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

        user2 = new User();
        user2.setId("2");
        user2.setUsername("eyesocketdisc2");
        user2.setDisplayName("EyeSocketDisc2");

        userRepository.saveAndFlush(user2);
    }

    @Test
    void getUserInformation() throws Exception {
        mockMvc.perform(get("/api/user/{id}", user.getId())
                        .contentType("application/json"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items.username").value(user.getUsername()));
    }

    @Test
    @WithMockUser("1")
    void insertNewUser() throws Exception {
        User newUser = new User();
        newUser.setId("3");
        newUser.setUsername("eyesocketdisc3");
        newUser.setDisplayName("EyeSocketDisc3");

        mockMvc.perform(post("/api/user")
                        .with(csrf())
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(newUser)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items.username").value(newUser.getUsername()));
    }

    @Test
    @WithMockUser("1")
    void editUser() throws Exception {
        User editedUser = new User();
        editedUser.setId("3");
        editedUser.setUsername("eyesocketdisc3");
        editedUser.setDisplayName("EyeSocketDisc3");

        mockMvc.perform(put("/api/user")
                        .with(csrf())
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(editedUser)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items.username").value(editedUser.getUsername()));
    }

    @Test
    @WithMockUser("1")
    void followAUser() throws Exception {
        mockMvc.perform(put("/api/user/{id}/follow", user2.getId())
                        .with(csrf())
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(user)))
                .andDo(print())
                .andExpect(status().isOk());

        User followingUser = userRepository.findById(user.getId()).get();

        //check if user successfully followed
        assertThat(userRepository.findFollow(user.getId(), Optional.of(user2))).isNotNull().isEqualTo(List.of(followingUser));
    }

    @Test
    @WithMockUser("1")
    void unfollowAUser() throws Exception {
        //follow newly created users first
        userRepository.followUser(user.getId(), user2.getId());

        //then unfollow them
        mockMvc.perform(put("/api/user/{id}/unfollow", user2.getId())
                        .with(csrf())
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(user)))
                .andDo(print())
                .andExpect(status().isOk());

        //check if user successfully unfollowed
        assertThat(userRepository.findFollow(user.getId(), Optional.of(user2))).isEqualTo(List.of());
    }

}
