package top.dzurl.task.bridge.model.param;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JobLogParam {

    private String jobId;

    private List<String> logs;

}
