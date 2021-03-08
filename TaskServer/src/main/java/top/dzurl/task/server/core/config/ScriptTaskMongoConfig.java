package top.dzurl.task.server.core.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import top.dzurl.task.server.other.mongo.config.MongoConfiguration;

@Configuration
//jpa必须
@EnableMongoRepositories("top.dzurl.task.server.core.dao")
@Import({MongoConfiguration.class})
public class ScriptTaskMongoConfig {


}
