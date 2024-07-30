package com.ljh.main.ScopeTask.mapper;


import com.ljh.main.ScopeTask.pojo.Task;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.session.RowBounds;

import java.util.List;

@Mapper
public interface TaskMapper {

    @Insert("insert into task (taskid, scopetype, filetype, content, status, resultid,username) " +
            "values (#{taskId}, #{scopeType}, #{fileType}, #{content}, #{status}, #{resultId},#{username})")
    void addTask(Task task);

    @Select("select * from task where taskid = #{taskId} and username=#{username}")
    Task getTaskById(String taskId, String username);

    @Select("select * from task where username=#{username}")
    List<Task> getAllTasks(String username, RowBounds rowBounds);




}
