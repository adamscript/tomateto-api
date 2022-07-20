package com.adamscript.tomatetoapi.repository;

import com.adamscript.tomatetoapi.models.entities.User;
import com.adamscript.tomatetoapi.models.repos.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.persistence.EntityManager;
import javax.sql.DataSource;
import java.util.*;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UserRepositoryTest {

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private UserRepository userRepository;

    private User user;
    private User user2;

    @BeforeEach
    void initUserRepository(){
        user = new User();
        user.setId("user");
        entityManager.persist(user);

        user2 = new User();
        user2.setId("user2");
        entityManager.persist(user2);
    }

    @Test
    void followAUser() {
        userRepository.followUser(user.getId(), user2.getId());

        Optional<User> followingUser = userRepository.findById(user.getId());
        Optional<User> followedUser = userRepository.findById(user2.getId());

        assertThat(followingUser.get().getFollow().iterator().next()).isNotNull().isEqualTo(followedUser.get());
        assertThat(followedUser.get().getFollowers().iterator().next()).isNotNull().isEqualTo(followingUser.get());

    }

    @Test
    void unfollowAUser() {
        userRepository.followUser(user.getId(), user2.getId());
        userRepository.unfollowUser(user.getId(), user2.getId());

        Optional<User> followingUser = userRepository.findById(user.getId());
        Optional<User> followedUser = userRepository.findById(user2.getId());

        assertThat(followingUser.get().getFollow()).isEqualTo(Set.of());
        assertThat(followedUser.get().getFollowers()).isEqualTo(Set.of());

    }

    @Test
    void findFollow() {
        userRepository.followUser(user.getId(), user2.getId());

        Optional<User> followingUser = userRepository.findById(user.getId());

        assertThat(userRepository.findFollow(user.getId(), Optional.of(user2))).isNotNull().isEqualTo(List.of(followingUser.get()));

    }

}
