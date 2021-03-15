package top.dzurl.task.server.core.dao.impl;

import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;
import top.dzurl.task.bridge.script.SuperScript;
import top.dzurl.task.bridge.util.ScriptUtil;
import top.dzurl.task.server.core.dao.extend.ScriptDaoExtend;
import top.dzurl.task.server.core.domain.Script;
import top.dzurl.task.server.other.mongo.helper.DBHelper;

import java.util.Optional;

public class ScriptDaoImpl implements ScriptDaoExtend {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private DBHelper dbHelper;

    @Override
    @SneakyThrows
    public Script put(byte[] buffer) {
        String body = new String(buffer, "UTF-8");
        String md5 = DigestUtils.md5DigestAsHex(buffer);

        SuperScript script = ScriptUtil.parse(body);

        //删除存在的脚本
        this.mongoTemplate.remove(buildQueryFromName(script.name()), Script.class);


        Script entity = new Script();
        //脚本名
        entity.setName(script.name());
        //数据
        entity.setBody(buffer);

        entity.setHash(md5);

        //备注
        entity.setRemark(script.remark());

        //设备
        entity.setDeviceType(script.environment().getDevice().getType());

        //参数
        Optional.ofNullable(script.parameters()).ifPresent((it) -> {
            entity.setParameters(it);
        });

        //环境
        Optional.of(script.environment()).ifPresent((it) -> {
            entity.setEnvironment(it);
        });


        this.dbHelper.saveTime(entity);
        this.mongoTemplate.save(entity);

        return entity;
    }

    @Override
    public Page<Script> list(String word, Pageable pageable) {
        Criteria criteria = new Criteria();
        if (StringUtils.hasText(word)) {
            criteria.orOperator(new Criteria[]{
                    Criteria.where("name").regex(word),
                    Criteria.where("remark").regex(word)
            });
        }
        Query query = Query.query(criteria);
        return this.dbHelper.pages(query, pageable, Script.class);
    }

    @Override
    public Boolean del(String scriptName) {
        return this.mongoTemplate.remove(buildQueryFromName(scriptName), Script.class).getDeletedCount() > 0;
    }


    Query buildQueryFromName(String name) {
        return Query.query(Criteria.where("name").is(name));
    }


}
