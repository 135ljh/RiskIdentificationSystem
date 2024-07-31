package com.ljh.main.ScopeTask.ProducerConsumerModel;

import com.google.gson.Gson;
import com.ljh.main.ScopeTask.Dto.ResultDto;
import com.ljh.main.ScopeTask.Service.CreateTextTaskService;
import com.ljh.main.ScopeTask.mapper.ResultMapper;
import com.ljh.main.ScopeTask.pojo.Product;
import com.ljh.main.ScopeTask.pojo.Result;
import com.ljh.main.utils.GenerateIdUtils;
import com.ljh.main.utils.JWTUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;

public class Consumer implements Runnable {


    private final CreateTextTaskService createTextTaskService;
    private final ResultMapper resultMapper;

    public Consumer(CreateTextTaskService createTextTaskService, ResultMapper resultMapper) {
        this.createTextTaskService = createTextTaskService;
        this.resultMapper = resultMapper;
    }





  @Override
public void run() {
      String text = createTextTaskService.queue.poll();
      System.out.println("Java中动态参数已经初始化,准备传参");

      // 设置命令行传入参数
      String pythexeonpath = "\"C:\\Users\\13530\\AppData\\Local\\Microsoft\\WindowsApps\\python.exe\"";
      String pythonscriptpath = "C:\\code\\RiskIdentificationSystem\\Main\\mock.py";
      String[] args1 = new String[]{pythexeonpath, pythonscriptpath, text};

      // Java数据text传入Python
      Process pr;
      Map<String, Object> result = null;
      try {
          pr = Runtime.getRuntime().exec(args1);
          BufferedReader in = new BufferedReader(new InputStreamReader(pr.getInputStream(), "UTF-8")); // 使用UTF-8编码
          StringBuilder sb = new StringBuilder();
          String line;
          while ((line = in.readLine()) != null) {
              sb.append(line);
          }
          in.close();
          pr.waitFor();

          // 解析Python返回的JSON数据
          Gson gson = new Gson();
          result = gson.fromJson(sb.toString(), Map.class);

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
      //results.setTaskId();
      results.setCategory((String) result.get("category"));
      results.setScore((String) result.get("score"));
      results.setMessage((String) result.get("message"));


      resultMapper.addResult(results);


  }

}
