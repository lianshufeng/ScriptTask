package top.dzurl.task.bridge.model.param;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import top.dzurl.task.bridge.device.type.DeviceType;

import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JobCreateParam {

    private String scriptName;
}
