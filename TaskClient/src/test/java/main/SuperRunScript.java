package main;

import lombok.Getter;
import lombok.SneakyThrows;
import org.apache.commons.io.FileUtils;
import org.junit.BeforeClass;
import org.springframework.boot.system.ApplicationHome;
import org.springframework.context.ConfigurableApplicationContext;
import top.dzurl.task.bridge.script.Environment;
import top.dzurl.task.bridge.script.SuperScript;
import top.dzurl.task.client.ClientApplication;
import top.dzurl.task.client.core.factory.ScriptFactory;

import java.io.File;
import java.util.Map;

public abstract class SuperRunScript {

    @Getter
    private static ConfigurableApplicationContext applicationContext = null;

    @BeforeClass
    public static void runOnceBeforeClass() {
        ClientApplication.main(new String[]{});
        applicationContext = ClientApplication.getApplicationContext();
    }

    public <T> T runScript(String fileName) {
        return runScript(fileName, null, null);
    }

    public <T> T runScript(String fileName, final Environment environment) {
        return runScript(fileName, environment, null);
    }

    public <T> T runScript(String fileName, final Environment environment, final Map<String, Object> parameters) {
        ApplicationHome applicationHome = new ApplicationHome();
        File file = new File(applicationHome.getDir().getAbsolutePath() + "/src/test/java/" + fileName);
        return runScript(file, environment, parameters);
    }

    /**
     * 执行groovy脚本
     *
     * @param file
     * @param <T>
     * @return
     */
    @SneakyThrows
    public <T> T runScript(File file, final Environment environment, final Map<String, Object> parameters) {
        String scriptBody = FileUtils.readFileToString(file, "UTF-8");

        //脚本工厂
        ScriptFactory scriptFactory = applicationContext.getBean(ScriptFactory.class);
        //转换脚本
        SuperScript script = scriptFactory.parse(scriptBody, environment, parameters);
        //执行脚本
        return (T) scriptFactory.run(script);
    }


}