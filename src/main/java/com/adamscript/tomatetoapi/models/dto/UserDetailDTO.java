package com.adamscript.tomatetoapi.models.dto;

import lombok.Getter;

import java.util.HashMap;

@Getter
public class UserDetailDTO {

    private String id;
    private String displayName;
    private String username;
    private String bio;

    private String avatarDefault;
    private String avatarMedium;
    private String avatarSmall;
    private String avatarExtrasmall;

    private Long postsCount;
    private Long followCount;
    private Long followersCount;

    private Boolean isFollowed;
    private Boolean isMine;

    public UserDetailDTO(String id, String displayName, String username, String bio, String avatarDefault, String avatarMedium, String avatarSmall, String avatarExtrasmall, Long postsCount, Long followCount, Long followersCount){
        this.id = id;
        this.displayName = displayName;
        this.username = username;
        this.bio = bio;

        this.avatarDefault = avatarDefault;
        this.avatarMedium = avatarMedium;
        this.avatarSmall = avatarSmall;
        this.avatarExtrasmall = avatarExtrasmall;

        this.postsCount = postsCount;
        this.followCount = followCount;
        this.followersCount = followersCount;
        this.isFollowed = false;
        this.isMine = false;
    }

    public HashMap<String, Object> getAvatar(){
        HashMap<String, Object> avatar = new HashMap<>();

        avatar.put("default", avatarDefault);
        avatar.put("medium", avatarMedium);
        avatar.put("small", avatarSmall);
        avatar.put("extraSmall", avatarExtrasmall);

        return avatar;
    }

    public void setIsFollowed(Boolean isFollowed){
        this.isFollowed = isFollowed;
    }

    public void setMine(Boolean isMine){
        this.isMine = isMine;
    }

}
