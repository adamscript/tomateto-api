package com.adamscript.tomatetoapi.models.repos;

import com.adamscript.tomatetoapi.models.entities.Post;
import com.adamscript.tomatetoapi.models.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {

    @Modifying(clearAutomatically = true)
    @Query(value = "insert into post_likes (post_id, user_id) values (?1, ?2)", nativeQuery = true)
    void likePost(Long postId, Long userId);

    @Modifying(clearAutomatically = true)
    @Query(value = "delete from post_likes where post_id = ?1 and user_id = ?2", nativeQuery = true)
    void unlikePost(Long postId, Long userId);

    @Query("select p from Post p where p.id = ?1 and ?2 member of p.likes")
    List<Post> findLike(Long postId, Optional<User> userLiked);
}
