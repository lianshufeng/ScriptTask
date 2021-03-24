package com.github.script.task.bridge.model.userrobot.user;

import com.github.script.task.bridge.model.userrobot.type.RobotInterfaceType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDrag extends UserInterface {
    //base64编码
    private String picture;

    @Override
    public RobotInterfaceType getType() {
        return RobotInterfaceType.Drag;
    }
}
