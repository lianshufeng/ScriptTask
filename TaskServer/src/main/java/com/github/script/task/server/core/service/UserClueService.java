package com.github.script.task.server.core.service;

import com.github.script.task.bridge.model.MatchWordModel;
import com.github.script.task.bridge.model.UserClueModel;
import com.github.script.task.bridge.model.param.UserClueParam;
import com.github.script.task.bridge.result.ResultContent;
import com.github.script.task.bridge.result.ResultState;
import com.github.script.task.server.core.conf.TTLConf;
import com.github.script.task.server.core.dao.UserClueDao;
import com.github.script.task.server.core.domain.MatchWord;
import com.github.script.task.server.core.domain.UserClue;
import com.github.script.task.server.other.mongo.util.PageEntityUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Service
public class UserClueService {

    @Autowired
    private UserClueDao userClueDao;

    @Autowired
    private TTLConf ttlConf;


    public ResultContent<String> save(UserClueParam param){
        UserClue userClue = new UserClue();
        BeanUtils.copyProperties(param,userClue);
        Set<MatchWord> matchWords = new HashSet<>();
        param.getMatchWordIds().forEach((it)->{
            MatchWord matchWord = new MatchWord();
            matchWord.setId(it);
            matchWords.add(matchWord);
        });
        userClue.setMatchWords(matchWords);
        if (param.getTimeOut() != 0) {
            userClue.setTtl(new Date(param.getTimeOut()));
        } else {
            userClue.setTtl(new Date(ttlConf.getUserClueTimeout()));
        }
        userClueDao.save(userClue);
        return ResultContent.build(ResultState.Success);
    }

    public Page<UserClueModel> list(UserClueParam param, Pageable pageable){
        return PageEntityUtil.concurrent2PageModel(this.userClueDao.list(param, pageable), (it) -> {
            return toModel(it);
        });
    }

    UserClueModel toModel(UserClue userClue){
        if (userClue == null){
            return null;
        }
        UserClueModel model = new UserClueModel();
        BeanUtils.copyProperties(userClue,model);
        Set<MatchWordModel> matchWords = new HashSet<>();
        userClue.getMatchWords().forEach((it)->{
            MatchWordModel matchWordModel = new MatchWordModel();
            BeanUtils.copyProperties(it,matchWordModel);
            matchWords.add(matchWordModel);
        });
        model.setMatchWords(matchWords);
        return model;
    }

}
