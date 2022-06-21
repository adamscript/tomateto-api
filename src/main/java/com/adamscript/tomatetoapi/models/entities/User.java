package com.adamscript.tomatetoapi.models.entities;

import com.fasterxml.jackson.annotation.*;
import com.sun.istack.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.search.annotations.ContainedIn;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.Store;

import javax.persistence.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter @Setter @NoArgsConstructor
@JsonIdentityInfo(
        scope = User.class,
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id"
)
@Table(name = "users")
@Indexed
public class User implements Serializable {

    @Id
    @NotNull
    private String id;

    @NotNull
    @Column(length = 17)
    @Field(store = Store.YES)
    private String username;

    private Instant date;

    @NotNull
    @Column(length = 30)
    @Field(store = Store.YES)
    private String displayName;

    @Column(length = 160)
    @Field(store = Store.YES)
    private String bio;

    private String avatar;

    private long followCount;

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

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Post> post = new HashSet<>();

    @ManyToMany(mappedBy = "likes")
    private Set<Post> likedPosts = new HashSet<>();

    @OneToMany(mappedBy = "user")
    private Set<Comment> comment = new HashSet<>();

    @ManyToMany(mappedBy = "likes")
    private Set<Comment> likedComments = new HashSet<>();

}