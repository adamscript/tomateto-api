package com.adamscript.tomatetoapi.models.entities;

import com.sun.istack.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.time.Instant;

@Entity
@Getter @Setter @NoArgsConstructor
@Table(name = "user")
public class User implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

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

    public User(long id, String username, String displayName, String bio, String avatar){
        this.id = id;
        this.username = username;
        this.date = Instant.now();
        this.displayName = displayName;
        this.bio = bio;
        this.avatar = avatar;
    }

}