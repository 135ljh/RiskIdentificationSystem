package com.ljh.main.ScopeTask.Service;


import com.ljh.main.Info;
import com.ljh.main.ScopeTask.Dto.Results;
import com.ljh.main.ScopeTask.mapper.ResultMapper;
import com.ljh.main.ScopeTask.pojo.Result;
import com.ljh.main.utils.JWTUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Service
public class GetResultByIdService {

    private final ResultMapper resultMapper;

    public GetResultByIdService(ResultMapper resultMapper) {
        this.resultMapper = resultMapper;
    }


    public ResponseEntity<?> getResult(String resultId, HttpServletRequest req, HttpServletResponse resp) {
        String username = JWTUtils.getUsername(req, resp);
        Result result = resultMapper.getResultById(resultId,username);
        if (result == null) {
            Info info = new Info();
            info.setMessage("记录不存在");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(info);


        }

        Results results=new Results();
        results.setResultId(resultId);
        results.setTaskId(result.getTaskId());
        results.setCategory(new String[]{result.getCategory()});
        results.setScore(result.getScore());
        results.setMessage(result.getMessage());
        results.setUsername(result.getUsername());

        //记录存在，返回所有信息
        return ResponseEntity.ok(results);

    }








}
