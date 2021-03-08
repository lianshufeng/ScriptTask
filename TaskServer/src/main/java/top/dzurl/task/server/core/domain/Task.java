package top.dzurl.task.server.core.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;
import top.dzurl.task.server.other.mongo.domain.SuperEntity;

import java.util.Map;

@Document
@Data
@AllArgsConstructor
public class Task extends SuperEntity {

    private String corn;



    private String scriptName;

    private Map<String,Object> parm;

    private Map<String,Object> env;





}
