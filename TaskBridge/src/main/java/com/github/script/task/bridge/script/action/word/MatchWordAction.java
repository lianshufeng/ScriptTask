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

import java.util.Set;

@Slf4j
public class MatchWordAction extends SuperScriptAction {

    private String platformName = null;

    private Set<MatchWordModel> matchWordModels = null;

    @Autowired
    private MatchWordService matchWordService;

    @Autowired
    private UserClueService userClueService;


    public MatchWordAction build(String platformName,Set<String> collectionName){
        this.platformName = platformName;
        ResultContent content = matchWordService.findByCollectionName(SearchMatchWordParam.builder().collectionName(collectionName).build());
        if (content != null && content.getState() == ResultState.Success){
            matchWordModels = (Set<MatchWordModel>) content.getContent();
        }
        return this;
    }


    public void match(String user,String text){
        UserClueParam param = new UserClueParam();
        long weightValue = 0L;
        for (MatchWordModel matchWordModel:matchWordModels){
            if (text.indexOf(matchWordModel.getKeyWord()) > -1){
                weightValue = weightValue + matchWordModel.getWeightValue();
            }
        }
        if (param.getWeightValue() > 0){
            param.setPlatform(platformName);
            param.setUser(user);
            userClueService.save(param);
        }
    }

}
