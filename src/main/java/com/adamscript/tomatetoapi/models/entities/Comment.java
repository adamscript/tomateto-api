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

    @ManyToOne
    @NotNull
    @JoinColumn(name = "userId", referencedColumnName = "id")
    private User userId;

    private Instant date;

    @ManyToOne
    @NotNull
    @JoinColumn(name = "postId", referencedColumnName = "id")
    private Post postId;

    @NotNull
    private String content;

    private long likesCount;

}
