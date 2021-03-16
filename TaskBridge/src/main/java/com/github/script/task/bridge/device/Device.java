package com.github.script.task.bridge.device;

import com.fasterxml.jackson.annotation.*;
import com.github.script.task.bridge.device.impl.AndroidMachineDevice;
import com.github.script.task.bridge.device.impl.AndroidSimulatorDevice;
import com.github.script.task.bridge.device.impl.NoDevice;
import com.github.script.task.bridge.device.impl.WebDevice;
import com.github.script.task.bridge.device.type.DeviceType;

import java.util.HashMap;
import java.util.Map;

/**
 * 设备
 */


@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
//@type=AndroidSimulatorDevice
@JsonSubTypes({
        @JsonSubTypes.Type(value = AndroidSimulatorDevice.class, name = "AndroidSimulatorDevice"),
        @JsonSubTypes.Type(value = AndroidMachineDevice.class, name = "AndroidMachineDevice"),
        @JsonSubTypes.Type(value = WebDevice.class, name = "WebDevice"),
        @JsonSubTypes.Type(value = NoDevice.class, name = "NoDevice")
})
@JsonInclude(JsonInclude.Include.NON_NULL)
public abstract class Device {

    //设备类型
    @JsonIgnore
    public abstract DeviceType getType();


    private Map<String, Object> _properties = new HashMap<String, Object>();

    @JsonAnySetter
    public Map<String, Object> any() {
        return _properties;
    }

    @JsonAnySetter
    public void set(String key, Object value) {
        _properties.put(key, value);
    }


}
