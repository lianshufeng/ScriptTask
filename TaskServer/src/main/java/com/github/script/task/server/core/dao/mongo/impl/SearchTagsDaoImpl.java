package com.github.script.task.server.core.dao.mongo.impl;

import com.github.script.task.server.core.dao.mongo.extend.SearchTagsDaoExtend;
import com.github.script.task.server.core.domain.SearchTags;
import com.github.script.task.server.other.mongo.helper.DBHelper;
import com.github.script.task.server.other.mongo.util.EntityObjectUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.BulkOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.util.Date;
import java.util.Map;

public class SearchTagsDaoImpl implements SearchTagsDaoExtend {

    @Autowired
    private DBHelper dbHelper;

    @Autowired
    private MongoTemplate mongoTemplate;

    //超时时间
    private final static long timeOut = 1000 * 60 * 60 * 2;

    @Override
    public void update(Map<String, Long> tags) {
        if (tags == null || tags.size() == 0) {
            return;
        }
        BulkOperations bulkOperations = this.mongoTemplate.bulkOps(BulkOperations.BulkMode.UNORDERED, SearchTags.class);
        for (Map.Entry<String, Long> entry : tags.entrySet()) {
            Query query = Query.query(Criteria.where("tag").is(entry.getKey()));
            Update update = new Update();
            update.setOnInsert("tag", entry.getKey());
            update.set("count", entry.getValue());
            update.set("TTL", new Date(this.dbHelper.getTime() + timeOut));
            this.dbHelper.updateTime(update);
            bulkOperations.upsert(query, update);
        }
        bulkOperations.execute();
    }
}
