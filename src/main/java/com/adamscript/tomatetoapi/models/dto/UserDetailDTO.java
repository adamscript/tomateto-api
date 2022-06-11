package com.adamscript.tomatetoapi.models.dto;

import lombok.Getter;

@Getter
public class UserDetailDTO {

    private String id;
    private String displayName;
    private String username;
    private String bio;
    private String avatar;

    private Long postsCount;
    private Long followCount;
    private Long followersCount;

    private Boolean isFollowed;
    private Boolean isMine;

    public UserDetailDTO(String id, String displayName, String username, String bio, String avatar, Long postsCount, Long followCount, Long followersCount){
        this.id = id;
        this.displayName = displayName;
        this.username = username;
        this.bio = bio;
        this.avatar = avatar;
        this.postsCount = postsCount;
        this.followCount = followCount;
        this.followersCount = followersCount;
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
