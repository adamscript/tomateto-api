package com.adamscript.tomatetoapi.models.entities;

import com.adamscript.tomatetoapi.helpers.bridge.UserBridge;
import com.fasterxml.jackson.annotation.*;
import com.sun.istack.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.search.annotations.*;
import org.hibernate.search.annotations.Index;

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
@Indexed
public class Post implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Field(name = "postId", index = Index.NO, store = Store.YES)
    private long id;

    @ManyToOne
    @NotNull
    @JoinColumn(name = "users", referencedColumnName = "id")
    @Field(index = Index.NO, store = Store.YES)
    @FieldBridge(impl = UserBridge.class)
    private User user;

    @Field(index = Index.NO, store = Store.YES)
    private Instant date = Instant.now();

    @NotNull
    @Field(store = Store.YES)
    @Column(columnDefinition="text")
    private String content;

    @Field(index = Index.NO, store = Store.YES)
    private String photo;

    @Field(index = Index.NO, store = Store.YES)
    private boolean isEdited;

    @Field(index = Index.NO, store = Store.YES)
    private long likesCount;

    @ManyToMany
    @JoinTable(
            name = "postLikes",
            joinColumns = @JoinColumn(name = "post"),
            inverseJoinColumns = @JoinColumn(name = "users")
    )
    private Set<User> likes = new HashSet<>();

    @Field(index = Index.NO, store = Store.YES)
    private long commentsCount;

    @OneToMany(mappedBy = "post")
    private Set<Comment> comments = new HashSet<>();

}
