package com.adamscript.tomatetoapi.models.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.Instant;

@Entity
@Getter @Setter @NoArgsConstructor
@Table(name = "post_like")
public class PostLike {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @OneToOne
    @JoinColumn(name = "userId")
    private User userId;

    @OneToOne
    @JoinColumn(name = "postId")
    private Post postId;

    private Instant date;

    public PostLike(long id, User userId, Post postId){
        this.id = id;
        this.userId = userId;
        this.postId = postId;
        this.date = Instant.now();
    }

}
