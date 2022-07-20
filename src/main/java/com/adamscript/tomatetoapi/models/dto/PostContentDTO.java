package com.adamscript.tomatetoapi.models.dto;

import com.adamscript.tomatetoapi.models.entities.User;
import lombok.Getter;

import java.time.Instant;
import java.util.HashMap;

@Getter
public class PostContentDTO {

    private long id;
    private String content;
    private String photo;
    private Instant date;
    private long likesCount;
    private long commentsCount;

    private User user;

    private Boolean isEdited;
    private Boolean isLiked;
    private Boolean isMine;

    public PostContentDTO(long id, String content, String photo, Instant date, long likesCount, long commentsCount, User user, Boolean isEdited){
        this.id = id;
        this.content = content;
        this.photo = photo;
        this.date = date;
        this.likesCount = likesCount;
        this.commentsCount = commentsCount;
        this.user = user;
        this.isEdited = isEdited;
        this.isLiked = false;
        this.isMine = false;
    }

    public HashMap<String, Object> getUser(){
        HashMap<String, Object> feedUser = new HashMap<>();
        HashMap<String, Object> avatar = new HashMap<>();

        avatar.put("default", user.getAvatarDefault());
        avatar.put("medium", user.getAvatarMedium());
        avatar.put("small", user.getAvatarSmall());
        avatar.put("extraSmall", user.getAvatarExtrasmall());

        feedUser.put("id", user.getId());
        feedUser.put("displayName", user.getDisplayName());
        feedUser.put("username", user.getUsername());
        feedUser.put("avatar", avatar);

        return feedUser;
    }

    public void setLiked(Boolean isLiked){
        this.isLiked = isLiked;
    }

    public void setMine(Boolean isMine){
        this.isMine = isMine;
    }

}
