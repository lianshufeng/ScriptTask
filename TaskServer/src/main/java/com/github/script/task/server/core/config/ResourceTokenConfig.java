package com.github.script.task.server.core.config;

import com.github.script.task.server.other.token.config.ResourceTokenConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(ResourceTokenConfiguration.class)
public class ResourceTokenConfig {

}
