package com.github.script.task.bridge.util;

import com.github.script.task.bridge.device.impl.AndroidSimulatorDevice;
import lombok.Cleanup;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.util.StreamUtils;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.nio.charset.Charset;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 仅适合 4.0
 */
@Slf4j
public class LeiDianSimulatorUtil {

    //模拟器配置的key
    public static final String SimulatorPlayerName = "statusSettings.playerName";
    public static final String SimulatorMacAddress = "propertySettings.macAddress";
    public static final String DeviceBindName = "script.task.bind";

    private final static String fileFlag = "leidian";

    //扩展目录
    private final static String extendName = "extend";


    /**
     * 关闭应用
     *
     * @param home
     * @param bundleId
     */
    public static void stopApp(File home, String name, String bundleId) {
        runCmd(home, "adb", "--name", name, "--command", "shell am force-stop " + bundleId);
    }


    /**
     * 启动
     *
     * @param home
     * @param name
     */
    public static void launch(File home, String name) {
        runCmd(home, "launch", "--name", name);
    }


    /**
     * 定位坐标
     *
     * @param home
     * @param lng
     * @param lat
     */
    public static void locate(File home, String name, String lng, String lat) {
        runCmd(home, "locate", "--name", name, "--LLI", String.format("%s,%s", lng, lat));
    }


    /**
     * 关闭虚拟机
     *
     * @param home
     * @param name
     */
    public static void quit(File home, String name) {
        runCmd(home, "quit", "--name", name);
    }


    /**
     * 获取序列号
     *
     * @param home
     * @param name
     * @return
     */
    public static String getSerialno(File home, final String name) {
        //每次取设备号之前，先刷新adb状态
        runCmd(home, "adb", "--name", name, "reconnect", "device");

        //重新adb的方法，避免缓存错误的设备名

        //取出所有的设备
        List<String> devices = new ArrayList<>(ADBUtil.list(home));

        //取出所有模拟器的信息
        Map<String, Object> deviceInfo = get(home, name);
        Object macAddress = deviceInfo.get("propertySettings.macAddress");
        if (macAddress == null) {
            return null;
        }
        String deviceMac = String.valueOf(macAddress).trim().toUpperCase();

        for (String device : devices) {
            //取出当前运行设备的mac地址
            final String address = ADBUtil.getMac(home, device);
            if (!StringUtils.hasText(address)) {
                continue;
            }
            //格式化mac地址
            if (address.equals(deviceMac)) {
                return device;
            }

        }

        return null;
    }


    /**
     * 创建模拟器
     *
     * @param home
     * @param name
     */
    public static void create(File home, String name) {
        runCmd(home, "add", "--name", name);
    }


    /**
     * 创建模拟器
     *
     * @param home
     * @param name
     */
    public static void restore(File home, String name) {

        String simulatorFile = null;
        for (Map.Entry<String, Map<String, Object>> entry : list(home).entrySet()) {
            Object o = entry.getValue().get("statusSettings.playerName");
            if (o != null && name.equals(o)) {
                simulatorFile = entry.getKey();
                break;
            }
        }

        if (simulatorFile == null) {
            return;
        }


        //重置数据
        for (String fileName : Set.of("data.vmdk", "sdcard.vmdk", "system.vmdk")) {
            File file = new File(home.getAbsolutePath() + "/vms/" + simulatorFile + "/" + fileName);
            List<String> cmds = new ArrayList<>() {{
                add("restore");
                add("--name");
                add(name);
                add("--file");
                add(file.getAbsolutePath());
            }};
            runCmd(home, cmds.toArray(new String[0]));
        }


    }

    /**
     * 修改模拟器
     *
     * @param home
     * @param name
     * @param device
     */
    @SneakyThrows
    public static void modify(File home, String name, AndroidSimulatorDevice device) {

        //转换对象到map，且过滤空参数
        Map<String, Object> ret = new HashMap<>();
        for (Field field : AndroidSimulatorDevice.class.getDeclaredFields()) {
            field.setAccessible(true);
            Object val = field.get(device);
            if (val != null) {
                ret.put(field.getName(), val);
            }
        }

        if (ret.size() == 0) {
            return;
        }

        List<String> cmds = new ArrayList<>() {{
            add("modify");
            add("--name");
            add(name);
        }};

        ret.entrySet().forEach((it) -> {
            cmds.add("--" + it.getKey());
            cmds.add(String.valueOf(it.getValue()));
        });

        runCmd(home, cmds.toArray(new String[0]));
    }


    /**
     * 取运行列表
     *
     * @param home
     * @return
     */
    public static List<String> runninglist(File home) {
        return List.of(runCmd(home, "runninglist").split("\r\n"));
    }


