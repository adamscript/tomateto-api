package com.adamscript.tomatetoapi.repository;

import com.adamscript.tomatetoapi.models.entities.Comment;
import com.adamscript.tomatetoapi.models.entities.Post;
import com.adamscript.tomatetoapi.models.entities.User;
import com.adamscript.tomatetoapi.models.repos.CommentRepository;
import com.adamscript.tomatetoapi.models.repos.PostRepository;
import com.adamscript.tomatetoapi.models.repos.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class CommentRepositoryTest {

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private UserRepository userRepository;

    private User user;
    private Comment comment;

    @BeforeEach
    void initCommentRepository(){
        comment = new Comment();
        entityManager.persist(comment);

        user = new User();
        user.setId("user");
        entityManager.persist(user);
    }

    @Test
    void likeComment(){
        commentRepository.likeComment(comment.getId(), user.getId());

        Comment likedComment = commentRepository.findById(comment.getId()).get();
        User likedUser = userRepository.findById(user.getId()).get();

        assertThat(likedComment.getLikes().iterator().next()).isNotNull().isEqualTo(likedUser);
        assertThat(likedUser.getLikedComments().iterator().next()).isNotNull().isEqualTo(likedComment);
    }

    @Test
    void unlikeComment(){
        commentRepository.likeComment(comment.getId(), user.getId());
        commentRepository.unlikeComment(comment.getId(), user.getId());

        Comment likedComment = commentRepository.findById(comment.getId()).get();
        User likedUser = userRepository.findById(user.getId()).get();

        assertThat(likedComment.getLikes()).isEqualTo(Set.of());
        assertThat(likedUser.getLikedComments()).isEqualTo(Set.of());
    }

    @Test
    void findLike(){
        commentRepository.likeComment(comment.getId(), user.getId());

        Optional<Comment> likedComment = commentRepository.findById(comment.getId());

        assertThat(commentRepository.findLike(comment.getId(), Optional.of(user))).isEqualTo(List.of(likedComment.get()));
    }


}
