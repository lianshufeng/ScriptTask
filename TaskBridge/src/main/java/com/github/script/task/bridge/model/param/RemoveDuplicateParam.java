package com.github.script.task.bridge.model.param;

import lombok.Data;

import java.util.List;

@Data
public class RemoveDuplicateParam {

    private List<String> values;

    private String scriptName;

    private Long ttl;
}
