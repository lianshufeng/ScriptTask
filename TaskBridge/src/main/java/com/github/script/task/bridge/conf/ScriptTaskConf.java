package com.github.script.task.bridge.conf;

import com.github.script.task.bridge.device.type.DeviceType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.io.FilenameUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.system.ApplicationHome;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.Set;

@Data
@Component
@NoArgsConstructor
@AllArgsConstructor
@ConfigurationProperties(prefix = "script.task")
public class ScriptTaskConf {

    //主机地址
    private String host = "http://localhost:8080/";

    //最大并发任务的数量
    private int maxRunTaskCount = 5;


    //远程任务
    private RemoteTask remoteTask = new RemoteTask();


    //运行环境的配置
    private RunTime runTime = new RunTime();

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RemoteTask {


        //是否执行远程任务
        private boolean work = true;

        //能力值
        private Set<DeviceType> powerType = Set.of(
                DeviceType.AndroidMachine,
                DeviceType.AndroidSimulator,
                DeviceType.None,
                DeviceType.Web
        );
    }


    /**
     * 运行环境
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RunTime {

        private ApplicationHome applicationHome = new ApplicationHome();

        //运行环境的跟目录
        protected String home = "../runtime";


        /**
         * 取目录
         *
         * @param fileName
         * @return
         */
        private File getHome(String fileName) {
            return new File(FilenameUtils.normalize(applicationHome.getDir().getAbsolutePath() + "/" + home + "/" + fileName));
        }

        /**
         * 取出脚本目录
         *
         * @return
         */
        public File getScriptHome() {
            return getHome("script");
        }

        /**
         * 获取JDK的路径
         *
         * @return
         */
        public File getJdkHome() {
            return getHome("jdk");
        }

        /**
         * 取Chrome的路径
         *
         * @return
         */
        public File getChromeHome() {
            return getHome("chrome");
        }

        /**
         * 获取Appium的路径
         *
         * @return
         */
        public File getAppiumHome() {
            return getHome("appium");
        }

        /**
         * 获取nodejs的路径
         *
         * @return
         */
        public File getNodeHome() {
            return getHome("node");
        }


        /**
         * 获取android的命令行工具
         *
         * @return
         */
        public File getAndroidCommandLineToolHome() {
            return getHome("/android-cmdline-tools");
        }

        /**
         * 取出AndroidSdk路径
         *
         * @return
         */
        public File getAndroidSdkHome() {
            return getHome("/android-sdk");
        }


        /**
         * 取出adb所在的路径
         *
         * @return
         */
        public File getADBHome() {
            return getHome("/android-sdk/platform-tools");
        }


        /**
         * 获取模拟器目录
         *
         * @return
         */
        public File getSimulator() {
            return getHome("simulator");
        }


    }


}
