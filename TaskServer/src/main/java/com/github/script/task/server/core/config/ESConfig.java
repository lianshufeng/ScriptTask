package com.github.script.task.server.core.config;

import com.github.script.task.server.other.es.config.ElasticsearchConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

@Configuration
@Import({ElasticsearchConfiguration.class})
@EnableElasticsearchRepositories("com.github.script.task.server.core.dao.es")
public class ESConfig {
}
