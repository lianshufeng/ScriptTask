package com.github.script.task.server.core.conf;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@NoArgsConstructor
@AllArgsConstructor
@ConfigurationProperties(prefix = "ttl")
public class TTLConf {

    private Long jobTimeOut = 86400000L;

    private Long taskTimeOut = 86400000L;

    private Long duplicateTimout = 86400000L * 7 * 12;

    private Long UserClueTimeout = 86400000L * 30 * 12;

    //人机交互超时时间
    private Long userRobotTimeout = 60*60*1000L;
}
