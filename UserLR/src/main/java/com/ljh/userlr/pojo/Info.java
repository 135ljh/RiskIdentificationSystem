package com.ljh.userlr.pojo;

import lombok.Data;

@Data
public class Info {

    private String access_token;
    private String token_type;
    private String message;
    private int statusCode;
}
