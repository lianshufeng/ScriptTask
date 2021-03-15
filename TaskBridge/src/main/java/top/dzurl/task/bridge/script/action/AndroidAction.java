package top.dzurl.task.bridge.script.action;


import io.appium.java_client.android.AndroidDriver;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.html5.Location;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import top.dzurl.task.bridge.conf.ScriptTaskConf;
import top.dzurl.task.bridge.device.Device;
import top.dzurl.task.bridge.device.impl.AndroidSimulatorDevice;
import top.dzurl.task.bridge.helper.MapHelper;
import top.dzurl.task.bridge.script.ScriptRuntime;
import top.dzurl.task.bridge.script.SuperScriptAction;
import top.dzurl.task.bridge.util.LeiDianSimulatorUtil;

import java.util.HashMap;

/**
 * android的工具
 */
@Slf4j
public class AndroidAction extends SuperScriptAction {

    //驱动
    private AndroidDriver driver;

    @Autowired
    private MapHelper mapHelper;

    @Autowired
    private ScriptTaskConf scriptTaskConf;


    @Override
    public void after() {
        Object d = _script.getRuntime().getDriver();
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
        return setLocation(location.getLng(), location.getLat());
    }

    /**
     * 设置经纬度
     *
     * @param lng
     * @param lat
     * @return
     */
    public boolean setLocation(String lng, String lat) {
        //运行环境
        final ScriptRuntime runtime = _script.getRuntime();

        //取出device
        Device device = runtime.getEnvironment().getDevice();

        //如果是是模拟器则用模拟器内置方法进行定位
        if (device instanceof AndroidSimulatorDevice) {
            //todo 未完成
            //取出模拟器名字
//            final String simulatorName = ((AndroidSimulatorScriptRuntime) runtime).getSimulatorName();
//            LeiDianSimulatorUtil.locate(scriptTaskConf.getRunTime().getSimulator(), simulatorName, lng, lat);
        } else {
            //真机用 Driver
            driver.setLocation(new Location(Double.valueOf(lat), Double.valueOf(lng), 0));
        }
        return true;
    }


}
