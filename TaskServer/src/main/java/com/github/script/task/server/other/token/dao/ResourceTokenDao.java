package com.github.script.task.server.other.token.dao;

import com.github.script.task.server.other.mongo.dao.MongoDao;
import com.github.script.task.server.other.token.dao.extend.ResourceTokenDaoExtend;
import com.github.script.task.server.other.token.domain.ResourceToken;

/**
 * 资源令牌的Dao
 */
public interface ResourceTokenDao extends MongoDao<ResourceToken>, ResourceTokenDaoExtend {


    /**
     * 排序查询第一个资源令牌
     *
     * @param resourceName
     * @return
     */
    ResourceToken findTop1ByResourceNameAndTypeOrderByCounterAsc(String resourceName, ResourceToken.ResourceType type);


    /**
     * 删除资源与计数器
     *
     * @param resourceName
     * @param counter
     * @return
     */
    long removeByResourceNameAndCounterAndType(String resourceName, long counter, ResourceToken.ResourceType type);


    /**
     * 是否存在
     *
     * @param resoueceName
     * @param type
     * @return
     */
    boolean existsByResourceNameAndType(String resoueceName, ResourceToken.ResourceType type);

}
