package top.dzurl.task.server.core.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import top.dzurl.task.bridge.device.type.DeviceType;
import top.dzurl.task.bridge.script.Environment;
import top.dzurl.task.server.other.mongo.domain.SuperEntity;

import java.util.List;
import java.util.Map;

@Document
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Job extends SuperEntity {

    //脚本名
    @Indexed
    private String scriptName;

    //脚本的参数,作为参数的时候不需要描述
    private Map<String, Object> parameters;

    //脚本的环境
    private Environment environment;

    //绑定的设备标识
    @Indexed
    private String deviceId;

    //关联的任务
    @Indexed
    @DBRef(lazy = true)
    private Task task;

    //执行设备
    private DeviceType deviceType;

    //操作索引
    @Indexed
    private String uuid;


}
