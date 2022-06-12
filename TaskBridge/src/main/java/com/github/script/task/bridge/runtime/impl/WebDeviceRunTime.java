package com.github.script.task.bridge.runtime.impl;

import com.github.script.task.bridge.conf.ScriptTaskConf;
import com.github.script.task.bridge.device.impl.WebDevice;
import com.github.script.task.bridge.device.type.DeviceType;
import com.github.script.task.bridge.runtime.SuperDeviceRunTime;
import io.github.bonigarcia.wdm.WebDriverManager;
import io.github.bonigarcia.wdm.config.Config;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.github.script.task.bridge.script.ScriptRuntime;

import java.io.File;
import java.util.Optional;

/**
 * android真机
 */
@Slf4j
@Component
public class WebDeviceRunTime extends SuperDeviceRunTime {


    @Autowired
    private ScriptTaskConf scriptTaskConf;


    @Override
    public void create(ScriptRuntime runtime) {
        final WebDevice webDevice = (WebDevice) runtime.getEnvironment().getDevice();
        runtime.setDriver(buildDriver(webDevice));
    }

    @Override
    public void close(ScriptRuntime runtime) {
        Object obj = runtime.getDriver();
        if (obj == null || !(obj instanceof ChromeDriver)) {
            return;
        }
        ChromeDriver driver = (ChromeDriver) obj;
        driver.quit();
    }

    /**
     * 安装Chrome的驱动
     */
    @SneakyThrows
    private void installChromeDriver() {
//        WebDriverManager webDriverManager = WebDriverManager
//                .browserPath(scriptTaskConf.getRunTime().getChromeHome().getAbsolutePath());
        WebDriverManager webDriverManager = WebDriverManager.getInstance();
        Config config = webDriverManager.config();
        //使用淘宝镜像安装
        config.setUseMirror(true);
        WebDriverManager.chromedriver().setup();
    }


    private File ChromeBinaryFile() {
        String home = scriptTaskConf.getRunTime().getChromeHome().getAbsolutePath();
        if (new File(home + "/chrome.exe").exists()) {
            return new File(home + "/chrome.exe");
        } else if (new File(home + "/chrome").exists()) {
            return new File(home + "/chrome");
        }
        throw new RuntimeException("请将 chrome 安装到 [" + home + "] 目录下");
    }


    /**
     * 创建驱动
     */
    private ChromeDriver buildDriver(WebDevice webDevice) {
        installChromeDriver();

        ChromeOptions options = new ChromeOptions();
        options.setBinary(ChromeBinaryFile());

        //无头模式
        if (webDevice.isHeadless()) {
            options.setHeadless(true);
        }

        //无痕模式
        if (webDevice.isIncognito()) {
            options.addArguments("--incognito");
        }

        //无沙箱模式
        if (webDevice.isNoSandbox()) {
            options.addArguments("--no-sandbox");
            options.addArguments("--disable-dev-shm-usage");
        }


        //允许运行部安全的内容
        if (webDevice.isAllowInsecure()) {
            options.addArguments("--allow-running-insecure-content");
        }


        //启动命令行
        Optional.ofNullable(webDevice.getArguments()).ifPresent((it) -> {
            options.addArguments(it);
        });


        //实验性扩展配置
        Optional.ofNullable(webDevice.getExperimentalOption()).ifPresent((experimentalOption) -> {
            experimentalOption.entrySet().forEach((entry) -> {
                options.setExperimentalOption(entry.getKey(), entry.getValue());
            });
        });


        ChromeDriver driver = new ChromeDriver(options);
        //设置浏览器的宽度与高度
        driver.manage().window().setSize(new Dimension(webDevice.getWidth(), webDevice.getHeight()));
        return driver;
    }


    @Override
    public DeviceType deviceType() {
        return DeviceType.Web;
    }
}
