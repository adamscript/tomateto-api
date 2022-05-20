package com.adamscript.tomatetoapi.models.repos;

import com.adamscript.tomatetoapi.models.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    @Modifying(clearAutomatically = true)
    @Query(value = "insert into user_follow (user_following, user_followed) values (?1, ?2)", nativeQuery = true)
    void followUser(Long userFollowing, Long userFollowed);

    @Modifying(clearAutomatically = true)
    @Query(value = "delete from user_follow where user_following = ?1 and user_followed = ?2", nativeQuery = true)
    void unfollowUser(Long userFollowing, Long userFollowed);

    @Query("select u from User u where u.id = ?1 and ?2 member of u.follow")
    List<User> findFollow(Long userFollowingId, Optional<User> userFollowed);

}
