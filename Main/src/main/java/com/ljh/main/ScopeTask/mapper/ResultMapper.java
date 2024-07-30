package com.ljh.main.ScopeTask.mapper;

import com.ljh.main.ScopeTask.pojo.Result;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface ResultMapper {

    @Select("select * from result where resultid = #{resultId} and username=#{username}")
    Result getResultById(String resultId,String username);
}
