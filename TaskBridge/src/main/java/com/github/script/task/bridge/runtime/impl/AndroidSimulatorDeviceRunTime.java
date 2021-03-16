package com.github.script.task.bridge.runtime.impl;

import com.github.script.task.bridge.device.impl.AndroidSimulatorDevice;
import com.github.script.task.bridge.device.type.DeviceType;
import com.github.script.task.bridge.helper.AppiumHelper;
import com.github.script.task.bridge.script.Environment;
import com.github.script.task.bridge.script.action.device.AndroidDeviceAction;
import com.github.script.task.bridge.util.ADBUtil;
import com.github.script.task.bridge.util.LeiDianSimulatorUtil;
import com.github.script.task.bridge.util.TokenUtil;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import com.github.script.task.bridge.script.ScriptRuntime;
import com.github.script.task.bridge.util.BeanUtil;

import java.io.File;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Slf4j
@Component
public class AndroidSimulatorDeviceRunTime extends AndroidMachineDeviceRunTime {


    private static final String CustomName = "device";

    //模拟器配置的key
    private static final String SimulatorPlayerName = "statusSettings.playerName";
    private static final String SimulatorMacAddress = "propertySettings.macAddress";


    @Autowired
    private AppiumHelper appiumHelper;

    @Override
    public DeviceType deviceType() {
        return DeviceType.AndroidSimulator;
    }


    //雷电模拟器的目录
    private File leiDianHome = null;


    //是否第一次运行
    private boolean firstRun = true;


    @Autowired
    private void init(ApplicationContext applicationContext) {
        leiDianHome = scriptTaskConf.getRunTime().getSimulator();
    }

    /**
     * 重启ADB
     */
    private synchronized void restartADB() {
        if (!firstRun) {
            return;
        }
        firstRun = false;
        log.info("[adb] - {}", "restart");
        ADBUtil.restartADB(this.scriptTaskConf.getRunTime().getADBHome());
    }


    /**
     * 设备id = mac地址
     * <p>
     * 缓存寻找 ( 内存中包含已经初始化过的 Android Driver )
     * 磁盘寻找 - 创建| 启动 - 连接 -  Android Driver - 缓存
     *
     * @param runtime
     */
    @Override
    public synchronized void create(ScriptRuntime runtime) {
        //重启adb服务
        restartADB();

        //查询内存中的模拟器
        RunningSimulator simulator = findSimulatorFromMemory(runtime);
        if (simulator == null) {
            simulator = loadDiskSimulator(runtime);
        }


        //设置运行环境的驱动与模拟器名称
        runtime.setDriver(simulator.getDriver());


        //设置访问模拟器对象
        simulator.setLastAccessTime(System.currentTimeMillis());
        simulator.setWorking(true);
        simulator.setScript(runtime.getScript());

    }

    @Override
    public void close(ScriptRuntime runtime) {
        this.runningSimulators.stream().filter((it) -> {
            return it.getScript() != null && runtime.equals(it.getScript().getRuntime());
        }).findFirst().ifPresent((simulator) -> {
            simulator.setWorking(false);
            simulator.setScript(null);
        });

    }


    /**
     * 载入磁盘上的虚拟机到内存
     */
    private RunningSimulator loadDiskSimulator(final ScriptRuntime runtime) {
        //匹配满足规则的模拟器
        String simulatorName = findAndBuildSimulator(runtime);

        final RunningSimulator runningSimulator = new RunningSimulator();

        runningSimulator.setCreateTime(System.currentTimeMillis());
        runningSimulator.setWorking(true);
        runningSimulator.setSimulatorName(simulatorName);
        this.runningSimulators.add(runningSimulator);


        //启动模拟器,返回驱动
        String adbConnectionName = startSimulatorAndWaitAdbConnect(simulatorName);
        runningSimulator.setAdbConnectionName(adbConnectionName);

        //构建appuim的服务端
        log.info("[启动] - Appium服务");
        AppiumDriverLocalService appiumDriverLocalService = this.appiumHelper.buildService();
        runningSimulator.setAppiumDriverLocalService(appiumDriverLocalService);


        //构建客户端,并连接
        log.info("[连接] - {} -> {}", simulatorName, appiumDriverLocalService.getUrl().toString());
        AndroidDriver driver = this.appiumHelper.buildAndroidDriver(appiumDriverLocalService, adbConnectionName);
        runningSimulator.setDriver(driver);

        //设置磁盘上数据
        runningSimulator.setInfo(LeiDianSimulatorUtil.get(leiDianHome, simulatorName));


        return runningSimulator;
    }

