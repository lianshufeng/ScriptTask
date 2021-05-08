package com.github.script.task.server.core.service;

import com.github.script.task.server.core.dao.mongo.DataSetDao;
import com.github.script.task.server.core.dao.mongo.MatchWordDao;
import com.github.script.task.server.core.dao.mongo.SearchTagsDao;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@EnableScheduling
public class SearchTagsService {

    @Autowired
    private SearchTagsDao searchTagsDao;

    @Autowired
    private MatchWordDao matchWordDao;

    @Autowired
    private DataSetDao dataSetDao;


    @Autowired
    private void init(ApplicationContext applicationContext) {
        new Thread(() -> {
            resetTags();
        }).start();
    }


    @Scheduled(cron = "0 0 */1 * * ?")
    private void resetTimer() {
        resetTags();
    }


    /**
     * 重新更新标签
     */
    public void resetTags() {
        //查询所有的标签
        Map<String, Long> tags = new HashMap<>();

        //取出自定义库的标签
        this.matchWordDao.collectionNames().forEach((it) -> {
            tags.put(it, 1L);
        });

        //取出数据集的标签
        tags.putAll(this.dataSetDao.tags());


        log.info("resetTags -> {}", tags);
        searchTagsDao.update(tags);
    }


}
