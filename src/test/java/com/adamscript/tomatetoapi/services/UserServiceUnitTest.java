package com.adamscript.tomatetoapi.services;

import com.adamscript.tomatetoapi.helpers.handler.Response;
import com.adamscript.tomatetoapi.models.entities.User;
import com.adamscript.tomatetoapi.models.repos.CommentRepository;
import com.adamscript.tomatetoapi.models.repos.PostRepository;
import com.adamscript.tomatetoapi.models.repos.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

class UserServiceUnitTest {

    private UserRepository userRepository = Mockito.mock(UserRepository.class);
    private PostRepository postRepository = Mockito.mock(PostRepository.class);
    private CommentRepository commentRepository = Mockito.mock(CommentRepository.class);

    private Principal principal = Mockito.mock(Principal.class);

    private UserService userService;

    @BeforeEach
    void initUserService(){
        userService = new UserService(userRepository, postRepository, commentRepository);
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
        when(principal.getName()).thenReturn("1");

        Response newUser = userService.insert(user, principal);

        assertThat(newUser.getCode()).isEqualTo(0);
    }

    @Test
    void whenInsert_ifUsernameAlreadyExists_thenReturnsError() {
        User user = new User();
        user.setUsername("eyesocketdisc");
        user.setDisplayName("EyeSocketDisc");

        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));

        assertThat(userService.insert(user, principal).getCode()).isEqualTo(101);
    }

    @Test
    void whenInsert_ifNullValue_thenReturnsError() {
        User user = new User();

        assertThat(userService.insert(user, principal).getCode()).isEqualTo(103);
    }

    @Test
    void editUser() {
        User user = new User();
        user.setUsername("eyesocketdisc");
        user.setDisplayName("EyeSocketDisc");

        when(userRepository.findById(anyString())).thenReturn(Optional.of(user));
        when(principal.getName()).thenReturn("1");

        assertThat(userService.edit(user, principal).getCode()).isEqualTo(0);
    }

    @Test
    void whenEdit_ifUsernameDoesNotExists_thenReturnsError() {
        User user = new User();
        user.setUsername("eyesocketdisc");
        user.setDisplayName("EyeSocketDisc");

        when(userRepository.findById(anyString())).thenReturn(Optional.empty());

        assertThat(userService.edit(user, principal).getCode()).isEqualTo(102);
    }

    @Test
    void whenEdit_ifUsernameAlreadyUsed_thenReturnsError() {

    }

    @Test
    void whenEdit_ifNullValue_thenReturnsError() {
        User user = new User();

        assertThat(userService.edit(user, principal).getCode()).isEqualTo(103);
    }

    @Test
    void followAUser() {
        User user1 = new User();
        user1.setId("4");

        User user2 = new User();
        user2.setId("5");

        when(principal.getName()).thenReturn("5");
        when(userRepository.findById("4")).thenReturn(Optional.of(user1));

        assertThat(userService.follow("4L", principal).getCode()).isEqualTo(0);
    }

    @Test
    void ifFollowingYourself_thenReturnsError() {
        User user = new User();
        user.setId("4");

        when(principal.getName()).thenReturn("4");
        when(userRepository.findById("4")).thenReturn(Optional.of(user));

        assertThat(userService.follow("4", principal).getCode()).isEqualTo(105);
    }

    @Test
    void ifFollowingNonExistingUser_thenReturnsError() {
        User user = new User();

        when(principal.getName()).thenReturn("5");
        when(userRepository.findById("4")).thenReturn(Optional.of(user));
        when(userRepository.findById("5")).thenReturn(Optional.empty());

        assertThat(userService.follow("4", principal).getCode()).isEqualTo(106);
    }

    @Test
    void ifFollowerUserDoesNotExist_thenReturnsError() {
        User user = new User();

        when(principal.getName()).thenReturn("4");
        when(userRepository.findById("4")).thenReturn(Optional.of(user));
        when(userRepository.findById("5")).thenReturn(Optional.empty());

        assertThat(userService.follow("5", principal).getCode()).isEqualTo(107);
    }

    @Test
    void ifAlreadyFollowed_thenReturnsError() {
        User user = new User();

        when(principal.getName()).thenReturn("5");
        when(userRepository.findById("4")).thenReturn(Optional.of(user));
        when(userRepository.findById("5")).thenReturn(Optional.of(user));
        when(userRepository.findFollow(anyString(), any())).thenReturn(List.of(user));

        assertThat(userService.follow("4", principal).getCode()).isEqualTo(108);
    }

    @Test
    void unfollowAUser() {
        User user = new User();

        when(principal.getName()).thenReturn("5");
        when(userRepository.findFollow(anyString(), any())).thenReturn(List.of(user));

        assertThat(userService.unfollow("4", principal).getCode()).isEqualTo(0);
    }

    @Test
    void ifAlreadyUnfollowed_thenReturnsError(){
        when(principal.getName()).thenReturn("5");
        when(userRepository.findFollow(anyString(), any())).thenReturn(List.of());

        assertThat(userService.unfollow("4L", principal).getCode()).isEqualTo(109);
    }
}