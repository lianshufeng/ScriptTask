package top.dzurl.task.bridge.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import top.dzurl.task.bridge.device.type.DeviceType;
import top.dzurl.task.bridge.script.Environment;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class JobModel extends ScriptRunTimeModel {

    //脚本名
    private String scriptName;

    //执行设备
    private DeviceType deviceType;

}
