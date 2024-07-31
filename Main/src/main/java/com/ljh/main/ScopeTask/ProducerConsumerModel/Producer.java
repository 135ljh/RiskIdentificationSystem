package com.ljh.main.ScopeTask.ProducerConsumerModel;

import com.ljh.main.ScopeTask.pojo.Product;
import com.ljh.main.ScopeTask.pojo.Result;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.concurrent.BlockingQueue;

public class Producer implements Runnable {


    private final BlockingQueue<Product> queue;



    public Producer( BlockingQueue<Product> queue) {

        this.queue = queue;
    }

    @Override
    public void run() {





    }
}


/*
@Override
public void run() {
    //调用python脚本，获取数据
    // 设置命令行传入参数
    String pythexeonpath = "C:\\Users\\13530\\AppData\\Local\\Microsoft\\WindowsApps\\python.exe";
    String pythonscriptpath = "C:\\code\\RiskIdentificationSystem\\mock.py";
    String[] args1 = new String[]{pythexeonpath, pythonscriptpath, textContent};
    //Java数据textContent传入Python
    Process pr;
    try {
        pr = Runtime.getRuntime().exec(args1);
        BufferedReader in = new BufferedReader(new InputStreamReader(pr.getInputStream(), "GBK"));
        String line;
        while ((line = in.readLine()) != null) {
            System.out.println(line);
            */
/*lines.add(line); //把Python的print值保存了下来*//*

            buffer.add(line);
        }
        in.close();
        pr.waitFor();
    } catch (Exception e) {
        e.printStackTrace();
        System.out.println(e.getMessage());
    }
    System.out.println("Java调Python结束");


}*/
