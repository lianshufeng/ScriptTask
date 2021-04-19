package com.github.script.task.server.core.dao.impl;

import com.github.script.task.bridge.model.ClientModel;
import com.github.script.task.server.core.dao.extend.ClientDaoExtend;
import com.github.script.task.server.core.domain.Client;
import com.github.script.task.server.other.mongo.helper.DBHelper;
import com.github.script.task.server.other.mongo.util.EntityObjectUtil;
import com.mongodb.client.result.UpdateResult;
import org.apache.commons.compress.archivers.sevenz.CLI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.util.StringUtils;

import java.util.Date;

public class ClientDaoImpl implements ClientDaoExtend {


    private final static long ttl = 1000 * 60;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private DBHelper dbHelper;

    @Override
    public boolean updateClient(ClientModel clientModel) {
        if (!StringUtils.hasText(clientModel.getUuid())) {
            return false;
        }
        Query query = new Query(Criteria.where("uuid").is(clientModel.getUuid()));

        Update update = new Update();
        EntityObjectUtil.entity2Update(clientModel, update, "createTime");
        dbHelper.saveTime(update);

        //过期时间
        update.set("ttl", new Date(dbHelper.getTime() + ttl));

        UpdateResult updateResult = this.mongoTemplate.upsert(query, update, Client.class);
        return updateResult.getModifiedCount() > 0 || updateResult.getUpsertedId() != null;
    }
}
