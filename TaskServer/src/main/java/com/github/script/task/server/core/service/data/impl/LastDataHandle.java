package com.github.script.task.server.core.service.data.impl;

import com.github.script.task.server.core.dao.mongo.DataSetDao;
import com.github.script.task.server.core.domain.DataSet;
import com.github.script.task.server.core.service.data.DataHandle;
import com.github.script.task.server.other.mongo.helper.DBHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;


/**
 * 初始化对象
 */
@Slf4j
@Order(Integer.MAX_VALUE)
@Service
public class LastDataHandle implements DataHandle {

    @Autowired
    private DataSetDao dataSetDao;
    @Autowired
    private DBHelper dbHelper;

    @Override
    public void execute(DataSet dataSet) {
        log.info("save : {} ", dataSet.getHash());
        this.dbHelper.saveTime(dataSet);
        this.dataSetDao.save(dataSet);
    }
}
