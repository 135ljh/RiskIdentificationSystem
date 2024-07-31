package com.ljh.main.ScopeTask.ProducerConsumerModel;

import com.ljh.main.ScopeTask.Dto.ResultDto;
import com.ljh.main.ScopeTask.mapper.ResultMapper;
import com.ljh.main.ScopeTask.pojo.Product;
import com.ljh.main.ScopeTask.pojo.Result;
import com.ljh.main.utils.GenerateIdUtils;
import com.ljh.main.utils.JWTUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.modelmapper.ModelMapper;

import java.util.concurrent.BlockingQueue;

public class Consumer implements Runnable {
    private final BlockingQueue<Product> queue;

    public Consumer(BlockingQueue<Product> queue) {
        this.queue = queue;
    }

    @Override
    public void run() {

    }
}
