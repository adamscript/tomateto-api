package com.adamscript.tomatetoapi.controllers;

import com.adamscript.tomatetoapi.helpers.handler.Response;
import com.adamscript.tomatetoapi.helpers.service.ServiceStatus;
import com.adamscript.tomatetoapi.models.entities.User;
import com.adamscript.tomatetoapi.services.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.security.Principal;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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

    @MockBean
    private Principal principal;

    private User user;
    private User user2;

    @BeforeEach
    void initUserController(){
        user = new User();
        user.setId("1");
        user.setUsername("eyesocketdisc");
        user.setDisplayName("EyeSocketDisc");

        user2 = new User();
        user2.setId("2");
        user2.setUsername("eyesocketdisc2");
        user2.setDisplayName("EyeSocketDisc2");
    }

    @Test
    void getUserInformation() throws Exception {
        User user = new User();
        user.setUsername("eyesocketdisc");
        user.setDisplayName("EyeSocketDisc");

        Response response = new Response(user, ServiceStatus.SUCCESS);

        when(userService.list(anyString())).thenReturn(response);

        MvcResult mvcResult = mockMvc.perform(get("/api/user/{id}", 1)
                                    .contentType("application/json"))
                                .andDo(print())
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.items.username").value(user.getUsername()))
                                .andReturn();

        assertThat(mvcResult.getResponse().getContentAsString()).isEqualToIgnoringWhitespace(objectMapper.writeValueAsString(response));
    }

    @Test
    void ifUserNotFound_thenReturns404() throws Exception {
        Response response = new Response(null, ServiceStatus.USER_NOT_FOUND);

        when(userService.list(anyString())).thenReturn(response);

        mockMvc.perform(get("/api/user/{id}", 3)
                        .contentType("application/json"))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void whenGetUser_ifFails_thenReturns400() throws Exception {
        Response response = new Response(null, ServiceStatus.ERROR);

        when(userService.list(anyString())).thenReturn(response);

        mockMvc.perform(get("/api/user/{id}", 3)
                        .contentType("application/json"))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser
    void insertNewUser() throws Exception {
        User user = new User();
        user.setUsername("eyesocketdisc");
        user.setDisplayName("EyeSocketDisc");

        Response response = new Response(user, ServiceStatus.SUCCESS);

        when(userService.insert(any(User.class), any(Principal.class))).thenReturn(response);

        mockMvc.perform(post("/api/user")
                        .with(csrf())
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(user)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items.username").value(user.getUsername()));
    }

    @Test
    @WithMockUser
    void ifUsernameAlreadyExists_thenReturns409() throws Exception {
        User user = new User();

        Response response = new Response(null, ServiceStatus.USERNAME_ALREADY_EXIST);

        when(userService.insert(any(User.class), any(Principal.class))).thenReturn(response);

        mockMvc.perform(post("/api/user")
                        .with(csrf())
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(user)))
                .andDo(print())
                .andExpect(status().isConflict());
    }

    @Test
    @WithMockUser
    void whenInsertUser_ifFails_thenReturns400() throws Exception {
        User user = new User();

        Response response = new Response(null, ServiceStatus.ERROR);

        when(userService.insert(any(User.class), any(Principal.class))).thenReturn(response);

        mockMvc.perform(post("/api/user")
                        .with(csrf())
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(user)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser
    void editUser() throws Exception {
        User user = new User();
        user.setUsername("eyesocketdisc");
        user.setDisplayName("EyeSocketDisc");

        Response response = new Response(user, ServiceStatus.SUCCESS);

        when(userService.edit(any(User.class), any(Principal.class))).thenReturn(response);

        mockMvc.perform(put("/api/user")
                        .with(csrf())
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(user)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items.username").value(user.getUsername()));
    }

    @Test
    @WithMockUser
    void ifUsernameDoesNotExists_thenReturns404() throws Exception {
        User user = new User();

        Response response = new Response(null, ServiceStatus.USER_DOES_NOT_EXIST                                  );

        when(userService.edit(any(User.class), any(Principal.class))).thenReturn(response);

        mockMvc.perform(put("/api/user")
                        .with(csrf())
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(user)))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser
    void whenEditUser_ifFails_thenReturns400() throws Exception {
        User user = new User();

        Response response = new Response(null, ServiceStatus.ERROR);

        when(userService.edit(any(User.class), any(Principal.class))).thenReturn(response);

        mockMvc.perform(put("/api/user")
                        .with(csrf())
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(user)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser
    void followAUser() throws Exception {
        Response response = new Response(null, ServiceStatus.SUCCESS);

        when(userService.follow(anyString(), any(Principal.class))).thenReturn(response);

        mockMvc.perform(put("/api/user/{id}/follow", user2.getId())
                        .with(csrf())
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(user)))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    void whenFollow_ifFails_thenReturns400() throws Exception {
        Response response = new Response(null, ServiceStatus.ERROR);

        when(userService.follow(anyString(), any(Principal.class))).thenReturn(response);

        mockMvc.perform(put("/api/user/{id}/follow", 4L)
                        .with(csrf())
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(user)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser
    void unfollowAUser() throws Exception {
        Response response = new Response(null, ServiceStatus.SUCCESS);

        when(userService.unfollow(anyString(), any(Principal.class))).thenReturn(response);

        mockMvc.perform(put("/api/user/{id}/unfollow", user2.getId())
                        .with(csrf())
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(user)))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    void whenUnfollow_ifFails_thenReturns400() throws Exception {
        Response response = new Response(null, ServiceStatus.ERROR);

        when(userService.unfollow(anyString(), any(Principal.class))).thenReturn(response);

        mockMvc.perform(put("/api/user/{id}/unfollow", 4L)
                        .with(csrf())
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(user)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

}