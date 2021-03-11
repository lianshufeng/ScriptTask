package top.dzurl.task.bridge.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;
import top.dzurl.task.bridge.script.Environment;

import java.util.Map;

/**
 * 脚本执行的模型
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class ScriptRunTimeModel {

    //脚本的参数,作为参数的时候不需要描述
    private Map<String, Object> parameters;

    //脚本的环境
    private Environment environment;

    //工作id
    private String jobId;

    //任务id
    private String taskId;

}
