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

    private User user;
    private User user2;

    @BeforeEach
    void initUserService(){
        userService = new UserService(userRepository, postRepository, commentRepository);

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
    void getUserInformation() {
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
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(principal.getName()).thenReturn(user.getId());

        Response newUser = userService.insert(user, principal);

        assertThat(newUser.getCode()).isEqualTo(0);
    }

    @Test
    void whenInsert_ifUsernameAlreadyExists_thenReturnsError() {
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));

        assertThat(userService.insert(user, principal).getCode()).isEqualTo(101);
    }

    @Test
    void whenInsert_ifNullValue_thenReturnsError() {
        user.setUsername(null);

        assertThat(userService.insert(user, principal).getCode()).isEqualTo(103);
    }

    @Test
    void editUser() {
        when(userRepository.findById(anyString())).thenReturn(Optional.of(user));
        when(principal.getName()).thenReturn(user.getId());

        assertThat(userService.edit(user, principal).getCode()).isEqualTo(0);
    }

    @Test
    void whenEdit_ifUsernameDoesNotExists_thenReturnsError() {
        when(userRepository.findById(anyString())).thenReturn(Optional.empty());

        assertThat(userService.edit(user, principal).getCode()).isEqualTo(102);
    }

    @Test
    void whenEdit_ifUsernameAlreadyUsed_thenReturnsError() {
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user2));
        when(userRepository.findById(anyString())).thenReturn(Optional.of(user));
        when(principal.getName()).thenReturn(user.getId());

        assertThat(userService.edit(user2, principal).getCode()).isEqualTo(101);
    }

    @Test
    void whenEdit_ifNullValue_thenReturnsError() {
        user.setUsername(null);

        assertThat(userService.edit(user, principal).getCode()).isEqualTo(103);
    }

    @Test
    void followAUser() {
        when(principal.getName()).thenReturn(user.getId());
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(userRepository.findById(user2.getId())).thenReturn(Optional.of(user2));

        assertThat(userService.follow(user2.getId(), principal).getCode()).isEqualTo(0);
    }

    @Test
    void ifFollowingYourself_thenReturnsError() {
        when(principal.getName()).thenReturn(user.getId());
        when(userRepository.findById(anyString())).thenReturn(Optional.of(user));

        assertThat(userService.follow(user.getId(), principal).getCode()).isEqualTo(105);
    }

    @Test
    void ifFollowingNonExistingUser_thenReturnsError() {
        when(principal.getName()).thenReturn(user.getId());
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(userRepository.findById(user2.getId())).thenReturn(Optional.empty());

        assertThat(userService.follow(user2.getId(), principal).getCode()).isEqualTo(106);
    }

    @Test
    void ifFollowerUserDoesNotExist_thenReturnsError() {
        when(principal.getName()).thenReturn(user.getId());
        when(userRepository.findById(user2.getId())).thenReturn(Optional.of(user2));
        when(userRepository.findById(user.getId())).thenReturn(Optional.empty());

        assertThat(userService.follow(user2.getId(), principal).getCode()).isEqualTo(107);
    }

    @Test
    void ifAlreadyFollowed_thenReturnsError() {
        when(principal.getName()).thenReturn(user.getId());
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(userRepository.findById(user2.getId())).thenReturn(Optional.of(user2));
        when(userRepository.findFollow(anyString(), any())).thenReturn(List.of(user));

        assertThat(userService.follow(user2.getId(), principal).getCode()).isEqualTo(108);
    }

    @Test
    void unfollowAUser() {
        when(principal.getName()).thenReturn(user.getId());
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(userRepository.findById(user2.getId())).thenReturn(Optional.of(user2));

        when(userRepository.findFollow(anyString(), any())).thenReturn(List.of(user));

        assertThat(userService.unfollow(user2.getId(), principal).getCode()).isEqualTo(0);
    }

    @Test
    void ifAlreadyUnfollowed_thenReturnsError(){
        when(principal.getName()).thenReturn("5");
        when(userRepository.findFollow(anyString(), any())).thenReturn(List.of());

        assertThat(userService.unfollow("4L", principal).getCode()).isEqualTo(109);
    }
}