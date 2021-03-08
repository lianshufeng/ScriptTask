package top.dzurl.task.bridge.device.type;

import lombok.Getter;

/**
 * 设备类型
 */
public enum DeviceType {
    //Android 模拟器
    AndroidSimulator(PlatformType.Android),
    AndroidMachine(PlatformType.Android),

    Web(PlatformType.Android),

    ;

    DeviceType(PlatformType platform) {
        this.platform = platform;
    }

    //平台
    @Getter
    private PlatformType platform;


}

