package com.adamscript.tomatetoapi.models.repos;

import com.adamscript.tomatetoapi.models.dto.FeedCommentDTO;
import com.adamscript.tomatetoapi.models.dto.FeedPostDTO;
import com.adamscript.tomatetoapi.models.dto.PostContentDTO;
import com.adamscript.tomatetoapi.models.entities.Post;
import com.adamscript.tomatetoapi.models.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {

    @Modifying(clearAutomatically = true)
    @Query(value = "insert into post_likes (post, users) values (?1, ?2)", nativeQuery = true)
    void likePost(Long postId, String userId);

    @Modifying(clearAutomatically = true)
    @Query(value = "delete from post_likes where post = ?1 and users = ?2", nativeQuery = true)
    void unlikePost(Long postId, String userId);

    @Query("select p from Post p where p.id = ?1 and ?2 member of p.likes")
    List<Post> findLike(Long postId, Optional<User> userLiked);

    @Query("select new com.adamscript.tomatetoapi.models.dto.PostContentDTO(p.id, p.content, p.date, p.likesCount, p.commentsCount, p.user) from Post p where p = ?1")
    PostContentDTO findContent(Post post);

    @Query("select new com.adamscript.tomatetoapi.models.dto.FeedCommentDTO(c.id, c.content, c.date, c.likesCount, c.user) from Comment c where c.post = ?1")
    List<FeedCommentDTO> findCommentByPost(Post post);

}
