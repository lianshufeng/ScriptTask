package com.github.script.task.server.core.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import com.github.script.task.server.other.mongo.config.MongoConfiguration;

@Configuration
//jpa必须
@EnableMongoRepositories("com.github.script.task.server.core.dao.mongo")
@Import({MongoConfiguration.class})
public class ScriptTaskMongoConfig {


}
