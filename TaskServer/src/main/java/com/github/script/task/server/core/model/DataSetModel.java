package com.github.script.task.server.core.model;

import com.github.script.task.server.core.model.nlp.NLPModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DataSetModel {

    private String id;

    private String hash;

    //自然语言处理模型
    private NLPModel nlp;
}
