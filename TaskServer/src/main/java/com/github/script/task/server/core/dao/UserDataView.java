package com.github.script.task.server.core.dao;

import com.mongodb.client.MongoDatabase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

/**
 * 构建视图
 */
@Repository
public class UserDataView {

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
        MongoDatabase mongoDatabase = this.mongoTemplate.getMongoDbFactory().getMongoDatabase();
        
    }


}
