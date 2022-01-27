package com.adamscript.tomatetoapi.models.entities;

import com.sun.istack.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter @Setter @NoArgsConstructor
@Table(name = "post")
public class Post implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @NotNull
    @JoinColumn(name = "userId", referencedColumnName = "id")
    private User userId;

    private Instant date = Instant.now();

    private String status;

    @OneToOne
    @JoinColumn(name = "storyId")
    private Story storyId;

    @OneToOne
    @JoinColumn(name = "ratingId")
    private Rating ratingId;

    @ManyToMany
    @JoinTable(
            name = "postLike",
            joinColumns = @JoinColumn(name = "postId"),
            inverseJoinColumns = @JoinColumn(name = "userId")
    )
    Set<User> userLiked = new HashSet<>();

}
