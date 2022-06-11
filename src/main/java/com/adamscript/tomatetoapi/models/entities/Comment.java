package com.adamscript.tomatetoapi.models.entities;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.sun.istack.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.*;

@Entity
@Getter @Setter @NoArgsConstructor
@JsonIdentityInfo(
        scope = Comment.class,
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id"
)
@Table(name = "comment")
public class Comment implements Serializable{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @NotNull
    @JoinColumn(name = "users", referencedColumnName = "id")
    private User user;

    private Instant date;

    @ManyToOne
    @NotNull
    @JoinColumn(name = "post", referencedColumnName = "id")
    private Post post;

    @NotNull
    private String content;

    private long likesCount;

    @ManyToMany
    @JoinTable(
            name = "commentLikes",
            joinColumns = @JoinColumn(name = "comment"),
            inverseJoinColumns = @JoinColumn(name = "users")
    )
    private Set<User> likes = new HashSet<>();

}
