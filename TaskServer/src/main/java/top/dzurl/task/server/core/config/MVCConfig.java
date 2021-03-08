package top.dzurl.task.server.core.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import top.dzurl.task.server.other.mvc.MVCConfiguration;
import top.dzurl.task.server.other.mvc.MVCResponseConfiguration;

@Configuration
@Import({MVCConfiguration.class, MVCResponseConfiguration.class})
public class MVCConfig {
}
