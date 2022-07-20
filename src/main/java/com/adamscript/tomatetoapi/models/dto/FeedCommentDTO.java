package com.adamscript.tomatetoapi.models.dto;

import com.adamscript.tomatetoapi.models.entities.Post;
import com.adamscript.tomatetoapi.models.entities.User;
import lombok.Getter;

import java.time.Instant;
import java.util.HashMap;

@Getter
public class FeedCommentDTO {

    private long id;
    private String content;
    private Instant date;
    private long likesCount;

    private User user;
    private Post post;

    private Boolean isLiked;
    private Boolean isMine;

    public FeedCommentDTO(long id, String content, Instant date, long likesCount, User user, Post post){
        this.id = id;
        this.content = content;
        this.date = date;
        this.likesCount = likesCount;
        this.user = user;
        this.post = post;
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

    public long getPost(){
        return post.getId();
    }

    public void setLiked(Boolean isLiked){
        this.isLiked = isLiked;
    }

    public void setMine(Boolean isMine){
        this.isMine = isMine;
    }

}
