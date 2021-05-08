package com.github.script.task.server.core.dao.mongo.impl;

import com.github.script.task.server.core.dao.mongo.extend.DataSetDaoExtend;
import com.github.script.task.server.core.domain.DataSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        String map = "function(){var tags={};this.nlp.keyword.forEach(function(it){tags[it]=tags[it]?tags[it]+1:1});this.nlp.topic.lv1_tag_list.forEach(function(it){tags[it]=tags[it]?tags[it]+1:1});this.nlp.topic.lv2_tag_list.forEach(function(it){tags[it]=tags[it]?tags[it]+1:1});emit('tags',tags)}";
        String reduce = "function(key,values){var tags={};values.forEach(function(kw){for(name in kw){tags[name]=NumberLong(tags[name]?tags[name]+1:1)}});return tags}";
        List<Map> ret = this.mongoTemplate.mapReduce(Map.class)
                .map(map)
                .reduce(reduce)
                .inCollection(mongoTemplate.getCollectionName(DataSet.class))
                .all();
        if (ret.size() == 0) {
            return null;
        }
        Map<String, Long> result = new HashMap<>();
        for (Map.Entry<String, Object> entry : ((Map<String, Object>) ret.get(0).get("value")).entrySet()) {
            result.put(entry.getKey(), Long.valueOf(String.valueOf(entry.getValue())));
        }
        return result;
    }
}
