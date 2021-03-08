package top.dzurl.task.server.core.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;
import top.dzurl.task.server.other.mongo.domain.SuperEntity;

@Document
@Data
@AllArgsConstructor
public class Job extends SuperEntity {
}