    /**
     * 启动模拟器或等待adb连接
     */
    private String startSimulatorAndWaitAdbConnect(String simulatorName) {

        if (!LeiDianSimulatorUtil.isrunning(leiDianHome, simulatorName)) {
            //启动虚拟机
            log.info("[启动] - 模拟器: {}", simulatorName);
            LeiDianSimulatorUtil.launch(leiDianHome, simulatorName);
        } else {
            log.info("[复用] - 模拟器: {}", simulatorName);
        }


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
     * 查找或者创建新的模拟器
     */
    private String findAndBuildSimulator(final ScriptRuntime runtime) {

        final Environment environment = runtime.getEnvironment();

        //查询现有的模拟器
        String simulatorName = findSimulatorFromDisk(runtime);

        //是否新的模拟器
        if (!StringUtils.hasText(simulatorName)) {
            simulatorName = findCustomName(Set.of(), TokenUtil.create());
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
    private String findSimulatorFromDisk(final ScriptRuntime runtime) {
        //取出现有的模拟器
        final List<Map<String, Object>> simulators = LeiDianSimulatorUtil.list(leiDianHome).values().stream()
                .filter((it) -> {
                    //过滤内存中存在的模拟器
                    for (RunningSimulator runningSimulator : this.runningSimulators) {
                        if (runningSimulator.getSimulatorName().equals(it.get(SimulatorPlayerName)) && runningSimulator.isWorking()) {
                            return false;
                        }
                    }
                    return true;
                }).collect(Collectors.toList());
        return this.findSimulatorFromList(runtime, simulators);
    }

    /**
     * 通过缓存找到模拟器
     *
     * @return
     */
    private RunningSimulator findSimulatorFromMemory(ScriptRuntime runtime) {
        //取出当前正在运行的模拟器，且没有在执行任务的
        final List<Map<String, Object>> canUsedList = this.runningSimulators.stream()
                .filter((it) -> {
                    return !it.isWorking();
                }).map((it) -> {
                    return it.getInfo();
                }).collect(Collectors.toList());
        String name = findSimulatorFromList(runtime, canUsedList);
        if (name == null) {
            return null;
        }


        RunningSimulator simulator = findRunningSimulatorByName(name);


        //是否已过期
        boolean expire = false;

        //如果没有运行则删除内存中状态
        if (!LeiDianSimulatorUtil.isrunning(leiDianHome, simulator.getSimulatorName())) {
            this.runningSimulators.remove(simulator);
            log.info("[过期] - 模拟器: {}", simulator.getSimulatorName());
            return null;
        }

        //驱动连接超时,删掉内存后，重新添加到内存里
        if (System.currentTimeMillis() - simulator.getCreateTime() > AppiumHelper.NewCommandTimeout) {
            this.runningSimulators.remove(simulator);
            log.info("[过期] - 模拟器: {}", simulator.getSimulatorName());
            return null;
        }


        log.info("[缓存] - 模拟器: {}", simulator.getSimulatorName());
        return simulator;

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
        //取出运行环境的设备id( mac地址 )
        final String deviceId = runtime.getDeviceId();


        //通过设备id进行匹配,如果绑定过
        if (StringUtils.hasText(deviceId)) {
            AtomicReference<String> simulatorDeviceInfo = new AtomicReference();
            //目标查询的模拟器
            canUsedList.stream().filter((it) -> {
                Object simulatorMac = it.get(SimulatorMacAddress);
                if (simulatorMac != null && deviceId.equals(simulatorMac)) {
                    return true;
                }
                return false;
            }).findFirst().ifPresent((it) -> {
                simulatorDeviceInfo.set(String.valueOf(it.get(SimulatorPlayerName)));
            });
            return simulatorDeviceInfo.get();
        }


        //匹配模拟器设备信息
        final AndroidSimulatorDevice device = (AndroidSimulatorDevice) environment.getDevice();
        for (Map<String, Object> map : canUsedList) {
            if (matchSimulator(device, map)) {
                Object simulatorName = map.get(SimulatorPlayerName);
                if (simulatorName == null) {
                    return null;
                }
                return String.valueOf(simulatorName);
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


}
