package com.github.script.task.server.core.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import com.github.script.task.server.other.mvc.MVCConfiguration;
import com.github.script.task.server.other.mvc.MVCResponseConfiguration;

@Configuration
@Import({MVCConfiguration.class, MVCResponseConfiguration.class})
public class MVCConfig {
}
