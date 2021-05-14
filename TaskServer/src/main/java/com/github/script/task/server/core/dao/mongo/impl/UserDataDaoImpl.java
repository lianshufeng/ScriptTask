package com.github.script.task.server.core.dao.mongo.impl;

import com.github.script.task.server.core.dao.mongo.DataSetDao;
import com.github.script.task.server.core.dao.mongo.extend.UserDataDaoExtend;
import com.github.script.task.server.core.domain.DataSet;
import com.github.script.task.server.core.domain.UserData;
import com.github.script.task.server.other.mongo.helper.DBHelper;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.Fields;
import org.springframework.data.mongodb.core.query.Criteria;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserDataDaoImpl implements UserDataDaoExtend {

    @Autowired
    private DataSetDao dataSetDao;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private DBHelper dbHelper;

    @Override
    public UserData append(String platform, String user, String contentHash) {
        //如果hash不存在则错误
        if (!dataSetDao.existsByHash(contentHash)) {
            return null;
        }

        //数据里增加数据
        UserData userData = UserData.builder().platform(platform).user(user).contentHash(contentHash).del(false).build();
        this.dbHelper.saveTime(userData);
        this.mongoTemplate.save(userData);

        return userData;
    }

    @Override
    public Map<String, Long> platforms() {
        //构建分页查询条件
        List<AggregationOperation> aggregations = new ArrayList<AggregationOperation>() {{
            //统计每行的tags
            add(Aggregation.group(Fields.from(Fields.field("_id", "$platform"))).count().as("count"));
        }};
        List<Document> ret = this.mongoTemplate.aggregate(Aggregation.newAggregation(aggregations), this.mongoTemplate.getCollectionName(UserData.class), Document.class).getMappedResults();
        Map<String, Long> result = new HashMap<>();
        for (Document item : ret) {
            result.put(item.getString("_id"), Long.parseLong(String.valueOf(item.get("count"))));
        }
        return result;
    }


}
