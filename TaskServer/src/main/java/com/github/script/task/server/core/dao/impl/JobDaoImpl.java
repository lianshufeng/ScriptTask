package com.github.script.task.server.core.dao.impl;

import com.github.script.task.bridge.device.type.DeviceType;
import com.github.script.task.bridge.model.param.JobParam;
import com.github.script.task.server.core.conf.TTLConf;
import com.github.script.task.server.core.dao.extend.JobDaoExtend;
import com.github.script.task.server.core.domain.Job;
import com.github.script.task.server.other.mongo.helper.DBHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Slf4j
public class JobDaoImpl implements JobDaoExtend {


    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private DBHelper dbHelper;

    @Autowired
    private TTLConf ttlConf;


    /**
     * 构建查询条件`
     *
     * @param param
     * @return
     */
    public Job queryJob(JobParam param, boolean isContainDeviceId) {
        final Criteria rootCriteria = new Criteria();
        final Query query = new Query(rootCriteria);

        List<Criteria> andCriteria = new ArrayList<>();
        Sort.Order[] orders = null;
        if (isContainDeviceId) {
            //包含 : 'deviceType' in ()    and   'deviceId' in ()  order by deviceId desc
            orders = new Sort.Order[]{Sort.Order.desc("deviceId"), Sort.Order.asc("createTime")};

            //deviceType in ()
            andCriteria.add(buildDeviceTypeCriteria(param));

            andCriteria.add(Criteria.where("deviceId").in(param.getDeviceIds()));

        } else {
            orders = new Sort.Order[]{Sort.Order.asc("createTime"), Sort.Order.asc("deviceId")};
            //排除 : 'deviceType' in ()    and   ( 'deviceId' is null or 'deviceId'  exists false )  order by deviceId asc

            //deviceType in ()
            andCriteria.add(buildDeviceTypeCriteria(param));

            andCriteria.add(new Criteria().andOperator(
                    Criteria.where("deviceId").exists(false),
                    Criteria.where("deviceId").is(null)
            ));
        }


        query.with(Sort.by(orders));
        rootCriteria.andOperator(andCriteria.toArray(new Criteria[0]));
        return this.mongoTemplate.findOne(query, Job.class);
    }

    /**
     * 构建设备类型查询条件
     *
     * @param param
     * @return
     */
    private Criteria buildDeviceTypeCriteria(JobParam param) {
        Set<DeviceType> deviceTypes = null;
        if (param.getDeviceTypes() == null || param.getDeviceTypes().size() == 0) {
            deviceTypes = Set.of();
        } else {
            deviceTypes = param.getDeviceTypes();
        }
        return Criteria.where("deviceType").in(deviceTypes);
    }


    @Override
    public Job get(JobParam param) {
        Job job = queryJob(param, param.getDeviceIds() != null);
        if (job == null) {
            job = queryJob(param, false);
        }
        if (job == null) {
            return null;
        }
        log.info("get job : {}", job.getId());

        Update update = new Update();
        update.inc("inc", 1);
        this.mongoTemplate.updateFirst(Query.query(Criteria.where("_id").is(job.getId())), update, Job.class);

        return this.mongoTemplate.findAndRemove(
                Query.query(
                        Criteria.where("_id").is(job.getId()).and("inc").is(1)
                ), Job.class);
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

       /*@Override
    public Job get(String deviceId) {
        Sort sort = Sort.by(Sort.Direction.ASC,"createTime");
        Job job = this.mongoTemplate
                .findAndRemove(Query.query(Criteria.where("deviceId").is(deviceId)).with(sort).limit(1),Job.class);
        return job != null ? job : this.mongoTemplate
                .findAndRemove(Query.query(Criteria.where("createTime").gte(0)).with(sort).limit(1),Job.class);
    }*/

/*
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
    }*/

}
