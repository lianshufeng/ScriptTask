package com.github.script.task.bridge.model.param;

import com.github.script.task.bridge.device.type.DeviceType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JobParam {

    private String jobId;

    //设备id
    private Set<String> deviceIds;

    //设备类型
    private Set<DeviceType> deviceTypes;


}
