package com.github.script.task.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan({"com.github.script.task.server.core"})
@ComponentScan({"com.github.script.task.bridge.conf"})
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class ServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServerApplication.class, args);
    }

}
