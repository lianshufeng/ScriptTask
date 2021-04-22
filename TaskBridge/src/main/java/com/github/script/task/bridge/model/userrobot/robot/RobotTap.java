package com.github.script.task.bridge.model.userrobot.robot;

import com.github.script.task.bridge.model.userrobot.type.RobotInterfaceType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RobotTap extends RobotInterface {

    //项
    private Item[] items;

    @Override
    public RobotInterfaceType getType() {
        return RobotInterfaceType.Tap;
    }


    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Item {
        //名称
        private String name;

        //base64编码
        private String picture;
    }
}