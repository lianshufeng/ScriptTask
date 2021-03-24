package com.github.script.task.bridge.model.userrobot.user;

import com.github.script.task.bridge.model.userrobot.type.RobotInterfaceType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserInput extends UserInterface {

    //用户输入文本字符
    private String[] text;
    @Override
    public RobotInterfaceType getType() {
        return RobotInterfaceType.Input;
    }

}