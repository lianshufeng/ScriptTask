package com.github.script.task.server.core.service.data;

import com.github.script.task.server.core.domain.DataSet;


public interface DataHandle {


    /**
     * 数据处理
     *
     * @param dataSet
     */
    void execute(DataSet dataSet);

}
