package com.ljh.main.ScopeTask.Dto;

import lombok.Data;

import java.util.Arrays;

@Data
public class Results {
    private String resultId;
    private String taskId;
    private String[] category;
    private String score;
    private String message;
    private String username;
}
