package com.github.script.task.bridge.model.userrobot.user;

import com.github.script.task.bridge.model.userrobot.type.RobotInterfaceType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserTap extends UserInterface {

    /**
     * 图片集合
     */
    private LinkedHashMap<String, String> tap;

    @Override
    public RobotInterfaceType getType() {
        return RobotInterfaceType.Tap;
    }
}