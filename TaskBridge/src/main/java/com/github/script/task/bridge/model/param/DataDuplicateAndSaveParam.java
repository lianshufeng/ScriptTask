package com.github.script.task.bridge.model.param;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DataDuplicateAndSaveParam extends DataDuplicateParam {


    //过期时间
    private Long ttl;

}
