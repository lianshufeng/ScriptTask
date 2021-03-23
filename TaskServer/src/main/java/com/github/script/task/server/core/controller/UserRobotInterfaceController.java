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
     * 创建任务
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
    @RequestMapping("list")
    public Object list(@PageableDefault(size = 5, sort = {"createTime"}, direction = Sort.Direction.ASC) Pageable pageable) {
        return this.userRobotInterfaceService.list(pageable);
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
