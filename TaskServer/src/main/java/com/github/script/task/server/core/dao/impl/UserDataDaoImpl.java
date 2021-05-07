package com.github.script.task.server.core.dao.impl;

import com.github.script.task.server.core.dao.DataSetDao;
import com.github.script.task.server.core.dao.extend.UserDataDaoExtend;
import com.github.script.task.server.core.domain.UserData;
import com.github.script.task.server.other.mongo.helper.DBHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;

public class UserDataDaoImpl implements UserDataDaoExtend {

    @Autowired
    private DataSetDao dataSetDao;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private DBHelper dbHelper;

    @Override
    public String append(String user, String contentHash) {
        //如果hash不存在则错误
        if (!dataSetDao.existsByHash(contentHash)) {
            return null;
        }

        //数据里增加数据
        UserData userData = UserData.builder().user(user).contentHash(contentHash).del(false).build();
        this.dbHelper.saveTime(userData);
        this.mongoTemplate.save(userData);

        return userData.getId();
    }
}
