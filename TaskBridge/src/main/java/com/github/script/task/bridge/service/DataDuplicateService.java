package com.github.script.task.bridge.service;

import com.github.script.task.bridge.model.param.DataDuplicateParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class DataDuplicateService extends SuperService {

    public Object check(String key, List<String> values) {
        try {
            DataDuplicateParam param = new DataDuplicateParam();
            param.setKey(key);
            param.setValues(values);
            return postJson("removeDuplicate/duplicateAndSave", param, Object.class);
        } catch (Exception e) {
            log.error("exception : {}", e.getMessage());
        }
        return null;
    }
}
