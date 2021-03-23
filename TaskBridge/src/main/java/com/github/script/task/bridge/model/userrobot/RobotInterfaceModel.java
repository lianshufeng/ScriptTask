package com.github.script.task.bridge.model.userrobot;

import com.github.script.task.bridge.model.userrobot.robot.RobotInterface;
import com.github.script.task.bridge.model.userrobot.type.RobotInterfaceType;
import com.github.script.task.bridge.model.userrobot.user.UserInterface;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RobotInterfaceModel {

    //人机交互的id
    private String id;

    // 任务工作的ID
    private String taskId;

    //机器人交互的类型
    private RobotInterfaceType type;

    //提示
    private String tips;

    //存放的数值(JSON字符串)
    private RobotInterface robotInterface;

    //存放的数值(JSON字符串)
    private UserInterface userInterface;

    //创建时间
    private long createTime;

}
