package com.adamscript.tomatetoapi.models.repos;

import com.adamscript.tomatetoapi.models.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    @Modifying(clearAutomatically = true)
    @Query(value = "insert into user_follow (user_following, user_followed) values (?1, ?2)", nativeQuery = true)
    void followUser(Long userFollowing, Long userFollowed);

}
