package com.adamscript.tomatetoapi.models.dto;

import com.adamscript.tomatetoapi.models.entities.User;
import lombok.Getter;

import java.time.Instant;
import java.util.HashMap;

@Getter
public class PostContentDTO {

    private long id;
    private String content;
    private Instant date;
    private long likesCount;
    private long commentsCount;

    private User user;

    private Boolean isLiked;
    private Boolean isMine;

    public PostContentDTO(long id, String content, Instant date, long likesCount, long commentsCount, User user){
        this.id = id;
        this.content = content;
        this.date = date;
        this.likesCount = likesCount;
        this.commentsCount = commentsCount;
        this.user = user;
        this.isLiked = false;
        this.isMine = false;
    }

    public HashMap<String, Object> getUser(){
        HashMap<String, Object> feedUser = new HashMap<>();

        feedUser.put("id", user.getId());
        feedUser.put("displayName", user.getDisplayName());
        feedUser.put("username", user.getUsername());
        feedUser.put("avatar", user.getAvatar());

        return feedUser;
    }

    public void setLiked(Boolean isLiked){
        this.isLiked = isLiked;
    }

    public void setMine(Boolean isMine){
        this.isMine = isMine;
    }

}
