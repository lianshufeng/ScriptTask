package com.github.script.task.server.core.service.data.impl;

import com.github.script.task.server.core.domain.DataSet;
import com.github.script.task.server.core.model.nlp.NLPModel;
import com.github.script.task.server.core.service.data.DataHandle;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;


/**
 * 初始化对象
 */
@Slf4j
@Order(Integer.MIN_VALUE)
@Service
public class InitializationDataHandle implements DataHandle {

    @Override
    public void execute(DataSet dataSet) {
        if (dataSet.getNlp() == null) {
            log.info("init : {} - {} ", dataSet.getHash(), dataSet.getContent());
            dataSet.setNlp(new NLPModel());
        }
    }
}
