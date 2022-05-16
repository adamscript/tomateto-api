package com.adamscript.tomatetoapi.models.entities;

import com.fasterxml.jackson.annotation.*;
import com.sun.istack.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter @Setter @NoArgsConstructor
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id"
)
@Table(name = "users")
public class User implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotNull
    @Column(length = 17)
    private String username;

    private Instant date;

    @NotNull
    @Column(length = 30)
    private String displayName;

    @Column(length = 160)
    private String bio;

    private String avatar;

    private long followCount;

    //@JsonIgnore
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "userFollow",
            joinColumns = @JoinColumn(name = "userFollowing"),
            inverseJoinColumns = @JoinColumn(name = "userFollowed")
    )
    private Set<User> follow = new HashSet<>();

    private long followersCount;

    //@JsonIgnore
    @ManyToMany(mappedBy = "follow", fetch = FetchType.LAZY)
    private Set<User> followers = new HashSet<>();

    private long postsCount;
    @OneToMany(mappedBy = "userId", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Post> post = new HashSet<>();

    @ManyToMany(mappedBy = "likes")
    private Set<Post> likedPosts = new HashSet<>();

    @OneToMany(mappedBy = "userId")
    private Set<Comment> comment = new HashSet<>();

    @ManyToMany(mappedBy = "likes")
    private Set<Comment> likedComments = new HashSet<>();

}