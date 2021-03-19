package com.github.script.task.server.core.dao.impl;

import com.github.script.task.bridge.model.param.JobParam;
import com.github.script.task.server.core.conf.TTLConf;
import com.github.script.task.server.core.dao.extend.JobDaoExtend;
import com.github.script.task.server.core.domain.Job;
import com.github.script.task.server.other.mongo.helper.DBHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.BulkOperations;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.UUID;

public class JobDaoImpl implements JobDaoExtend {


    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private DBHelper dbHelper;

    @Autowired
    private TTLConf ttlConf;

    /*@Override
    public Job get(String deviceId) {
        Sort sort = Sort.by(Sort.Direction.ASC,"createTime");
        Job job = this.mongoTemplate
                .findAndRemove(Query.query(Criteria.where("deviceId").is(deviceId)).with(sort).limit(1),Job.class);
        return job != null ? job : this.mongoTemplate
                .findAndRemove(Query.query(Criteria.where("createTime").gte(0)).with(sort).limit(1),Job.class);
    }*/

    @Override
    public List<Job> get(JobParam param) {
        List<Job> jobs = getJobByDeviceId(param);
        return jobs.size() > 0 ? jobs : getJob(param);
    }

    @Override
    public Job resetDeice() {
        Query query = new Query();
        query.addCriteria(Criteria.where("creatTime").lt(dbHelper.getTime() - ttlConf.getJobTimeOut()));
        Update update = new Update();
        update.unset("deviceId");
        FindAndModifyOptions findAndModifyOptions = new FindAndModifyOptions();
        findAndModifyOptions.returnNew(true);
        dbHelper.updateTime(update);
        return this.mongoTemplate.findAndModify(query, update, findAndModifyOptions, Job.class);
    }

    private List<Job> getJob(JobParam param){
        Query query = new Query();
        query.with(Sort.by(Sort.Direction.ASC,"createTime"));
        Criteria criteria = Criteria.where("uuid").exists(false);
        criteria.and("deviceId").exists(false);
        if (!CollectionUtils.isEmpty(param.getDeviceTypes())){
            criteria.and("deviceType").in(param.getDeviceTypes());
        }
        query.addCriteria(criteria);
        String uuid = UUID.randomUUID().toString();
        batchSetUUID(query,param.getCount(),uuid);
        List<Job> jobs = this.mongoTemplate.findAllAndRemove(Query.query(Criteria.where("uuid").is(uuid)),Job.class);
        return jobs;
    }

    private List<Job> getJobByDeviceId(JobParam param){
        Query query = new Query();
        query.with(Sort.by(Sort.Direction.DESC,"deviceId"));
        query.with(Sort.by(Sort.Direction.ASC,"createTime"));
        Criteria criteria = Criteria.where("uuid").exists(false);
        if (!CollectionUtils.isEmpty(param.getDeviceIds())){
            criteria.and("deviceId").in(param.getDeviceIds());
        } else {
            return List.of();
        }
        if (!CollectionUtils.isEmpty(param.getDeviceTypes())){
            criteria.and("deviceType").in(param.getDeviceTypes());
        }
        query.addCriteria(criteria);
        String uuid = UUID.randomUUID().toString();
        batchSetUUID(query,param.getCount(),uuid);
        List<Job> jobs = this.mongoTemplate.findAllAndRemove(Query.query(Criteria.where("uuid").is(uuid)),Job.class);
        return jobs;
    }


    void batchSetUUID(Query query,int count,String uuid){
        BulkOperations bulkOperations = this.mongoTemplate.bulkOps(BulkOperations.BulkMode.ORDERED, Job.class);
        for(int i = 0;i < count; i++){
            Update update = new Update();
            update.set("uuid", uuid);
            this.dbHelper.saveTime(update);
            bulkOperations.updateOne(query, update);
        }
        bulkOperations.execute();
    }

}
