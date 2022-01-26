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
import java.util.UUID;

@Entity
@Getter @Setter @NoArgsConstructor
@Table(name = "comment_tbl")
public class Comment implements Serializable{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private UUID commentId;

    @OneToOne
    @NotNull
    private User userId;

    private Instant date;

    @NotNull
    private String content;

    public Comment(long id, User userId, Date date, String content){
        this.id = id;
        this.commentId = UUID.randomUUID();
        this.userId = userId;
        this.date = Instant.now();
        this.content = content;
    }

}
