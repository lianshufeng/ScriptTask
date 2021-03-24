package com.github.script.task.bridge.service;

import com.github.script.task.bridge.model.param.SearchMatchWordParam;
import com.github.script.task.bridge.result.ResultContent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class MatchWordService extends SuperService {

    public ResultContent findByCollectionName(SearchMatchWordParam param){
        try {
            return postJson("matchWord/findByCollectionName", param, ResultContent.class);
        } catch (Exception e) {
            log.error("exception : {}", e.getMessage());
        }
        return null;
    }
}
