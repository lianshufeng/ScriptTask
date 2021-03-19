package com.github.script.task.server.core.dao.impl;

import com.github.script.task.bridge.model.param.UpdateTaskParam;
import com.github.script.task.bridge.util.BeanUtil;
import com.github.script.task.server.core.domain.Job;
import com.github.script.task.server.core.domain.Script;
import com.github.script.task.server.core.domain.Task;
import com.github.script.task.server.other.mongo.helper.DBHelper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import com.github.script.task.bridge.model.param.TaskParam;
import com.github.script.task.server.core.dao.extend.TaskDaoExtend;
import com.github.script.task.server.other.mongo.util.EntityObjectUtil;

import java.util.HashSet;

public class TaskDaoImpl implements TaskDaoExtend {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private DBHelper dbHelper;


    @Override
    public Page<Task> list(TaskParam param, Pageable pageable) {
        Criteria criteria = EntityObjectUtil.buildCriteria(new Criteria(), param, EntityObjectUtil.CriteriaType.Like, "scriptName");
        Query query = Query.query(criteria);
        query.with(Sort.by(Sort.Direction.DESC,"createTime"));
        return this.dbHelper.pages(query, pageable, Task.class);
    }

    @Override
    public Boolean del(String id) {
        return this.mongoTemplate.remove(Query.query(Criteria.where("_id").is(id)), Script.class).getDeletedCount() > 0;
    }

    @Override
    public Boolean update(UpdateTaskParam param) {
        Query query = new Query();
        query.addCriteria(Criteria.where("_id").is(param.getId()));
        Update update=new Update();

        if (param.getDevice() != null){
            param.getDevice().forEach((key,value)->{
                if (value != null){
                    update.set("environment.device." + key,value);
                }
            });
        }
        EntityObjectUtil.entity2Update(param,update,new HashSet<String>(){{
            add("id");
            add("scriptName");
            add("environment");
            add("device");
        }});
        dbHelper.updateTime(update);
        return mongoTemplate.updateFirst(query, update, Task.class).getModifiedCount() > 0;
    }

    @Override
    public Task resetDeice(String id) {
        Query query = new Query();
        query.addCriteria(Criteria.where("id").is(id));
        Update update = new Update();
        update.unset("deviceId");
        FindAndModifyOptions findAndModifyOptions = new FindAndModifyOptions();
        findAndModifyOptions.returnNew(true);
        dbHelper.updateTime(update);
        return this.mongoTemplate.findAndModify(query, update, findAndModifyOptions, Task.class);
    }
}
