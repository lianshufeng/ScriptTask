package com.github.script.task.server.core.dao.mongo.impl;

import com.github.script.task.server.core.dao.mongo.extend.DataSetDaoExtend;
import com.github.script.task.server.core.domain.DataSet;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.Field;
import org.springframework.data.mongodb.core.aggregation.Fields;
import org.springframework.data.mongodb.core.query.Criteria;

import java.util.*;

public class DataSetDaoImpl implements DataSetDaoExtend {

    @Autowired
    private MongoTemplate mongoTemplate;

    /**
     * 通过视图取出所有的标签
     *
     * @return
     */
    @Override
    public Map<String, Long> tags() {

        //构建分页查询条件
        List<AggregationOperation> aggregations = new ArrayList<AggregationOperation>() {{

            //连表查询仅存在的
            add(Aggregation.lookup("userData", "hash", "contentHash", "user"));
            add(Aggregation.match(Criteria.where("$expr").is(new Document("$gt", new Object[]{
                    new Document("$size", "$user"), 0
            }))));

            //将字段内所有的关键词提取到行并增加新属性tags
            add(Aggregation.addFields().addField("tags").withValue(new Document("$setUnion", new String[]{"$nlp.keyword", "$nlp.topic.lv1_tag_list", "$nlp.topic.lv2_tag_list"})).build());

            //每行的tags文本数组拆分多条记录
            add(Aggregation.unwind("$tags"));

            //统计每行的tags
            add(Aggregation.group(Fields.from(Fields.field("_id", "$tags"))).count().as("count"));

        }};
        List<Document> ret = this.mongoTemplate.aggregate(Aggregation.newAggregation(aggregations), this.mongoTemplate.getCollectionName(DataSet.class), Document.class).getMappedResults();
        Map<String, Long> result = new HashMap<>();
        for (Document item : ret) {
            result.put(item.getString("_id"), Long.parseLong(String.valueOf(item.get("count"))));
        }
        return result;

    }
}
