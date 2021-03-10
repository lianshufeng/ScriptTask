package top.dzurl.task.bridge.device.impl;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import top.dzurl.task.bridge.device.Device;
import top.dzurl.task.bridge.device.type.DeviceType;

import java.util.Map;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WebDevice extends Device {

    //宽度
    private int width = 1024;

    //高度
    private int height = 768;


    //无头模式
    private boolean headless = true;

    //无痕模式
    private boolean incognito = true;

    //沙箱模式
    private boolean noSandbox = true;

    //允许运行部安全的内容
    private boolean allowInsecure = true;


    //启动命令行参数
    private String[] arguments;

    //实验性扩展配置
    private Map<String, Map<String, Object>> experimentalOption;


    @Override
    public DeviceType getType() {
        return DeviceType.Web;
    }
}
