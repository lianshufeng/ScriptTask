package com.github.script.task.server.core.dao.mongo;

import com.github.script.task.server.core.dao.mongo.extend.DataSetDaoExtend;
import com.github.script.task.server.core.domain.DataSet;
import com.github.script.task.server.other.mongo.dao.MongoDao;

public interface DataSetDao extends MongoDao<DataSet>, DataSetDaoExtend {


    /**
     * 通过hash查询该记录
     *
     * @param hash
     * @return
     */
    DataSet findByHash(String hash);


    /**
     * 通过hash查询是否存在
     *
     * @param hash
     * @return
     */
    boolean existsByHash(String hash);


}
