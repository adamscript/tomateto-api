package com.adamscript.tomatetoapi.models.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.Instant;

@Entity
@Getter @Setter @NoArgsConstructor
@Table(name = "user_follow")
public class UserFollow {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @OneToOne
    @JoinColumn(name = "userId")
    private User userId;

    @OneToOne
    @JoinColumn(name = "followedUserId")
    private User followedUserId;

    private Instant date;

    public UserFollow(long id, User userId, User followedUserId, Instant date){
        this.id = id;
        this.userId = userId;
        this.followedUserId = followedUserId;
        this.date = date;
    }

}
