package com.adamscript.tomatetoapi.models.repos;

import com.adamscript.tomatetoapi.models.dto.FeedCommentDTO;
import com.adamscript.tomatetoapi.models.dto.FeedPostDTO;
import com.adamscript.tomatetoapi.models.dto.FeedUserDTO;
import com.adamscript.tomatetoapi.models.dto.UserDetailDTO;
import com.adamscript.tomatetoapi.models.entities.Post;
import com.adamscript.tomatetoapi.models.entities.User;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {

    Optional<User> findByUsername(String username);

    @Query("select new com.adamscript.tomatetoapi.models.dto.UserDetailDTO(u.id, u.displayName, u.username, u.bio, u.avatarDefault, u.avatarMedium, u.avatarSmall, u.avatarExtrasmall, u.postsCount, u.followCount, u.followersCount) from User u where u.username = ?1")
    Optional<UserDetailDTO> findProfileByUsername(String username);

    @Modifying(clearAutomatically = true)
    @Query(value = "insert into user_follow (user_following, user_followed) values (?1, ?2)", nativeQuery = true)
    void followUser(String userFollowing, String userFollowed);

    @Modifying(clearAutomatically = true)
    @Query(value = "delete from user_follow where user_following = ?1 and user_followed = ?2", nativeQuery = true)
    void unfollowUser(String userFollowing, String userFollowed);

    @Query("select u from User u where u.id = ?1 and ?2 member of u.follow")
    List<User> findFollow(String userFollowingId, Optional<User> userFollowed);

    @Query("select new com.adamscript.tomatetoapi.models.dto.FeedUserDTO(u.id, u.displayName, u.username, u.bio, u.avatarDefault, u.avatarMedium, u.avatarSmall, u.avatarExtrasmall) from User u")
    List<FeedUserDTO> findAllUsers();

    @Query("select new com.adamscript.tomatetoapi.models.dto.FeedUserDTO(u.id, u.displayName, u.username, u.bio, u.avatarDefault, u.avatarMedium, u.avatarSmall, u.avatarExtrasmall) from User u where ?1 member of u.followers")
    List<FeedUserDTO> findFollows(User user);

    @Query("select new com.adamscript.tomatetoapi.models.dto.FeedUserDTO(u.id, u.displayName, u.username, u.bio, u.avatarDefault, u.avatarMedium, u.avatarSmall, u.avatarExtrasmall) from User u where ?1 member of u.follow")
    List<FeedUserDTO> findFollowers(User user);

    @Query("select new com.adamscript.tomatetoapi.models.dto.FeedUserDTO(u.id, u.displayName, u.username, u.bio, u.avatarDefault, u.avatarMedium, u.avatarSmall, u.avatarExtrasmall) from User u where u <> ?1 and ?1 not member of u.followers")
    List<FeedUserDTO> findNonFollows(Optional<User> user);

    @Query("select new com.adamscript.tomatetoapi.models.dto.FeedPostDTO(p.id, p.content, p.photo, p.date, p.likesCount, p.commentsCount, p.user, p.isEdited) from Post p where p.user = ?1")
    List<FeedPostDTO> findPostByUser(User user, Sort sort);

    @Query("select new com.adamscript.tomatetoapi.models.dto.FeedPostDTO(p.id, p.content, p.photo, p.date, p.likesCount, p.commentsCount, p.user, p.isEdited) from Post p where ?1 member of p.likes")
    List<FeedPostDTO> findLikedByUser(User user, Sort sort);

    @Query("select new com.adamscript.tomatetoapi.models.dto.FeedCommentDTO(c.id, c.content, c.date, c.likesCount, c.user, c.post) from Comment c where c.user = ?1")
    List<FeedCommentDTO> findCommentByUser(User user, Sort sort);
}
