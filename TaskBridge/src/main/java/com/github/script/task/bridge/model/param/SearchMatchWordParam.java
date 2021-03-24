package com.github.script.task.bridge.model.param;

import lombok.Builder;
import lombok.Data;

import java.util.Set;

@Data
@Builder
public class SearchMatchWordParam {

    //集合名
    private Set<String> collectionName ;

}
