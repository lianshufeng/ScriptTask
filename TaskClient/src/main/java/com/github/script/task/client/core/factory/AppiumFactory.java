package com.github.script.task.client.core.factory;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import io.appium.java_client.service.local.flags.GeneralServerFlag;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import com.github.script.task.bridge.conf.ScriptTaskConf;

import java.io.File;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class AppiumFactory {

    @Autowired
    private ScriptTaskConf appTaskConf;


    private File node_path;
    private File JS_Path;


    @Autowired
    private void initPath(ApplicationContext applicationContext) {
        node_path = new File(this.appTaskConf.getRunTime().getNodeHome().getAbsolutePath() + "/node.exe");
        JS_Path = new File(this.appTaskConf.getRunTime().getAppiumHome().getAbsolutePath() + "/node_modules/appium/build/lib/main.js");
    }


    /**
     * 创建服务
     *
     * @return
     */
    public AppiumDriverLocalService buildService() {
        log.info("[创建] - [appium] - AppiumDriverLocalService");
        AppiumDriverLocalService service = AppiumDriverLocalService.buildService(new AppiumServiceBuilder()
                .withArgument(GeneralServerFlag.LOG_LEVEL, "error")
                .withArgument(GeneralServerFlag.SESSION_OVERRIDE)
                .withArgument(GeneralServerFlag.RELAXED_SECURITY)
                .usingAnyFreePort()
                .usingDriverExecutable(node_path)
                .withAppiumJS(JS_Path));
        service.start();
        return service;
    }

    /**
     * 创建客户端
     */
    public AndroidDriver buildAndroidDriver(AppiumDriverLocalService appiumDriverLocalService, String adbConnectionName) {
        log.info("[创建] - [appium] - [AndroidDriver]");
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("udid", adbConnectionName);
        capabilities.setCapability("platformName", "Android");
        capabilities.setCapability("newCommandTimeout", 60 * 60 * 24);

        //不允许脚本退出与关闭
        AndroidDriver driver = new AndroidDriver(appiumDriverLocalService.getUrl(), capabilities) {
            @Override
            public void quit() {
                log.info("不允许执行 : {}", "quit");
            }

            @Override
            public void close() {
                log.info("不允许执行 : {}", "close");
            }
        };
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        return driver;
    }


}
