package com.github.script.task.server.core.dao.impl;

import com.github.script.task.bridge.model.param.MatchWordParam;
import com.github.script.task.server.core.dao.extend.MatchWordDaoExtend;
import com.github.script.task.server.core.domain.MatchWord;
import com.github.script.task.server.core.domain.Script;
import com.github.script.task.server.core.domain.UserClue;
import com.github.script.task.server.other.mongo.helper.DBHelper;
import com.github.script.task.server.other.mongo.util.EntityObjectUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.util.HashSet;

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
        Update update=new Update();
        EntityObjectUtil.entity2Update(param,update,new HashSet<String>(){{
            add("id");
        }});
        dbHelper.updateTime(update);
        return mongoTemplate.updateFirst(query, update, MatchWord.class).getModifiedCount() > 0;
    }

    @Override
    public Page<MatchWord> list(MatchWordParam param, Pageable pageable) {
        Criteria criteria = EntityObjectUtil.buildCriteria(new Criteria(), param, EntityObjectUtil.CriteriaType.Like, "collectionName","keyWord");
        Query query = Query.query(criteria);
        return this.dbHelper.pages(query, pageable, MatchWord.class);
    }
}
