package com.github.script.task.bridge.service;

import com.github.script.task.bridge.model.param.JobLogParam;
import com.github.script.task.bridge.model.param.RemoveDuplicateParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class RemoveDuplicateService extends SuperService{

    public Object check(String scriptName,List<String> values){
        try {
            RemoveDuplicateParam param = new RemoveDuplicateParam();
            param.setScriptName(scriptName);
            param.setValues(values);
            return postJson("removeDuplicate/duplicateAndSave", param, Object.class);
        } catch (Exception e) {
            log.error("exception : {}", e.getMessage());
        }
        return null;
    }
}
