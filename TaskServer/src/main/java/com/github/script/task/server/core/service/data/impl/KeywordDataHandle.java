package com.github.script.task.server.core.service.data.impl;

import com.github.script.task.server.core.domain.DataSet;
import com.github.script.task.server.core.model.nlp.NLPModel;
import com.github.script.task.server.core.service.NLPService;
import com.github.script.task.server.core.service.data.DataHandle;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

/**
 * 评论标签抽取
 */
@Slf4j
@Order(200)
@Service
public class KeywordDataHandle implements DataHandle {

    @Autowired
    private NLPService nlpService;


    @Override
    public void execute(DataSet dataSet) {
        log.debug("keyword : {}", dataSet.getHash());
        final String text = dataSet.getContent();
        NLPModel model = dataSet.getNlp();
        model.setKeyword(nlpService.keyword(text, dataSet.getNlp().getTitle()));
    }
}
