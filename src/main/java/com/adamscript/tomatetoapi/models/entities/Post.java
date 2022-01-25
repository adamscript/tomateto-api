package com.adamscript.tomatetoapi.models.entities;

import com.sun.istack.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.ArrayList;
import java.util.UUID;

@Entity
@Getter @Setter @NoArgsConstructor
@Table(name = "post_tbl")
public class Post implements Serializable {

    @Id
    private String postId;

    @OneToOne
    @NotNull
    private User userId;

    private Instant date;

    private String status;

    @OneToOne
    private Story storyId;

    @OneToOne
    private Rating ratingId;

    @OneToMany
    private ArrayList<Comment> comments = new ArrayList<>();

    @OneToMany
    private ArrayList<User> likes = new ArrayList<>();

    public Post(User userId, String status, Story storyId, Rating ratingId){
        this.postId = UUID.randomUUID().toString();
        this.userId = userId;
        this.date = Instant.now();
        this.status = status;
        this.storyId = storyId;
        this.ratingId = ratingId;
    }

}
