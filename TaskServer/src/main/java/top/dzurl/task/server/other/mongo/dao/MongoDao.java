package top.dzurl.task.server.other.mongo.dao;

import org.springframework.data.mongodb.repository.MongoRepository;
import top.dzurl.task.server.other.mongo.domain.SuperEntity;

/**
 * Mongodb的一些通用方法
 *
 * @param <T>
 * @作者 练书锋
 * @时间 2016年5月13日
 */
public interface MongoDao<T extends SuperEntity> extends MongoRepository<T, String> {


}
