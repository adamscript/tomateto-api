package com.adamscript.tomatetoapi.models.repos;

import com.adamscript.tomatetoapi.models.dto.FeedPostDTO;
import com.adamscript.tomatetoapi.models.entities.Post;
import com.adamscript.tomatetoapi.models.entities.User;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface FeedRepository extends JpaRepository<Post, Long> {

    @Query("select new com.adamscript.tomatetoapi.models.dto.FeedPostDTO(p.id, p.content, p.photo, p.date, p.likesCount, p.commentsCount, p.user, p.isEdited) from Post p")
    List<FeedPostDTO> findBy();

    @Query("select new com.adamscript.tomatetoapi.models.dto.FeedPostDTO(p.id, p.content, p.photo, p.date, p.likesCount, p.commentsCount, p.user, p.isEdited) from Post p")
    List<FeedPostDTO> findAllSort(Sort sort);

    @Query("select new com.adamscript.tomatetoapi.models.dto.FeedPostDTO(p.id, p.content, p.photo, p.date, p.likesCount, p.commentsCount, p.user, p.isEdited) from Post p where p.user = ?1 or ?1 member of p.user.followers")
    List<FeedPostDTO> findByFollow(Optional<User> user, Sort sort);

}
