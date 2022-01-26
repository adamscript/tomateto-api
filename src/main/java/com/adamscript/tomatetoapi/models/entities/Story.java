package com.adamscript.tomatetoapi.models.entities;

import com.sun.istack.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@Entity
@Getter @Setter @NoArgsConstructor
@Table(name = "story")
public class Story implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @OneToOne
    @NotNull
    @JoinColumn(name = "userId")
    private User userId;

    private Instant date;

    @OneToOne
    @NotNull
    @JoinColumn(name = "postId")
    private Post postId;

    @NotNull
    private String content;

    public Story(long id, User userId, Post postId, String content){
        this.id = id;
        this.userId = userId;
        this.date = Instant.now();
        this.postId = postId;
        this.content = content;
    }

}
