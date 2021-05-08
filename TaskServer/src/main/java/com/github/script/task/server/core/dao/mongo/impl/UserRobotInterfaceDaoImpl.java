package com.github.script.task.server.core.dao.mongo.impl;

import com.github.script.task.bridge.model.userrobot.parm.RobotInterfacePutParm;
import com.github.script.task.bridge.model.userrobot.parm.UserInterfaceParm;
import com.github.script.task.bridge.model.userrobot.user.UserInterface;
import com.github.script.task.bridge.util.JsonUtil;
import com.github.script.task.server.core.conf.TTLConf;
import com.github.script.task.server.core.dao.mongo.extend.UserRobotInterfaceDaoExtend;
import com.github.script.task.server.core.domain.UserRobotInterface;
import com.github.script.task.server.other.mongo.helper.DBHelper;
import lombok.SneakyThrows;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.Optional;

public class UserRobotInterfaceDaoImpl implements UserRobotInterfaceDaoExtend {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private DBHelper dbHelper;

    @Autowired
    private TTLConf ttlConf;

    @Override
    public String put(RobotInterfacePutParm parm) {
        UserRobotInterface robotInterface = new UserRobotInterface();
        BeanUtils.copyProperties(parm, robotInterface);

        //传有值
        Optional.ofNullable(parm.getValue()).ifPresent((it) -> {
            robotInterface.setRobotInput(JsonUtil.toJson(it));
        });
        //过期时间
        robotInterface.setTtl(new Date(this.dbHelper.getTime() + (parm.getTimeOut() == null ? ttlConf.getUserRobotTimeout() : parm.getTimeOut())));

        this.dbHelper.saveTime(robotInterface);
        this.mongoTemplate.save(robotInterface);
        return robotInterface.getId();
    }

    @Override
    public boolean updateUserInput(UserInterfaceParm parm) {

        UserRobotInterface userRobotInterface = this.mongoTemplate.findById(parm.getId(), UserRobotInterface.class);
        if (userRobotInterface == null) {
            return false;
        }

        //用户已经输入过了就不在输入
        if (StringUtils.hasText(userRobotInterface.getUserInput())) {
            return false;
        }

        //用户输入
        String userInput = JsonUtil.toJson(parm.getValue());

        //转换为用户交互,仅为了校验
        UserInterface userInterface = toUserInput(userRobotInterface, userInput);
        if (userInterface == null) {
            return false;
        }

        //更新数据
        Update update = Update.update("userInput", userInput);
        this.dbHelper.updateTime(update);

        return this.mongoTemplate.updateFirst(Query.query(Criteria.where("_id").is(parm.getId())), update, UserRobotInterface.class).getModifiedCount() > 0;
    }


    @SneakyThrows
    private UserInterface toUserInput(UserRobotInterface userRobotInterface, String userInput) {
        return JsonUtil.toObject(userInput, userRobotInterface.getType().getUserClass());
    }

}
