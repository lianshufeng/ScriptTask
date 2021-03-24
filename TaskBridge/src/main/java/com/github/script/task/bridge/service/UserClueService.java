package com.github.script.task.bridge.service;

import com.github.script.task.bridge.model.param.UserClueParam;
import com.github.script.task.bridge.result.ResultContent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UserClueService extends SuperService {

    public ResultContent save(UserClueParam param) {
        try {
            return postJson("userClue/save", param, ResultContent.class);
        } catch (Exception e) {
            log.error("exception : {}", e.getMessage());
        }
        return null;
    }

}
