package com.github.script.task.bridge.model.userrobot.user;

import com.github.script.task.bridge.model.userrobot.type.RobotInterfaceType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserTap extends UserInterface {

    /**
     * 图片集合
     */
    private Map<String, Object>[] tap;

    @Override
    public RobotInterfaceType getType() {
        return RobotInterfaceType.Tap;
    }


    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Items {

        //项名
        private String name;

        //值
        private Item[] item;

    }


    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Item {

        //触发时间
        private long time;

        private int x;
        private int y;

    }


}