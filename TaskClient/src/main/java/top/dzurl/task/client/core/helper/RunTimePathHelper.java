package top.dzurl.task.client.core.helper;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import top.dzurl.task.bridge.conf.ScriptTaskConf;
import top.dzurl.task.bridge.util.JsonUtil;

import java.io.File;
import java.util.HashMap;

@Component
@Slf4j
public class RunTimePathHelper {

    @Autowired
    private ScriptTaskConf scriptTaskConf;


    @Autowired
    private void init(ApplicationContext applicationContext) {
        ScriptTaskConf.RunTime runTime = scriptTaskConf.getRunTime();
        mkdirs(runTime.getChromeHome(), "Chrome浏览器", "https://www.google.cn/chrome/");
        mkdirs(runTime.getNodeHome(), "NodeJs", "http://nodejs.cn/download/");
        mkdirs(runTime.getSimulator(), "雷电模拟器", "https://www.ldmnq.com/");
        setupAppiumHome(runTime);
        setupAndroidSdkHome(runTime);


    }

    /**
     * 创建目录
     *
     * @param file
     * @param title
     */
    private void mkdirs(File file, String title, String url) {
        log.info("[{}] -> {}", title, url);
        if (!file.exists()) {
            file.mkdirs();
        }
    }


    private void setupAndroidSdkHome(ScriptTaskConf.RunTime runTime) {
        mkdirs(runTime.getAndroidCommandLineToolHome(), "Android命令行工具", "https://developer.android.com/studio#Command_line_tools_only");
        mkdirs(runTime.getAndroidSdkHome(), "Android的SDK", "https://developer.android.com/studio#Command_line_tools_only");

    }

    @SneakyThrows
    private void setupAppiumHome(ScriptTaskConf.RunTime runTime) {
        mkdirs(runTime.getAppiumHome(), "Appium", "https://appium.io/");
        File packageFile = new File(runTime.getAppiumHome() + "/package.json");
        if (!packageFile.exists()) {
            FileUtils.writeStringToFile(packageFile, JsonUtil.toJson(new HashMap<String, Object>() {{
                put("name", "appium");
                put("version", "1.0.0");
                put("dependencies", new HashMap<String, Object>() {{
                    put("appium", "^1.20.0");
                }});
            }}, true), "UTF-8");
        }
    }


}

