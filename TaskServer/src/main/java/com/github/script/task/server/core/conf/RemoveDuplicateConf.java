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
@ConfigurationProperties(prefix = "removeduplicate")
public class RemoveDuplicateConf {
    //过期时间
    private Long defaultTTl = 86400000L;
}
