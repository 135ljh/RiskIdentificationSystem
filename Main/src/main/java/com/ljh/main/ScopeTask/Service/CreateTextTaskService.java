package com.ljh.main.ScopeTask.Service;


import com.google.gson.Gson;
import com.ljh.main.Info;
import com.ljh.main.ScopeTask.Dto.TaskDto;
import com.ljh.main.ScopeTask.mapper.ResultMapper;
import com.ljh.main.ScopeTask.mapper.TaskMapper;
import com.ljh.main.ScopeTask.pojo.Product;
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

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;

@Service
public class CreateTextTaskService implements Runnable{

    @Autowired
    private Gson gson;

    private final TaskMapper taskMapper;

    private final ModelMapper modelMapper = new ModelMapper();
    public TaskDto addTask(TaskDto task) {
        taskMapper.addTask(modelMapper.map(task, Task.class));
        return task;
    }


    //添加阻塞队列
    public final BlockingQueue<String> queue;

    public CreateTextTaskService(TaskMapper taskMapper,  BlockingQueue<String> queue) {
        this.taskMapper = taskMapper;
        this.queue = queue;
    }




    public ResponseEntity<?> createTextTask(String scopeType, String fileType,
                                            String textContent, HttpServletRequest req, HttpServletResponse resp){
            System.out.println("进来了1");

            try {
                if ("text".equals(fileType)) {


                    if (textContent == null || textContent.isEmpty()) {
                        String json = gson.toJson("文本内容不能为空");
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(json);

                    }

                    TaskDto taskDto = new TaskDto();
                    taskDto.setTaskId(GenerateIdUtils.generateTaskID());
                    taskDto.setScopeType(scopeType);
                    taskDto.setFileType(fileType);
                    taskDto.setContent(textContent);
                    taskDto.setStatus("排队中");
                    String username = JWTUtils.getUsername(req, resp);
                    taskDto.setUsername(username);

                    System.out.println(taskDto);

                    addTask(taskDto);


                    //生产者，产生的数据textContent放入阻塞队列中
                    queue.put(textContent);

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


    @Override
    public void run() {

    }


    }












