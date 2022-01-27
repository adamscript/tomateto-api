package com.adamscript.tomatetoapi.models.entities;

import com.sun.istack.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.List;

@Entity
@Getter @Setter @NoArgsConstructor
@Table(name = "post")
public class Post implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @OneToOne(fetch = FetchType.LAZY)
    @NotNull
    @JoinColumn(name = "userId")
    private User userId;

    private Instant date;

    private String status;

    @OneToOne
    @JoinColumn(name = "storyId")
    private Story storyId;

    @OneToOne
    @JoinColumn(name = "ratingId")
    private Rating ratingId;

    public Post(long id, User userId, String status, Story storyId, Rating ratingId, List<Comment> comment, List<User> like){
        this.id = id;
        this.userId = userId;
        this.date = Instant.now();
        this.status = status;
        this.storyId = storyId;
        this.ratingId = ratingId;
    }

}
