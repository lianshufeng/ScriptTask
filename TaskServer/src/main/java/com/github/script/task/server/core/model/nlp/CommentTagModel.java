package com.github.script.task.server.core.model.nlp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommentTagModel {

    //匹配上的属性词
    private String prop;

    //匹配上的描述词
    private String adj;

}