    /**
     * 是否在运行
     *
     * @param home
     * @param name
     * @return
     */
    public static Boolean isrunning(File home, String name) {
        String ret = runCmd(home, "isrunning", "--name", name).trim().replaceAll("\n", "").replaceAll("\r", "");
        if (!StringUtils.hasText(ret)) {
            return null;
        }
        return "running".equals(ret);
    }

    /**
     * 扩展文件
     *
     * @param home
     * @param name
     * @return
     */
    private static File extendFile(File home, String name) {
        return new File(home.getAbsolutePath() + "/vms/" + extendName + "/" + name + ".json");
    }

    /**
     * @param home
     * @param name
     * @param items
     */
    @SneakyThrows
    public static void updateExtendConfig(File home, String name, Map<String, Object> items) {
        File configFile = extendFile(home, name);
        final Map<String, Object> ret = new HashMap<>();
        if (configFile.exists()) {
            ret.putAll(JsonUtil.toObject(FileUtils.readFileToString(configFile, "UTF-8"), Map.class));
        }
        ret.putAll(items);
        FileUtils.writeStringToFile(configFile, JsonUtil.toJson(ret, true), "UTF-8");
    }


    /**
     * 查询雷电模拟
     */
    public static Map<String, Map<String, Object>> list(File home) {
        Map<String, Map<String, Object>> ret = new HashMap<>();
        final File vmsFile = new File(home.getAbsolutePath() + "/vms");
        Arrays.stream(vmsFile.listFiles()).filter((it) -> {
            String fileName = it.getName();
            return fileName.length() > fileFlag.length() && "leidian".equals(fileName.substring(0, fileFlag.length()));
        }).forEach((file) -> {
            File configFile = new File(vmsFile.getAbsolutePath() + "/config/" + file.getName() + ".config");
            if (configFile.exists()) {
                Map<String, Object> items = readFileFromJson(configFile);
                Object playerName = items.get(SimulatorPlayerName);
                //过滤存在模拟器名
                if (playerName != null) {
                    File extendFile = extendFile(home, String.valueOf(playerName));
                    if (extendFile.exists()) {
                        items.putAll(readFileFromJson(extendFile));
                    }
                    ret.put(file.getName(), items);
                }
            }
        });
        return ret;
    }

    /**
     * 查询
     *
     * @param home
     * @param name
     * @return
     */
    public static Map<String, Object> get(File home, String name) {
        for (Map<String, Object> map : list(home).values()) {
            Object playerName = map.get(SimulatorPlayerName);
            if (playerName != null && name.equals(playerName)) {
                //加载扩展信息
                File extendFile = extendFile(home, String.valueOf(playerName));
                if (extendFile.exists()) {
                    map.putAll(readFileFromJson(extendFile));
                }
                return map;
            }
        }
        return null;
    }


    /**
     * 取出所有模拟器的名称
     *
     * @param leiDianHome
     * @return
     */
    public static Set<String> listNames(File leiDianHome) {
        return LeiDianSimulatorUtil.list(leiDianHome).values().stream().map((it) -> {
            return it.get("statusSettings.playerName");
        }).filter((it) -> {
            return it != null;
        }).map((it) -> {
            return String.valueOf(it);
        }).collect(Collectors.toSet());
    }


    /**
     * 读取json对象到map
     *
     * @param file
     * @return
     */
    @SneakyThrows
    private static Map<String, Object> readFileFromJson(File file) {
        return JsonUtil.toObject(FileUtils.readFileToString(file, "UTF-8"), Map.class);
    }


    @SneakyThrows
    private static String runCmd(File home, String... cmds) {
        return command(home, "ldconsole.exe", cmds);
    }

    @SneakyThrows
    private static String runAdb(File home, String... cmds) {
        return ADBUtil.runAdb(home, cmds);
    }

    @SneakyThrows
    private static String command(File home, String fileName, String... cmds) {
        String filePath = home.getAbsolutePath() + "/" + fileName;
        List<String> cmdLines = new ArrayList() {{
            add("cmd");
            add("/c");
            add(FilenameUtils.normalize(filePath));
            addAll(List.of(cmds));
        }};
        log.debug("cmd : {}", org.apache.commons.lang3.StringUtils.join(cmdLines.toArray(new String[0]), " "));
        Process p = Runtime.getRuntime().exec(cmdLines.toArray(new String[0]));
        @Cleanup InputStream inputStream = p.getInputStream();
        String ret = StreamUtils.copyToString(inputStream, Charset.forName("UTF-8"));
        p.waitFor(5, TimeUnit.SECONDS);
        return ret.trim();
    }


}
