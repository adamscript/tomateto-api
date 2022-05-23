package com.adamscript.tomatetoapi.models.entities;

import com.fasterxml.jackson.annotation.*;
import com.sun.istack.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter @Setter @NoArgsConstructor
@JsonIdentityInfo(
        scope = Post.class,
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id"
)
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

    @NotNull
    private String content;

    private String picture;

    private boolean isEdited;

    private long likesCount;

    @ManyToMany
    @JoinTable(
            name = "postLikes",
            joinColumns = @JoinColumn(name = "postId"),
            inverseJoinColumns = @JoinColumn(name = "userId")
    )
    private Set<User> likes = new HashSet<>();

    private long commentsCount;

    @OneToMany(mappedBy = "postId")
    private Set<Comment> comments = new HashSet<>();

}
