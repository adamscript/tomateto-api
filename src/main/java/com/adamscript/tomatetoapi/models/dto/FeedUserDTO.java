package com.adamscript.tomatetoapi.models.dto;

import lombok.Getter;

import java.util.HashMap;
import java.util.List;

@Getter
public class FeedUserDTO {

    private String id;
    private String displayName;
    private String username;
    private String bio;

    private HashMap<String, Object> avatar = new HashMap<>();

    private Boolean isFollowed;
    private Boolean isMine;

    public FeedUserDTO(String id, String displayName, String username, String bio, String avatarDefault, String avatarMedium, String avatarSmall, String avatarExtrasmall){
        this.id = id;
        this.displayName = displayName;
        this.username = username;
        this.bio = bio;

        avatar.put("default", avatarDefault);
        avatar.put("medium", avatarMedium);
        avatar.put("small", avatarSmall);
        avatar.put("extraSmall", avatarExtrasmall);

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
