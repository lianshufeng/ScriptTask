package top.dzurl.task.bridge.device.impl;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import top.dzurl.task.bridge.device.Device;
import top.dzurl.task.bridge.device.type.DeviceType;

/**
 * 非模拟器仅支持部分修改
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AndroidMachineDevice extends Device {

    //系统版本
    private String version;

    //系统api版本
    private String sdk;

    //手机设备型号
    private String productModel;

    //手机厂商名称
    private String productBrand;

    //手机序列号
    private String serialno;

    //mac地址
    private String mac;


    @Override
    public DeviceType getType() {
        return DeviceType.AndroidMachine;
    }
}
