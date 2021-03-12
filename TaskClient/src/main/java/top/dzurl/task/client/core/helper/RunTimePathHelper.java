package top.dzurl.task.client.core.helper;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
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
        //env.bat
        makeEnvCmd(runTime);

        //chrome
        mkdirs(runTime.getChromeHome(), "Chrome浏览器", "https://www.google.cn/chrome/");

        //node
        mkdirs(runTime.getNodeHome(), "NodeJs", "http://nodejs.cn/download/");

        //jdk
        mkdirs(runTime.getJdkHome(), "Jdk", "https://www.oracle.com/java/technologies/javase-jdk11-downloads.html");

        //simulator
        mkdirs(runTime.getSimulator(), "雷电模拟器", "https://www.ldmnq.com/");

        //appium
        setupAppiumHome(runTime);

        //android-tools sdk
        setupAndroidSdkHome(runTime);
    }


    /**
     * 创建目录
     *
     * @param file
     * @param title
     */
    private static void mkdirs(File file, String title, String remark) {
        log.info("[{}] -> {}", title, remark);
        if (!file.exists()) {
            file.mkdirs();
        }
    }


    /**
     * 写命令行文件到磁盘
     *
     * @param file
     * @param cmds
     */
    @SneakyThrows
    private static void writeFile(File file, String[] cmds) {
        FileUtils.writeStringToFile(file, String.join("\r\n", cmds), "GBK");
    }


    /**
     * 生产环境变量
     */
    private void makeEnvCmd(ScriptTaskConf.RunTime runTime) {
        File file = new File(runTime.getHome() + "/env.bat");
        if (!file.exists()) {
            String[] cmds = new String[]{
                    "@echo off",
                    "set JAVA_HOME=%~dp0jdk",
                    "set ANDROID_HOME=%~dp0android-sdk",
                    "set NODE_HOME=%~dp0node",
                    "set Path=%JAVA_HOME%\\bin;%Path%",
                    "set Path=%ANDROID_HOME%\\platform-tools;%Path%",
                    "set Path=%NODE_HOME%;%Path%"
            };
            writeFile(file, cmds);
        }
    }


    private void setupAndroidSdkHome(ScriptTaskConf.RunTime runTime) {
        mkdirs(runTime.getAndroidCommandLineToolHome(), "Android命令行工具", "https://developer.android.com/studio#Command_line_tools_only");


        //android sdk
        mkdirs(runTime.getAndroidSdkHome(), "Android的SDK", "updateAndroidSdk.cmd");
        File android_sdk_install_bat = new File(runTime.getAndroidSdkHome().getAbsolutePath() + "/install.bat");
        if (!android_sdk_install_bat.exists()) {
            String[] install_sdk_cmds = new String[]{
                    "@echo off",
                    "call %~dp0..\\env.bat",
                    "set SdkmanagerPath=%~dp0..\\android-cmdline-tools\\bin\\sdkmanager",
                    "cmd /c %SdkmanagerPath% --sdk_root=%ANDROID_HOME% --licenses",
                    "cmd /c %SdkmanagerPath% --sdk_root=%ANDROID_HOME%  \"platform-tools\" \"build-tools;29.0.2\"",
                    "pause"
            };
            writeFile(android_sdk_install_bat, install_sdk_cmds);
        }

    }

    @SneakyThrows
    private void setupAppiumHome(ScriptTaskConf.RunTime runTime) {
        mkdirs(runTime.getAppiumHome(), "Appium", "https://appium.io/ && npm install");
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

