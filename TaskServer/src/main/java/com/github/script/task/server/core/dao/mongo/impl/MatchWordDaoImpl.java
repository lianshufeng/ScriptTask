package com.github.script.task.server.core.dao.mongo.impl;

import com.github.script.task.bridge.model.param.MatchWordParam;
import com.github.script.task.server.core.dao.mongo.extend.MatchWordDaoExtend;
import com.github.script.task.server.core.domain.MatchWord;
import com.github.script.task.server.other.mongo.helper.DBHelper;
import com.github.script.task.server.other.mongo.util.EntityObjectUtil;
import org.apache.commons.lang3.StringUtils;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

public class MatchWordDaoImpl implements MatchWordDaoExtend {


    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private DBHelper dbHelper;

    @Override
    public boolean del(String id) {
        return this.mongoTemplate.remove(Query.query(Criteria.where("_id").is(id)), MatchWord.class).getDeletedCount() > 0;
    }

    @Override
    public boolean update(MatchWordParam param) {
        Query query = new Query();
        query.addCriteria(Criteria.where("_id").is(param.getId()));
        Update update = new Update();
        EntityObjectUtil.entity2Update(param, update, new HashSet<String>() {{
            add("id");
        }});
        dbHelper.updateTime(update);
        return mongoTemplate.updateFirst(query, update, MatchWord.class).getModifiedCount() > 0;
    }

    @Override
    public Page<MatchWord> list(MatchWordParam param, Pageable pageable) {
        Criteria criteria = new Criteria();
        if (!StringUtils.isBlank(param.getCollectionName())) {
            Pattern pattern = Pattern.compile("^.*" + param.getCollectionName() + ".*$");
            criteria.and("collectionName").regex(pattern);
        }
        if (!StringUtils.isBlank(param.getKeyWord())) {
            Pattern pattern = Pattern.compile("^.*" + param.getKeyWord() + ".*$");
            criteria.and("keyWord").regex(pattern);
        }
        Query query = Query.query(criteria);
        return this.dbHelper.pages(query, pageable, MatchWord.class);
    }


    @Override
    public Set<String> collectionNames() {
        Set<String> ids = new HashSet<>();
        List<? extends Bson> pipeline = Arrays.asList(
                new Document("$group", new Document()
                        .append("_id", "$collectionName")
                        .append("total", new Document()
                                .append("$sum", 1)
                        ))
        );
        this.mongoTemplate.getCollection(this.mongoTemplate.getCollectionName(MatchWord.class)).aggregate(pipeline).map((it) -> {
            return it.getString("_id");
        }).forEach((it) -> {
            ids.add(it);
        });
        return ids;
    }
}
