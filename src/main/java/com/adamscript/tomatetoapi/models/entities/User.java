package com.adamscript.tomatetoapi.models.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sun.istack.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter @Setter @NoArgsConstructor
@Table(name = "users")
public class User implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotNull
    @Column(length = 17)
    private String username;

    private Instant dateCreated;

    @NotNull
    @Column(length = 30)
    private String displayName;

    @Column(length = 160)
    private String bio;

    private String avatar;

    private long followersCount;

    private long followCount;

    private long postsCount;

}