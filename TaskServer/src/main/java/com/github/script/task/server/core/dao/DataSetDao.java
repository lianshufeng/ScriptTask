package com.github.script.task.server.core.dao;

import com.github.script.task.server.core.domain.DataSet;
import com.github.script.task.server.other.mongo.dao.MongoDao;

public interface DataSetDao extends MongoDao<DataSet> {


    /**
     * 通过hash查询该记录
     *
     * @param hash
     * @return
     */
    DataSet findByHash(String hash);



}
