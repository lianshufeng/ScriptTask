package com.github.script.task.server.other.es.config;

import com.github.script.task.server.other.es.conf.ElasticsearchConfig;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.RestClients;
import org.springframework.data.elasticsearch.config.ElasticsearchConfigurationSupport;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

@Configuration
@ComponentScan("com.github.script.task.server.other.es")
@EnableElasticsearchRepositories("com.github.script.task.server.other.es.dao")
public class ElasticsearchConfiguration extends ElasticsearchConfigurationSupport {

    @Value("${elasticsearch.hostAndPort}")
    private String[] hostAndPort;
//
//
//    @Bean
//    public ElasticsearchConfig elasticsearchConfig() {
//        return new ElasticsearchConfig();
//    }

    @Bean
    public RestHighLevelClient restHighLevelClient() {
        final ClientConfiguration clientConfiguration = ClientConfiguration.builder()
                .connectedTo(hostAndPort)
                .build();

        return RestClients.create(clientConfiguration).rest();
    }


}
