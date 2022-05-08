package com.adamscript.tomatetoapi.services;

import com.adamscript.tomatetoapi.models.entities.User;
import com.adamscript.tomatetoapi.models.repos.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

class UserServiceUnitTest {

    private UserRepository userRepository = Mockito.mock(UserRepository.class);

    private UserService userService;

    @BeforeEach
    void initUserService(){
        userService = new UserService(userRepository);
    }

    @Test
    void listUserInformation() {
        User user = new User();
        user.setUsername("adamscript");
        user.setDisplayName("Adam Darmawan");

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));

        assertThat(userService.list(1)).isNotNull();
    }
}