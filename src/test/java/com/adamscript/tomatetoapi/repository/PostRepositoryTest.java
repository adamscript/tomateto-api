package com.adamscript.tomatetoapi.repository;

import com.adamscript.tomatetoapi.models.entities.Post;
import com.adamscript.tomatetoapi.models.entities.User;
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
public class PostRepositoryTest {

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    private User user;
    private Post post;

    @BeforeEach
    void initPostRepository(){
        post = new Post();
        entityManager.persist(post);

        user = new User();
        user.setId("user");
        entityManager.persist(user);
    }

    @Test
    void likePost(){
        postRepository.likePost(post.getId(), user.getId());

        Post likedPost = postRepository.findById(post.getId()).get();
        User likedUser = userRepository.findById(user.getId()).get();

        assertThat(likedPost.getLikes().iterator().next()).isNotNull().isEqualTo(likedUser);
        assertThat(likedUser.getLikedPosts().iterator().next()).isNotNull().isEqualTo(likedPost);
    }

    @Test
    void unlikePost(){
        postRepository.likePost(post.getId(), user.getId());
        postRepository.unlikePost(post.getId(), user.getId());

        Post likedPost = postRepository.findById(post.getId()).get();
        User likedUser = userRepository.findById(user.getId()).get();

        assertThat(likedPost.getLikes()).isEqualTo(Set.of());
        assertThat(likedUser.getLikedPosts()).isEqualTo(Set.of());
    }

    @Test
    void findLike(){
        postRepository.likePost(post.getId(), user.getId());

        Optional<Post> likedPost = postRepository.findById(post.getId());

        assertThat(postRepository.findLike(post.getId(), Optional.of(user))).isEqualTo(List.of(likedPost.get()));
    }

}
