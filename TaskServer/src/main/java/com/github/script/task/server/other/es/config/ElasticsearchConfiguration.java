package com.github.script.task.server.other.es.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.config.ElasticsearchConfigurationSupport;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

@Configuration
@ComponentScan("com.github.script.task.server.other.es")
@EnableElasticsearchRepositories("com.github.script.task.server.other.es.dao")
public class ElasticsearchConfiguration extends ElasticsearchConfigurationSupport {

//    @Value("${elasticsearch.hostAndPort}")
//    private String[] hostAndPort;
//
//
//    @Bean
//    public ElasticsearchConfig elasticsearchConfig() {
//        return new ElasticsearchConfig();
//    }


//    @Bean
//    public RestClient restClient() {
//        String host = hostAndPort[0];
//        int port = Integer.parseInt(hostAndPort[1]);
//        return RestClient.builder(new HttpHost(host, port)).build();
//    }
//
//    @Bean
//    public ElasticsearchTransport elasticsearchTransport(JacksonJsonpMapper jacksonJsonpMapper) {
//        return new RestClientTransport(restClient(), jacksonJsonpMapper);
//    }
//
//    @Bean
//    public ElasticsearchClient elasticsearchClient(JacksonJsonpMapper jacksonJsonpMapper) {
//        return new ElasticsearchClient(elasticsearchTransport(jacksonJsonpMapper));
//    }





//    @Bean
//    public RestHighLevelClient restHighLevelClient() {
//        final ClientConfiguration clientConfiguration = ClientConfiguration.builder()
//                .connectedTo(hostAndPort)
//                .build();
//
//        return RestClients.create(clientConfiguration).rest();
//    }


}
