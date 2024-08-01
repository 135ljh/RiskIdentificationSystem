package com.ljh.main;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.*;
import java.util.*;


//@SpringBootTest
class MainApplicationTests {

    @Test
    public void testUuid () {
        for(int i = 0; i < 10; i++){
            String uuid= UUID.randomUUID().toString();
            System.out.println(uuid);
        }
    }

    @Test
    public void testJWT() {
        Map<String, Object> claims=new HashMap<>();
        claims.put("id",1);
        claims.put("username","tom");
        //byte[] key = Keys.secretKeyFor(SignatureAlgorithm.HS256).getEncoded();
        String key="sssssssseeeeeeeeccccccccrrrrrrrreeeeeeeetttttttt";

        String jwt=Jwts.builder()
                .signWith(SignatureAlgorithm.HS256, key)//签名算法
                .setClaims(claims) //自定义内容（载荷）
                .setExpiration(new Date(System.currentTimeMillis()+1000*3600))//设置有效期为1h
                .compact();
        System.out.println(jwt);
        System.out.println(key);

        /*解析jwt*/
        System.out.println(Jwts.parser().setSigningKey(key).parseClaimsJws(jwt).getBody());

        Map<String, Object> claims2 = Jwts.parser()
                .setSigningKey("sssssssseeeeeeeeccccccccrrrrrrrreeeeeeeetttttttt")
                .parseClaimsJws(jwt)
                .getBody();
        System.out.println(claims2);
    }


    @Test
    public void testPython() {
        // 需传入的参数
        String textContent = "djfdvdfgfdgfd";
        System.out.println("Java中动态参数已经初始化,准备传参");

        // 设置命令行传入参数
        String pythexeonpath= "python3";
        String pythonscriptpath= "mock.py";
        String[] args1 = new String[] {pythexeonpath ,pythonscriptpath,"--text",textContent};

        // Java数据text传入Python
        Process pr;
        try {
            pr = Runtime.getRuntime().exec(args1);
            System.out.println("pr:"+pr);

            BufferedReader in = new BufferedReader(new InputStreamReader(pr.getInputStream(), "UTF-8")); // 使用UTF-8编码
            System.out.println("in:"+in);
            StringBuilder sb = new StringBuilder();
            System.out.println("sb:"+sb);
            String line;

            while ((line = in.readLine()) != null) {
                sb.append(line);
                System.out.println("line:"+line);
            }
            System.out.println("line:"+line);
            System.out.println("sb值："+sb);
            in.close();
            pr.waitFor();

            // 解析Python返回的JSON数据
            Gson gson = new Gson();

            Map<String, Object> result = gson.fromJson(sb.toString(), Map.class);
            System.out.println(result);

            // 打印解析后的数据
            System.out.println(result.get("category"));
            System.out.println(result.get("score"));
            System.out.println(result.get("message"));

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
        System.out.println("Java调Python结束");
    }

}