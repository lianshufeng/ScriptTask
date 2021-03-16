package com.github.script.task.server.core.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import com.github.script.task.server.other.mongo.domain.SuperEntity;

import java.util.Date;
import java.util.List;

@Document
@Data
@AllArgsConstructor
@NoArgsConstructor
public class JobRunLog extends SuperEntity {

    //脚本名称
    @Indexed
    private String scriptName;

    //jobId
    @Indexed
    private String jobId;

    //设备ID
    private String deviceId;

    //获取jobTime
    private Long getJobTime;

    //执行日志
    private List<String> logs;


    @Indexed(expireAfterSeconds = 0)
    private Date ttl;

}
