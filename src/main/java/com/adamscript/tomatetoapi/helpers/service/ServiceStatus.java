package com.adamscript.tomatetoapi.helpers.service;

import lombok.Getter;

@Getter
public enum ServiceStatus {

    SUCCESS(0, "Success!"),
    USER_NOT_FOUND(100, "User not found"),
    USERNAME_ALREADY_EXISTS(101, "Username already exists"),
    USER_DOES_NOT_EXISTS(102, "User does not exist (Invalid User ID)"),
    USERNAME_EMPTY(103, "Username can't be empty"),
    DISPLAYNAME_EMPTY(104, "Name can't be empty"),
    FOLLOW_YOURSELF(105, "You can't follow yourself"),
    FOLLOW_DOES_NOT_EXIST(106, "You can't follow non existing user"),
    FOLLOWER_DOES_NOT_EXIST(107, "You can't follow if you does not exist"),
    FOLLOWED_ALREADY(108, "User already followed"),
    NOT_FOLLOWED(109, "User not followed"),
    ERROR(400, "An unknown error occurred");

    private final int code;
    private final String message;

    private ServiceStatus(int code, String message){
        this.code = code;
        this.message = message;
    }
}
