package com.ljh.main.ScopeTask.ProducerConsumerModel;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ljh.main.ScopeTask.Dto.TaskDto;
import com.ljh.main.ScopeTask.mapper.ResultMapper;
import com.ljh.main.ScopeTask.mapper.TaskMapper;
import com.ljh.main.ScopeTask.pojo.Result;
import com.ljh.main.ScopeTask.pojo.Task;
import com.ljh.main.config.QueueConfig;
import com.ljh.main.utils.GenerateIdUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.BlockingQueue;

@Component
public class Consumer implements Runnable {



    private final ResultMapper resultMapper;
    private final BlockingQueue<TaskDto> taskQueue;
    private final TaskMapper taskMapper;



    @Autowired
    public Consumer(ResultMapper resultMapper, BlockingQueue<TaskDto> taskQueue, TaskMapper taskMapper) {

        this.resultMapper = resultMapper;

        this.taskQueue = taskQueue;
        this.taskMapper = taskMapper;
    }







  @Override
public void run() {

      while (true) {
          try {
              TaskDto task = taskQueue.take(); // 从队列中取出任务
              processTask(task); // 处理任务
          } catch (InterruptedException e) {
              Thread.currentThread().interrupt();
              break; // 中断时退出循环
          }
      }


  }

  private void processTask(TaskDto task) {

    String text = task.getContent();
    System.out.println("Java中动态参数已经初始化,准备传参");

    // 设置命令行传入参数
    String pythexeonpath = "\"C:\\Users\\13530\\AppData\\Local\\Microsoft\\WindowsApps\\python.exe\"";
    String pythonscriptpath = "C:\\code\\RiskIdentificationSystem\\Main\\mock.py";
    String[] args1 = new String[]{pythexeonpath, pythonscriptpath, "--text", text};

    // Java数据text传入Python
    Process pr;
    Map<String, Object> result = null;
    try {
        pr = Runtime.getRuntime().exec(args1);
        BufferedReader in = new BufferedReader(new InputStreamReader(pr.getInputStream(), "UTF-8"));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = in.readLine()) != null) {
            sb.append(line);
        }
        in.close();
        pr.waitFor();

        // 解析Python返回的JSON数据
        Gson gson = new Gson();
        result = gson.fromJson(sb.toString(), new TypeToken<Map<String, Object>>(){}.getType());

        // 打印解析后的数据
        System.out.println(result.get("category"));
        System.out.println(result.get("score"));
        System.out.println(result.get("message"));

    } catch (IOException | InterruptedException e) {
        e.printStackTrace();
        System.out.println(e.getMessage());
    }
    System.out.println("Java调Python结束");

    Result results = new Result();
    results.setResultId(GenerateIdUtils.generateResultID());
    results.setTaskId(task.getTaskId());


    // 安全地获取并转换结果
    results.setCategory(getStringValue(result, "category"));
    results.setScore(getStringValue(result, "score"));
    results.setMessage(getStringValue(result, "message"));

    results.setUsername(task.getUsername());

    resultMapper.addResult(results);

    taskMapper.updateTaskStatus(task.getTaskId(), "已完成", task.getUsername());

    taskMapper.addResultId(results.getResultId(), task.getTaskId(), task.getUsername());


}

private static String getStringValue(Map<String, Object> map, String key) {
    return Optional.ofNullable(map.get(key))
                   .map(Object::toString)
                   .orElse(null);
}







}
