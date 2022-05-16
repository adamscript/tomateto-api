package com.adamscript.tomatetoapi.controllers;

import com.adamscript.tomatetoapi.models.entities.User;
import com.adamscript.tomatetoapi.services.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    @Test
    void getUserInformation() throws Exception {
        User user = new User();
        user.setUsername("eyesocketdisc");
        user.setDisplayName("EyeSocketDisc");

        when(userService.list(anyLong())).thenReturn(Optional.of(user));

        mockMvc.perform(get("/api/user/{id}", 1)
                        .contentType("application/json"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value(user.getUsername()));
    }

    @Test
    void ifUserNotFound_thenReturns404() throws Exception {
        when(userService.list(anyLong())).thenReturn(null);

        mockMvc.perform(get("/api/user/{id}", 3)
                        .contentType("application/json"))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void insertNewUser() throws Exception {
        User user = new User();
        user.setUsername("eyesocketdisc");
        user.setDisplayName("EyeSocketDisc");

        when(userService.insert(any(User.class))).thenReturn(Optional.of(user));

        mockMvc.perform(post("/api/user")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(user)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value(user.getUsername()));
    }

    @Test
    void ifUsernameAlreadyExists_thenReturns409() throws Exception {
        User user = new User();

        when(userService.insert(any(User.class))).thenReturn(null);

        mockMvc.perform(post("/api/user")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(user)))
                .andDo(print())
                .andExpect(status().isConflict());
    }

    @Test
    void editUser() throws Exception {

    }

    @Test
    void ifUsernameDoesNotExists_thenReturns400() throws Exception {

    }

    @Test
    void ifNullValue_thenReturns400() throws Exception {

    }

}