package com.github.script.task.bridge.model.userrobot.parm;

import com.github.script.task.bridge.model.userrobot.type.RobotInterfaceType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RobotInterfaceParm {

    //类型
    private RobotInterfaceType type;

    //提示
    private String tips;


    //type.RobotInterface
    private Object value;


    //过期时间
    private Long timeOut;


}
