package com.github.script.task.server.core.dao.mongo;

import com.github.script.task.server.core.dao.mongo.extend.UserRobotInterfaceDaoExtend;
import com.github.script.task.server.core.domain.UserRobotInterface;
import com.github.script.task.server.other.mongo.dao.MongoDao;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserRobotInterfaceDao extends MongoDao<UserRobotInterface>, UserRobotInterfaceDaoExtend {


    /**
     * 分页查询用户是否已输入过数据
     *
     * @param exists
     * @return
     */
    Page<UserRobotInterface> findByUserInputExists(boolean exists, Pageable pageable);


    /**
     * 人机交互的id
     *
     * @param id
     * @return
     */
    long removeById(String id);


    /**
     * 查询人机交互
     *
     * @param id
     * @return
     */
    UserRobotInterface findTop1ById(String id);


}
