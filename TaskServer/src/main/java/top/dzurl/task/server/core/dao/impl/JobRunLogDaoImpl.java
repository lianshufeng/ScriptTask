package top.dzurl.task.server.core.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import top.dzurl.task.server.core.dao.extend.JobRunLogDaoExtend;
import top.dzurl.task.server.core.domain.JobRunLog;
import top.dzurl.task.server.other.mongo.helper.DBHelper;

import java.util.List;

public class JobRunLogDaoImpl implements JobRunLogDaoExtend {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private DBHelper dbHelper;

    @Override
    public Boolean update(JobRunLog jobRunLog) {
        Update update = new Update();
        update.set("logs", jobRunLog.getLogs());
        update.set("jobId", jobRunLog.getJobId());
        return mongoTemplate.upsert(Query.query(Criteria.where("jobId").is(jobRunLog.getJobId())), update, JobRunLog.class).getModifiedCount() > 0;

    }

    @Override
    public boolean appendInfo(String jobId, List<String> info) {
        Query query = Query.query(Criteria.where("jobId").is(jobId));
        Update update = new Update();
        update.setOnInsert("jobId", jobId);
        info.forEach((i) -> {
            update.addToSet("logs", i);
        });
        this.dbHelper.updateTime(update);
        return this.mongoTemplate.upsert(query, update, JobRunLog.class).getModifiedCount() > 0;
    }


}
