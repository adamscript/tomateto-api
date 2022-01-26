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
@Table(name = "account")
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

    public User(long id, String username, Instant date,String displayName, String bio, String avatar){
        this.id = id;
        this.username = username;
        this.date = date;
        this.displayName = displayName;
        this.bio = bio;
        this.avatar = avatar;
    }

}