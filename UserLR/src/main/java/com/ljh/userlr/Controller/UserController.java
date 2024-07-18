package com.ljh.userlr.Controller;

import com.ljh.userlr.Dto.UserDto;
import com.ljh.userlr.Services.UserService;
import com.ljh.userlr.mapper.UserMapper;
import com.ljh.userlr.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
public class UserController {
    private final UserService userService;


    @Autowired
    private  UserMapper userMapper;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }


    @PostMapping("/user/register")
    public  ResponseEntity<String> Register(@RequestBody UserDto user) {

            // 检查用户名或密码是否为空 状态码400
            if (user.getUsername() == null || user.getUsername().trim().isEmpty() ||
                    user.getPassword() == null || user.getPassword().trim().isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("用户名或密码为空");
            }

            // 检查用户名是否已存在 状态码400
            User existingUser = userMapper.selectByUsername(user.getUsername());
            if (existingUser != null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("用户名重复，请使用其他用户名");

            }

            // 注册成功 状态码200
            userService.addUser(user);
            return ResponseEntity.status(HttpStatus.OK).body("注册成功");
            // return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal Server Error");

    }

    @PostMapping("/user/login")
    public ResponseEntity<Map> Login(
            @RequestParam("username") String username,
            @RequestParam("password") String password) {

        // 检查用户名或密码是否为空 状态码400
        if (username == null || username.trim().isEmpty() ||
                password == null || password.trim().isEmpty()) {
            Map<String, Object> map = new HashMap<>();
            map.put("message", "用户名或密码为空");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(map);
        }

        // 用户名或密码错误 状态码400
        User existingUser = userMapper.select(username,password);
        if (existingUser ==null) {
            Map<String, Object> map = new HashMap<>();
            map.put("message", "用户名或密码错误");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(map);
        }else{
            // 登录成功 状态码200
            //生成token令牌
            String access_token=UUID.randomUUID()+""+username;
            //存到map(后续看能不能存到redis数据库)
            Map<String,Object> map=new HashMap<>();
            map.put("access_token",access_token);
            map.put("token_type","bearer");
            map.put("message","登录成功");

            return ResponseEntity.status(HttpStatus.OK).body(map);


        }


        }





}
