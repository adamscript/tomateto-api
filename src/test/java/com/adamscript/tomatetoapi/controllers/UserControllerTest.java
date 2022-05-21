package com.adamscript.tomatetoapi.controllers;

import com.adamscript.tomatetoapi.helpers.handler.Response;
import com.adamscript.tomatetoapi.helpers.service.ServiceStatus;
import com.adamscript.tomatetoapi.models.entities.User;
import com.adamscript.tomatetoapi.services.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
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

    @Test
    void getUserInformation() throws Exception {
        User user = new User();
        user.setUsername("eyesocketdisc");
        user.setDisplayName("EyeSocketDisc");

        Response response = new Response(user, ServiceStatus.SUCCESS);

        when(userService.list(anyLong())).thenReturn(response);

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

        when(userService.list(anyLong())).thenReturn(response);

        mockMvc.perform(get("/api/user/{id}", 3)
                        .contentType("application/json"))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void whenGetUser_ifFails_thenReturns400() throws Exception {
        Response response = new Response(null, ServiceStatus.ERROR);

        when(userService.list(anyLong())).thenReturn(response);

        mockMvc.perform(get("/api/user/{id}", 3)
                        .contentType("application/json"))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void insertNewUser() throws Exception {
        User user = new User();
        user.setUsername("eyesocketdisc");
        user.setDisplayName("EyeSocketDisc");

        Response response = new Response(user, ServiceStatus.SUCCESS);

        when(userService.insert(any(User.class))).thenReturn(response);

        mockMvc.perform(post("/api/user")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(user)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items.username").value(user.getUsername()));
    }

    @Test
    void ifUsernameAlreadyExists_thenReturns409() throws Exception {
        User user = new User();

        Response response = new Response(null, ServiceStatus.USERNAME_ALREADY_EXIST);

        when(userService.insert(any(User.class))).thenReturn(response);

        mockMvc.perform(post("/api/user")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(user)))
                .andDo(print())
                .andExpect(status().isConflict());
    }

    @Test
    void whenInsertUser_ifFails_thenReturns400() throws Exception {
        User user = new User();

        Response response = new Response(null, ServiceStatus.ERROR);

        when(userService.insert(any(User.class))).thenReturn(response);

        mockMvc.perform(post("/api/user")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(user)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void editUser() throws Exception {
        User user = new User();
        user.setUsername("eyesocketdisc");
        user.setDisplayName("EyeSocketDisc");

        Response response = new Response(user, ServiceStatus.SUCCESS);

        when(userService.edit(any(User.class))).thenReturn(response);

        mockMvc.perform(put("/api/user")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(user)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items.username").value(user.getUsername()));
    }

    @Test
    void ifUsernameDoesNotExists_thenReturns404() throws Exception {
        User user = new User();

        Response response = new Response(null, ServiceStatus.USER_DOES_NOT_EXIST                                  );

        when(userService.edit(any(User.class))).thenReturn(response);

        mockMvc.perform(put("/api/user")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(user)))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void whenEditUser_ifFails_thenReturns400() throws Exception {
        User user = new User();

        Response response = new Response(null, ServiceStatus.ERROR);

        when(userService.edit(any(User.class))).thenReturn(response);

        mockMvc.perform(put("/api/user")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(user)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void followAUser() throws Exception {
        User user1 = new User();
        User user2 = new User();

        Response response = new Response(null, ServiceStatus.SUCCESS);

        when(userService.follow(anyLong(), anyLong())).thenReturn(response);

        mockMvc.perform(put("/api/user/{id}/follow", user2.getId())
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(user1)))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void whenFollow_ifFails_thenReturns400() throws Exception {
        User user = new User();

        Response response = new Response(null, ServiceStatus.ERROR);

        when(userService.follow(anyLong(), anyLong())).thenReturn(response);

        mockMvc.perform(put("/api/user/{id}/follow", 4L)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(user)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void unfollowAUser() throws Exception {
        User user1 = new User();
        User user2 = new User();

        Response response = new Response(null, ServiceStatus.SUCCESS);

        when(userService.unfollow(anyLong(), anyLong())).thenReturn(response);

        mockMvc.perform(put("/api/user/{id}/unfollow", user2.getId())
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(user1)))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void whenUnfollow_ifFails_thenReturns400() throws Exception {
        User user = new User();

        Response response = new Response(null, ServiceStatus.ERROR);

        when(userService.unfollow(anyLong(), anyLong())).thenReturn(response);

        mockMvc.perform(put("/api/user/{id}/unfollow", 4L)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(user)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

}