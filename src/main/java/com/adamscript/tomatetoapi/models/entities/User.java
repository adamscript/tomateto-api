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
@Table(name = "user_tbl")
public class User implements Serializable {

    @Id
    private String userId;

    @NotNull
    @Column(length = 17)
    private String username;

    private Instant date;

    @NotNull
    @Column(length = 30)
    private String displayName;

    @Column(length = 160)
    private String bio;

    private String avatar;

    public User(String username, String displayName, String bio, String avatar){
        this.userId = UUID.randomUUID().toString();
        this.username = username;
        this.date = Instant.now();
        this.displayName = displayName;
        this.bio = bio;
        this.avatar = avatar;
    }

}