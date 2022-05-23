package com.adamscript.tomatetoapi.models.repos;

import com.adamscript.tomatetoapi.models.entities.Comment;
import com.adamscript.tomatetoapi.models.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    @Modifying(clearAutomatically = true)
    @Query(value = "insert into comment_likes (comment_id, user_id) values (?1, ?2)", nativeQuery = true)
    void likeComment(Long commentId, Long userId);

    @Modifying(clearAutomatically = true)
    @Query(value = "delete from comment_likes where comment_id = ?1 and user_id = ?2", nativeQuery = true)
    void unlikeComment(Long commentId, Long userId);

    @Query("select c from Comment c where c.id = ?1 and ?2 member of c.likes")
    List<Comment> findLike(Long commentId, Optional<User> userLiked);

}
