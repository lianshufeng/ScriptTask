package com.github.script.task.bridge.model.userrobot.robot;

import com.github.script.task.bridge.model.userrobot.type.RobotInterfaceType;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RobotInput extends RobotInterface {

    @Override
    public RobotInterfaceType getType() {
        return RobotInterfaceType.Input;
    }
}