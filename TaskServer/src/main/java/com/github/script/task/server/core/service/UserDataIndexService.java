package com.github.script.task.server.core.service;

import com.github.script.task.server.core.dao.es.UserDataIndexDao;
import com.github.script.task.server.core.dao.mongo.DataSetDao;
import com.github.script.task.server.core.dao.mongo.MatchWordDao;
import com.github.script.task.server.core.dao.mongo.UserDataDao;
import com.github.script.task.server.core.dao.mongo.impl.UserDataDaoImpl;
import com.github.script.task.server.core.domain.DataSet;
import com.github.script.task.server.core.domain.MatchWord;
import com.github.script.task.server.core.domain.UserData;
import com.github.script.task.server.core.domain.UserDataIndex;
import com.github.script.task.server.core.model.nlp.NLPModel;
import com.github.script.task.server.other.mongo.helper.DBHelper;
import com.mongodb.MongoException;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.reindex.DeleteByQueryRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.dao.DataAccessException;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.mongodb.core.DocumentCallbackHandler;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 用户数据索引
 */
@Slf4j
@Service
public class UserDataIndexService {

    @Autowired
    private UserDataDao userDataDao;

    @Autowired
    private DataSetDao dataSetDao;

    @Autowired
    private UserDataIndexDao userDataIndexDao;

    @Autowired
    private MatchWordDao matchWordDao;

    @Autowired
    private MongoTemplate mongoTemplate;


    @Autowired
    private RestHighLevelClient restHighLevelClient;

    @Autowired
    private DBHelper dbHelper;

    @Autowired
    private UserDataService userDataService;


    private ExecutorService executorService = null;

    @Autowired
    private void init(ApplicationContext applicationContext) {
        executorService = Executors.newFixedThreadPool(30);
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            executorService.shutdownNow();
        }));
    }


    /**
     * 重新构建索引
     */
    @SneakyThrows
    public void reindex() {

        //清空数据
        DeleteByQueryRequest request = new DeleteByQueryRequest("userdata");
        request.setQuery(QueryBuilders.matchAllQuery());
        this.restHighLevelClient.deleteByQuery(request, RequestOptions.DEFAULT);


        //匹配到的关键词
        final List<MatchWord> matchWords = this.matchWordDao.findAll();


        // 抽取所有数据并重新构建索引
        this.mongoTemplate.executeQuery(Query.query(Criteria.where("del").is(false)), this.mongoTemplate.getCollectionName(UserData.class), (new DocumentCallbackHandler() {
            @Override
            public void processDocument(Document document) throws MongoException, DataAccessException {
                executorService.execute(() -> {
                    try {
                        final DataSet dataSet = dataSetDao.findByHash(document.getString("contentHash"));
                        if (dataSet == null) {
                            return;
                        }
                        update(userDataService.toModel(document), dataSet, matchWords);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
            }
        }));


    }


    /**
     * 更新索引
     */
    public void update(final UserData userData) {
        final DataSet dataSet = this.dataSetDao.findByHash(userData.getContentHash());
        if (dataSet == null) {
            return;
        }
        executorService.execute(() -> {
            try {
                this.update(userData, dataSet, this.matchWordDao.findAll());
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }


    private void update(final UserData userData, final DataSet dataSet, final List<MatchWord> matchWords) {
        final UserDataIndex userDataIndex = new UserDataIndex();

        //数据集
        userDataIndex.setUserDataId(userData.getId());
        userDataIndex.setUser(userData.getUser());
        userDataIndex.setPlatform(userData.getPlatform());
        userDataIndex.setContent(dataSet.getContent());

        //提取标签
        final Set<String> tags = new HashSet<>();
        // nlp 取标签
        nlp2Tags(dataSet.getNlp(), tags);


        //匹配的词库到标签, 并返回权重值
        final int weight = matchWord2Tags(dataSet, matchWords, tags);

        userDataIndex.setWeight(weight);
        userDataIndex.setTags(tags);


        Optional.ofNullable(dataSet.getNlp()).ifPresent((it) -> {
            userDataIndex.setSummary(it.getSummary());
        });

        //增加索引
        this.userDataIndexDao.save(userDataIndex);

        log.info("update index : {}", userDataIndex.getUserDataId());
    }

    /**
     * 匹配的词库到标签
     *
     * @param tags
     */
    private int matchWord2Tags(final DataSet dataSet, final List<MatchWord> matchWords, final Set<String> tags) {
        BigDecimal weight = new BigDecimal(0);
        //内容
        final String content = dataSet.getContent();
        matchWords.forEach((matchWord) -> {
            //如果出现关键词
            if (content.indexOf(matchWord.getKeyWord()) > -1) {
                weight.add(new BigDecimal(matchWord.getWeightValue()));
                tags.add(matchWord.getCollectionName());
            }
        });

        return weight.intValue();
    }


    /**
     * nlp提取标签
     *
     * @param nlpModel
     * @param tags
     */
    private void nlp2Tags(final NLPModel nlpModel, final Set<String> tags) {
        if (nlpModel == null) {
            return;
        }
        //keyword
        Optional.ofNullable(nlpModel.getKeyword()).ifPresent((it) -> {
            tags.addAll(Set.of(it));
        });


        // tags
        Optional.ofNullable(nlpModel.getTopic()).ifPresent((it) -> {
            Optional.ofNullable(it.getLv1_tag_list()).ifPresent((tag) -> {
                tags.addAll(Set.of(tag));
            });
            Optional.ofNullable(it.getLv2_tag_list()).ifPresent((tag) -> {
                tags.addAll(Set.of(tag));
            });
        });

    }


}
