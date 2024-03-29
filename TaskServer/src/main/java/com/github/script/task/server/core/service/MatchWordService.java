package com.github.script.task.server.core.service;

import com.github.script.task.bridge.model.MatchWordModel;
import com.github.script.task.bridge.model.param.MatchWordParam;
import com.github.script.task.bridge.model.param.SearchMatchWordParam;
import com.github.script.task.bridge.result.ResultContent;
import com.github.script.task.bridge.result.ResultState;
import com.github.script.task.server.core.dao.mongo.MatchWordDao;
import com.github.script.task.server.core.domain.MatchWord;
import com.github.script.task.server.other.mongo.util.PageEntityUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;


import java.util.Set;
import java.util.stream.Collectors;

@Service
public class MatchWordService {


    @Autowired
    private MatchWordDao matchWordDao;


    public ResultContent<String> upset(MatchWordParam param){
        if (StringUtils.isBlank(param.getId())){
            Assert.hasText(param.getCollectionName(),"集合名称不能为空");
            Assert.hasText(param.getKeyWord(),"关键词不能为空");
            Assert.isTrue(param.getWeightValue() > 0,"权重值要大于零");
            //新增
            if (matchWordDao.existsByCollectionNameAndKeyWord(param.getCollectionName(),param.getKeyWord())){
                return ResultContent.build(ResultState.MatchWordExists);
            }
            MatchWord matchWord = new MatchWord();
            BeanUtils.copyProperties(param,matchWord);
            matchWordDao.save(matchWord);
        } else {
            //修改
            Assert.hasText(param.getId(),"id不能为空");
            if (!matchWordDao.update(param)){
                ResultContent.build(ResultState.Fail);
            }
        }
        return ResultContent.build(ResultState.Success);
    }

    public ResultContent<Set> findByCollectionName(SearchMatchWordParam param){
        Assert.notNull(param.getCollectionNames(),"集合名称不能为空");
        return ResultContent.buildContent(matchWordDao.findByCollectionNameIn(param.getCollectionNames()).stream().map((it)->{
            return toModel(it);
        }).collect(Collectors.toList()));
    }

    public ResultContent<String> del(String id){
        return matchWordDao.del(id) ? ResultContent.buildContent(ResultState.Success) : ResultContent.buildContent(ResultState.Fail);
    }


    MatchWordModel toModel(MatchWord matchWord){
        if (matchWord == null){
            return null;
        }
        MatchWordModel model = new MatchWordModel();
        BeanUtils.copyProperties(matchWord,model);
        return model;
    }

    public Page<MatchWordModel> list(MatchWordParam param, Pageable pageable) {
        return PageEntityUtil.concurrent2PageModel(this.matchWordDao.list(param, pageable), (it) -> {
            return toModel(it);
        });
    }
}
