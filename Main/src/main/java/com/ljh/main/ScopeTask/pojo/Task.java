package com.ljh.main.ScopeTask.pojo;

import lombok.Data;

@Data
public class Task {
    private int id;
    private String taskId;
    private String scopeType;
    private String fileType;
    private String Content;
    private String status;
    private String resultId;
}