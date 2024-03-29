package com.github.script.task.server.core.dao.mongo.impl;

import com.github.script.task.bridge.model.param.UserClueParam;
import com.github.script.task.server.core.dao.mongo.extend.UserClueDaoExtend;
import com.github.script.task.server.core.domain.UserClue;
import com.github.script.task.server.other.mongo.helper.DBHelper;
import com.github.script.task.server.other.mongo.util.EntityObjectUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.util.HashSet;
import java.util.regex.Pattern;

public class UserClueDaoImpl implements UserClueDaoExtend {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private DBHelper dbHelper;

    @Override
    public Page<UserClue> list(UserClueParam param, Pageable pageable) {
        Criteria criteria = new Criteria();
        if (!StringUtils.isBlank(param.getPlatform())){
            Pattern pattern = Pattern.compile("^.*" + param.getPlatform() + ".*$");
            criteria.and("platform").regex(pattern);
        }
        if (!StringUtils.isBlank(param.getRemark())){
            Pattern pattern = Pattern.compile("^.*" + param.getRemark() + ".*$");
            criteria.and("remark").regex(pattern);
        }
        Query query = Query.query(criteria);
        /*Sort.Order[] orders = new Sort.Order[]{Sort.Order.desc("weightValue"), Sort.Order.asc("createTime")};
        query.with(Sort.by(orders));*/
        return this.dbHelper.pages(query, pageable, UserClue.class);
    }

    @Override
    public boolean del(String id) {
        return this.mongoTemplate.remove(Query.query(Criteria.where("_id").is(id)), UserClue.class).getDeletedCount() > 0;
    }

    @Override
    public Boolean update(UserClueParam param) {
        Query query = new Query();
        query.addCriteria(Criteria.where("_id").is(param.getId()));
        Update update=new Update();
        EntityObjectUtil.entity2Update(param,update,new HashSet<String>(){{
            add("id");
            add("weightValue");
        }});
        dbHelper.updateTime(update);
        return mongoTemplate.updateFirst(query, update, UserClue.class).getModifiedCount() > 0;
    }
}
