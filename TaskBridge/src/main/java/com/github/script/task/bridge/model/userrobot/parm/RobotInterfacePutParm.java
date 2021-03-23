package com.github.script.task.bridge.model.userrobot.parm;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RobotInterfacePutParm extends RobotInterfaceParm {

    //工作id
    private String taskId;


}
