package com.ljh.main.ScopeTask.Controller;


import com.ljh.main.ScopeTask.Service.CreateTextTaskService;
import com.ljh.main.anno.Log;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/")
public class CreateTextTaskController {
    private final CreateTextTaskService createTextTaskService;

    @Autowired
    public CreateTextTaskController(CreateTextTaskService createTextTaskService)
    {
        this.createTextTaskService = createTextTaskService;

    }





    @Log
    @PostMapping("/task_text")
    public ResponseEntity<?> createTextTask(
            @RequestParam("scopeType") String scopeType,
            @RequestParam("fileType") String fileType,
            @RequestParam("textContent") String textContent,HttpServletRequest req, HttpServletResponse resp) {
        System.out.println("进来了1");

        return createTextTaskService.createTextTask(scopeType,fileType,textContent,req,resp);

    }















}


