package top.dzurl.task.server.core.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import top.dzurl.task.server.core.dao.extend.JobDaoExtend;
import top.dzurl.task.server.core.domain.Job;

public class JobDaoImpl implements JobDaoExtend {


    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public Job get(String deviceId) {
        Sort sort = Sort.by(Sort.Direction.ASC,"createTime");
        Job job = this.mongoTemplate
                .findAndRemove(Query.query(Criteria.where("deviceId").is(deviceId)).with(sort).limit(1),Job.class);
        return job != null ? job : this.mongoTemplate
                .findAndRemove(Query.query(Criteria.where("createTime").gte(0)).with(sort).limit(1),Job.class);
    }
}
