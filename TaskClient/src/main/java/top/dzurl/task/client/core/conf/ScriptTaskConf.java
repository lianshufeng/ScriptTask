package top.dzurl.task.client.core.conf;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.io.FilenameUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.system.ApplicationHome;
import org.springframework.stereotype.Component;

import java.io.File;

@Data
@Component
@NoArgsConstructor
@AllArgsConstructor
@ConfigurationProperties(prefix = "script.task")
public class ScriptTaskConf {

    //最大并发任务的数量
    private int maxRunTaskCount = 5;

    //运行环境的配置
    private RunTime runTime = new RunTime();


    /**
     * 模拟器
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
         * 取Chrome的路径
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
