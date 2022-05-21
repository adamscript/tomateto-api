package com.adamscript.tomatetoapi.integration;

import com.adamscript.tomatetoapi.models.entities.User;
import com.adamscript.tomatetoapi.models.repos.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
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
public class UserIntegrationTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Test
    void getUserInformation() throws Exception {
        User user = new User();
        user.setUsername("eyesocketdisc");
        user.setDisplayName("EyeSocketDisc");

        userRepository.saveAndFlush(user);

        mockMvc.perform(get("/api/user/{id}", user.getId())
                        .contentType("application/json"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items.username").value(user.getUsername()));

    }

    @Test
    void insertNewUser() throws Exception {
        User user = new User();
        user.setUsername("eyesocketdisc2");
        user.setDisplayName("EyeSocketDisc");

        mockMvc.perform(post("/api/user")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(user)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items.username").value(user.getUsername()));
    }

    @Test
    void editUser() throws Exception {
        User user = new User();
        user.setUsername("eyesocketdisc");
        user.setDisplayName("EyeSocketDisc");

        userRepository.saveAndFlush(user);

        User editedUser = new User();
        editedUser.setId(user.getId());
        editedUser.setUsername("eyesocketdisc2");
        editedUser.setDisplayName(user.getDisplayName());

        mockMvc.perform(put("/api/user")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(editedUser)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items.username").value(editedUser.getUsername()));
    }

    @Test
    void followAUser() throws Exception {
        User user1 = new User();
        User user2 = new User();
        userRepository.saveAll(List.of(user1, user2));

        mockMvc.perform(put("/api/user/{id}/follow", user2.getId())
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(user1)))
                .andDo(print())
                .andExpect(status().isOk());

        User followingUser = userRepository.findById(user1.getId()).get();

        //check if user successfully followed
        assertThat(userRepository.findFollow(user1.getId(), Optional.of(user2))).isNotNull().isEqualTo(List.of(followingUser));
    }

    @Test
    void unfollowAUser() throws Exception {
        User user1 = new User();
        User user2 = new User();
        userRepository.saveAll(List.of(user1, user2));

        //follow newly created users first
        userRepository.followUser(user1.getId(), user2.getId());

        //then unfollow them
        mockMvc.perform(put("/api/user/{id}/unfollow", user2.getId())
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(user1)))
                .andDo(print())
                .andExpect(status().isOk());

        //check if user successfully unfollowed
        assertThat(userRepository.findFollow(user1.getId(), Optional.of(user2))).isEqualTo(List.of());
    }

}
