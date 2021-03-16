package com.github.script.task.bridge.device.type;

import lombok.Getter;

/**
 * 设备类型
 */
public enum DeviceType {
    //Android 模拟器
    AndroidSimulator(PlatformType.Android),
    //android 真机
    AndroidMachine(PlatformType.Android),

    Web(PlatformType.Android),
    None(PlatformType.None),

    ;

    DeviceType(PlatformType platform) {
        this.platform = platform;
    }

    //平台
    @Getter
    private PlatformType platform;


}

