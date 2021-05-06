package com.github.script.task.server.core.service.data.impl;

import com.github.script.task.server.core.conf.NLPConf;
import com.github.script.task.server.core.domain.DataSet;
import com.github.script.task.server.core.model.nlp.NLPModel;
import com.github.script.task.server.core.service.NLPService;
import com.github.script.task.server.core.service.data.DataHandle;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

@Slf4j
@Order(100)
@Service
public class SummaryDataHandle implements DataHandle {

    @Autowired
    private NLPService nlpService;

    @Autowired
    private NLPConf nlpConf;


    @Override
    public void execute(DataSet dataSet) {
        log.debug("summary : {}", dataSet.getHash());
        final String text = dataSet.getContent();
        NLPModel model = dataSet.getNlp();
        //标题
        model.setTitle(nlpService.summary(text, nlpConf.getTitleLength()));
        //摘要
        model.setSummary(nlpService.summary(text, nlpConf.getSummaryLength()));
    }
}
