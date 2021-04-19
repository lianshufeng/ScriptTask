package com.github.script.task.bridge.script.action.word;

import com.github.script.task.bridge.model.MatchWordModel;
import com.github.script.task.bridge.model.param.SearchMatchWordParam;
import com.github.script.task.bridge.model.param.UserClueParam;
import com.github.script.task.bridge.result.ResultContent;
import com.github.script.task.bridge.result.ResultState;
import com.github.script.task.bridge.script.SuperScriptAction;
import com.github.script.task.bridge.service.MatchWordService;
import com.github.script.task.bridge.service.UserClueService;
import com.github.script.task.bridge.util.BeanUtil;
import com.github.script.task.bridge.util.JsonUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;
import java.util.function.Function;

@Slf4j
public class MatchWordAction extends SuperScriptAction {

    private String platformName = null;

    private List<MatchWord> matchWordModels = new ArrayList<>();

    @Autowired
    private MatchWordService matchWordService;

    @Autowired
    private UserClueService userClueService;

    private List<String> matchInfos = new ArrayList<>();

    private static int substrCount = 100;


    @SneakyThrows
    public MatchWordAction build(String platformName, List<String> collectionNames) {
        this.platformName = platformName;
        ResultContent content = matchWordService.findByCollectionName(SearchMatchWordParam.builder().collectionNames(collectionNames).build());
        if (content != null && content.getState() == ResultState.Success) {
            List<Map<String,Object>> result = (List) content.getContent();
            for (Map m : result){
                MatchWord word = JsonUtil.toObject(JsonUtil.toJson(m),MatchWord.class);
                matchWordModels.add(word);
            }
        }
        return this;
    }


    public boolean match(MatchResult result, String text) {
        long weightValue = result.getWeightValue();
        for (MatchWord word : matchWordModels) {
            if (text.indexOf(word.getKeyWord()) > -1){
                weightValue = result.getWeightValue() + word.weightValue;
                result.getMatchWords().add(word);
                int index = text.indexOf(word.getKeyWord());
                String frontStr = "";
                String afterStr = "";
                if (index - substrCount > 0){
                    frontStr = text.substring(index - substrCount,index);
                } else {
                    frontStr = text.substring(0,index);
                }
                if (index + substrCount > text.length()){
                    afterStr = text.substring(index);
                } else {
                    afterStr = text.substring(index,index + substrCount);
                }
                matchInfos.add(frontStr + afterStr);
            }

        }
        result.setWeightValue(weightValue);
        return true;
    }

    public void save(String user, MatchResult result) {
        UserClueParam param = new UserClueParam();
        param.setWeightValue(result.getWeightValue());
        param.setPlatform(platformName);
        param.setUser(user);
        Set<String> matchWords = new HashSet<>();
        for (MatchWord word : result.matchWords) {
            matchWords.add(word.getId());
        }
        param.setMatchWordIds(matchWords);
        param.setMatchInfo(matchInfos.get(0));
        userClueService.save(param);
    }

    public MatchResult getMatchResult(){
        return new MatchResult();
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MatchResult {
        private Set<MatchWord> matchWords = new HashSet<>();
        private long weightValue = 0;
    }

    @Data
    @NoArgsConstructor
    static class MatchWord {
        private String id;
        private String keyWord;
        private long weightValue;
    }
}
