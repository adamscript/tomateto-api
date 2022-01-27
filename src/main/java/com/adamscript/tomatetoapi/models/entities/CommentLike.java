package com.adamscript.tomatetoapi.models.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.Instant;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "comment_like")
public class CommentLike {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @OneToOne
    @JoinColumn(name = "userId")
    private User userId;

    @OneToOne
    @JoinColumn(name = "commentId")
    private Comment commentId;

    private Instant date;

    public CommentLike(long id, User userId, Post postId){
        this.id = id;
        this.userId = userId;
        this.commentId = commentId;
        this.date = Instant.now();

}

