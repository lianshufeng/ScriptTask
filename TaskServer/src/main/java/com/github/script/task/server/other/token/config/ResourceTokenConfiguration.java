package com.github.script.task.server.other.token.config;

import com.github.script.task.server.other.token.service.ResourceTokenService;
import com.github.script.task.server.other.token.service.impl.ResourceTokenServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories(basePackages = {"com.github.script.task.server.other.token.dao"})
public class ResourceTokenConfiguration {

    @Bean
    public ResourceTokenService resourceTokenService() {
        return new ResourceTokenServiceImpl();
    }

}
