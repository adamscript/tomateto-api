package com.adamscript.tomatetoapi.models.entities;

import com.sun.istack.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@Entity
@Getter @Setter @NoArgsConstructor
@Table(name = "story_tbl")
public class Story implements Serializable {

    @Id
    private String storyId;

    @OneToOne
    @NotNull
    private User userId;

    private Instant date;

    @OneToOne
    @NotNull
    private Post postId;

    @NotNull
    private String content;

    public Story(User userId, Post postId, String content){
        this.storyId = UUID.randomUUID().toString();
        this.userId = userId;
        this.date = Instant.now();
        this.postId = postId;
        this.content = content;
    }

}
