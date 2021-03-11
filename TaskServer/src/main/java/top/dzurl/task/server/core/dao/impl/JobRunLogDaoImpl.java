package top.dzurl.task.server.core.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.BulkOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import top.dzurl.task.server.core.dao.extend.JobRunLogDaoExtend;
import top.dzurl.task.server.core.domain.JobRunLog;
import top.dzurl.task.server.other.mongo.helper.DBHelper;

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
        BulkOperations bulkOperations = this.mongoTemplate.bulkOps(BulkOperations.BulkMode.ORDERED, JobRunLog.class);
        info.forEach((i) -> {
            Query query = Query.query(Criteria.where("jobId").is(jobId));
            Update update = new Update();
            update.setOnInsert("jobId", jobId);
            update.setOnInsert("ttl", ttlDate());
            update.addToSet("logs", i);
            this.dbHelper.saveTime(update);
            bulkOperations.upsert(query, update);
        });
        return bulkOperations.execute().getModifiedCount() > 0;
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
