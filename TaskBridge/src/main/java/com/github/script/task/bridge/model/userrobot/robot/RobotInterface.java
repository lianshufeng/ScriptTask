package com.github.script.task.bridge.model.userrobot.robot;

import com.github.script.task.bridge.model.userrobot.type.RobotInterfaceType;
import lombok.Data;

@Data
public abstract class RobotInterface {

    public abstract RobotInterfaceType getType();


}