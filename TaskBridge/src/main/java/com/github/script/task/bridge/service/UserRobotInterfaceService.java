package com.github.script.task.bridge.service;

import com.github.script.task.bridge.model.userrobot.parm.RobotInterfacePutParm;
import com.github.script.task.bridge.model.userrobot.user.UserInterface;
import com.github.script.task.bridge.result.ResultContent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@Service
public class UserRobotInterfaceService extends SuperService {


    /**
     * 增加人机交互
     *
     * @return
     */
    public ResultContent<String> addUserRobotInterface(RobotInterfacePutParm robotInterfacePutParm) {
        try {
            return postJson("ur/put", robotInterfacePutParm, ResultContent.class);
        } catch (Exception e) {
            log.error("exception : {}", e.getMessage());
        }
        return null;
    }


    /**
     * 增加人机交互
     *
     * @return
     */
    public ResultContent<UserInterface> getUserInput(String id) {
        try {
            return postForm("ur/getUserInput", Map.of("id", id), ResultContent.class);
        } catch (Exception e) {
            log.error("exception : {}", e.getMessage());
        }
        return null;
    }


    /**
     * 增加人机交互
     *
     * @return
     */
    public ResultContent<Boolean> remove(String id) {
        try {
            return postForm("ur/remove", Map.of("id", id), ResultContent.class);
        } catch (Exception e) {
            log.error("exception : {}", e.getMessage());
        }
        return null;
    }


}
