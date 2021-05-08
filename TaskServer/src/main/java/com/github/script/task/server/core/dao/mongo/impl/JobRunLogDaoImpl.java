package com.github.script.task.server.core.dao.mongo.impl;

import com.github.script.task.server.core.dao.mongo.extend.JobRunLogDaoExtend;
import com.github.script.task.server.other.mongo.helper.DBHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import com.github.script.task.server.core.domain.JobRunLog;

import java.util.Date;
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
    public boolean appendLogs(String jobId, List<String> info) {
        Query query = Query.query(Criteria.where("jobId").is(jobId));
        Update update = new Update();
        update.setOnInsert("jobId", jobId);
        update.setOnInsert("ttl", ttlDate());
        update.push("logs").each(info.toArray());
        this.dbHelper.saveTime(update);
        return this.mongoTemplate.upsert(query, update, JobRunLog.class).getModifiedCount() > 0;

    }

    /**
     * TTL时间
     *
     * @return
     */
    private Date ttlDate() {
        return new Date(this.dbHelper.getTime() + 1000l * 60 * 60 * 24 * 30);
    }


}
