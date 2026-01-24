package com.amrat.ECommerceApp.exceptions;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ExceptionMessage {
    private LocalDateTime timeStamp;
    private String error;
    private int statusCode;

    public ExceptionMessage(){
        this.timeStamp = LocalDateTime.now();
    }
    public ExceptionMessage(String error, int statusCode){
        this();
        this.error = error;
        this.statusCode = statusCode;
    }
}
