package com.github.script.task.server.core.domain;

import com.github.script.task.server.other.mongo.domain.SuperEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RemoveDuplicate extends SuperEntity {

    //脚本名称
    private String scriptName;

    //值
    private String value;

    @Indexed(expireAfterSeconds = 0)
    private Date ttl;
}
