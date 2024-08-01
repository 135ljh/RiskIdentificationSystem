package com.ljh.main.ScopeTask.mapper;

import com.ljh.main.ScopeTask.pojo.Result;
import com.ljh.main.ScopeTask.pojo.Task;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface ResultMapper {

    @Select("select * from result where resultid = #{resultId} and username=#{username}")
    Result getResultById(String resultId,String username);

    @Insert("insert into result (resultid, taskid, category, score, message,username) " +
            "values (#{resultId}, #{taskId}, #{category}, #{score}, #{message},#{username})")
    void addResult(Result result);
}
