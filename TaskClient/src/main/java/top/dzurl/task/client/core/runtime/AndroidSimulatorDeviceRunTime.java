package top.dzurl.task.client.core.runtime;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import top.dzurl.task.bridge.conf.ScriptTaskConf;
import top.dzurl.task.bridge.device.impl.AndroidSimulatorDevice;
import top.dzurl.task.bridge.device.type.DeviceType;
import top.dzurl.task.bridge.runtime.SuperDeviceRunTime;
import top.dzurl.task.bridge.script.Environment;
import top.dzurl.task.bridge.script.ScriptRuntime;
import top.dzurl.task.bridge.util.ADBUtil;
import top.dzurl.task.bridge.util.BeanUtil;
import top.dzurl.task.bridge.util.LeiDianSimulatorUtil;
import top.dzurl.task.client.core.factory.AppiumFactory;

import java.io.File;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Slf4j
@Component
public class AndroidSimulatorDeviceRunTime extends SuperDeviceRunTime {


    private static final String CustomName = "device";


    @Autowired
    private ScriptTaskConf scriptTaskConf;

    @Autowired
    private AppiumFactory appiumFactory;

    @Override
    public DeviceType deviceType() {
        return DeviceType.AndroidSimulator;
    }

    //当前运行的模拟器
    private Map<ScriptRuntime, RunningSimulator> runningSimulatorMap = new ConcurrentHashMap<>();

    //正在运行的虚拟机
    private Vector<RunningSimulator> runningSimulators = new Vector<>();

    //雷电模拟器的目录
    private File leiDianHome = null;


    @Autowired
    private void init(ApplicationContext applicationContext) {
        leiDianHome = scriptTaskConf.getRunTime().getSimulator();
        ADBUtil.restartADB(this.scriptTaskConf.getRunTime().getADBHome());
    }


    @Override
    public synchronized void create(ScriptRuntime runtime) {

        //查询内存中的模拟器
        RunningSimulator simulator = findSimulatorFromMemory(runtime);
        if (simulator == null) {
            simulator = loadDiskSimulator(runtime);
        }

        //设置运行环境的驱动与模拟器名称
        runtime.setDriver(simulator.getDriver());

        runningSimulatorMap.put(runtime, simulator);
    }

    @Override
    public synchronized void close(ScriptRuntime runtime) {
        //取出模拟器
        RunningSimulator simulator = runningSimulatorMap.remove(runtime);
        if (simulator != null) {
            simulator.setWorking(false);
        }
    }


    /**
     * 载入磁盘上的虚拟机到内存
     */
    private RunningSimulator loadDiskSimulator(final ScriptRuntime runtime) {
        RunningSimulator runningSimulator = new RunningSimulator();

        //匹配合适的模拟器
        String simulatorName = findAndBuildSimulator(runtime);
        runningSimulator.setSimulatorName(simulatorName);

        //启动模拟器,返回驱动
        String adbConnectionName = startSimulator(simulatorName);
        runningSimulator.setAdbConnectionName(adbConnectionName);

        //构建appuim的服务端
        log.info("[启动] - Appium服务");
        AppiumDriverLocalService appiumDriverLocalService = this.appiumFactory.buildService();
        runningSimulator.setAppiumDriverLocalService(appiumDriverLocalService);


        //构建客户端,并连接
        log.info("[连接] - {} -> {}", simulatorName, appiumDriverLocalService.getUrl().toString());
        AndroidDriver driver = this.appiumFactory.buildAndroidDriver(appiumDriverLocalService, adbConnectionName);
        runningSimulator.setDriver(driver);


        //最后一次访问时间
        runningSimulator.setLastAccessTime(System.currentTimeMillis());
        runningSimulator.setWorking(true);

        //设置磁盘上数据
        runningSimulator.setInfo(LeiDianSimulatorUtil.get(leiDianHome, simulatorName));

        this.runningSimulators.add(runningSimulator);

        return runningSimulator;
    }

    /**
     * 启动模拟器
     */
    private String startSimulator(String simulatorName) {
        //启动虚拟机
        log.info("[启动] - 模拟器: {}", simulatorName);
        LeiDianSimulatorUtil.launch(leiDianHome, simulatorName);

        //取出adb的连接名
        String adbConnectionName = waitAndGetAdbName(simulatorName);
        log.info("[adb] - 模拟器: {}", adbConnectionName);

        return adbConnectionName;
    }

