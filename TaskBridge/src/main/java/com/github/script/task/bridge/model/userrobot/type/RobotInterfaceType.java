package com.github.script.task.bridge.model.userrobot.type;

import com.github.script.task.bridge.model.userrobot.robot.RobotInput;
import com.github.script.task.bridge.model.userrobot.robot.RobotInterface;
import com.github.script.task.bridge.model.userrobot.robot.RobotOcr;
import com.github.script.task.bridge.model.userrobot.robot.RobotTap;
import com.github.script.task.bridge.model.userrobot.user.*;
import lombok.Getter;

/**
 * 机器人接口类型
 */
public enum RobotInterfaceType {

    Input(RobotInput.class, UserInput.class, "用户输入,可用于手机验证码，登录令牌等"),
    Ocr(RobotOcr.class, UserOcr.class, "图文识别,用于提供转换图片的内容"),
    Tap(RobotTap.class, UserTap.class, "用户点击"),
    Drag(RobotTap.class, UserDrag.class, "用户拖拽"),
    ;

    RobotInterfaceType(Class<? extends RobotInterface> robotClass, Class<? extends UserInterface> userClass, String remark) {
        this.robotClass = robotClass;
        this.userClass = userClass;
        this.remark = remark;
    }

    //数据模型
    @Getter
    private Class<? extends RobotInterface> robotClass;

    //数据模型
    @Getter
    private Class<? extends UserInterface> userClass;

    //备注
    @Getter
    private String remark;
}
