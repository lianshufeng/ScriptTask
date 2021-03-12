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
import top.dzurl.task.bridge.util.SystemUtil;

import java.io.File;
import java.lang.reflect.Field;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

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
        mkdirs(runTime.getChromeHome(), "chrome浏览器", "https://www.google.cn/chrome/");

        //node
        mkdirs(runTime.getNodeHome(), "nodejs", "http://nodejs.cn/download/");

        //jdk
        mkdirs(runTime.getJdkHome(), "JavaSdk", "https://www.oracle.com/java/technologies/javase-jdk11-downloads.html");

        //simulator
        mkdirs(runTime.getSimulator(), "雷电模拟器4", "https://www.ldmnq.com");

        //appium
        setupAppiumHome(runTime);

        //android-tools sdk
        setupAndroidSdkHome(runTime);


        //设置到系统的环境变量
        setSystemEnv(runTime);

    }


    /**
     * 设置系统环境变量
     */
    private void setSystemEnv(ScriptTaskConf.RunTime runTime) {
        //设置android_home的环境变量
        SystemUtil.setEnv(new HashMap<String, String>() {{
            put("ANDROID_HOME", runTime.getAndroidSdkHome().getAbsolutePath());
        }});
    }


    /**
     * 创建目录
     *
     * @param file
     * @param title
     */
    private static void mkdirs(File file, String title, String remark) {
        log.info("[{}] - [{}] -> {}", file.getName(), title, remark);
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


    /**
     * 更改模拟器的adb为android-sdk的adb.exe
     */
    @SneakyThrows
    private void updateSimulatorAdbFile(ScriptTaskConf.RunTime runTime) {
        //取android_sdk的adb路径
        File abdFile = new File(runTime.getADBHome().getAbsolutePath() + "/adb.exe");
        Path adbPath = FileSystems.getDefault().getPath(abdFile.getAbsolutePath());
        if (!abdFile.exists()) {
            abdFile = new File(runTime.getADBHome().getAbsolutePath() + "/adb");
        }
        if (!abdFile.exists()) {
            return;
        }

        //模拟器里的adb.exe
        File simulatorAdbFile = new File(runTime.getSimulator().getAbsolutePath() + "/adb.exe");
        Path simulatorAdbPath = FileSystems.getDefault().getPath(simulatorAdbFile.getAbsolutePath());

        //创建软连接
        if (!simulatorAdbFile.exists()) {
            //软连接
            Files.createSymbolicLink(simulatorAdbPath, adbPath);
        } else if (simulatorAdbFile.exists() && !Files.isSymbolicLink(simulatorAdbPath)) {
            simulatorAdbFile.delete();
            //软连接
            Files.createSymbolicLink(simulatorAdbPath, adbPath);
        }

    }


    /**
     * 安装android的SDK运行环境
     *
     * @param runTime
     */
    private void setupAndroidSdkHome(ScriptTaskConf.RunTime runTime) {
        //android_commandline_tool
        mkdirs(runTime.getAndroidCommandLineToolHome(), "Android命令行工具", "https://developer.android.com/studio#Command_line_tools_only");

        //android sdk
        mkdirs(runTime.getAndroidSdkHome(), "Android的sdk", "install_android_sdk.bat");
        File android_sdk_install_bat = new File(runTime.getAndroidSdkHome().getAbsolutePath() + "/install_android_sdk.bat");
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

        //更新模拟器的adb.exe
        updateSimulatorAdbFile(runTime);

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

