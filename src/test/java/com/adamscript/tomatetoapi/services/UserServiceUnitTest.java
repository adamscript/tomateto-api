package com.adamscript.tomatetoapi.services;

import com.adamscript.tomatetoapi.models.entities.User;
import com.adamscript.tomatetoapi.models.repos.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

class UserServiceUnitTest {

    private UserRepository userRepository = Mockito.mock(UserRepository.class);

    private UserService userService;

    @BeforeEach
    void initUserService(){
        userService = new UserService(userRepository);
    }

    @Test
    void getUserInformation() {
        User user = new User();
        user.setUsername("eyesocketdisc");
        user.setDisplayName("EyeSocketDisc");

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));

        assertThat(userService.list(1)).isNotNull();
    }

    @Test
    void ifUserNotFound_thenReturnsNull() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThat(userService.list(1)).isNull();
    }

    @Test
    void insertNewUser() {
        User user = new User();

        when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenReturn(user);

        Optional<User> newUser = userService.insert(user);

        assertThat(newUser).isNotNull();
    }

    @Test
    void ifUsernameAlreadyExists_thenReturnsError() {
        User user = new User();
        user.setUsername("eyesocketdisc");
        user.setDisplayName("EyeSocketDisc");

        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));

        assertThat(userService.insert(user)).isNull();
    }

    @Test
    void editUser() {

    }

    @Test
    void ifUsernameDoesNotExists_thenReturnsError() {

    }

    @Test
    void ifNullValue_thenReturnsError() {

    }

    @Test
    void followAUser() {
        User user1 = new User();
        user1.setId(4);

        User user2 = new User();
        user2.setId(5);

        when(userRepository.findById(4L)).thenReturn(Optional.of(user1));
        when(userRepository.findById(5L)).thenReturn(Optional.of(user2));

        assertThat(userService.follow(4L, 5L)).isNotNull();
    }

    @Test
    void ifFollowingYourself_thenReturnsError() {
        User user = new User();

        when(userRepository.findById(4L)).thenReturn(Optional.of(user));

        assertThat(userService.follow(4L, 4L)).isNull();
    }

    @Test
    void ifFollowingNonExistingUser_thenReturnsError() {
        User user = new User();

        when(userRepository.findById(4L)).thenReturn(Optional.of(user));
        when(userRepository.findById(5L)).thenReturn(Optional.empty());

        assertThat(userService.follow(4L, 5L)).isNull();
    }
}