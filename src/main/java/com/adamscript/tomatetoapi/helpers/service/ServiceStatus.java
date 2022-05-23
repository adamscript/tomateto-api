package com.adamscript.tomatetoapi.helpers.service;

import lombok.Getter;

@Getter
public enum ServiceStatus {

    //General
    SUCCESS(0, "Success!"),

    //User
    USER_NOT_FOUND(100, "User not found"),
    USERNAME_ALREADY_EXIST(101, "Username already exists"),
    USER_DOES_NOT_EXIST(102, "User does not exist (Invalid User ID)"),
    USERNAME_EMPTY(103, "Username can't be empty"),
    DISPLAYNAME_EMPTY(104, "Name can't be empty"),
    FOLLOW_YOURSELF(105, "You can't follow yourself"),
    FOLLOW_DOES_NOT_EXIST(106, "You can't follow non existing user"),
    FOLLOWER_DOES_NOT_EXIST(107, "You can't follow if you does not exist"),
    FOLLOWED_ALREADY(108, "User already followed"),
    NOT_FOLLOWED(109, "User not followed"),

    //Post
    POST_NOT_FOUND(200, "Post not found"),
    POST_DOES_NOT_EXIST(201, "Post does not exist (Invalid Post ID)"),
    POST_USER_EMPTY(202, "Post's user can't be empty"),
    POST_CONTENT_EMPTY(203, "Post's content can't be empty"),
    POST_LIKED_ALREADY(204, "Post already liked"),
    POST_NOT_LIKED(205, "Post not liked"),

    //Comment
    COMMENT_NOT_FOUND(300, "Comment not found"),
    COMMENT_DOES_NOT_EXIST(301, "Comment does not exist (Invalid Comment ID)"),
    COMMENT_USER_EMPTY(302, "Comment's user can't be empty"),
    COMMENT_POST_EMPTY(303, "Comment's post can't be empty"),
    COMMENT_CONTENT_EMPTY(304, "Comment's content can't be empty"),
    COMMENT_LIKED_ALREADY(305, "Comment already liked"),
    COMMENT_NOT_LIKED(306, "Comment not liked"),

    //Unknown
    ERROR(400, "An unknown error occurred");

    private final int code;
    private final String message;

    private ServiceStatus(int code, String message){
        this.code = code;
        this.message = message;
    }
}
