package com.github.script.task.server.core.service;

import com.github.script.task.bridge.model.userrobot.RobotInterfaceModel;
import com.github.script.task.bridge.model.userrobot.parm.RobotInterfacePutParm;
import com.github.script.task.bridge.model.userrobot.parm.UserInterfaceParm;
import com.github.script.task.bridge.model.userrobot.user.UserInterface;
import com.github.script.task.bridge.result.ResultContent;
import com.github.script.task.bridge.result.ResultState;
import com.github.script.task.server.core.dao.TaskDao;
import com.github.script.task.server.core.dao.UserRobotInterfaceDao;
import com.github.script.task.server.core.domain.UserRobotInterface;
import com.github.script.task.server.other.mongo.util.PageEntityUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

@Service
public class UserRobotInterfaceService {

    @Autowired
    private UserRobotInterfaceDao userRobotInterfaceDao;

    @Autowired
    private TaskDao taskDao;

    @Autowired
    private MongoTemplate mongoTemplate;

    /**
     * 设置用户交互事件
     *
     * @param userRobotInterface
     * @return
     */
    public ResultContent<String> put(RobotInterfacePutParm userRobotInterface) {
        if (!taskDao.existsById(userRobotInterface.getTaskId())) {
            return ResultContent.build(ResultState.JobNotExists);
        }
        return ResultContent.buildContent(userRobotInterfaceDao.put(userRobotInterface));
    }

    /**
     * 设置用户交互事件
     *
     * @return
     */
    public ResultContent<UserInterface> getUserInput(String id) {
        UserRobotInterface userRobotInterface = this.userRobotInterfaceDao.findTop1ById(id);
        if (userRobotInterface == null) {
            return ResultContent.build(ResultState.UserRobotNotExists);
        }
        return ResultContent.build(userRobotInterface.getUserInterface() == null ? ResultState.UserRobotNotExistsUserInput : ResultState.Success, userRobotInterface.getUserInterface());
    }


    /**
     * 设置用户交互事件
     *
     * @return
     */
    public ResultContent<Boolean> updateUserInput(UserInterfaceParm parm) {
        Assert.hasText(parm.getId(), "人机交互的id不能为空");
        Assert.notNull(parm.getValue(), "参数不能为空");
        return ResultContent.build(userRobotInterfaceDao.updateUserInput(parm));
    }

    /**
     * 分页查询
     *
     * @param pageable
     * @return
     */
    public Page<RobotInterfaceModel> listRobotInput(Pageable pageable) {
        return PageEntityUtil.toPageModel(userRobotInterfaceDao.findByUserInputExists(false, pageable), (it) -> {
            return toModel(it);
        });
    }


    /**
     * 删除
     *
     * @param id
     * @return
     */
    public boolean remove(String id) {
        return this.userRobotInterfaceDao.removeById(id) > 0;
    }


    /**
     * 转换到模型
     *
     * @param robotInterface
     * @return
     */
    public RobotInterfaceModel toModel(UserRobotInterface robotInterface) {
        RobotInterfaceModel model = new RobotInterfaceModel();
        BeanUtils.copyProperties(robotInterface, model, "robotInterface", "userInterface");

        //机器输入
        if (StringUtils.hasText(robotInterface.getRobotInput())) {
            model.setRobotInterface(robotInterface.getRobotInterface());
        }

        if (StringUtils.hasText(robotInterface.getUserInput())) {
            model.setUserInterface(robotInterface.getUserInterface());
        }


        return model;
    }


}
