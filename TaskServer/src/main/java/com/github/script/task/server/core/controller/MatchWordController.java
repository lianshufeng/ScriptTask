package com.github.script.task.server.core.controller;

import com.github.script.task.bridge.model.param.MatchWordParam;
import com.github.script.task.bridge.model.param.SearchMatchWordParam;
import com.github.script.task.server.core.domain.MatchWord;
import com.github.script.task.server.core.service.MatchWordService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("matchWord")
public class MatchWordController {

    @Autowired
    private MatchWordService matchWordService;

    @RequestMapping("upset")
    public Object upset(@RequestBody MatchWordParam param){
        Assert.isTrue(param.getWeightValue() > 0,"权重值必须大于0");
        return matchWordService.upset(param);
    }

    @RequestMapping("findByCollectionName")
    public Object findByCollectionName(@RequestBody SearchMatchWordParam param){
        return matchWordService.findByCollectionName(param);
    }

    @RequestMapping("del")
    public Object del(String id){
        return matchWordService.del(id);
    }

    @RequestMapping("list")
    public Object list(MatchWordParam param, Pageable pageable){
        return matchWordService.list(param,pageable);
    }
}
