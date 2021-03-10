package top.dzurl.task.server.core.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import top.dzurl.task.bridge.model.param.TaskParam;
import top.dzurl.task.server.core.dao.extend.TaskDaoExtend;
import top.dzurl.task.server.core.domain.Script;
import top.dzurl.task.server.core.domain.Task;
import top.dzurl.task.server.other.mongo.helper.DBHelper;
import top.dzurl.task.server.other.mongo.util.EntityObjectUtil;

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
    public Boolean update(TaskParam param) {
        Task task = new Task();
        Query query = new Query();
        query.addCriteria(Criteria.where("_id").is(param.getId()));
        Update update=new Update();
        EntityObjectUtil.entity2Update(task,update,new HashSet<String>(){{
            add("id");
        }});
        dbHelper.updateTime(update);
        return mongoTemplate.updateFirst(query, update, Task.class).getModifiedCount() > 0;
    }
}
