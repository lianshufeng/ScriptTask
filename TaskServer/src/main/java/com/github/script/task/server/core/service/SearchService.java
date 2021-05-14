package com.github.script.task.server.core.service;

import com.github.script.task.server.core.dao.mongo.SearchPlatformDao;
import com.github.script.task.server.core.dao.mongo.SearchTagsDao;
import com.github.script.task.server.core.domain.SearchPlatform;
import com.github.script.task.server.core.domain.SearchTags;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SearchService {

    @Autowired
    private SearchPlatformDao searchPlatformDao;

    @Autowired
    private SearchTagsDao searchTagsDao;

    @Autowired
    private MongoTemplate mongoTemplate;


    /**
     * 搜索平台
     *
     * @return
     */
    public List<SearchPlatform> searchPlatforms() {
        Query query = Query.query(new Criteria());
        query.with(Sort.by(Sort.Direction.DESC, "count"));
        return mongoTemplate.find(query, SearchPlatform.class);
    }

    /**
     * 取出所有的标签
     *
     * @return
     */
    public List<SearchTags> searchTags() {
        Query query = Query.query(new Criteria());
        query.with(Sort.by(Sort.Direction.DESC, "count"));
        return mongoTemplate.find(query, SearchTags.class);
    }

}
