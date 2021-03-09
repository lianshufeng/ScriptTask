package top.dzurl.task.client;

import lombok.Getter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan({
        "top.dzurl.task.client.core",
        "top.dzurl.task.bridge.helper"
})
@SpringBootApplication
public class ClientApplication {


    @Getter
    protected static ConfigurableApplicationContext applicationContext = null;


    public static void main(String[] args) {
        applicationContext = SpringApplication.run(ClientApplication.class, args);
    }

}
