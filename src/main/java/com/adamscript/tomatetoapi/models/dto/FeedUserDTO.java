package com.adamscript.tomatetoapi.models.dto;

import lombok.Getter;

@Getter
public class FeedUserDTO {

    private String id;
    private String displayName;
    private String username;
    private String bio;
    private String avatar;

    private Boolean isFollowed;
    private Boolean isMine;

    public FeedUserDTO(String id, String displayName, String username, String bio, String avatar){
        this.id = id;
        this.displayName = displayName;
        this.username = username;
        this.bio = bio;
        this.avatar = avatar;
        this.isFollowed = false;
        this.isMine = false;
    }

    public void setIsFollowed(Boolean isFollowed){
        this.isFollowed = isFollowed;
    }

    public void setMine(Boolean isMine){
        this.isMine = isMine;
    }

}
