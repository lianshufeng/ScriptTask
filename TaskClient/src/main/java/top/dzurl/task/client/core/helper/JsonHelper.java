package top.dzurl.task.client.core.helper;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class JsonHelper {


    @Autowired
    private ObjectMapper objectMapper;


    /**
     * 转换到json字符串
     *
     * @param object
     * @return
     * @throws Exception
     */
    @SneakyThrows
    public String toJson(Object object, boolean format) {
        if (format) {
            return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(object);
        } else {
            return objectMapper.writeValueAsString(object);
        }
    }


    /**
     * 转换到json字符串
     *
     * @param object
     * @return
     */
    public String toJson(Object object) {
        return toJson(object, false);
    }


    /**
     * 转换为对象
     *
     * @param json
     * @param cls
     * @return
     * @throws Exception
     */
    @SneakyThrows
    public <T> T toObject(String json, Class<T> cls) {
        return objectMapper.readValue(json, cls);
    }
}
