package com.github.script.task.bridge.model.userrobot.parm;

import com.github.script.task.bridge.model.userrobot.type.RobotInterfaceType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RobotInterfaceParm {

    //类型
    private RobotInterfaceType type;

    //提示
    private String tips;


    //人机交互的参数
    private Object value;


    //过期时间
    private Long timeOut;


}
