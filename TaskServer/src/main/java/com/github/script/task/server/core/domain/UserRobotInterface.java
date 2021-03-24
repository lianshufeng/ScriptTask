package com.github.script.task.server.core.domain;

import com.github.script.task.bridge.model.userrobot.robot.RobotInterface;
import com.github.script.task.bridge.model.userrobot.type.RobotInterfaceType;
import com.github.script.task.bridge.model.userrobot.user.UserInterface;
import com.github.script.task.bridge.util.JsonUtil;
import com.github.script.task.server.other.mongo.domain.SuperEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.util.StringUtils;

import java.util.Date;

/**
 * 机器人接口
 */
@Document
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRobotInterface extends SuperEntity {

    // 任务工作的ID
    @Indexed
    private String taskId;

    //机器人交互的类型
    @Indexed
    private RobotInterfaceType type;

    //提示
    private String tips;

    //存放的数值(JSON字符串)
    private String robotInput;

    //存放的数值(JSON字符串)
    private String userInput;

    //过期时间
    @Indexed(expireAfterSeconds = 0)
    private Date ttl;


    /**
     * 获取机器输入
     *
     * @return
     */
    @SneakyThrows
    public RobotInterface getRobotInterface() {
        if (!StringUtils.hasText(robotInput)) {
            return null;
        }
        return JsonUtil.toObject(robotInput, type.getRobotClass());
    }

    /**
     * 获取用户输入
     *
     * @return
     */
    @SneakyThrows
    public UserInterface getUserInterface() {
        if (!StringUtils.hasText(userInput)) {
            return null;
        }
        return JsonUtil.toObject(userInput, type.getUserClass());
    }


}
