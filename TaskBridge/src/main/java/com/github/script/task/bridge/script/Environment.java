package com.github.script.task.bridge.script;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.github.script.task.bridge.device.Device;
import com.github.script.task.bridge.device.impl.NoDevice;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Environment {

    //设备
    private Device device = NoDevice.builder().build();


    //线程池数量
    private int threadPoolCount = 10;


    //超时，默认10分钟
    private long timeout = 1000 * 60 * 10;

}
