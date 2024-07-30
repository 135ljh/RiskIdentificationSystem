package com.ljh.main.ScopeTask.Service;


import com.ljh.main.ScopeTask.Dto.ResultDto;
import com.ljh.main.ScopeTask.Dto.TaskDto;
import com.ljh.main.ScopeTask.mapper.ResultMapper;
import com.ljh.main.ScopeTask.mapper.TaskMapper;
import com.ljh.main.ScopeTask.pojo.Result;
import com.ljh.main.ScopeTask.pojo.Task;
import com.ljh.main.utils.GenerateIdUtils;
import com.ljh.main.utils.JWTUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.modelmapper.ModelMapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;



public class Identify {

    public void identify(ResultMapper resultMapper,String textContent,String taskId, HttpServletRequest req, HttpServletResponse resp) {
        Buffer buffer=new Buffer(3);

        Producer producer=new Producer(buffer, textContent);
        Consumer consumer=new Consumer(buffer, resultMapper, req, resp, taskId);


        producer.start();
        consumer.start();


    }
}



/**
 * Buffer类用于实现一个固定大小的缓冲区。
 */
class Buffer {

    private final List<String> lines = new ArrayList<>();
    private final int maxSize;

    public Buffer(int maxSize) {
        this.maxSize = maxSize;
    }

    /**
     * 向缓冲区中添加一个数据项。
     * 如果缓冲区已满，则当前线程进入等待状态，直到缓冲区有空位。
     *
     * @param val 要添加到缓冲区的值。
     * @throws InterruptedException 如果线程在等待时被中断。
     */
    public synchronized void add(String val) throws InterruptedException {
        while (lines.size() >= maxSize) {
            wait(); // 等待直到有空间可用
        }
        lines.add(val);
        notifyAll(); // 唤醒所有等待的线程
    }

    /**
     * 从缓冲区中提取一个数据项。
     * 如果缓冲区为空，则当前线程进入等待状态，直到有数据可用。
     *
     * @return 从缓冲区中提取的数据项。
     * @throws InterruptedException 如果线程在等待时被中断。
     */
    public synchronized String pull() throws InterruptedException {
        while (lines.isEmpty()) {
            wait(); // 等待直到有数据可用
        }
        String val = lines.remove(0); // 移除并返回第一个元素
        notifyAll(); // 唤醒所有等待的线程
        return val;
    }
}


/**
 * 生产者类，继承自Thread，负责向缓冲区添加数据。
 * 该类的实例将作为一个线程运行，不断地向缓冲区添加数据。
 */
class Producer extends Thread {
    /**
     * 缓冲区对象，用于存储生产者产生的数据。
     */
    private Buffer buffer;
    private String textContent;


    /**
     * 构造函数，初始化生产者的缓冲区。
     *
     * @param buffer 缓冲区实例，生产者将向这个缓冲区添加数据。
     */
    public Producer(Buffer buffer, String textContent) {
        this.buffer = buffer;
        this.textContent = textContent;
    }

    /**
     * 重写run方法，定义生产者的线程执行逻辑。
     */
    @Override
    public void run() {
        //调用python脚本，获取数据
        // 设置命令行传入参数
        String pythexeonpath= "C:\\Users\\13530\\AppData\\Local\\Microsoft\\WindowsApps\\python.exe";
        String pythonscriptpath= "C:\\code\\RiskIdentificationSystem\\mock.py";
        String[] args1 = new String[] {pythexeonpath ,pythonscriptpath, textContent};
        //Java数据textContent传入Python
        Process pr;
        try {
            pr = Runtime.getRuntime().exec(args1);
            BufferedReader in = new BufferedReader(new InputStreamReader(pr.getInputStream(),"GBK"));
            String line;
            while ((line = in.readLine()) != null) {
                System.out.println(line);
                /*lines.add(line); //把Python的print值保存了下来*/
                buffer.add(line);
            }
            in.close();
            pr.waitFor();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
        System.out.println("Java调Python结束");


    }
}

/**
 * 消费者类，继承自Thread，负责从缓冲区获取数据并消费。
 */
class Consumer extends Thread {
    private final HttpServletRequest req;
    private final HttpServletResponse resp;
    private final String taskId;
    /**
     * 缓冲区对象，用于存储和提取数据。
     */
    private Buffer buffer;

    /**
     * 构造函数，初始化消费者类的缓冲区对象。
     *
     * @param buffer 缓冲区对象，用于数据消费。
     */
    public Consumer(Buffer buffer, ResultMapper resultMapper, HttpServletRequest req, HttpServletResponse resp,String taskId) {
        this.buffer = buffer;
        this.resultMapper = resultMapper;
        this.req = req;
        this.resp = resp;
        this.taskId = taskId;
    }

    /**
     * 重写run方法，定义消费者线程的执行逻辑。
     * 主要负责从缓冲区获取数据并打印，重复执行10次。
     */

    private final ResultMapper resultMapper;
    private final ModelMapper modelMapper = new ModelMapper();


    public ResultDto addResult(ResultDto result) {
        resultMapper.addResult(modelMapper.map(result, Result.class));
        return result;
    }
    @Override
    public void run() {
        ResultDto resultDto = new ResultDto();

        // 循环直至缓冲区为空或满足特定条件
        while (true) {
            try {
                String line = buffer.pull();
                if (line == null || line.isEmpty()) {
                    break; // 如果从缓冲区拉取的数据为空或无效，退出循环
                }
                System.out.println("Consumer: " + line);

                String[] split = line.split(",");
                if (split.length < 3) {
                    // 数据格式不正确，跳过这条数据
                    continue;
                }

                resultDto.setResultId(GenerateIdUtils.generateResultID());
                resultDto.setTaskId(taskId);

                // 假设数据格式为：category,score,message
                resultDto.setCategory(split[0]);
                resultDto.setScore(split[1]);
                resultDto.setMessage(split[2]);

                String username = JWTUtils.getUsername(req, resp);
                resultDto.setUsername(username);

                addResult(resultDto);

                // 清空或重置 ResultDto 对象，准备处理下一条数据
                resultDto = new ResultDto();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt(); // 保留中断状态
                System.err.println("Consumer thread interrupted.");
                break; // 中断时退出循环
            }
        }

    }
}










