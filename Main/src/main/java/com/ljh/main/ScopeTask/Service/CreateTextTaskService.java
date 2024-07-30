package com.ljh.main.ScopeTask.Service;


import com.google.gson.Gson;
import com.ljh.main.Info;
import com.ljh.main.ScopeTask.Dto.TaskDto;
import com.ljh.main.ScopeTask.mapper.TaskMapper;
import com.ljh.main.ScopeTask.pojo.Task;
import com.ljh.main.utils.GenerateIdUtils;
import com.ljh.main.utils.JWTUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Service
public class CreateTextTaskService {
    private final TaskMapper taskMapper;
    private final ModelMapper modelMapper = new ModelMapper();

    public CreateTextTaskService(TaskMapper taskMapper) {
        this.taskMapper = taskMapper;
    }

    @Autowired
    private Gson gson;


    public TaskDto addTask(TaskDto task) {
        taskMapper.addTask(modelMapper.map(task, Task.class));
        return task;
    }


    public ResponseEntity<?> createTextTask(String scopeType, String fileType,
                                            String textContent, HttpServletRequest req, HttpServletResponse resp)  {
        System.out.println("进来了1");

        try {
            if ("text".equals(fileType)) {


                if (textContent == null || textContent.isEmpty()) {
                    /*Info info = new Info();
                    info.setMessage("文本内容不能为空");
                    return ResponseEntity.badRequest().body(info);*/
                    String json = gson.toJson("文本内容不能为空");
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(json);

                }

                TaskDto taskDto = new TaskDto();
                taskDto.setTaskId(GenerateIdUtils.generateTaskID());
                taskDto.setScopeType(scopeType);
                taskDto.setFileType(fileType);
                taskDto.setContent(textContent);
                taskDto.setStatus("排队中");
                //taskDto.setResultId(generateResultID());
                String username = JWTUtils.getUsername(req, resp);
                taskDto.setUsername(username);

                System.out.println(taskDto);

                addTask(taskDto);
                //System.out.println("任务提交成功");

                Map<String, Object> map = new HashMap<>();
                map.put("message", "提交成功");
                map.put("taskId", taskDto.getTaskId());
                return ResponseEntity.ok(map);

            } else {
                System.out.println(fileType);
                Info info = new Info();
                info.setMessage("暂不支持其他文件格式");
                return ResponseEntity.badRequest().body(info);
            }
        } catch (Exception e) {
            Info info = new Info();
            info.setMessage("服务器错误");
            // 其他异常处理
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(info);
        }
    }











}
