package com.github.script.task.bridge.model.userrobot.parm;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserInterfaceParm {
    //交互id
    private String id;


    //type.RobotInterface
    private Object value;

}
