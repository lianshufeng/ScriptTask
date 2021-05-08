package com.github.script.task.server.core.dao.mongo.extend;

import com.github.script.task.bridge.model.userrobot.parm.RobotInterfacePutParm;
import com.github.script.task.bridge.model.userrobot.parm.UserInterfaceParm;

public interface UserRobotInterfaceDaoExtend {

    /**
     * 增加一个人机交互
     */
    String put(RobotInterfacePutParm userRobotInterface);


    /***
     * 更新人机交互
     * @param parm
     * @return
     */
    boolean updateUserInput(UserInterfaceParm parm);

}
