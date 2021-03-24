package com.github.script.task.server.core.dao.impl;

import com.github.script.task.bridge.model.param.UserClueParam;
import com.github.script.task.server.core.dao.extend.UserClueDaoExtend;
import com.github.script.task.server.core.domain.Task;
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

public class UserClueDaoImpl implements UserClueDaoExtend {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private DBHelper dbHelper;

    @Override
    public Page<UserClue> list(UserClueParam param, Pageable pageable) {
        Criteria criteria = EntityObjectUtil.buildCriteria(new Criteria(), param, EntityObjectUtil.CriteriaType.Like, "platform");
        Query query = Query.query(criteria);
        Sort.Order[] orders = new Sort.Order[]{Sort.Order.desc("weightValue"), Sort.Order.asc("createTime")};
        query.with(Sort.by(orders));
        return this.dbHelper.pages(query, pageable, Task.class);
    }
}
