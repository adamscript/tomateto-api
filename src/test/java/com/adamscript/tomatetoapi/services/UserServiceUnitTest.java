package com.adamscript.tomatetoapi.services;

import com.adamscript.tomatetoapi.helpers.handler.Response;
import com.adamscript.tomatetoapi.models.entities.User;
import com.adamscript.tomatetoapi.models.repos.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;
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

        when(userRepository.findById(anyString())).thenReturn(Optional.of(user));

        assertThat(userService.list("1").getCode()).isEqualTo(0);
    }

    @Test
    void ifUserNotFound_thenReturnsError() {
        when(userRepository.findById(anyString())).thenReturn(Optional.empty());

        assertThat(userService.list("1").getCode()).isEqualTo(100);
    }

    @Test
    void insertNewUser() {
        User user = new User();
        user.setUsername("eyesocketdisc");
        user.setDisplayName("EyeSocketDisc");

        when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenReturn(user);

        Response newUser = userService.insert(user);

        assertThat(newUser.getCode()).isEqualTo(0);
    }

    @Test
    void whenInsert_ifUsernameAlreadyExists_thenReturnsError() {
        User user = new User();
        user.setUsername("eyesocketdisc");
        user.setDisplayName("EyeSocketDisc");

        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));

        assertThat(userService.insert(user).getCode()).isEqualTo(101);
    }

    @Test
    void whenInsert_ifNullValue_thenReturnsError() {
        User user = new User();

        assertThat(userService.insert(user).getCode()).isEqualTo(103);
    }

    @Test
    void editUser() {
        User user = new User();
        user.setUsername("eyesocketdisc");
        user.setDisplayName("EyeSocketDisc");

        when(userRepository.findById(anyString())).thenReturn(Optional.of(user));

        assertThat(userService.edit(user).getCode()).isEqualTo(0);
    }

    @Test
    void whenEdit_ifUsernameDoesNotExists_thenReturnsError() {
        User user = new User();
        user.setUsername("eyesocketdisc");
        user.setDisplayName("EyeSocketDisc");

        when(userRepository.findById(anyString())).thenReturn(Optional.empty());

        assertThat(userService.edit(user).getCode()).isEqualTo(102);
    }

    @Test
    void whenEdit_ifUsernameAlreadyUsed_thenReturnsError() {

    }

    @Test
    void whenEdit_ifNullValue_thenReturnsError() {
        User user = new User();

        assertThat(userService.edit(user).getCode()).isEqualTo(103);
    }

    @Test
    void followAUser() {
        User user1 = new User();
        user1.setId("4");

        User user2 = new User();
        user2.setId("5");

        when(userRepository.findById("4L")).thenReturn(Optional.of(user1));
        when(userRepository.findById("5L")).thenReturn(Optional.of(user2));

        assertThat(userService.follow("4L", "5L").getCode()).isEqualTo(0);
    }

    @Test
    void ifFollowingYourself_thenReturnsError() {
        User user = new User();

        when(userRepository.findById("4L")).thenReturn(Optional.of(user));

        assertThat(userService.follow("4L", "4L").getCode()).isEqualTo(105);
    }

    @Test
    void ifFollowingNonExistingUser_thenReturnsError() {
        User user = new User();

        when(userRepository.findById("4L")).thenReturn(Optional.of(user));
        when(userRepository.findById("5L")).thenReturn(Optional.empty());

        assertThat(userService.follow("4L", "5L").getCode()).isEqualTo(106);
    }

    @Test
    void ifFollowerUserDoesNotExist_thenReturnsError() {
        User user = new User();

        when(userRepository.findById("4L")).thenReturn(Optional.of(user));
        when(userRepository.findById("5L")).thenReturn(Optional.empty());

        assertThat(userService.follow("5L", "4L").getCode()).isEqualTo(107);
    }

    @Test
    void ifAlreadyFollowed_thenReturnsError() {
        User user = new User();

        when(userRepository.findById("4L")).thenReturn(Optional.of(user));
        when(userRepository.findById("5L")).thenReturn(Optional.of(user));
        when(userRepository.findFollow(anyString(), any())).thenReturn(List.of(user));

        assertThat(userService.follow("4L", "5L").getCode()).isEqualTo(108);
    }

    @Test
    void unfollowAUser() {
        User user = new User();

        when(userRepository.findFollow(anyString(), any())).thenReturn(List.of(user));

        assertThat(userService.unfollow("4L", "5L").getCode()).isEqualTo(0);
    }

    @Test
    void ifAlreadyUnfollowed_thenReturnsError(){
        when(userRepository.findFollow(anyString(), any())).thenReturn(List.of());

        assertThat(userService.unfollow("4L", "5L").getCode()).isEqualTo(109);
    }
}