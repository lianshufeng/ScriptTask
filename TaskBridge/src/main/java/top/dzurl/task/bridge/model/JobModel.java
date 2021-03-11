package top.dzurl.task.bridge.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import top.dzurl.task.bridge.script.Environment;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class JobModel {

    private String id;

    //脚本名
    private String scriptName;

    //脚本的参数,作为参数的时候不需要描述
    private Map<String, Object> parameters;

    //脚本的环境
    private Environment environment;

    //绑定的设备标识
    private String deviceId;

}
