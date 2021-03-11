package top.dzurl.task.bridge.model.param;

import lombok.Data;

import java.util.List;

@Data
public class JobParam {

    private String jobId;

    private List<String> logs;
}
