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
@ConfigurationProperties(prefix = "nlp")
public class NLPConf {

    //标题长度
    private int titleLength = 40;

    //摘要长度
    private int summaryLength = 200;

    //应用配置
    private App[] app;


    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class App {

        //应用id
        private String appId;

        //apikey
        private String apiKey;

        //秘钥
        private String secretKey;


    }

}
