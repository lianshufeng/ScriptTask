package com.github.script.task.bridge.util;

import lombok.SneakyThrows;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.Map;

/**
 * 系统工具
 */
public class SystemUtil {


    /**
     * 是否linux系统
     *
     * @return
     */
    public static boolean isLinux() {
        return System.getProperty("os.name").toLowerCase().contains("linux");
    }

    /**
     * 是否windows系统
     *
     * @return
     */
    public static boolean isWindows() {
        return System.getProperty("os.name").toLowerCase().contains("windows");
    }

    /**
     * 获取CPu核心数
     *
     * @return
     */
    public static int getCpuCoreCount() {
        return Runtime.getRuntime().availableProcessors();
    }


    /**
     * 设置环境变量
     *
     * @param envMap
     * @throws Exception
     */
    @SneakyThrows
    public static void setEnv(Map<String, String> envMap) {
        try {
            Class<?> processEnvironmentClass = Class.forName("java.lang.ProcessEnvironment");
            Field theEnvironmentField = processEnvironmentClass.getDeclaredField("theEnvironment");
            theEnvironmentField.setAccessible(true);
            Map<String, String> env = (Map<String, String>) theEnvironmentField.get(null);
            env.putAll(envMap);
            Field theCaseInsensitiveEnvironmentField = processEnvironmentClass.getDeclaredField("theCaseInsensitiveEnvironment");
            theCaseInsensitiveEnvironmentField.setAccessible(true);
            Map<String, String> cienv = (Map<String, String>) theCaseInsensitiveEnvironmentField.get(null);
            cienv.putAll(envMap);
        } catch (NoSuchFieldException e) {
            Class[] classes = Collections.class.getDeclaredClasses();
            Map<String, String> env = System.getenv();
            for (Class cl : classes) {
                if ("java.util.Collections$UnmodifiableMap".equals(cl.getName())) {
                    Field field = cl.getDeclaredField("m");
                    field.setAccessible(true);
                    Object obj = field.get(env);
                    Map<String, String> map = (Map<String, String>) obj;
                    map.clear();
                    map.putAll(envMap);
                }
            }
        }
    }

}
