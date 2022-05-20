package com.adamscript.tomatetoapi.helpers.handler;

import com.adamscript.tomatetoapi.helpers.service.ServiceStatus;
import lombok.Getter;

@Getter
public class Response<T> {
    private String message;

    private int code;

    private T items;

    public Response(T object, ServiceStatus status){
        this.message = status.getMessage();
        this.code = status.getCode();
        this.items = object;
    }

}
