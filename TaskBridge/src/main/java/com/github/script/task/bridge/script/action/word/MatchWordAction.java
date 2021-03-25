package com.github.script.task.bridge.script.action.word;

import com.github.script.task.bridge.model.MatchWordModel;
import com.github.script.task.bridge.model.param.SearchMatchWordParam;
import com.github.script.task.bridge.model.param.UserClueParam;
import com.github.script.task.bridge.result.ResultContent;
import com.github.script.task.bridge.result.ResultState;
import com.github.script.task.bridge.script.SuperScriptAction;
import com.github.script.task.bridge.service.MatchWordService;
import com.github.script.task.bridge.service.UserClueService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;
import java.util.function.Function;

@Slf4j
public class MatchWordAction extends SuperScriptAction {

    private String platformName = null;

    private List<Map> matchWordModels = new ArrayList<>();

    @Autowired
    private MatchWordService matchWordService;

    @Autowired
    private UserClueService userClueService;


    public MatchWordAction build(String platformName, List<String> collectionNames){
        this.platformName = platformName;
        ResultContent content = matchWordService.findByCollectionName(SearchMatchWordParam.builder().collectionNames(collectionNames).build());
        if (content != null && content.getState() == ResultState.Success){
            matchWordModels = (List<Map>) content.getContent();
        }
        return this;
    }


    public long match(String user,String text){
        long weightValue = 0L;
        for (Map map : matchWordModels){
            if (text.indexOf((String) map.get("keyWord")) > -1){
                weightValue = weightValue + (int)map.get("weightValue");
            }
        }
        return weightValue;
    }

    public void save(String user,long weightValue){
        UserClueParam param = new UserClueParam();
        param.setWeightValue(weightValue);
        param.setPlatform(platformName);
        param.setUser(user);
        Set<String> matchWords = new HashSet<>();
        for (Map map : matchWordModels){
            matchWords.add((String) map.get("id"));
        }
        param.setMatchWordIds(matchWords);
        userClueService.save(param);
    }

}
