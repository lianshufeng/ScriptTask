package com.github.script.task.server.core.dao.mongo.impl;

import com.github.script.task.server.core.domain.UserData;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.List;

/**
 * 构建视图
 */
@Repository
public class UserDataViewImpl {

    //视图名
    private static final String ViewName = "UserDataView";

    @Autowired
    private MongoTemplate mongoTemplate;


    @Autowired
    private void createView(ApplicationContext applicationContext) {
        if (!this.mongoTemplate.collectionExists(ViewName)) {
            createView();
        }
    }

    /**
     * 创建视图
     */
    private void createView() {
        List<? extends Bson> pipeline = Arrays.asList(
                new Document("$lookup", new Document()
                        .append("from", "dataSet")
                        .append("localField", "contentHash")
                        .append("foreignField", "hash")
                        .append("as", "content"))
        );
        this.mongoTemplate.getDb().createView(ViewName, this.mongoTemplate.getCollectionName(UserData.class), pipeline);
    }


}
