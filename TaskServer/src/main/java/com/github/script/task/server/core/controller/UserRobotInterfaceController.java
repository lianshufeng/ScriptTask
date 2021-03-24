package com.github.script.task.server.core.controller;

import com.github.script.task.bridge.model.userrobot.parm.RobotInterfacePutParm;
import com.github.script.task.bridge.model.userrobot.parm.UserInterfaceParm;
import com.github.script.task.bridge.result.ResultContent;
import com.github.script.task.server.core.service.UserRobotInterfaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("ur")
public class UserRobotInterfaceController {

    @Autowired
    private UserRobotInterfaceService userRobotInterfaceService;


    /**
     * 获取用户的输入
     *
     * @return
     */
    @RequestMapping("getUserInput")
    public Object getUserInput(String id) {
        Assert.hasText(id, "人机交互id不能为空");
        return this.userRobotInterfaceService.getUserInput(id);
    }


    /**
     * 更新用户输入
     *
     * @return
     */
    @RequestMapping("updateUserInput")
    public Object updateUserInput(@RequestBody UserInterfaceParm userRobotInterface) {
        Assert.hasText(userRobotInterface.getId(), "人机交互id不能为空");
        Assert.notNull(userRobotInterface.getValue(), "人机交互值不能为空");
        return this.userRobotInterfaceService.updateUserInput(userRobotInterface);
    }


    /**
     * 增加一个人机交互
     *
     * @return
     */
    @RequestMapping("put")
    public Object put(@RequestBody RobotInterfacePutParm userRobotInterface) {
        Assert.hasText(userRobotInterface.getTaskId(), "taskId不能为空");
        Assert.notNull(userRobotInterface.getType(), "人机交互类型不能为空");
        return this.userRobotInterfaceService.put(userRobotInterface);
    }


    /**
     * 分页查询
     *
     * @param pageable
     * @return
     */
    @RequestMapping("listRobotInput")
    public Object listRobotInput(@PageableDefault(size = 5, sort = {"createTime"}, direction = Sort.Direction.ASC) Pageable pageable) {
        return this.userRobotInterfaceService.listRobotInput(pageable);
    }


    /**
     * 删除人机交互
     *
     * @param id
     * @return
     */
    @RequestMapping("remove")
    public Object remove(String id) {
        return ResultContent.build(this.userRobotInterfaceService.remove(id));
    }


}
