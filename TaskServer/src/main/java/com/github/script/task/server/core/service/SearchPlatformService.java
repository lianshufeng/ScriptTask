package com.github.script.task.server.core.service;

import com.github.script.task.server.core.dao.mongo.*;
import com.github.script.task.server.core.domain.UserData;
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
public class SearchPlatformService {

    @Autowired
    private SearchPlatformDao searchPlatformDao;

    @Autowired
    private UserDataDao userDataDao;

    @Autowired
    private void init(ApplicationContext applicationContext) {
        new Thread(() -> {
            resetPlatform();
        }).start();
    }


    @Scheduled(cron = "0 0 */1 * * ?")
    private void resetTimer() {
        resetPlatform();
    }


    /**
     * 重新更新标签
     */
    public void resetPlatform() {
        //查询所有的标签
        Map<String, Long> platform = new HashMap<>();
        platform.putAll(userDataDao.platforms());

        log.info("resetPlatform -> {}", platform);
        searchPlatformDao.update(platform);
    }






}
