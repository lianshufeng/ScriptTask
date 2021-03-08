package top.dzurl.task.bridge.model;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import top.dzurl.task.bridge.script.Environment;
import top.dzurl.task.bridge.script.Parameter;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ScriptModel {

    //脚本名
    private String name;

    //脚本的备注
    private String remark;

    //脚本的参数
    private Map<String, Parameter> parameters;

    //脚本的环境
    private Environment environment;

    //脚本的md5
    private String hash;

    //脚本内容
    private byte[] body;


}
