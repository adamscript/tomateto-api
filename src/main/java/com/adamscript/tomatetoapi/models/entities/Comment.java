package com.adamscript.tomatetoapi.models.entities;

import com.sun.istack.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Entity
@Getter @Setter @NoArgsConstructor
@Table(name = "comment")
public class Comment implements Serializable{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private UUID commentId;

    @OneToOne(fetch = FetchType.LAZY)
    @NotNull
    @JoinColumn(name = "userId")
    private User userId;

    private Instant date;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "postId")
    private Post postId;

    @NotNull
    private String content;

    @OneToMany(fetch = FetchType.LAZY)
    private List<User> likes;

    public Comment(long id, User userId, Date date, String content){
        this.id = id;
        this.userId = userId;
        this.date = Instant.now();
        this.content = content;
    }

}
