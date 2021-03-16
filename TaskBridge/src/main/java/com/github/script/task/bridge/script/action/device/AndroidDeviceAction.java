package com.github.script.task.bridge.script.action.device;


import io.appium.java_client.android.AndroidDriver;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.html5.Location;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import com.github.script.task.bridge.conf.ScriptTaskConf;
import com.github.script.task.bridge.helper.MapHelper;
import com.github.script.task.bridge.runtime.impl.AndroidMachineDeviceRunTime;
import com.github.script.task.bridge.runtime.impl.AndroidSimulatorDeviceRunTime;
import com.github.script.task.bridge.script.SuperScriptAction;
import com.github.script.task.bridge.util.ADBUtil;

import java.util.HashMap;

/**
 * android的工具
 */
@Slf4j
public class AndroidDeviceAction extends SuperScriptAction {

    //驱动
    protected AndroidDriver driver;

    @Autowired
    protected MapHelper mapHelper;

    @Autowired
    protected ScriptTaskConf scriptTaskConf;


    @Autowired
    private AndroidMachineDeviceRunTime androidMachineDeviceRunTime;


    @Override
    protected void after() {
        Object d = this.script.getRuntime().getDriver();
        Assert.state(d instanceof AndroidDriver, "必须为Android驱动");
        driver = (AndroidDriver) d;
    }

    /**
     * 关闭一个App
     *
     * @param bundleId 包名
     */
    public void closeApp(String bundleId) {
        driver.executeScript("mobile:shell", new HashMap<>() {{
            put("command", "am");
            put("args", new String[]{"force-stop", bundleId});
        }});
    }

    /**
     * 打开一个视图
     *
     * @param url
     */
    public void openView(String url) {
        driver.executeScript("mobile:shell", new HashMap<>() {{
            put("command", "am");
            put("args", new String[]{
                    "start", "-a", "android.intent.action.VIEW", "-d", url
            });
        }});
    }


    /**
     * 设置GPS地址
     *
     * @param address
     * @return
     */
    public boolean setLocation(String address) {
        MapHelper.Location location = this.mapHelper.query(address);
        log.info("{} -> {}", address, location);
        if (location == null) {
            return false;
        }
        return this.setLocation(location.getLng(), location.getLat());
    }

    /**
     * 设置经纬度
     *
     * @param lng
     * @param lat
     * @return
     */
    public boolean setLocation(String lng, String lat) {
        driver.setLocation(new Location(Double.valueOf(lat), Double.valueOf(lng), 0));
        return true;
    }


    /**
     * 取出Mac地址
     *
     * @return
     */
    public String getMacAddress() {
        AndroidSimulatorDeviceRunTime.RunningSimulator runningSimulator = androidMachineDeviceRunTime.queryRunningSimulator(this.script);
        return ADBUtil.getMac(scriptTaskConf.getRunTime().getADBHome(), runningSimulator.getAdbConnectionName());
    }


}