    /**
     * 等待并取出adb的连接名
     *
     * @return
     */
    @SneakyThrows
    private String waitAndGetAdbName(String simulatorName) {
        String adbConnectionName = null;
        for (int i = 0; i < 5 * 60; i++) {
            adbConnectionName = LeiDianSimulatorUtil.getSerialno(leiDianHome, simulatorName);

            if (!StringUtils.hasText(adbConnectionName)) {
                Thread.sleep(3000);
                continue;
            }

            if ("unknown".equalsIgnoreCase(adbConnectionName)) {
                Thread.sleep(1000);
                continue;
            }

            if (adbConnectionName.indexOf("error:") > -1) {
                Thread.sleep(1000);
                continue;
            }

            return adbConnectionName;
        }
        return null;
    }

    /**
     * 查找模拟器
     */
    private String findAndBuildSimulator(final ScriptRuntime runtime) {

        final Environment environment = runtime.getEnvironment();

        //查询现有的模拟器
        String simulatorName = findSimulatorFromDisk(runtime);

        //是否新的模拟器
        if (simulatorName == null) {
            simulatorName = findCustomName(Set.of(), runtime.getDeviceId());
            log.info("[新建] - 模拟器: {}", simulatorName);
            //创建虚拟机
            LeiDianSimulatorUtil.create(leiDianHome, simulatorName);
            //修改模拟器
            LeiDianSimulatorUtil.modify(leiDianHome, simulatorName, (AndroidSimulatorDevice) environment.getDevice());
        } else {
            log.info("[寻到] - 模拟器: {}", simulatorName);
        }


        return simulatorName;
    }

    /**
     * 查询现有的模拟器
     *
     * @return
     */
    //todo 需要调整，不管当前是否已经运行的模拟器，内存中的除外)
    private String findSimulatorFromDisk(final ScriptRuntime runtime) {
        final Environment environment = runtime.getEnvironment();

        //模拟器配置的key
        final String playerName = "statusSettings.playerName";
        //获取现有的模拟器
        Map<String, Map<String, Object>> allList = LeiDianSimulatorUtil.list(leiDianHome);

        //取出正在运行的模拟器
        List<String> runningList = LeiDianSimulatorUtil.runninglist(leiDianHome);

        //取出可以用的模拟器(没有运行)
        final List<Map<String, Object>> canUsedList = allList.values().stream().filter((it) -> {
            Object pName = it.get(playerName);
            return pName != null && !runningList.contains(pName);
        }).collect(Collectors.toList());

        return this.findSimulatorFromList(runtime, canUsedList);
    }

    /**
     * 通过缓存找到模拟器
     *
     * @return
     */
    private RunningSimulator findSimulatorFromMemory(ScriptRuntime runtime) {
        return null;
//        final Environment environment = runtime.getEnvironment();
//
//        final List<Map<String, Object>> canUsedList = this.runningSimulators.stream()
//                .filter((it) -> {
//                    return !it.isWorking();
//                }).filter((it) -> {
//                    return Boolean.TRUE == LeiDianSimulatorUtil.isrunning(leiDianHome, it.getSimulatorName());
//                }).map((it) -> {
//                    return it.getInfo();
//                }).collect(Collectors.toList());
//        String name = findSimulatorFromList(runtime, canUsedList);
//        if (name == null) {
//            return null;
//        }
//        RunningSimulator simulator = findRunningSimulatorByName(name);
//        if (!LeiDianSimulatorUtil.isrunning(leiDianHome, simulator.getSimulatorName())) {
//            return null;
//        }
//        simulator.setLastAccessTime(System.currentTimeMillis());
//        simulator.setWorking(true);
//        return simulator;
    }


    /**
     * 通过模拟器名称查询正在运行的模拟器
     *
     * @param name
     * @return
     */
    private RunningSimulator findRunningSimulatorByName(String name) {
        for (RunningSimulator simulator : this.runningSimulators) {
            if (simulator.getSimulatorName().equals(name)) {
                return simulator;
            }
        }
        return null;
    }


    /**
     * 查询现有的模拟器
     *
     * @return
     */
    private String findSimulatorFromList(ScriptRuntime runtime, List<Map<String, Object>> canUsedList) {
        final Environment environment = runtime.getEnvironment();
        //模拟器配置的key
        final String playerName = "statusSettings.playerName";
        final String environmentName = runtime.getDeviceId();

        final Set<String> canUsedNames = canUsedList.stream().map((it) -> {
            return String.valueOf(it.get(playerName));
        }).collect(Collectors.toSet());

        //通过名字匹配,若没有匹配到则直接返回
        if (StringUtils.hasText(environmentName)) {
            //目标查询的模拟器
            String simulatorName = findCustomName(canUsedNames, environmentName);
            if (canUsedNames.contains(simulatorName)) {
                return simulatorName;
            }
            return null;
        }

        //若没有要求的设备环境，则随意取一个
        if (environment.getDevice() == null && canUsedNames.size() > 0) {
            return canUsedNames.toArray(new String[0])[0];
        }

        //取出模拟器设备的要求
        final AndroidSimulatorDevice device = (AndroidSimulatorDevice) environment.getDevice();
        for (Map<String, Object> map : canUsedList) {
            if (matchSimulator(device, map)) {
                return String.valueOf(map.get(playerName));
            }
        }

        return null;
    }


