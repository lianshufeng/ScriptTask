package com.github.script.task.client;

import lombok.Getter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan({
        "com.github.script.task.client.core",
        "com.github.script.task.bridge",
})
@SpringBootApplication
public class ClientApplication {


    @Getter
    protected static ConfigurableApplicationContext applicationContext = null;


    public static void main(String[] args) {
        applicationContext = SpringApplication.run(ClientApplication.class, args);
    }

}
