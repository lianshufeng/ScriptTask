package com.github.script.task.bridge.script;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.github.script.task.bridge.device.Device;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.github.script.task.bridge.device.impl.NoDevice;

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


}