    /**
     * 模拟器是否匹配
     *
     * @return
     */
    @SneakyThrows
    private boolean matchSimulator(AndroidSimulatorDevice device, Map<String, Object> simulatorMap) {

        Map<String, String> matchWord = new HashMap<>() {{
            put("imei", "propertySettings.phoneIMEI");
            put("imsi", "propertySettings.phoneIMSI");
            put("simserial", "propertySettings.phoneSimSerial");
            put("androidid", "propertySettings.phoneAndroidId");
            put("model", "propertySettings.phoneModel");
            put("manufacturer", "propertySettings.phoneManufacturer");
            put("mac", "propertySettings.macAddress");
        }};

        Map<String, Object> deviceMap = BeanUtil.toMap(device);


        for (Map.Entry<String, String> entry : matchWord.entrySet()) {
            if (!matchMap(deviceMap, entry.getKey(), simulatorMap, entry.getValue())) {
                return false;
            }
        }

        //分辨率
        if (!matchResolution(device.getResolution(), simulatorMap.get("advancedSettings.resolution"), simulatorMap.get("basicSettings.width"), simulatorMap.get("basicSettings.height"))) {
            return false;
        }
        return true;
    }


    /**
     * 判断分辨率是否匹配
     *
     * @param userResolution
     * @param simulatorWidth
     * @param simulatorHeight
     * @return
     */
    private boolean matchResolution(String userResolution, Object simulatorResolution, Object simulatorWidth, Object simulatorHeight) {
        if (userResolution == null) {
            return true;
        }
        if (simulatorWidth == null || simulatorHeight == null) {
            return false;
        }

        String[] userResolutions = userResolution.split(",");


        if (simulatorResolution != null) {
            Map<String, Object> ret = (Map<String, Object>) simulatorResolution;
            return userResolutions[0].equals(String.valueOf(ret.get("width"))) && userResolutions[1].equals(String.valueOf(ret.get("height")));
        }


        return userResolutions[0].equals(String.valueOf(simulatorWidth)) && userResolutions[1].equals(String.valueOf(simulatorHeight));
    }


    /**
     * 匹配配置
     *
     * @param deviceMap
     * @param dKey
     * @param simulatorMap
     * @param sKey
     */
    private boolean matchMap(Map<String, Object> deviceMap, String dKey, Map<String, Object> simulatorMap, String sKey) {
        Object dVal = deviceMap.get(dKey);
        if (dVal == null) {
            return true;
        }
        //取出值并转到字符串
        String dValues = String.valueOf(dVal);
        if (StringUtils.hasText(dValues) && !"auto".equalsIgnoreCase(dValues)) {
            return dValues.equals(simulatorMap.get(sKey));
        }
        return true;
    }


    /***
     * 构建新的自定义模拟器名称
     * @return
     */
    private String findCustomName(Set<String> canUsed, String userName) {
        //获取现有的模拟器
        Set<String> allNames = LeiDianSimulatorUtil.listNames(leiDianHome);

        //用户名
        if (userName != null) {

            //优先取出可以用的模拟器
            if (canUsed != null) {
                for (int i = 0; i < 50; i++) {
                    String simulatorName = buildSimulatorName(userName, i);
                    //如果在可用列表中找到则直接返回模拟器名
                    if (canUsed.contains(simulatorName)) {
                        return simulatorName;
                    }
                }
            }


            //如果找不到则返回新的模拟器名
            for (int i = 0; i < 50; i++) {
                String simulatorName = buildSimulatorName(userName, i);
                //如果在可用列表中找到则直接返回模拟器名
                if (!allNames.contains(simulatorName)) {
                    return simulatorName;
                }
            }
        }

        return String.format("%s_%s", CustomName, UUID.randomUUID().toString().replaceAll("-", ""));
    }

    /**
     * 构建模拟器名称
     *
     * @param userName
     * @param index
     * @return
     */
    private String buildSimulatorName(String userName, int index) {
        return String.format("%s_%s_%s", CustomName, userName, String.valueOf(index));
    }


    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    private static class RunningSimulator {
        //模拟器名称
        private String simulatorName;

        //取出adb的androidid
        private String adbConnectionName;

        //模拟器对应的本地服务
        private AppiumDriverLocalService appiumDriverLocalService;

        //是否正在工作
        private boolean working;

        //最后一次访问时间
        private long lastAccessTime;

        //android的驱动
        private AndroidDriver driver;

        //信息
        private Map<String, Object> info;

    }
}
