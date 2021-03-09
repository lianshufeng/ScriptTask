package top.dzurl.task.bridge.script;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import top.dzurl.task.bridge.device.Device;
import top.dzurl.task.bridge.device.impl.NoDevice;

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
